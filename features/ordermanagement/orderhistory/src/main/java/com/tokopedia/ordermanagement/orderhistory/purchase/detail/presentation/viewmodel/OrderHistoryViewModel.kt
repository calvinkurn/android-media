package com.tokopedia.ordermanagement.orderhistory.purchase.detail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toLongOrZero
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
        private const val PARAM_ORDER_ID = "orderID"
        private const val PARAM_LANGUAGE = "lang"
        private const val LANGUAGE_ID = "id"
        private const val PARAM_USER_ID = "userID"
        private const val PARAM_REQUEST_BY = "requestBy"
    }

    private val _orderHistory = MutableLiveData<OrderHistoryResult>()
    val orderHistory: LiveData<OrderHistoryResult>
        get() = _orderHistory

    fun getOrderHistory(orderId: String, userMode: Int) {
        launchCatchError(block = {
            _orderHistory.postValue(OrderHistoryResult.OrderHistoryLoading)
            val params = HashMap<String, Any?>()
            params[PARAM_REQUEST_BY] = userMode
            params[PARAM_ORDER_ID] = orderId
            params[PARAM_USER_ID] = userSession.userId.toLongOrZero()
            params[PARAM_LANGUAGE] = LANGUAGE_ID
            orderHistoryUseCase.setRequestParams(params.toMap())
            _orderHistory.postValue(OrderHistoryResult.OrderHistorySuccess(orderHistoryUseCase.execute()))
        }) {
            _orderHistory.postValue(OrderHistoryResult.OrderHistoryFail(it))
        }
    }
}