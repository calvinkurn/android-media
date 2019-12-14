package com.tokopedia.notifcenter.presentation.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.notifcenter.domain.pojo.NotifCenterSendNotifData
import com.tokopedia.notifcenter.domain.pojo.NotificationUpdateUnread

interface NotificationActivityContract {

    interface View : CustomerView {
        fun resetCounterNotificationUpdate()
        fun goToUpdateTab()
    }

    interface Presenter : CustomerPresenter<View> {
        fun clearNotifCounter()
        fun getUpdateUnreadCounter(onSuccess: (NotificationUpdateUnread) -> Unit)
        fun getIsTabUpdate(context: Context)
        fun sendNotif(onSuccessSendNotif: (NotifCenterSendNotifData) -> Unit, onErrorSendNotif: (Throwable) -> Unit)
    }
}
