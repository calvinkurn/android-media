package com.tokopedia.profile.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.affiliatecommon.domain.DeletePostUseCase
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.config.GlobalConfig
import com.tokopedia.feedcomponent.analytics.topadstracker.SendTopAdsUseCase
import com.tokopedia.feedcomponent.data.pojo.FeedPostRelated
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.domain.usecase.GetPostStatisticCommissionUseCase
import com.tokopedia.feedcomponent.domain.usecase.GetRelatedPostUseCase
import com.tokopedia.feedcomponent.view.mapper.PostStatisticMapper
import com.tokopedia.feedcomponent.view.subscriber.TrackPostClickSubscriber
import com.tokopedia.feedcomponent.view.viewmodel.statistic.PostStatisticCommissionUiModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.profile.domain.usecase.GetDynamicFeedProfileFirstUseCase
import com.tokopedia.profile.domain.usecase.GetDynamicFeedProfileUseCase
import com.tokopedia.profile.domain.usecase.ShouldChangeUsernameUseCase
import com.tokopedia.profile.view.listener.ProfileContract
import com.tokopedia.profile.view.subscriber.DeletePostSubscriber
import com.tokopedia.profile.view.subscriber.FollowSubscriber
import com.tokopedia.profile.view.subscriber.GetProfileFirstPageSubscriber
import com.tokopedia.profile.view.subscriber.GetProfilePostSubscriber
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by milhamj on 9/21/18.
 */
class ProfilePresenter @Inject constructor(
        private val getDynamicFeedProfileFirstUseCase: GetDynamicFeedProfileFirstUseCase,
        private val getDynamicFeedProfileUseCase: GetDynamicFeedProfileUseCase,
        private val likeKolPostUseCase: com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase,
        private val followKolPostGqlUseCase: com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase,
        private val deletePostUseCase: DeletePostUseCase,
        private val trackAffiliateClickUseCase: TrackAffiliateClickUseCase,
        private val shouldChangeUsernameUseCase: ShouldChangeUsernameUseCase,
        private val getRelatedPostUseCase: GetRelatedPostUseCase,
        private val atcUseCase: AddToCartUseCase,
        private val getPostStatisticCommissionUseCase: GetPostStatisticCommissionUseCase,
        private val sendTopAdsUseCase: SendTopAdsUseCase)
    : BaseDaggerPresenter<ProfileContract.View>(), ProfileContract.Presenter {

    override var cursor: String = ""

    override fun detachView() {
        super.detachView()
        getDynamicFeedProfileFirstUseCase.unsubscribe()
        getDynamicFeedProfileUseCase.unsubscribe()
        likeKolPostUseCase.unsubscribe()
        followKolPostGqlUseCase.unsubscribe()
        deletePostUseCase.unsubscribe()
        trackAffiliateClickUseCase.unsubscribe()
        shouldChangeUsernameUseCase.unsubscribe()
        getRelatedPostUseCase.unsubscribe()
        atcUseCase.unsubscribe()
    }

    override fun getProfileFirstPage(targetUserId: Int, isFromLogin: Boolean) {
        cursor = ""
        getDynamicFeedProfileFirstUseCase.execute(
                GetDynamicFeedProfileFirstUseCase.createRequestParams(getUserId(), targetUserId.toString()),
                GetProfileFirstPageSubscriber(view, isFromLogin)
        )
    }

    override fun getProfilePost(targetUserId: Int) {
        getDynamicFeedProfileUseCase.execute(
                GetDynamicFeedProfileUseCase.createRequestParams(getUserId(), targetUserId.toString(), cursor),
                GetProfilePostSubscriber(view)
        )
    }

    override fun followKol(id: Int, isFollow: Boolean) {
        followKolPostGqlUseCase.clearRequest()
        if (isFollow) {
            followKolPostGqlUseCase.addRequest(followKolPostGqlUseCase.getRequest(id, com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase.PARAM_FOLLOW))
        } else {
            followKolPostGqlUseCase.getRequest(id, com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase.PARAM_UNFOLLOW)
        }
        followKolPostGqlUseCase.execute(FollowSubscriber(view))
    }

    override fun likeKol(id: Int, rowNumber: Int, likeListener: com.tokopedia.kolcommon.view.listener.KolPostLikeListener, isLiked: Boolean) {
        if (isLiked) {
            likeKolPostUseCase.execute(
                    com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase.getParam(id, com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase.LikeKolPostAction.Like),
                    com.tokopedia.kolcommon.view.subscriber.LikeKolPostSubscriber(likeListener, rowNumber, com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase.LikeKolPostAction.Like)
            )
        } else {
            likeKolPostUseCase.execute(
                    com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase.getParam(id, com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase.LikeKolPostAction.Unlike),
                    com.tokopedia.kolcommon.view.subscriber.LikeKolPostSubscriber(likeListener, rowNumber, com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase.LikeKolPostAction.Unlike)
            )
        }
    }

    override fun addPostTagItemToCart(postTagItem: PostTagItem) {
        val isShopNotEmpty = postTagItem.shop.isNotEmpty()
        if (isShopNotEmpty) {
            atcUseCase.execute(
                    AddToCartUseCase.getMinimumParams(postTagItem.id, postTagItem.shop.first().shopId, productName = postTagItem.text, price = postTagItem.price, userId = getUserId()),
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

    override fun deletePost(id: Int, rowNumber: Int) {
        deletePostUseCase.execute(
                DeletePostUseCase.createRequestParams(id.toString()),
                DeletePostSubscriber(view, id, rowNumber)
        )
    }

    override fun trackPostClick(uniqueTrackingId: String, redirectLink: String) {
        trackAffiliateClickUseCase.execute(
                TrackAffiliateClickUseCase.createRequestParams(
                        uniqueTrackingId,
                        view.getUserSession().deviceId,
                        if (view.getUserSession().isLoggedIn) view.getUserSession().userId else "0"
                ),
                TrackPostClickSubscriber()
        )
    }

    override fun trackPostClickUrl(url: String) {
        trackAffiliateClickUseCase.execute(
                TrackAffiliateClickUseCase.createRequestParams(url),
                TrackPostClickSubscriber()
        )
    }

    override fun getRelatedProfile(
            onErrorGetRelatedProfile: ((throwable: Throwable?) -> Unit)?,
            onSuccessGetRelatedProfile: ((feedPostRelated: FeedPostRelated?) -> Unit)?) {
        getRelatedPostUseCase.execute(
                GetRelatedPostUseCase.createRequestParams(""),
                object : Subscriber<FeedPostRelated>() {
                    override fun onCompleted() {}
                    override fun onError(e: Throwable?) {
                        onErrorGetRelatedProfile?.invoke(e)
                    }

                    override fun onNext(feedPostRelated: FeedPostRelated?) {
                        onSuccessGetRelatedProfile?.invoke(feedPostRelated)
                    }
                }
        )
    }

    override fun shouldChangeUsername(userId: Int, link: String) {
        shouldChangeUsernameUseCase.execute(
                ShouldChangeUsernameUseCase.createRequestParams(userId),
                object : Subscriber<Boolean>() {
                    override fun onNext(t: Boolean) {
                        view?.onSuccessShouldChangeUsername(t, link)
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        if (GlobalConfig.isAllowDebuggingTools()) {
                            e?.printStackTrace()
                        }
                        view?.onErrorShouldChangeUsername(
                                ErrorHandler.getErrorMessage(view.context, e),
                                link
                        )
                    }
                }
        )
    }

    override fun getPostStatistic(activityId: String, productIds: List<String>, likeCount: Int, commentCount: Int) {
        getPostStatisticCommissionUseCase.setParams(
                GetPostStatisticCommissionUseCase.getParam(
                        listOf(activityId),
                        productIds
                )
        )
        getPostStatisticCommissionUseCase.execute(
                onSuccess = {
                    view.onSuccessGetPostStatistic(
                            PostStatisticCommissionUiModel(
                                    it.second.totalProductCommission,
                                    PostStatisticMapper(likeCount, commentCount).call(it.first)
                            )
                    )
                },
                onError = {
                    view.onErrorGetPostStatistic(it, activityId, productIds)
                }
        )
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
        if (!view.getUserSession().userId.isEmpty()) {
            userId = view.getUserSession().userId
        }
        return userId
    }
}