package com.tokopedia.shop.feed.view.presenter

import android.text.TextUtils
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.affiliatecommon.domain.DeletePostUseCase
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.config.GlobalConfig
import com.tokopedia.feedcomponent.analytics.topadstracker.SendTopAdsUseCase
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.feedcomponent.domain.usecase.GetDynamicFeedUseCase
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kolcommon.data.pojo.follow.FollowKolQuery
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase
import com.tokopedia.kolcommon.view.listener.KolPostLikeListener
import com.tokopedia.kolcommon.view.subscriber.LikeKolPostSubscriber
import com.tokopedia.network.constant.ErrorNetMessage
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.shop.feed.domain.DynamicFeedShopDomain
import com.tokopedia.shop.feed.domain.usecase.GetFeedShopFirstUseCase
import com.tokopedia.shop.feed.view.contract.FeedShopContract
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by yfsx on 08/05/19.
 */
class FeedShopPresenter @Inject constructor(
        private val getDynamicFeedFirstUseCase: GetFeedShopFirstUseCase,
        private val getDynamicFeedUseCase: GetDynamicFeedUseCase,
        private val followKolPostGqlUseCase: FollowKolPostGqlUseCase,
        private val likeKolPostUseCase: LikeKolPostUseCase,
        private val deletePostUseCase: DeletePostUseCase,
        private val trackAffiliateClickUseCase: TrackAffiliateClickUseCase,
        private val atcUseCase: AddToCartUseCase,
        private val sendTopAdsUseCase: SendTopAdsUseCase
):
        BaseDaggerPresenter<FeedShopContract.View>(),
        FeedShopContract.Presenter {

    override var cursor: String = ""

    companion object {
        const val FOLLOW_SUCCESS = 1
    }

    override fun detachView() {
        super.detachView()
        getDynamicFeedFirstUseCase.unsubscribe()
        getDynamicFeedUseCase.unsubscribe()
        followKolPostGqlUseCase.unsubscribe()
        likeKolPostUseCase.unsubscribe()
        deletePostUseCase.unsubscribe()
        trackAffiliateClickUseCase.unsubscribe()
    }

    override fun getFeedFirstPage(shopId: String, isPullToRefresh: Boolean) {
        cursor = ""
        if (!getUserId().equals("0")) {
            getDynamicFeedFirstUseCase.execute(
                    GetFeedShopFirstUseCase.createRequestParams(getUserId(), shopId, isPullToRefresh),
                    object : Subscriber<DynamicFeedShopDomain>() {
                        override fun onNext(t: DynamicFeedShopDomain?) {
                            t?.let {
                                cursor = t.dynamicFeedDomainModel.cursor
                                view.onSuccessGetFeedFirstPage(t.dynamicFeedDomainModel.postList, t.dynamicFeedDomainModel.cursor, t.whitelistDomain)
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
        } else {
            getFeed(shopId)
        }
    }

    override fun getFeed(shopId: String) {
        getDynamicFeedUseCase.execute(
                GetDynamicFeedUseCase.createRequestParams(
                        userId = getUserId(),
                        cursor = cursor,
                        source = GetDynamicFeedUseCase.FeedV2Source.Shop,
                        sourceId = shopId),
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

    override fun followKol(id: Int) {
        followKolPostGqlUseCase.clearRequest()
        followKolPostGqlUseCase.addRequest(
                followKolPostGqlUseCase.getRequest(id, FollowKolPostGqlUseCase.PARAM_FOLLOW)
        )
        followKolPostGqlUseCase.execute(object : Subscriber<GraphqlResponse>() {

            override fun onError(throwable: Throwable?) {

                if (isViewAttached) {
                    if (GlobalConfig.isAllowDebuggingTools()) {
                        throwable?.printStackTrace()
                    }
                    view.onErrorFollowKol(
                            ErrorHandler.getErrorMessage(view.context, throwable)
                    )
                }
            }

            override fun onCompleted() {
            }

            override fun onNext(response: GraphqlResponse) {
                val query: FollowKolQuery? = response.getData(FollowKolQuery::class.java)

                if (query == null) {
                    onError(RuntimeException())
                    return
                }
                if (!TextUtils.isEmpty(query.data.error)) {
                    view.onErrorFollowKol(query.data.error)
                    return
                }

                val isSuccess = query.data.data.status == FOLLOW_SUCCESS
                if (isSuccess) {
                    view.onSuccessFollowKol()
                } else {
                    view.onErrorFollowKol(ErrorNetMessage.MESSAGE_ERROR_DEFAULT)
                }
            }
        })
    }

    override fun unfollowKol(id: Int) {
        followKolPostGqlUseCase.clearRequest()
        followKolPostGqlUseCase.addRequest(
                followKolPostGqlUseCase.getRequest(id, FollowKolPostGqlUseCase.PARAM_UNFOLLOW)
        )
        followKolPostGqlUseCase.execute(object : Subscriber<GraphqlResponse>() {
            override fun onCompleted() {

            }

            override fun onError(throwable: Throwable?) {

                if (isViewAttached) {
                    if (GlobalConfig.isAllowDebuggingTools()) {
                        throwable?.printStackTrace()
                    }
                    view.onErrorFollowKol(
                            ErrorHandler.getErrorMessage(view.context, throwable)
                    )
                }
            }

            override fun onNext(response: GraphqlResponse) {
                val query: FollowKolQuery? = response.getData(FollowKolQuery::class.java)

                if (query == null) {
                    onError(RuntimeException())
                    return
                }
                if (!TextUtils.isEmpty(query.data.error)) {
                    view.onErrorFollowKol(query.data.error)
                    return
                }

                val isSuccess = query.data.data.status == FOLLOW_SUCCESS
                if (isSuccess) {
                    view.onSuccessFollowKol()
                } else {
                    view.onErrorFollowKol(ErrorNetMessage.MESSAGE_ERROR_DEFAULT)
                }
            }
        })
    }

    override fun likeKol(id: Int, rowNumber: Int, likeListener: KolPostLikeListener) {
        if (isViewAttached) {
            likeKolPostUseCase.execute(
                    LikeKolPostUseCase.getParam(id, LikeKolPostUseCase.LikeKolPostAction.Like),
                    LikeKolPostSubscriber(likeListener, rowNumber, LikeKolPostUseCase.LikeKolPostAction.Like)
            )
        }
    }

    override fun unlikeKol(id: Int, rowNumber: Int, likeListener: KolPostLikeListener) {
        if (isViewAttached) {
            likeKolPostUseCase.execute(
                    LikeKolPostUseCase.getParam(id, LikeKolPostUseCase.LikeKolPostAction.Unlike),
                    LikeKolPostSubscriber(likeListener, rowNumber, LikeKolPostUseCase.LikeKolPostAction.Unlike)
            )
        }
    }

    override fun deletePost(id: Int, rowNumber: Int) {
        deletePostUseCase.execute(
                DeletePostUseCase.createRequestParams(id.toString()),
                object : Subscriber<Boolean>() {
                    override fun onNext(isSuccess: Boolean?) {
                        if (isSuccess == null || isSuccess.not()) {
                            onError(RuntimeException())
                            return
                        }
                        view.onSuccessDeletePost(rowNumber)
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        if (isViewAttached) {

                            if (GlobalConfig.isAllowDebuggingTools()) {
                                e?.printStackTrace()
                            }
                            view.onErrorDeletePost(ErrorHandler.getErrorMessage(view.context, e), id, rowNumber)
                        }
                    }
                }
        )
    }

    override fun trackPostClick(uniqueTrackingId: String, redirectLink: String) {
        trackAffiliateClickUseCase.execute(
                TrackAffiliateClickUseCase.createRequestParams(
                        uniqueTrackingId,
                        view.userSession.deviceId,
                        if (view.userSession.isLoggedIn) view.userSession.userId else "0"
                ),
                object : Subscriber<Boolean>() {
                    override fun onNext(isSuccess: Boolean?) {

                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        if (GlobalConfig.isAllowDebuggingTools()) {
                            e?.printStackTrace()
                        }
                    }
                }
        )
    }

    override fun trackPostClickUrl(url: String) {
        trackAffiliateClickUseCase.execute(
                TrackAffiliateClickUseCase.createRequestParams(url),
                object : Subscriber<Boolean>() {
                    override fun onNext(isSuccess: Boolean?) {

                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        if (GlobalConfig.isAllowDebuggingTools()) {
                            e?.printStackTrace()
                        }
                    }
                }
        )
    }

    override fun addPostTagItemToCart(postTagItem: PostTagItem) {
        if (postTagItem.shop.isNotEmpty()) {
            atcUseCase.execute(
                    AddToCartUseCase.getMinimumParams(postTagItem.id, postTagItem.shop.first().shopId, productName = postTagItem.text,
                            price = postTagItem.price, userId = getUserId()),
                    object : Subscriber<AddToCartDataModel>() {
                        override fun onNext(model: AddToCartDataModel?) {
                            if (model?.data?.success != 1) {
                                view.onAddToCartFailed(postTagItem.applink)
                            } else {
                                view.onAddToCartSuccess()
                            }
                        }

                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable?) {
                            if (GlobalConfig.isAllowDebuggingTools()) e?.printStackTrace()
                            view.onAddToCartFailed(postTagItem.applink)
                        }
                    }
            )
        } else {
            view.onAddToCartFailed(postTagItem.applink)
        }
    }

    override fun clearCache() {
        getDynamicFeedFirstUseCase.clearFeedFirstCache()
    }

    override fun doTopAdsTracker(url: String, shopId: String, shopName: String, imageUrl: String, isClick: Boolean) {
        if (isClick) {
            sendTopAdsUseCase.hitClick(url, shopId, shopName, imageUrl)
        } else {
            sendTopAdsUseCase.hitImpressions(url, shopId, shopName, imageUrl)
        }
    }



    private fun getUserId(): String {
        var userId = "0"
        if (view.userSession.userId.isNotEmpty()) {
            userId = view.userSession.userId
        }
        return userId
    }
}