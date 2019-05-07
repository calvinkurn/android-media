package com.tokopedia.navigation.presentation.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.navigation.domain.pojo.NotificationUpdateTotalUnread
import com.tokopedia.navigation.domain.pojo.NotificationUpdateUnread

interface NotificationActivityContract {

    interface View: CustomerView {

    }

    interface Presenter: CustomerPresenter<View> {
        fun clearNotifCounter()
        fun getTotalUnreadCounter(onSuccessGetTotalUnreadCounter: (NotificationUpdateTotalUnread) -> Unit)
        fun getUpdateUnreadCounter(onSuccess: (NotificationUpdateUnread) -> Unit)
    }
}
