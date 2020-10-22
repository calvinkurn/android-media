package com.tokopedia.home.account.presentation.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.home.account.domain.SendNotifUseCase
import com.tokopedia.home.account.presentation.listener.RedDotGimmickView

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