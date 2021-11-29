package com.tokopedia.home_account.account_settings.presentation.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.home_account.account_settings.domain.SendNotifUseCase
import com.tokopedia.home_account.account_settings.presentation.listener.RedDotGimmickView

class RedDotGimmickPresenter(private val sendNotifUseCase: SendNotifUseCase) : BaseDaggerPresenter<RedDotGimmickView>() {

    fun sendNotif() {
        sendNotifUseCase.executeCoroutines(onSuccess = {
            view.onSuccessSendNotif()
        }, onError = {
            view.onErrorSendNotif(it)
        })
    }

    override fun detachView() {
        super.detachView()
        sendNotifUseCase.cancelJobs()
    }

}