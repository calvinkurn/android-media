package com.tokopedia.epharmacy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.epharmacy.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.epharmacy.network.response.GetEPharmacyResponse
import com.tokopedia.epharmacy.usecase.GetEPharmacyOrderDetailUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class UploadPrescriptionViewModel @Inject constructor(
    private val getEPharmacyOrderDetailUseCase: GetEPharmacyOrderDetailUseCase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher
)  : BaseViewModel(dispatcher){

    private val _productDetailLiveData = MutableLiveData<Result<GetEPharmacyResponse.EPharmacyData>>()
    val productDetailLiveData: LiveData<Result<GetEPharmacyResponse.EPharmacyData>> = _productDetailLiveData

    fun getEPharmacyDetail(orderId: String) {
        getEPharmacyOrderDetailUseCase.cancelJobs()
        getEPharmacyOrderDetailUseCase.getEPharmacyDetail(
            ::onAvailableEPharmacyDetail,
            ::onFailEPharmacyDetail,
            orderId
        )
    }

    private fun onAvailableEPharmacyDetail(ePharmacyDetail: GetEPharmacyResponse) {
        ePharmacyDetail.ePharmacyData?.let { data ->
            if(data.ePharmacyProducts?.isEmpty() == true)
                onFailEPharmacyDetail(IllegalStateException("Data invalid"))
            else {
                _productDetailLiveData.postValue(Success(data))
            }
        }
    }

    private fun onFailEPharmacyDetail(throwable: Throwable) {
        _productDetailLiveData.postValue(Fail(throwable))
    }

    override fun onCleared() {
        getEPharmacyOrderDetailUseCase.cancelJobs()
        super.onCleared()
    }
}
