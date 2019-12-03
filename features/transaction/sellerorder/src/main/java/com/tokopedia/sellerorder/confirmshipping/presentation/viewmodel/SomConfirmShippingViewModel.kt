package com.tokopedia.sellerorder.confirmshipping.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.confirmshipping.data.model.*
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
class SomConfirmShippingViewModel @Inject constructor(dispatcher: CoroutineDispatcher,
                                                      private val graphqlRepository: GraphqlRepository) : BaseViewModel(dispatcher) {

    private val _confirmShippingResult = MutableLiveData<Result<SomConfirmShipping.Data>>()
    val confirmShippingResult: LiveData<Result<SomConfirmShipping.Data>>
        get() = _confirmShippingResult

    private val _courierListResult = MutableLiveData<Result<MutableList<SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment>>>()
    val courierListResult: LiveData<Result<MutableList<SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment>>>
        get() = _courierListResult

    private val _changeCourierResult = MutableLiveData<Result<SomChangeCourier.Data>>()
    val changeCourierResult: LiveData<Result<SomChangeCourier.Data>>
        get() = _changeCourierResult

    fun confirmShipping(queryString: String) {
        launch { doConfirmShipping(queryString) }
    }

    fun getCourierList(rawQuery: String) {
        launch { doGetCourierList(rawQuery) }
    }

    fun changeCourier(queryString: String) {
        launch { doChangeCourier(queryString) }
    }

    suspend fun doConfirmShipping(queryString: String) {
        launchCatchError(block = {
            val confirmShippingData = withContext(Dispatchers.IO) {
                val confirmShippingRequest = GraphqlRequest(queryString, SomConfirmShipping.Data::class.java)
                graphqlRepository.getReseponse(listOf(confirmShippingRequest))
                        .getSuccessData<SomConfirmShipping.Data>()
            }
            _confirmShippingResult.postValue(Success(confirmShippingData))
        }, onError = {
            _confirmShippingResult.postValue(Fail(it))
        })
    }

    suspend fun doGetCourierList(rawQuery: String) {
        launchCatchError(block = {
            val courierListData = withContext(Dispatchers.IO) {
                val orderRequest = GraphqlRequest(rawQuery, SomCourierList.Data::class.java)
                graphqlRepository.getReseponse(listOf(orderRequest))
                        .getSuccessData<SomCourierList.Data>()
            }
            _courierListResult.postValue(Success(courierListData.mpLogisticGetEditShippingForm.dataShipment.listShipment.toMutableList()))
        }, onError = {
            _courierListResult.postValue(Fail(it))
        })
    }

    suspend fun doChangeCourier(queryString: String) {
        launchCatchError(block = {
            val changeCourierData = withContext(Dispatchers.IO) {
                val changeCourierRequest = GraphqlRequest(queryString, SomChangeCourier.Data::class.java)
                graphqlRepository.getReseponse(listOf(changeCourierRequest))
                        .getSuccessData<SomChangeCourier.Data>()
            }
            _changeCourierResult.postValue(Success(changeCourierData))
        }, onError = {
            _changeCourierResult.postValue(Fail(it))
        })
    }
}