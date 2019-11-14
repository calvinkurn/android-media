package com.tokopedia.sellerorder.requestpickup.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.requestpickup.data.model.SomConfirmReqPickup
import com.tokopedia.sellerorder.requestpickup.data.model.SomConfirmReqPickupParam
import com.tokopedia.sellerorder.requestpickup.data.model.SomProcessReqPickup
import com.tokopedia.sellerorder.requestpickup.data.model.SomProcessReqPickupParam
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-11-12.
 */
class SomConfirmReqPickupViewModel @Inject constructor(dispatcher: CoroutineDispatcher,
                                                       private val graphqlRepository: GraphqlRepository) : BaseViewModel(dispatcher) {
    val confirmReqPickupResult = MutableLiveData<Result<SomConfirmReqPickup.Data>>()
    val processReqPickupResult = MutableLiveData<Result<SomProcessReqPickup>>()

    fun loadConfirmRequestPickup(detailQuery: String, reqPickupParam: SomConfirmReqPickupParam) {
        launch { getRequestPickupData(detailQuery, reqPickupParam) }
    }

    fun processRequestPickup(reqPickupQuery: String, processReqPickupParam: SomProcessReqPickupParam) {
        launch { doRequestPickup(reqPickupQuery, processReqPickupParam) }
    }

    suspend fun getRequestPickupData(rawQuery: String, reqPickupParam: SomConfirmReqPickupParam) {
        val reqPickupParamInput = mapOf(SomConsts.PARAM_INPUT to reqPickupParam)
        launchCatchError(block = {
            val reqPickupData = withContext(Dispatchers.IO) {
                val reqPickupRequest = GraphqlRequest(rawQuery, POJO_LOGISTIC_PRE_SHIP, reqPickupParamInput as Map<String, Any>?)
                graphqlRepository.getReseponse(listOf(reqPickupRequest))
                        .getSuccessData<SomConfirmReqPickup.Data>()
            }
            confirmReqPickupResult.postValue(Success(reqPickupData))
        }, onError = {
            confirmReqPickupResult.postValue(Fail(it))
        })
    }

    suspend fun doRequestPickup(reqPickupQuery: String, processReqPickupParam: SomProcessReqPickupParam) {
        val reqPickupParamInput = mapOf(SomConsts.PARAM_INPUT to processReqPickupParam)

        launchCatchError(block = {
            val processReqPickupData = withContext(Dispatchers.IO) {
                val reqPickupRequest = GraphqlRequest(reqPickupQuery, POJO_LOGISTIC_REQ_PICKUP, reqPickupParamInput as Map<String, Any>?)
                graphqlRepository.getReseponse(listOf(reqPickupRequest))
                        .getSuccessData<SomProcessReqPickup>()
            }
            processReqPickupResult.postValue(Success(processReqPickupData))
        }, onError = {
            processReqPickupResult.postValue(Fail(it))
        })
    }

    companion object {
        private val POJO_LOGISTIC_PRE_SHIP = SomConfirmReqPickup.Data::class.java
        private val POJO_LOGISTIC_REQ_PICKUP = SomProcessReqPickup::class.java
    }
}