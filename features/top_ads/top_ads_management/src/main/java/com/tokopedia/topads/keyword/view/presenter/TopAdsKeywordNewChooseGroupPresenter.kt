package com.tokopedia.topads.keyword.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.topads.dashboard.data.model.data.GroupAd
import com.tokopedia.topads.group.domain.usecase.TopAdsSearchGroupAdUseCase
import com.tokopedia.topads.keyword.view.listener.TopAdsKeywordNewChooseGroupView
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TopAdsKeywordNewChooseGroupPresenter
    @Inject constructor(val topAdsSearchGroupAdUseCase: TopAdsSearchGroupAdUseCase,
                        val userSession: UserSessionInterface): BaseDaggerPresenter<TopAdsKeywordNewChooseGroupView>() {

    companion object {
        private val TIMEOUT = 300L
    }

    private val searchGroupName: BehaviorSubject<String> = BehaviorSubject.create()
    private val subscriptionSearchGroupName: Subscription

    init {
        subscriptionSearchGroupName = searchGroupName.debounce(TIMEOUT, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({  topAdsSearchGroupAdUseCase.execute(TopAdsSearchGroupAdUseCase
                        .createRequestParams(it, userSession.shopId), getSubscriberSearchGroupName())
                }, {}, {} )
    }

    private fun getSubscriberSearchGroupName(): Subscriber<List<GroupAd>> {
        return object : Subscriber<List<GroupAd>>() {
            override fun onNext(groupAds: List<GroupAd>?) {
                view.onGetGroupAdList(groupAds)
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                if (!isViewAttached){
                    return
                }
                view.onGetGroupAdListError(e)
            }

        }
    }

    override fun detachView() {
        super.detachView()
        subscriptionSearchGroupName.unsubscribe()
    }

    fun searchGroupName(keyword: String) = searchGroupName.onNext(keyword)
}
