package com.tokopedia.profile.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.affiliatecommon.domain.DeletePostUseCase
import com.tokopedia.kol.feature.post.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.kol.feature.post.domain.usecase.GetContentListUseCase
import com.tokopedia.kol.feature.post.domain.usecase.LikeKolPostUseCase
import com.tokopedia.kol.feature.post.view.listener.KolPostListener
import com.tokopedia.kol.feature.post.view.subscriber.LikeKolPostSubscriber
import com.tokopedia.profile.domain.usecase.GetDynamicFeedProfileFirstUseCase
import com.tokopedia.profile.domain.usecase.GetDynamicFeedProfileUseCase
import com.tokopedia.profile.domain.usecase.GetProfileFirstPage
import com.tokopedia.profile.domain.usecase.TrackAffiliateClickUseCase
import com.tokopedia.profile.view.listener.ProfileContract
import com.tokopedia.profile.view.subscriber.*
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
        private val trackAffiliateClickUseCase: TrackAffiliateClickUseCase)
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
    }

    override fun getProfileFirstPage(targetUserId: Int, isFromLogin: Boolean) {
        cursor = ""
        getDynamicFeedProfileFirstUseCase.execute(
                GetDynamicFeedProfileFirstUseCase.createRequestParams(view.getUserSession().userId, targetUserId.toString()),
                GetProfileFirstPageSubscriber(view, isFromLogin)
        )
    }

    override fun getProfilePost(userId: Int) {
        getDynamicFeedProfileUseCase.execute(
                GetContentListUseCase.getProfileParams(userId, cursor),
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
                LikeKolPostUseCase.getParam(id, LikeKolPostUseCase.ACTION_LIKE),
                LikeKolPostSubscriber(likeListener, rowNumber)
        )
    }

    override fun unlikeKol(id: Int, rowNumber: Int, likeListener: KolPostListener.View.Like) {
        likeKolPostUseCase.execute(
                LikeKolPostUseCase.getParam(id, LikeKolPostUseCase.ACTION_UNLIKE),
                LikeKolPostSubscriber(likeListener, rowNumber)
        )
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
}