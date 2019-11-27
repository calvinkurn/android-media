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

    val confirmShippingResult = MutableLiveData<Result<SomConfirmShipping.Data>>()
    val courierListResult = MutableLiveData<Result<MutableList<SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment>>>()
    val changeCourierResult = MutableLiveData<Result<SomChangeCourier.Data>>()

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

    suspend fun doChangeCourier(queryString: String) {
        launchCatchError(block = {
            val changeCourierData = withContext(Dispatchers.IO) {
                val changeCourierRequest = GraphqlRequest(queryString, SomChangeCourier.Data::class.java)
                graphqlRepository.getReseponse(listOf(changeCourierRequest))
                        .getSuccessData<SomChangeCourier.Data>()
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