package com.tokopedia.notifcenter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.HttpErrorException
import com.tokopedia.abstraction.common.network.exception.ResponseDataNullException
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.network.constant.ErrorNetMessage
import com.tokopedia.notifcenter.data.entity.ProductData
import com.tokopedia.notifcenter.data.mapper.GetNotificationUpdateMapper
import com.tokopedia.notifcenter.data.mapper.ProductStockHandlerMapper
import com.tokopedia.notifcenter.data.model.NotificationViewData
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean
import com.tokopedia.notifcenter.domain.ProductStockHandlerUseCase
import com.tokopedia.notifcenter.domain.SingleNotificationUpdateUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import com.tokopedia.notifcenter.data.mapper.ProductStockHandlerMapper.map as stockHandlerMap
import com.tokopedia.notifcenter.domain.ProductStockHandlerUseCase.Companion.params as stockHandlerParam
import com.tokopedia.notifcenter.domain.SingleNotificationUpdateUseCase.Companion.params as singleNotificationParams

interface NotificationUpdateContract {
    fun isProductStockHandler(notificationId: String)
    fun getSingleNotification(notificationId: String)
    fun onErrorMessage(throwable: Throwable)
    fun cleared()
}

class NotificationUpdateViewModel @Inject constructor(
        private val productStockHandlerUseCase: ProductStockHandlerUseCase,
        private val singleNotificationUpdateUseCase: SingleNotificationUpdateUseCase,
        private var notificationMapper: GetNotificationUpdateMapper,
        dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io), NotificationUpdateContract {

    private val _productStockHandler = MutableLiveData<NotificationItemViewBean>()
    val productStockHandler: LiveData<NotificationItemViewBean>
        get() = _productStockHandler

    private val _singleNotification = MutableLiveData<NotificationViewData>()
    val singleNotification: LiveData<NotificationViewData>
        get() = _singleNotification

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    override fun getSingleNotification(notificationId: String) {
        val params = singleNotificationParams(notificationId)
        singleNotificationUpdateUseCase.get(params, {
            val data = notificationMapper.map(it)
            _singleNotification.postValue(data)
        }, ::onErrorMessage)
    }

    override fun isProductStockHandler(notificationId: String) {
        val params = stockHandlerParam(notificationId)
        productStockHandlerUseCase.get(params, {
            _productStockHandler.value = stockHandlerMap(it)
        }, ::onErrorMessage)
    }

    fun isProductStockHandlerMultiple(notificationId: String, originData: ProductData) {
        val params = stockHandlerParam(notificationId)
        productStockHandlerUseCase.get(params, {
            _productStockHandler.value = ProductStockHandlerMapper.mapEliminateMultipleProduct(it, originData)
        }, ::onErrorMessage)
    }

    override fun onErrorMessage(throwable: Throwable) {
        when(throwable) {
            is UnknownHostException ->
                _errorMessage.postValue(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL)
            is SocketTimeoutException ->
                _errorMessage.postValue(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT)
            is ConnectException ->
                _errorMessage.postValue(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT)
            is ResponseErrorException ->
                _errorMessage.postValue(throwable.message)
            is ResponseDataNullException ->
                _errorMessage.postValue(throwable.message)
            is HttpErrorException ->
                _errorMessage.postValue(throwable.message)
            else -> {
                _errorMessage.postValue(ErrorNetMessage.MESSAGE_ERROR_DEFAULT)
            }
        }
    }

    override fun cleared() {
        onCleared()
    }

}