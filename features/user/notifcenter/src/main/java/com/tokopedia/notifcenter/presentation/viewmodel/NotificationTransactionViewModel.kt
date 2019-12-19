package com.tokopedia.notifcenter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.constant.ErrorNetMessage
import com.tokopedia.abstraction.common.network.exception.HttpErrorException
import com.tokopedia.abstraction.common.network.exception.ResponseDataNullException
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.notifcenter.data.entity.NotificationEntity
import com.tokopedia.notifcenter.data.mapper.GetNotificationUpdateFilterMapper
import com.tokopedia.notifcenter.data.mapper.GetNotificationUpdateMapper
import com.tokopedia.notifcenter.domain.MarkReadNotificationUpdateItemUseCase
import com.tokopedia.notifcenter.domain.NotificationFilterUseCase
import com.tokopedia.notifcenter.domain.NotificationInfoTransactionUseCase
import com.tokopedia.notifcenter.domain.NotificationTransactionUseCase
import com.tokopedia.notifcenter.domain.model.NotificationFilterSectionWrapper
import com.tokopedia.notifcenter.presentation.view.subscriber.NotificationUpdateActionSubscriber
import com.tokopedia.notifcenter.presentation.view.viewmodel.NotificationViewBean
import com.tokopedia.notifcenter.util.coroutines.DispatcherProvider
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

interface NotificationTransactionContract {
    fun getNotificationViewBean(lastNotificationId: String)
    fun updateNotificationFilter(filter: HashMap<String, Int>)
    fun markReadNotification(notificationId: String)
    fun onErrorMessage(throwable: Throwable)
    fun getInfoStatusNotification()
    fun resetNotificationFilter()
    fun getNotificationFilter()
}

typealias FilterWrapper = NotificationFilterSectionWrapper

class NotificationTransactionViewModel @Inject constructor(
        private val notificationInfoTransactionUseCase: NotificationInfoTransactionUseCase,
        private var notificationTransactionUseCase: NotificationTransactionUseCase,
        private var markNotificationUseCase: MarkReadNotificationUpdateItemUseCase,
        private var notificationFilterUseCase: NotificationFilterUseCase,
        private var notificationFilterMapper : GetNotificationUpdateFilterMapper,
        private var notificationMapper: GetNotificationUpdateMapper,
        dispatcher: DispatcherProvider
): BaseViewModel(dispatcher.io()), NotificationTransactionContract {

    private val _notification = MediatorLiveData<NotificationViewBean>()
    val notification: LiveData<NotificationViewBean> get() = _notification

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
            getNotificationViewBean(_lastNotificationId.value?: "")
        }
    }

    fun setLastNotificationId(id: String) {
        _lastNotificationId.value = id
    }

    override fun getInfoStatusNotification() {
        notificationInfoTransactionUseCase.get({ data ->
            _infoNotification.value = data
        }, {
            onErrorMessage(it)
        })
    }

    override fun getNotificationViewBean(lastNotificationId: String) {
        val params = NotificationTransactionUseCase.params(
                FIRST_INITIAL_PAGE,
                variables,
                lastNotificationId)

        notificationTransactionUseCase.get(params, {
            val data = notificationMapper.mapToNotifTransaction(it)
            _hasNotification.value = data.list.isNotEmpty()
            _notification.postValue(data)
        }, {
            onErrorMessage(it)
        })
    }

    override fun markReadNotification(notificationId: String) {
        markNotificationUseCase.execute(
                MarkReadNotificationUpdateItemUseCase.getRequestParams(
                        notificationId,
                        TYPE_OF_NOTIF_TRANSACTION),
                NotificationUpdateActionSubscriber())
    }

    override fun getNotificationFilter() {
        val params = NotificationFilterUseCase.params()
        notificationFilterUseCase.get(params, {
            val notificationFilter = FilterWrapper(notificationFilterMapper.mapToFilter(it))
            _filterNotification.value = notificationFilter
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
        private const val TYPE_OF_NOTIF_TRANSACTION = 2
    }

}