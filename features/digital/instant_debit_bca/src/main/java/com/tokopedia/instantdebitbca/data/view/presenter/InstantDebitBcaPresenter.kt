package com.tokopedia.instantdebitbca.data.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.instantdebitbca.data.domain.GetAccessTokenBcaUseCase
import com.tokopedia.instantdebitbca.data.domain.NotifyDebitRegisterBcaUseCase
import com.tokopedia.instantdebitbca.data.domain.NotifyDebitRegisterEditLimit
import com.tokopedia.instantdebitbca.data.view.interfaces.InstantDebitBcaContract
import com.tokopedia.instantdebitbca.data.view.model.NotifyDebitRegisterBca
import com.tokopedia.instantdebitbca.data.view.model.TokenInstantDebitBca

import javax.inject.Inject

import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

/**
 * Created by nabillasabbaha on 25/03/19.
 */
class InstantDebitBcaPresenter @Inject
constructor(val getAccessTokenBcaUseCase: GetAccessTokenBcaUseCase,
            val notifyDebitRegisterBcaUseCase: NotifyDebitRegisterBcaUseCase,
            val notifyDebitRegisterEditLimit: NotifyDebitRegisterEditLimit) : BaseDaggerPresenter<InstantDebitBcaContract.View>(), InstantDebitBcaContract.Presenter {

    private val compositeSubscription: CompositeSubscription

    init {
        this.compositeSubscription = CompositeSubscription()
    }

    override fun getAccessTokenBca() {
        compositeSubscription.add(
                getAccessTokenBcaUseCase.createObservable(getAccessTokenBcaUseCase.createRequestParam())
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<TokenInstantDebitBca> {
                            override fun onCompleted() {

                            }

                            override fun onError(e: Throwable) {
                                if (isViewAttached) {
                                    view.showErrorMessage(e)
                                }
                            }

                            override fun onNext(tokenInstantDebitBca: TokenInstantDebitBca) {
                                view.openWidgetBca(tokenInstantDebitBca.accessToken ?: "")
                            }
                        }))
    }

    override fun notifyDebitRegisterBca(debitData: String, deviceId: String) {
        compositeSubscription.add(
                notifyDebitRegisterBcaUseCase.createObservable(notifyDebitRegisterBcaUseCase.createRequestParam(debitData, deviceId))
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<NotifyDebitRegisterBca> {
                            override fun onCompleted() {

                            }

                            override fun onError(e: Throwable) {
                                if (isViewAttached) {
                                    view.redirectPageAfterRegisterBca()
                                }
                            }

                            override fun onNext(notifyDebitRegisterBca: NotifyDebitRegisterBca) {
                                view.redirectPageAfterRegisterBca()
                            }
                        }))
    }

    override fun notifyDebitRegisterEditLimit(debitData: String, deviceId: String) {
        compositeSubscription.add(
                notifyDebitRegisterEditLimit.createObservable(notifyDebitRegisterEditLimit.createRequestParam(debitData, deviceId))
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<NotifyDebitRegisterBca> {
                            override fun onCompleted() {

                            }

                            override fun onError(e: Throwable) {
                                if (isViewAttached) {
                                    view.redirectPageAfterRegisterBca()
                                }
                            }

                            override fun onNext(notifyDebitRegisterBca: NotifyDebitRegisterBca) {
                                view.redirectPageAfterRegisterBca()
                            }
                        }))
    }

    override fun onDestroy() {
        detachView()
        if (compositeSubscription.hasSubscriptions()) compositeSubscription.unsubscribe()
    }
}
