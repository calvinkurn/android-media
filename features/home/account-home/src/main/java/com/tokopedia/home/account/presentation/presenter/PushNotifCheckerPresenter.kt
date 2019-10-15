package com.tokopedia.home.account.presentation.presenter

import com.tokopedia.home.account.data.model.PushNotifCheckerResponse
import com.tokopedia.home.account.domain.GetPushNotifCheckerStatusUseCase
import com.tokopedia.home.account.presentation.PushNotifCheckerContract
import com.tokopedia.usecase.RequestParams
import rx.Subscriber

class PushNotifCheckerPresenter(
        private val getPushNotifCheckerStatusUseCase: GetPushNotifCheckerStatusUseCase
) : PushNotifCheckerContract.Presenter {

    private var view: PushNotifCheckerContract.View? = null

    override fun getStatusPushNotifChecker() {
        view?.let {
            getPushNotifCheckerStatusUseCase.execute(RequestParams.EMPTY, object : Subscriber<PushNotifCheckerResponse>() {
                override fun onNext(t: PushNotifCheckerResponse?) {
                    if (t != null) {
                        view?.onSuccessGetStatusPushNotifChecker(t.notifierSendTroubleshooter)
                    }
                }

                override fun onCompleted() {
                }

                override fun onError(e: Throwable?) {
                    view?.onErrorGetPushNotifChecker(e.toString())
                }

            })
        }
    }

    override fun attachView(view: PushNotifCheckerContract.View?) {
        this.view = view
    }

    override fun detachView() {
        getPushNotifCheckerStatusUseCase.unsubscribe()
        view = null
    }

}