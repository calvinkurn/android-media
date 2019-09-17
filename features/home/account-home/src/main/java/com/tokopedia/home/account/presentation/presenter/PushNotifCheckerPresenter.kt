package com.tokopedia.home.account.presentation.presenter

import android.util.Log
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
                    Log.d("onNext: ", t.toString())
                    if (t != null) {
                        view?.onSuccessGetStatusPushNotifChecker(t.notifierSendTroubleshooter)
                    }
                }

                override fun onCompleted() {
                    Log.d("onCompleted: ", "onCompleted")
                }

                override fun onError(e: Throwable?) {
                    Log.d("onError: ", e.toString())
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