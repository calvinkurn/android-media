package com.tokopedia.notifcenter.presentation.contract

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.notifcenter.data.entity.NotifCenterSendNotifData
import com.tokopedia.notifcenter.data.entity.NotificationUpdateUnread

interface NotificationActivityContract {
    interface View : CustomerView {
        fun resetCounterNotificationUpdate()
        fun goToUpdateTab()
    }

    interface Presenter : CustomerPresenter<View> {
        fun clearNotifCounter()
        fun getIsTabUpdate(context: Context)
        fun getUpdateUnreadCounter(onSuccess: (NotificationUpdateUnread) -> Unit)
        fun sendNotif(onSuccessSendNotif: () -> Unit, onErrorSendNotif: () -> Unit)
    }
}
