package com.tokopedia.instantdebitbca

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.instantdebitbca.domain.GetAccessTokenUseCase
import com.tokopedia.instantdebitbca.view.TokenInstantDebitBca
import com.tokopedia.usecase.RequestParams
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 21/03/19.
 */
class InstantDebitBcaPresenter @Inject constructor(val getAccessTokenUseCase: GetAccessTokenUseCase)
    : BaseDaggerPresenter<InstantDebitBcaContract.View>(), InstantDebitBcaContract.Presenter {

    private val compositeSubscription: CompositeSubscription

    init {
        compositeSubscription = CompositeSubscription()
    }

    override fun getAccessTokenInstantDebitBca() {
        compositeSubscription.add(
                getAccessTokenUseCase.createObservable(RequestParams.EMPTY)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Subscriber<TokenInstantDebitBca>() {
                            override fun onNext(token: TokenInstantDebitBca) {
                                view.openWidgetBca(token.accessToken)
                            }

                            override fun onCompleted() {

                            }

                            override fun onError(e: Throwable) {
                                view.showErrorMessage(e)
                            }
                        })
        )
    }

    override fun onDestroy() {
        detachView()
        if (compositeSubscription.hasSubscriptions()) compositeSubscription.unsubscribe()
    }
}