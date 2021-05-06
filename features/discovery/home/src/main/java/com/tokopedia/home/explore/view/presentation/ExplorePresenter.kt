package com.tokopedia.home.explore.view.presentation

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.home.constant.ConstantKey
import com.tokopedia.home.explore.domain.GetExploreDataUseCase
import com.tokopedia.home.explore.domain.GetExploreLocalDataUseCase
import com.tokopedia.home.explore.view.adapter.datamodel.ExploreSectionDataModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSession
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import rx.subscriptions.Subscriptions
import javax.inject.Inject

/**
 * Created by Lukas on 12/18/20.
 */
class ExplorePresenter @Inject constructor(
        private val dataUseCase: GetExploreDataUseCase,
        private val localDataUseCase: GetExploreLocalDataUseCase,
        private val userSession: UserSession
): BaseDaggerPresenter<ExploreContract.View>(), ExploreContract.Presenter {

    val compositeSubscription: CompositeSubscription = CompositeSubscription()
    var subscription: Subscription = Subscriptions.empty()

    override fun detachView() {
        super.detachView()
        if (!compositeSubscription.isUnsubscribed) {
            compositeSubscription.unsubscribe()
        }
    }

    override fun getData() {
        subscription = localDataUseCase.getExecuteObservable(RequestParams.EMPTY)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(refreshAction())
                .onErrorResumeNext(getDataFromNetwork())
                .subscribe(getSubscriber())
        compositeSubscription.add(subscription)
    }


    private fun refreshAction(): Action1<List<ExploreSectionDataModel?>?> {
        return Action1 { compositeSubscription.add(getDataFromNetwork().subscribe(getSubscriber())) }
    }

    private fun getSubscriber(): Subscriber<List<ExploreSectionDataModel?>?> {
        return object : Subscriber<List<ExploreSectionDataModel?>?>() {
            override fun onStart() {
                if (isViewAttached) {
                    view.showLoading()
                }
            }

            override fun onCompleted() {
                if (isViewAttached) {
                    view.hideLoading()
                }
            }

            override fun onError(e: Throwable) {
                if (isViewAttached) {
                    view.showNetworkError(ErrorHandler.getErrorMessage(view.viewContext,
                            e))
                    onCompleted()
                }
            }

            override fun onNext(t: List<ExploreSectionDataModel?>?) {
                if (isViewAttached) {
                    view.renderData(t)
                }
            }
        }
    }

    private fun getDataFromNetwork(): Observable<List<ExploreSectionDataModel?>?> {
        val requestParams = RequestParams.create()
        requestParams.putString(ConstantKey.RequestKey.USER_ID, userSession.userId)
        return dataUseCase.getExecuteObservable(requestParams)
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}