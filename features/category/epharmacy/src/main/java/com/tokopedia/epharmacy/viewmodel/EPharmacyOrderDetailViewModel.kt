package com.tokopedia.epharmacy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.epharmacy.component.model.EPharmacyDataModel
import com.tokopedia.epharmacy.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.epharmacy.network.response.OrderButtonData
import com.tokopedia.epharmacy.usecase.EPharmacyConsultationOrderDetailUseCase
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

    private val _ePharmacyButtonData = MutableLiveData<OrderButtonData>()
    val ePharmacyButtonData: LiveData<OrderButtonData> = _ePharmacyButtonData

    fun getEPharmacyOrderDetail(tConsultationId: Long, orderUUId: String, waitingInvoice: Boolean) {
        ePharmacyConsultationOrderDetailUseCase.cancelJobs()
        ePharmacyConsultationOrderDetailUseCase.getEPharmacyOrderDetail(
            ::onAvailableEPharmacyOrderDetail,
            ::onFailEPharmacyOrderDetail,
            tConsultationId,
            orderUUId,
            waitingInvoice
        )
    }

    private fun onAvailableEPharmacyOrderDetail(data: EPharmacyDataModel, orderButtonData: OrderButtonData) {
        _ePharmacyOrderDetailData.postValue(Success(data))
        _ePharmacyButtonData.postValue(orderButtonData)
    }

    private fun onFailEPharmacyOrderDetail(throwable: Throwable) {
        _ePharmacyOrderDetailData.postValue(Fail(throwable))
    }
}
