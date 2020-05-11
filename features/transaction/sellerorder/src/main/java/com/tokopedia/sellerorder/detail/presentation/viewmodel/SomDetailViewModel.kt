package com.tokopedia.sellerorder.detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerorder.common.SomDispatcherProvider
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_INPUT
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_IS_FROM_FINTECH
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_LANG_ID
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_ORDER_ID
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_SHOP_ID
import com.tokopedia.sellerorder.common.util.SomConsts.VAR_PARAM_LANG
import com.tokopedia.sellerorder.common.util.SomConsts.VAR_PARAM_ORDERID
import com.tokopedia.sellerorder.detail.data.model.*
import com.tokopedia.sellerorder.detail.domain.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-09-30.
 */
class SomDetailViewModel @Inject constructor(dispatcher: SomDispatcherProvider,
                                             private val somGetOrderDetailUseCase: SomGetOrderDetailUseCase,
                                             private val somAcceptOrderUseCase: SomAcceptOrderUseCase,
                                             private val somReasonRejectUseCase: SomReasonRejectUseCase,
                                             private val somRejectOrderUseCase: SomRejectOrderUseCase,
                                             private val somEditRefNumUseCase: SomEditRefNumUseCase,
                                             private val somSetDeliveredUseCase: SomSetDeliveredUseCase) : BaseViewModel(dispatcher.ui()) {

    private val _orderDetailResult = MutableLiveData<Result<SomDetailOrder.Data.GetSomDetail>>()
    val orderDetailResult: LiveData<Result<SomDetailOrder.Data.GetSomDetail>>
        get() = _orderDetailResult

    private val _acceptOrderResult = MutableLiveData<Result<SomAcceptOrder.Data>>()
    val acceptOrderResult: LiveData<Result<SomAcceptOrder.Data>>
        get() = _acceptOrderResult

    private val _rejectReasonResult = MutableLiveData<Result<SomReasonRejectData.Data>>()
    val rejectReasonResult: LiveData<Result<SomReasonRejectData.Data>>
        get() = _rejectReasonResult

    private val _rejectOrderResult = MutableLiveData<Result<SomRejectOrder.Data>>()
    val rejectOrderResult: LiveData<Result<SomRejectOrder.Data>>
        get() = _rejectOrderResult

    private val _editRefNumResult = MutableLiveData<Result<SomEditAwbResponse.Data>>()
    val editRefNumResult: LiveData<Result<SomEditAwbResponse.Data>>
        get() = _editRefNumResult

    private val _setDelivered = MutableLiveData<Result<SetDeliveredResponse>>()
    val setDelivered: LiveData<Result<SetDeliveredResponse>>
        get() = _setDelivered

    fun loadDetailOrder(detailQuery: String, orderId: String) {
        launch {
            _orderDetailResult.postValue(somGetOrderDetailUseCase.execute(detailQuery, orderId))
        }
    }

    fun acceptOrder(acceptOrderQuery: String, orderId: String, shopId: String) {
        launch {
            _acceptOrderResult.postValue(somAcceptOrderUseCase.execute(acceptOrderQuery, orderId, shopId))
        }
    }

    fun getRejectReasons(rejectReasonQuery: String) {
        launch {
            _rejectReasonResult.postValue(somReasonRejectUseCase.execute(rejectReasonQuery, SomReasonRejectParam()))
        }
    }

    fun rejectOrder(rejectOrderQuery: String, rejectOrderRequest: SomRejectRequest) {
        launch {
            _rejectOrderResult.postValue(somRejectOrderUseCase.execute(rejectOrderQuery, rejectOrderRequest))
        }
    }

    fun editAwb(queryString: String) {
        launch {
            _editRefNumResult.postValue(somEditRefNumUseCase.execute(queryString))
        }
    }

    fun setDelivered(rawQuery: String, orderId: String, receivedBy: String) {
        launch {
            _setDelivered.postValue(somSetDeliveredUseCase.execute(rawQuery, orderId, receivedBy))
        }
    }

    fun rejectOrderOld(rejectOrderQuery: String, rejectOrderRequest: SomRejectRequest, userId: String) {
        // launch { doRejectOrder(rejectOrderQuery, rejectOrderRequest) }
    }

    fun editAwbOld(queryString: String) {
        // launch { doEditAwb(queryString) }
    }

    /*fun setDelivered(rawQuery: String, orderId: String, receivedBy: String) {
        launchCatchError(block = {
            val param = mapOf("input" to SetDeliveredRequest(orderId, receivedBy))
            val gqlRequest = GraphqlRequest(rawQuery, SetDeliveredResponse::class.java, param)
            val gqlResponse = withContext(Dispatchers.IO) {
                graphqlRepository.getReseponse(listOf(gqlRequest))
            }
            val response = gqlResponse.getData<SetDeliveredResponse>(SetDeliveredResponse::class.java)
            if (response.setDelivered.success == 1) {
                _setDelivered.postValue(Success(response))
            } else {
                _setDelivered.postValue(Fail(Throwable(message = response.setDelivered.message.joinToString())))
            }
        }, onError =
        {
            _setDelivered.postValue(Fail(it))
        })
    }

    suspend fun getDetailOrder(rawQuery: String, orderId: String) {
        val requestDetailParams: HashMap<String, String> = hashMapOf(VAR_PARAM_ORDERID to orderId,
                VAR_PARAM_LANG to PARAM_LANG_ID)
        launchCatchError(block = {
            val orderDetailData = withContext(Dispatchers.IO) {
                val detailRequest = GraphqlRequest(rawQuery, SomDetailOrder.Data::class.java, requestDetailParams as Map<String, Any>?)
                graphqlRepository.getReseponse(listOf(detailRequest))
                        .getSuccessData<SomDetailOrder.Data>()
            }
            _orderDetailResult.postValue(Success(orderDetailData.getSomDetail))
        }, onError = {
            _orderDetailResult.postValue(Fail(it))
        })
    }

    suspend fun doAcceptOrder(rawQuery: String, orderId: String, shopId: String) {
        val requestAcceptOrderParam = mapOf(PARAM_ORDER_ID to orderId,
                PARAM_SHOP_ID to shopId, PARAM_IS_FROM_FINTECH to false)
        val acceptOrderParams = mapOf(PARAM_INPUT to requestAcceptOrderParam)
        launchCatchError(block = {
            val acceptOrderData = withContext(Dispatchers.IO) {
                val acceptOrderRequest = GraphqlRequest(rawQuery, SomAcceptOrder.Data::class.java, acceptOrderParams as Map<String, Any>?)
                graphqlRepository.getReseponse(listOf(acceptOrderRequest))
                        .getSuccessData<SomAcceptOrder.Data>()
            }
            _acceptOrderResult.postValue(Success(acceptOrderData))
        }, onError = {
            _acceptOrderResult.postValue(Fail(it))
        })
    }

    suspend fun doGetRejectReasons(rawQuery: String, reasonRejectParam: SomReasonRejectParam) {
        val orderParams = mapOf(PARAM_INPUT to reasonRejectParam)
        launchCatchError(block = {
            val rejectReasonData = withContext(Dispatchers.IO) {
                val orderRequest = GraphqlRequest(rawQuery, SomReasonRejectData.Data::class.java, orderParams)
                graphqlRepository.getReseponse(listOf(orderRequest))
                        .getSuccessData<SomReasonRejectData.Data>()
            }
            _rejectReasonResult.postValue(Success(rejectReasonData))
        }, onError = {
            _rejectReasonResult.postValue(Fail(it))
        })
    }

    suspend fun doRejectOrder(rawQuery: String, rejectOrderRequest: SomRejectRequest) {
        rejectOrderRequest.userId = userSession.userId
        val rejectParam = mapOf(PARAM_INPUT to rejectOrderRequest)
        println("++ rejectParam = $rejectParam")
        launchCatchError(block = {
            val rejectOrderData = withContext(Dispatchers.IO) {
                val rejectRequest = GraphqlRequest(rawQuery, SomRejectOrder.Data::class.java, rejectParam)
                graphqlRepository.getReseponse(listOf(rejectRequest))
                        .getSuccessData<SomRejectOrder.Data>()
            }
            _rejectOrderResult.postValue(Success(rejectOrderData))
        }, onError = {
            _rejectOrderResult.postValue(Fail(it))
        })
    }

    suspend fun doEditAwb(queryString: String) {
        launchCatchError(block = {
            val editRefNum = withContext(Dispatchers.IO) {
                val rejectRequest = GraphqlRequest(queryString, SomEditAwbResponse.Data::class.java)
                graphqlRepository.getReseponse(listOf(rejectRequest))
                        .getSuccessData<SomEditAwbResponse.Data>()
            }
            _editRefNumResult.postValue(Success(editRefNum))
        }, onError = {
            _editRefNumResult.postValue(Fail(it))
        })
    }*/
}