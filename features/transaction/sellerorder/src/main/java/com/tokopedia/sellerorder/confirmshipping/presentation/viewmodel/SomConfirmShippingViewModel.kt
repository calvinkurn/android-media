package com.tokopedia.sellerorder.confirmshipping.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerorder.common.SomDispatcherProvider
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.confirmshipping.data.model.*
import com.tokopedia.sellerorder.confirmshipping.domain.SomChangeCourierUseCase
import com.tokopedia.sellerorder.confirmshipping.domain.SomGetConfirmShippingResultUseCase
import com.tokopedia.sellerorder.confirmshipping.domain.SomGetCourierListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-11-15.
 */
class SomConfirmShippingViewModel @Inject constructor(dispatcher: SomDispatcherProvider,
                                                      private val somGetConfirmShippingResultUseCase: SomGetConfirmShippingResultUseCase,
                                                      private val somGetCourierListUseCase: SomGetCourierListUseCase,
                                                      private val somChangeCourierUseCase: SomChangeCourierUseCase) : BaseViewModel(dispatcher.ui()) {

    private val _confirmShippingResult = MutableLiveData<Result<SomConfirmShipping.Data.MpLogisticConfirmShipping>>()
    val confirmShippingResult: LiveData<Result<SomConfirmShipping.Data.MpLogisticConfirmShipping>>
        get() = _confirmShippingResult

    private val _courierListResult = MutableLiveData<Result<MutableList<SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment>>>()
    val courierListResult: LiveData<Result<MutableList<SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment>>>
        get() = _courierListResult

    private val _changeCourierResult = MutableLiveData<Result<SomChangeCourier.Data>>()
    val changeCourierResult: LiveData<Result<SomChangeCourier.Data>>
        get() = _changeCourierResult

    fun confirmShipping(queryString: String) {
        launch {
            _confirmShippingResult.postValue(somGetConfirmShippingResultUseCase.execute(queryString))
        }
    }

    fun getCourierList(rawQuery: String) {
        launch {
            _courierListResult.postValue(somGetCourierListUseCase.execute(rawQuery))
        }
    }

    fun changeCourier(queryString: String) {
        launch {
            _changeCourierResult.postValue(somChangeCourierUseCase.execute(queryString))
        }
    }
}