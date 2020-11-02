package com.tokopedia.inbox.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.inbox.common.InboxCoroutineContextProvider
import com.tokopedia.inbox.domain.data.notification.InboxCounter
import com.tokopedia.inbox.domain.usecase.InboxNotificationUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class InboxViewModel @Inject constructor(
        private val notificationUseCase: InboxNotificationUseCase,
        private val dispatchers: InboxCoroutineContextProvider
) : BaseViewModel(dispatchers.IO) {

    private val _notifications = MutableLiveData<Result<InboxCounter>>()
    val notifications: LiveData<Result<InboxCounter>>
        get() = _notifications

    fun getNotifications() {
        notificationUseCase.getNotification(
                {
                    _notifications.value = Success(it)
                },
                {
                    _notifications.value = Fail(it)
                }
        )
    }

}