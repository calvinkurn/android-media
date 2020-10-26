package com.tokopedia.notifcenter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.notifcenter.domain.NotifcenterDetailUseCase
import com.tokopedia.notifcenter.presentation.adapter.typefactory.notification.NotificationTypeFactory
import com.tokopedia.notifcenter.util.coroutines.DispatcherProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class NotificationViewModel @Inject constructor(
        private val notifcenterDetailUseCase: NotifcenterDetailUseCase,
        dispatcher: DispatcherProvider
) : BaseViewModel(dispatcher.io()) {

    private val _mutateNotificationItems = MutableLiveData<Result<List<Visitable<NotificationTypeFactory>>>>()
    val notificationItems: LiveData<Result<List<Visitable<NotificationTypeFactory>>>>
        get() = _mutateNotificationItems

    fun loadNotification(page: Int) {
        notifcenterDetailUseCase.getNotifications(page,
                {
                    _mutateNotificationItems.value = Success(it)
                },
                {
                    _mutateNotificationItems.value = Fail(it)
                }
        )
    }

}