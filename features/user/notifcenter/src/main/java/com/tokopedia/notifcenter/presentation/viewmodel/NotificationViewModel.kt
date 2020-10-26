package com.tokopedia.notifcenter.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.notifcenter.domain.NotifcenterDetailUseCase
import com.tokopedia.notifcenter.util.coroutines.DispatcherProvider
import javax.inject.Inject

class NotificationViewModel @Inject constructor(
        private val notifcenterDetailUseCase: NotifcenterDetailUseCase,
        dispatcher: DispatcherProvider
) : BaseViewModel(dispatcher.io()) {

    fun loadNotification(page: Int) {
        notifcenterDetailUseCase.getNotifications(page,
                {
                    println("success")
                },
                {
                    println("error")
                }
        )
    }

}