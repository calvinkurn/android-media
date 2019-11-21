package com.tokopedia.navigation.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.constant.ErrorNetMessage
import com.tokopedia.abstraction.common.network.exception.HttpErrorException
import com.tokopedia.abstraction.common.network.exception.ResponseDataNullException
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.navigation.data.entity.NotificationEntity
import com.tokopedia.navigation.domain.NotificationTransactionUseCase
import com.tokopedia.navigation.util.coroutines.DispatcherProvider
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

interface NotificationTransactionContract {
    fun getNotification()
    fun onErrorMessage(throwable: Throwable)
}

class NotificationTransactionViewModel @Inject constructor(
        private val useCase: NotificationTransactionUseCase,
        dispatcher: DispatcherProvider
): BaseViewModel(dispatcher.io()), NotificationTransactionContract {

    private val _notification = MutableLiveData<NotificationEntity>()
    val notification: LiveData<NotificationEntity> get() = _notification

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    init {
        getNotification()
    }

    override fun getNotification() {
        useCase.get({ data ->
            _notification.postValue(data)
        }, {
            onErrorMessage(it)
        })
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

}