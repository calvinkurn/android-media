package com.tokopedia.home.account.presentation

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.home.account.data.model.PushNotifCheckerResponse
import com.tokopedia.home.account.presentation.listener.BaseAccountView

interface PushNotifCheckerContract {

    interface View : BaseAccountView {
        fun onSuccessGetStatusPushNotifChecker(data: PushNotifCheckerResponse)
    }

    interface Presenter : CustomerPresenter<View> {
        fun getStatusPushNotifChecker(query: String)
    }

}