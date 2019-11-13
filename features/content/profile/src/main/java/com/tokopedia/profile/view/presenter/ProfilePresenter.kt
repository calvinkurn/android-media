package com.tokopedia.profile.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.affiliatecommon.domain.DeletePostUseCase
import com.tokopedia.affiliatecommon.domain.TrackAffiliateClickUseCase
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.feedcomponent.data.pojo.FeedPostRelated
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.feedcomponent.domain.usecase.GetRelatedPostUseCase
import com.tokopedia.feedcomponent.view.subscriber.TrackPostClickSubscriber
import com.tokopedia.feedcomponent.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.feedcomponent.domain.usecase.LikeKolPostUseCase
import com.tokopedia.kol.feature.post.view.listener.KolPostListener
import com.tokopedia.kol.feature.post.view.subscriber.LikeKolPostSubscriber
import com.tokopedia.profile.domain.usecase.GetDynamicFeedProfileFirstUseCase
import com.tokopedia.profile.domain.usecase.GetDynamicFeedProfileUseCase
import com.tokopedia.profile.domain.usecase.ShouldChangeUsernameUseCase
import com.tokopedia.profile.view.listener.ProfileContract
import com.tokopedia.profile.view.subscriber.*
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by milhamj on 9/21/18.
 */
class ProfilePresenter @Inject constructor(
        private val getDynamicFeedProfileFirstUseCase: GetDynamicFeedProfileFirstUseCase,
        private val getDynamicFeedProfileUseCase: GetDynamicFeedProfileUseCase,
        private val likeKolPostUseCase: LikeKolPostUseCase,
        private val followKolPostGqlUseCase: FollowKolPostGqlUseCase,
        private val deletePostUseCase: DeletePostUseCase,
        private val trackAffiliateClickUseCase: TrackAffiliateClickUseCase,
        private val shouldChangeUsernameUseCase: ShouldChangeUsernameUseCase,
        private val getRelatedPostUseCase: GetRelatedPostUseCase,
        private val atcUseCase: AddToCartUseCase)
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

    override fun followKol(id: Int) {
        followKolPostGqlUseCase.clearRequest()
        followKolPostGqlUseCase.addRequest(
            followKolPostGqlUseCase.getRequest(id, FollowKolPostGqlUseCase.PARAM_FOLLOW)
        )
        followKolPostGqlUseCase.execute(FollowSubscriber(view))
    }

    override fun unfollowKol(id: Int) {
        followKolPostGqlUseCase.clearRequest()
        followKolPostGqlUseCase.addRequest(
            followKolPostGqlUseCase.getRequest(id, FollowKolPostGqlUseCase.PARAM_UNFOLLOW)
        )
        followKolPostGqlUseCase.execute(FollowSubscriber(view))
    }

    override fun likeKol(id: Int, rowNumber: Int, likeListener: KolPostListener.View.Like) {
        likeKolPostUseCase.execute(
            LikeKolPostUseCase.getParam(id, LikeKolPostUseCase.LikeKolPostAction.Like),
            LikeKolPostSubscriber(likeListener, rowNumber, LikeKolPostUseCase.LikeKolPostAction.Like)
        )
    }

    override fun unlikeKol(id: Int, rowNumber: Int, likeListener: KolPostListener.View.Like) {
        likeKolPostUseCase.execute(
            LikeKolPostUseCase.getParam(id, LikeKolPostUseCase.LikeKolPostAction.Unlike),
            LikeKolPostSubscriber(likeListener, rowNumber, LikeKolPostUseCase.LikeKolPostAction.Unlike)
        )
    }

    override fun addPostTagItemToCart(postTagItem: PostTagItem) {
        val isShopNotEmpty = postTagItem.shop.isNotEmpty()
        if (isShopNotEmpty) {
            atcUseCase.execute(
                    AddToCartUseCase.getMinimumParams(postTagItem.id, postTagItem.shop.first().shopId),
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

    private fun getUserId(): String {
        var userId = "0"
        if (!view.getUserSession().userId.isEmpty()) {
            userId = view.getUserSession().userId
        }
        return userId
    }
}