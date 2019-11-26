package com.tokopedia.sellerorder.confirmshipping.presentation.viewmodel

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

    val confirmShippingResult = MutableLiveData<Result<SomConfirmShipping>>()
    val courierListResult = MutableLiveData<Result<MutableList<SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment>>>()
    val changeCourierResult = MutableLiveData<Result<SomChangeCourier>>()

    fun processConfirmShipping(confirmShippingQuery: String, confirmShippingParam: SomConfirmShippingParam) {
        launch { doConfirmShipping(confirmShippingQuery, confirmShippingParam) }
    }

    fun getCourierList(rawQuery: String) {
        launch { doGetCourierList(rawQuery) }
    }

    fun processChangeCourier(rawQuery: String, inputs: SomChangeCourierParam) {
        launch { doChangeCourier(rawQuery, inputs) }
    }

    suspend fun doConfirmShipping(confirmShippingQuery: String, confirmShippingParam: SomConfirmShippingParam) {
        val confirmShippingParamInput = mapOf(SomConsts.PARAM_INPUT to confirmShippingParam)

        launchCatchError(block = {
            val confirmShippingData = withContext(Dispatchers.IO) {
                val confirmShippingRequest = GraphqlRequest(confirmShippingQuery, CONFIRM_SHIPPING_RESP, confirmShippingParamInput)
                graphqlRepository.getReseponse(listOf(confirmShippingRequest))
                        .getSuccessData<SomConfirmShipping>()
            }
            confirmShippingResult.postValue(Success(confirmShippingData))
        }, onError = {
            confirmShippingResult.postValue(Fail(it))
        })
    }

    suspend fun doGetCourierList(rawQuery: String) {
        launchCatchError(block = {
            val courierListData = withContext(Dispatchers.IO) {
                val orderRequest = GraphqlRequest(rawQuery, COURIER_LIST_RESP)
                graphqlRepository.getReseponse(listOf(orderRequest))
                        .getSuccessData<SomCourierList.Data>()
            }
            courierListResult.postValue(Success(courierListData.mpLogisticGetEditShippingForm.dataShipment.listShipment.toMutableList()))
        }, onError = {
            courierListResult.postValue(Fail(it))
        })
    }

    suspend fun doChangeCourier(rawQuery: String, inputs: SomChangeCourierParam) {
        val changeCourierParamInput = mapOf(SomConsts.PARAM_INPUT to inputs)

        launchCatchError(block = {
            val changeCourierData = withContext(Dispatchers.IO) {
                val changeCourierRequest = GraphqlRequest(rawQuery, SomChangeCourier::class.java, changeCourierParamInput as Map<String, Any>?)
                graphqlRepository.getReseponse(listOf(changeCourierRequest))
                        .getSuccessData<SomChangeCourier>()
            }
            changeCourierResult.postValue(Success(changeCourierData))
        }, onError = {
            changeCourierResult.postValue(Fail(it))
        })
    }

    companion object {
        private val CONFIRM_SHIPPING_RESP = SomConfirmShipping.Data::class.java
        private val COURIER_LIST_RESP = SomCourierList.Data::class.java
    }
}