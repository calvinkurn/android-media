package com.tokopedia.ordermanagement.orderhistory.purchase.detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.domain.usecase.OrderHistoryUseCase
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.OrderHistoryResult
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import java.security.MessageDigest
import java.util.*
import javax.inject.Inject
import kotlin.experimental.and

class OrderHistoryViewModel @Inject constructor(
        private val orderHistoryUseCase: OrderHistoryUseCase,
        private val userSession: UserSessionInterface,
        dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    companion object {
        private const val PARAM_ORDER_ID = "order_id"
        private const val PARAM_LANGUAGE = "lang"
        private const val LANGUAGE_ID = "id"
        private const val PARAM_USER_ID = "user_id"
        private const val PARAM_DEVICE_ID = "device_id"
        private const val PARAM_HASH = "hash"
        private const val PARAM_OS_TYPE = "os_type"
        private const val PARAM_TIMESTAMP = "device_time"
        private const val PARAM_REQUEST_BY = "request_by"
    }

    private val _orderHistory = MutableLiveData<OrderHistoryResult>()
    val orderHistory: LiveData<OrderHistoryResult>
        get() = _orderHistory

    fun getOrderHistory(orderId: String, userMode: Int) {
        launchCatchError(block = {
            _orderHistory.postValue(OrderHistoryResult.OrderHistoryLoading)
            val temporaryParams = HashMap<String?, String?>()
            temporaryParams[PARAM_ORDER_ID] = orderId
            temporaryParams[PARAM_USER_ID] = userSession.userId
            temporaryParams[PARAM_LANGUAGE] = LANGUAGE_ID
            val params = HashMap<String?, Any?>(temporaryParams)
            params[PARAM_REQUEST_BY] = userMode
            orderHistoryUseCase.setRequestParams(generateParamsNetwork2(params))
            _orderHistory.postValue(OrderHistoryResult.OrderHistorySuccess(orderHistoryUseCase.executeOnBackground()))
        }) {
            _orderHistory.postValue(OrderHistoryResult.OrderHistoryFail(it))
        }
    }

    private fun generateParamsNetwork2(params: HashMap<String?, Any?>): HashMap<String?, Any?> {
        val deviceId = userSession.deviceId
        val userId = userSession.userId
        val hash = md5("$userId~$deviceId")
        params[PARAM_USER_ID] = userId
        params[PARAM_DEVICE_ID] = deviceId
        params[PARAM_HASH] = hash
        params[PARAM_OS_TYPE] = "1"
        params[PARAM_TIMESTAMP] = (Date().time / 1000).toString()
        return params
    }

    private fun md5(s: String): String {
        return try {
            val digest = MessageDigest.getInstance("MD5")
            digest.update(s.toByteArray())
            val messageDigest = digest.digest()
            val hexString = StringBuilder()
            for (b in messageDigest) {
                hexString.append(String.format("%02x", b and 0xff.toByte()))
            }
            hexString.toString()
        } catch (e: Throwable) {
            e.printStackTrace()
            ""
        }
    }
}