package com.tokopedia.sellerorder.requestpickup.presentation.viewmodel

import androidx.lifecycle.LiveData
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
    private val _confirmReqPickupResult = MutableLiveData<Result<SomConfirmReqPickup.Data>>()
    val confirmReqPickupResult: LiveData<Result<SomConfirmReqPickup.Data>>
        get() = _confirmReqPickupResult

    private val _processReqPickupResult = MutableLiveData<Result<SomProcessReqPickup>>()
    val processReqPickupResult: LiveData<Result<SomProcessReqPickup>>
        get() = _processReqPickupResult

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
                val reqPickupRequest = GraphqlRequest(rawQuery, SomConfirmReqPickup.Data::class.java, reqPickupParamInput as Map<String, Any>?)
                graphqlRepository.getReseponse(listOf(reqPickupRequest))
                        .getSuccessData<SomConfirmReqPickup.Data>()
            }
            _confirmReqPickupResult.postValue(Success(reqPickupData))
        }, onError = {
            _confirmReqPickupResult.postValue(Fail(it))
        })
    }

    suspend fun doRequestPickup(reqPickupQuery: String, processReqPickupParam: SomProcessReqPickupParam) {
        val reqPickupParamInput = mapOf(SomConsts.PARAM_INPUT to processReqPickupParam)

        launchCatchError(block = {
            val processReqPickupData = withContext(Dispatchers.IO) {
                val reqPickupRequest = GraphqlRequest(reqPickupQuery, SomProcessReqPickup::class.java, reqPickupParamInput as Map<String, Any>?)
                graphqlRepository.getReseponse(listOf(reqPickupRequest))
                        .getSuccessData<SomProcessReqPickup>()
            }
            _processReqPickupResult.postValue(Success(processReqPickupData))
        }, onError = {
            _processReqPickupResult.postValue(Fail(it))
        })
    }
}