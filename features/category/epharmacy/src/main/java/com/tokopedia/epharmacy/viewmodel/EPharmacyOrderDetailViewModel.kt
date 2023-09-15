package com.tokopedia.epharmacy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.epharmacy.component.model.EPharmacyDataModel
import com.tokopedia.epharmacy.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.epharmacy.network.response.EPharmacyOrderDetailResponse
import com.tokopedia.epharmacy.usecase.EPharmacyOrderDetailUseCase
import com.tokopedia.epharmacy.utils.EPharmacyUtils
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class EPharmacyOrderDetailViewModel @Inject constructor(
    private val ePharmacyOrderDetailUseCase: EPharmacyOrderDetailUseCase,
    @CoroutineBackgroundDispatcher private val dispatcherBackground : CoroutineDispatcher
) : BaseViewModel(dispatcherBackground){

    private val _ePharmacyOrderDetailData = MutableLiveData<Result<EPharmacyDataModel>>()
    val ePharmacyOrderDetailData: LiveData<Result<EPharmacyDataModel>> = _ePharmacyOrderDetailData

    fun getEPharmacyOrderDetail(tConsultationId: String, orderId: String) {
        ePharmacyOrderDetailUseCase.cancelJobs()
        ePharmacyOrderDetailUseCase.getEPharmacyOrderDetail(
            ::onAvailableEPharmacyOrderDetail,
            ::onFailEPharmacyOrderDetail,
            tConsultationId, orderId
        )
    }

    private fun onAvailableEPharmacyOrderDetail(data: EPharmacyOrderDetailResponse){
        _ePharmacyOrderDetailData.postValue(Success(EPharmacyUtils.mapResponseToOrderDetail(data.getConsultationOrderDetail?.ePharmacyOrderData)))
    }

    private fun onFailEPharmacyOrderDetail(throwable: Throwable){
        _ePharmacyOrderDetailData.postValue(Fail(throwable))
    }
}
