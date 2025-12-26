package com.example.questapi_015.viewmodel

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.questapi_015.modeldata.DataSiswa
import com.example.questapi_015.repositori.RepositoryDataSiswa
import com.example.questapi_015.uicontroller.route.DestinasiDetail
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

sealed interface StatusUiDetail {
    data class Success(val satusiswa: DataSiswa) : StatusUiDetail
    object Error : StatusUiDetail
    object Loading : StatusUiDetail
}

class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoryDataSiswa: RepositoryDataSiswa
) : ViewModel() {

    private val idSiswa: Int =
        checkNotNull(savedStateHandle[DestinasiDetail.itemIdArg])

    var statusUiDetail: StatusUiDetail by mutableStateOf(StatusUiDetail.Loading)
        private set

    init {
        getSatuSiswa()
    }

    fun getSatuSiswa() {
        viewModelScope.launch {
            statusUiDetail = StatusUiDetail.Loading
            statusUiDetail = try {
                StatusUiDetail.Success(
                    satusiswa = repositoryDataSiswa.getSatuSiswa(idSiswa)
                )
            } catch (e: IOException) {
                StatusUiDetail.Error
            } catch (e: HttpException) {
                StatusUiDetail.Error
            }
        }
    }

