package com.tokopedia.home.account.presentation.presenter

import com.tokopedia.home.account.domain.GetPushNotifCheckerStatusUseCase
import com.tokopedia.home.account.presentation.PushNotifCheckerContract

class PushNotifCheckerPresenter(
        private val getPushNotifCheckerStatusUseCase: GetPushNotifCheckerStatusUseCase
) : PushNotifCheckerContract.Presenter {

    private var view: PushNotifCheckerContract.View? = null

    override fun getStatusPushNotifChecker(query: String) {

    }

    override fun attachView(view: PushNotifCheckerContract.View?) {
        this.view = view
    }

    override fun detachView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}