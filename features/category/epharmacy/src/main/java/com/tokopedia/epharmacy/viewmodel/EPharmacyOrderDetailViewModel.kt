package com.tokopedia.epharmacy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.epharmacy.component.model.EPharmacyDataModel
import com.tokopedia.epharmacy.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.epharmacy.network.response.EPharmacyOrderDetailResponse
import com.tokopedia.epharmacy.usecase.EPharmacyConsultationOrderDetailUseCase
import com.tokopedia.epharmacy.utils.EPharmacyUtils
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class EPharmacyOrderDetailViewModel @Inject constructor(
    private val ePharmacyConsultationOrderDetailUseCase: EPharmacyConsultationOrderDetailUseCase,
    @CoroutineBackgroundDispatcher private val dispatcherBackground: CoroutineDispatcher
) : BaseViewModel(dispatcherBackground) {

    private val _ePharmacyOrderDetailData = MutableLiveData<Result<EPharmacyDataModel>>()
    val ePharmacyOrderDetailData: LiveData<Result<EPharmacyDataModel>> = _ePharmacyOrderDetailData

    private val _ePharmacyButtonData = MutableLiveData<EPharmacyOrderDetailResponse.OrderButtonData>()
    val ePharmacyButtonData: LiveData<EPharmacyOrderDetailResponse.OrderButtonData> = _ePharmacyButtonData

    fun getEPharmacyOrderDetail(tConsultationId: String, orderUUId: String) {
        ePharmacyConsultationOrderDetailUseCase.cancelJobs()
        ePharmacyConsultationOrderDetailUseCase.getEPharmacyOrderDetail(
            ::onAvailableEPharmacyOrderDetail,
            ::onFailEPharmacyOrderDetail,
            tConsultationId,
            orderUUId
        )
    }

    private fun onAvailableEPharmacyOrderDetail(data: EPharmacyDataModel, orderButtonData: EPharmacyOrderDetailResponse.OrderButtonData?) {
        _ePharmacyOrderDetailData.postValue(Success(data))
        _ePharmacyButtonData.value = orderButtonData
    }

    private fun onFailEPharmacyOrderDetail(throwable: Throwable) {
        _ePharmacyOrderDetailData.postValue(Fail(throwable))
    }
}
