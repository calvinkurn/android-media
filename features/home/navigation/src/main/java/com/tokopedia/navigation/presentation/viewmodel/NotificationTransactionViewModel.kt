package com.tokopedia.navigation.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.constant.ErrorNetMessage
import com.tokopedia.abstraction.common.network.exception.HttpErrorException
import com.tokopedia.abstraction.common.network.exception.ResponseDataNullException
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.navigation.data.entity.NotificationEntity
import com.tokopedia.navigation.data.mapper.GetNotificationUpdateMapper
import com.tokopedia.navigation.domain.MarkReadNotificationUpdateItemUseCase
import com.tokopedia.navigation.domain.NotificationInfoTransactionUseCase
import com.tokopedia.navigation.domain.NotificationTransactionUseCase
import com.tokopedia.navigation.domain.model.TransactionNotification
import com.tokopedia.navigation.presentation.view.subscriber.NotificationUpdateActionSubscriber
import com.tokopedia.navigation.util.coroutines.DispatcherProvider
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

interface NotificationTransactionContract {
    fun getInfoStatusNotification()
    fun getTransactionNotification(
            lastNotificationId: String,
            onSuccess: (TransactionNotification) -> Unit,
            onError: (Throwable) -> Unit)
    fun onErrorMessage(throwable: Throwable)
    fun updateFilter(filter: HashMap<String, Int>)
    fun markReadNotification(notificationId: String)
    fun resetFilter()
}

class NotificationTransactionViewModel @Inject constructor(
        private val notificationInfoTransactionUseCase: NotificationInfoTransactionUseCase,
        private var notificationTransactionUseCase: NotificationTransactionUseCase,
        private var markReadNotificationUpdateItemUseCase: MarkReadNotificationUpdateItemUseCase,
        private var notificationMapper: GetNotificationUpdateMapper,
        dispatcher: DispatcherProvider
): BaseViewModel(dispatcher.io()), NotificationTransactionContract {

    private val _notification = MutableLiveData<TransactionNotification>()
    val notification: LiveData<TransactionNotification> get() = _notification

    private val _infoNotification = MutableLiveData<NotificationEntity>()
    val infoNotification: LiveData<NotificationEntity> get() = _infoNotification

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    //filtering variables
    var variables: HashMap<String, Any> = HashMap()

    init {
        getInfoStatusNotification()
    }

    override fun getInfoStatusNotification() {
        notificationInfoTransactionUseCase.get({ data ->
            _infoNotification.postValue(data)
        }, {
            onErrorMessage(it)
        })
    }

    override fun getTransactionNotification(
            lastNotificationId: String,
            onSuccess: (TransactionNotification) -> Unit,
            onError: (Throwable) -> Unit) {
        val requestParams = NotificationTransactionUseCase.params(
                FIRST_INITIAL_PAGE,
                variables,
                lastNotificationId
        )
        notificationTransactionUseCase.get(
                requestParams,
                {
                    onSuccess(notificationMapper.mapToNotifTransaction(it))
                },
                onError)
    }

    override fun markReadNotification(notificationId: String) {
        markReadNotificationUpdateItemUseCase.execute(
                MarkReadNotificationUpdateItemUseCase.getRequestParams(notificationId),
                NotificationUpdateActionSubscriber())
    }

    override fun updateFilter(filter: HashMap<String, Int>) {
        resetFilter()
        variables.putAll(filter)
    }

    override fun resetFilter() {
        variables.clear()
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

    companion object {
        private const val FIRST_INITIAL_PAGE = 1
    }

}