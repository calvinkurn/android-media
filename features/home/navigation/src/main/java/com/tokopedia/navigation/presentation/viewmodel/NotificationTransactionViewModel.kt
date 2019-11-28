package com.tokopedia.navigation.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.constant.ErrorNetMessage
import com.tokopedia.abstraction.common.network.exception.HttpErrorException
import com.tokopedia.abstraction.common.network.exception.ResponseDataNullException
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.navigation.data.entity.NotificationEntity
import com.tokopedia.navigation.data.mapper.GetNotificationUpdateFilterMapper
import com.tokopedia.navigation.data.mapper.GetNotificationUpdateMapper
import com.tokopedia.navigation.domain.MarkReadNotificationUpdateItemUseCase
import com.tokopedia.navigation.domain.NotificationFilterUseCase
import com.tokopedia.navigation.domain.NotificationInfoTransactionUseCase
import com.tokopedia.navigation.domain.NotificationTransactionUseCase
import com.tokopedia.navigation.domain.model.NotificationFilterSectionWrapper
import com.tokopedia.navigation.domain.model.TransactionNotification
import com.tokopedia.navigation.presentation.view.subscriber.NotificationUpdateActionSubscriber
import com.tokopedia.navigation.util.coroutines.DispatcherProvider
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

interface NotificationTransactionContract {
    fun getTransactionNotification(lastNotificationId: String)
    fun updateNotificationFilter(filter: HashMap<String, Int>)
    fun markReadNotification(notificationId: String)
    fun onErrorMessage(throwable: Throwable)
    fun getInfoStatusNotification()
    fun resetNotificationFilter()
    fun getNotificationFilter()
}

class NotificationTransactionViewModel @Inject constructor(
        private val notificationInfoTransactionUseCase: NotificationInfoTransactionUseCase,
        private var notificationTransactionUseCase: NotificationTransactionUseCase,
        private var markNotificationUseCase: MarkReadNotificationUpdateItemUseCase,
        private var notificationFilterUseCase: NotificationFilterUseCase,
        private var notificationFilterMapper : GetNotificationUpdateFilterMapper,
        private var notificationMapper: GetNotificationUpdateMapper,
        dispatcher: DispatcherProvider
): BaseViewModel(dispatcher.io()), NotificationTransactionContract {

    private val _notification = MediatorLiveData<TransactionNotification>()
    val notification: LiveData<TransactionNotification> get() = _notification

    private val _infoNotification = MediatorLiveData<NotificationEntity>()
    val infoNotification: LiveData<NotificationEntity> get() = _infoNotification

    private val _filterNotification = MediatorLiveData<NotificationFilterSectionWrapper>()
    val filterNotification: LiveData<NotificationFilterSectionWrapper> get() = _filterNotification

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    //filtering variables
    var variables: HashMap<String, Any> = HashMap()

    //last notification id
    private val _lastNotificationId = MutableLiveData<String>()
    val lastNotificationId: LiveData<String> get() = _lastNotificationId

    //check if user has notification
    private val _hasNotification = MutableLiveData<Boolean>()
    val hasNotification: LiveData<Boolean> get() = _hasNotification

    init {
        _filterNotification.addSource(_infoNotification) {
            getNotificationFilter()
        }

        _notification.addSource(_filterNotification) {
            getTransactionNotification(_lastNotificationId.value?: "")
        }
    }

    fun setLastNotificationId(id: String) {
        _lastNotificationId.postValue(id)
    }

    override fun getInfoStatusNotification() {
        notificationInfoTransactionUseCase.get({ data ->
            _infoNotification.value = data
        }, {
            onErrorMessage(it)
        })
    }

    override fun getTransactionNotification(lastNotificationId: String) {
        val params = NotificationTransactionUseCase.params(
                FIRST_INITIAL_PAGE,
                variables,
                lastNotificationId)

        notificationTransactionUseCase.get(params, {
            val data = notificationMapper.mapToNotifTransaction(it)
            _hasNotification.postValue(data.list.isNotEmpty())
            _notification.postValue(data)
        }, {
            onErrorMessage(it)
        })
    }

    override fun markReadNotification(notificationId: String) {
        markNotificationUseCase.execute(
                MarkReadNotificationUpdateItemUseCase.getRequestParams(notificationId),
                NotificationUpdateActionSubscriber())
    }

    override fun getNotificationFilter() {
        val params = NotificationFilterUseCase.params()
        notificationFilterUseCase.get(params, {
            val notificationFilter = NotificationFilterSectionWrapper(notificationFilterMapper.mapToFilter(it))
            _filterNotification.postValue(notificationFilter)
        }, {
            onErrorMessage(it)
        })
    }

    override fun updateNotificationFilter(filter: HashMap<String, Int>) {
        resetNotificationFilter()
        variables.putAll(filter)
    }

    override fun resetNotificationFilter() {
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