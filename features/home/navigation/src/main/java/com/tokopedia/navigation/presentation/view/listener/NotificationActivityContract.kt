package com.tokopedia.navigation.presentation.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

interface NotificationActivityContract {

    interface View: CustomerView {

    }

    interface Presenter: CustomerPresenter<View> {
        fun clearNotifCounter()
    }
}
