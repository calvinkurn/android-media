package com.tokopedia.sellerorder.confirmshipping.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerorder.confirmshipping.data.model.SomChangeCourier
import com.tokopedia.sellerorder.confirmshipping.data.model.SomConfirmShipping
import com.tokopedia.sellerorder.confirmshipping.data.model.SomCourierList
import com.tokopedia.sellerorder.confirmshipping.domain.usecase.SomChangeCourierUseCase
import com.tokopedia.sellerorder.confirmshipping.domain.usecase.SomGetConfirmShippingResultUseCase
import com.tokopedia.sellerorder.confirmshipping.domain.usecase.SomGetCourierListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-11-15.
 */
class SomConfirmShippingViewModel @Inject constructor(dispatcher: CoroutineDispatchers,
                                                      private val somGetConfirmShippingResultUseCase: SomGetConfirmShippingResultUseCase,
                                                      private val somGetCourierListUseCase: SomGetCourierListUseCase,
                                                      private val somChangeCourierUseCase: SomChangeCourierUseCase
) : BaseViewModel(dispatcher.io) {

    private val _confirmShippingResult = MutableLiveData<Result<SomConfirmShipping.Data.MpLogisticConfirmShipping>>()
    val confirmShippingResult: LiveData<Result<SomConfirmShipping.Data.MpLogisticConfirmShipping>>
        get() = _confirmShippingResult

    private val _courierListResult = MutableLiveData<Result<MutableList<SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment>>>()
    val courierListResult: LiveData<Result<MutableList<SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment>>>
        get() = _courierListResult

    private val _changeCourierResult = MutableLiveData<Result<SomChangeCourier.Data>>()
    val changeCourierResult: LiveData<Result<SomChangeCourier.Data>>
        get() = _changeCourierResult

    fun confirmShipping(orderId: String, shippingRef: String) {
        launchCatchError(block = {
            _confirmShippingResult.postValue(Success(somGetConfirmShippingResultUseCase.execute(orderId, shippingRef)))
        }, onError = {
            _confirmShippingResult.postValue(Fail(it))
        })
    }

    fun getCourierList() {
        launchCatchError(block = {
            _courierListResult.postValue(Success(somGetCourierListUseCase.execute()))
        }, onError = {
            _courierListResult.postValue(Fail(it))
        })
    }

    fun changeCourier(orderId: String, shippingRef: String, agencyId: String, spId: String) {
        launchCatchError(block = {
            _changeCourierResult.postValue(Success(somChangeCourierUseCase.execute(orderId, shippingRef, agencyId, spId)))
        }, onError = {
            _changeCourierResult.postValue(Fail(it))
        })
    }
}