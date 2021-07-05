package com.tokopedia.notifcenter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.HttpErrorException
import com.tokopedia.abstraction.common.network.exception.ResponseDataNullException
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.network.constant.ErrorNetMessage
import com.tokopedia.notifcenter.data.entity.NotificationEntity
import com.tokopedia.notifcenter.data.entity.NotificationUpdateFilter
import com.tokopedia.notifcenter.data.mapper.GetNotificationUpdateFilterMapper
import com.tokopedia.notifcenter.data.mapper.GetNotificationUpdateMapper
import com.tokopedia.notifcenter.data.model.NotificationViewData
import com.tokopedia.notifcenter.data.viewbean.NotificationFilterSectionViewBean
import com.tokopedia.notifcenter.domain.*
import com.tokopedia.notifcenter.presentation.subscriber.GetNotificationTotalUnreadSubscriber
import com.tokopedia.notifcenter.presentation.subscriber.NotificationUpdateActionSubscriber
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

interface NotificationTransactionContract {
    fun getNotification(lastNotificationId: String)
    fun updateNotificationFilter(filter: HashMap<String, Int>)
    fun markReadNotification(notificationId: String)
    fun markAllReadNotification()
    fun onErrorMessage(throwable: Throwable)
    fun getTotalUnreadNotification()
    fun getInfoStatusNotification()
    fun resetNotificationFilter()
    fun getNotificationFilter()
    fun cleared()
}

typealias FilterWrapper = NotificationFilterSectionViewBean

class NotificationTransactionViewModel @Inject constructor(
        private val notificationInfoTransactionUseCase: NotificationInfoTransactionUseCase,
        private var markAllReadNotificationUseCase: MarkAllReadNotificationUpdateUseCase,
        private var getNotificationTotalUnreadUseCase: GetNotificationTotalUnreadUseCase,
        private var notificationTransactionUseCase: NotificationTransactionUseCase,
        private var markNotificationUseCase: MarkReadNotificationUpdateItemUseCase,
        private var notificationFilterMapper: GetNotificationUpdateFilterMapper,
        private var notificationFilterUseCase: NotificationFilterUseCase,
        private var notificationMapper: GetNotificationUpdateMapper,
        dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.io), NotificationTransactionContract {

    private val _notification = MediatorLiveData<NotificationViewData>()
    val notification: LiveData<NotificationViewData> get() = _notification

    private val _infoNotification = MediatorLiveData<NotificationEntity>()
    val infoNotification: LiveData<NotificationEntity> get() = _infoNotification

    private val _filterNotification = MutableLiveData<NotificationFilterSectionViewBean>()
    val filterNotification: LiveData<NotificationFilterSectionViewBean> get() = _filterNotification

    private val _markAllNotification = MutableLiveData<Boolean>()
    val markAllNotification: LiveData<Boolean> get() = _markAllNotification

    private val _totalUnreadNotification = MutableLiveData<Long>()
    val totalUnreadNotification: LiveData<Long> get() = _totalUnreadNotification

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

    //filter notification param
    var filterNotificationParams = NotificationFilterUseCase.params()

    init {
        _notification.addSource(_filterNotification) {
            getTotalUnreadNotification()
            getNotification(_lastNotificationId.value?: "")
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

    override fun getNotification(lastNotificationId: String) {
        val params = NotificationTransactionUseCase.params(
                FIRST_INITIAL_PAGE,
                variables,
                lastNotificationId)

        notificationTransactionUseCase.get(params, {
            val data = notificationMapper.map(it)
            _hasNotification.value = data.list.isNotEmpty()
            _notification.postValue(data)
        }, {
            onErrorMessage(it)
        })
    }

    override fun getTotalUnreadNotification() {
        getNotificationTotalUnreadUseCase.execute(
                GetNotificationTotalUnreadUseCase.getRequestParams(TYPE_OF_NOTIF_TRANSACTION),
                GetNotificationTotalUnreadSubscriber({
                    _totalUnreadNotification.value = it.pojo.notifUnreadInt
                })
        )
    }

    override fun markReadNotification(notificationId: String) {
        markNotificationUseCase.execute(
                MarkReadNotificationUpdateItemUseCase.getRequestParams(
                        notificationId,
                        TYPE_OF_NOTIF_TRANSACTION),
                NotificationUpdateActionSubscriber())
    }

    private fun filterMapToDataView(it: NotificationUpdateFilter): NotificationFilterSectionViewBean {
        return FilterWrapper(notificationFilterMapper.mapToFilter(it))
    }

    override fun markAllReadNotification() {
        markAllReadNotificationUseCase.execute(
                MarkAllReadNotificationUpdateUseCase.params(TYPE_OF_NOTIF_TRANSACTION),
                NotificationUpdateActionSubscriber({
                    _markAllNotification.postValue(true)
                })
        )
    }

    override fun getNotificationFilter() {
        if (filterNotificationParams.isEmpty()) return
        notificationFilterUseCase.get(filterNotificationParams, {
            _filterNotification.value = filterMapToDataView(it)
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

    override fun cleared() {
        onCleared()
    }

    companion object {
        private const val FIRST_INITIAL_PAGE = 1
        private const val TYPE_OF_NOTIF_TRANSACTION = 2
    }

}