package com.tokopedia.home.account.presentation

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.home.account.data.model.NotifierSendTroubleshooter
import com.tokopedia.home.account.presentation.listener.BaseAccountView

interface PushNotifCheckerContract {

    interface View : BaseAccountView {
        fun onSuccessGetStatusPushNotifChecker(data: NotifierSendTroubleshooter)
        fun onErrorGetPushNotifChecker(err: String)
    }

    interface Presenter : CustomerPresenter<View> {
        fun getStatusPushNotifChecker()
    }

}