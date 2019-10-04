package com.tokopedia.feedplus.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.feedplus.NON_LOGIN_USER_ID
import com.tokopedia.feedplus.view.listener.DynamicFeedContract
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kol.feature.post.domain.usecase.LikeKolPostUseCase
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by yoasfs on 2019-08-06
 */
class DynamicFeedPresenter @Inject constructor(val userSession: UserSessionInterface,
                                               val getDynamicFeedUseCase: GetDynamicFeedUseCase,
                                               val likeKolPostUseCase: LikeKolPostUseCase):
        BaseDaggerPresenter<DynamicFeedContract.View>(),
        DynamicFeedContract.Presenter {


    override var cursor: String = ""

    override fun getFeedFirstPage(isPullToRefresh: Boolean) {
        getDynamicFeedUseCase.graphqlUseCase.setCacheStrategy(
                GraphqlCacheStrategy.Builder(if (isPullToRefresh) CacheType.ALWAYS_CLOUD else CacheType.CACHE_FIRST)
                .setExpiryTime(GraphqlConstant.ExpiryTimes.MINUTE_30.`val`())
                .setSessionIncluded(true)
                .build())
        getDynamicFeedUseCase.execute(
                GetDynamicFeedUseCase.createRequestParams(getUserId(), cursor, GetDynamicFeedUseCase.SOURCE_TRENDING),
                object : Subscriber<DynamicFeedDomainModel>() {
                    override fun onNext(t: DynamicFeedDomainModel?) {
                        t?.let {
                            view.onSuccessGetFeed(t.postList, t.cursor)
                        }
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        if (isViewAttached) {
                            if (GlobalConfig.isAllowDebuggingTools()) {
                                e?.printStackTrace()
                            }
                            view.showGetListError(e)
                        }
                    }
                }
        )
    }

    override fun getFeed() {
        getDynamicFeedUseCase.execute(
                GetDynamicFeedUseCase.createRequestParams(getUserId(), cursor, GetDynamicFeedUseCase.SOURCE_TRENDING),
                object : Subscriber<DynamicFeedDomainModel>() {
                    override fun onNext(t: DynamicFeedDomainModel?) {
                        t?.let {
                            view.onSuccessGetFeed(t.postList, t.cursor)
                        }
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        if (isViewAttached) {
                            if (GlobalConfig.isAllowDebuggingTools()) {
                                e?.printStackTrace()
                            }
                            view.showGetListError(e)
                        }
                    }
                }
        )
    }

    override fun likeKol(id: Int, rowNumber: Int, columnNumber: Int) {
        likeKolPostUseCase.execute(
                LikeKolPostUseCase.getParam(id, LikeKolPostUseCase.ACTION_LIKE),
                object : Subscriber<Boolean>() {
                    override fun onNext(t: Boolean?) {
                        t?.let {
                            view.onSuccessLike(rowNumber, columnNumber)
                        }
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable) {
                        view.onErrorLikeUnlike(e.localizedMessage)
                    }
                }
        )
    }

    override fun unlikeKol(id: Int, rowNumber: Int, columnNumber: Int) {
        likeKolPostUseCase.execute(LikeKolPostUseCase.getParam(id, LikeKolPostUseCase.ACTION_LIKE),
                object : Subscriber<Boolean>() {
                    override fun onNext(t: Boolean?) {
                        t?.let {
                            view.onSuccessUnlike(rowNumber, columnNumber)
                        }
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable) {
                        view.onErrorLikeUnlike(e.localizedMessage)
                    }
                }
        )
    }

    override fun trackPostClick(uniqueTrackingId: String, redirectLink: String) {
    }

    override fun trackPostClickUrl(url: String) {
    }

    override fun attachView(view: DynamicFeedContract.View?) {
        super.attachView(view)
    }

    override fun detachView() {
        super.detachView()
        getDynamicFeedUseCase.unsubscribe()
        likeKolPostUseCase.unsubscribe()
    }

    private fun getUserId(): String {
        return if (userSession.isLoggedIn()) userSession.getUserId() else NON_LOGIN_USER_ID
    }
}