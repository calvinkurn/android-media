package com.tokopedia.profile.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.affiliatecommon.domain.DeletePostUseCase
import com.tokopedia.kol.feature.post.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.kol.feature.post.domain.usecase.GetContentListUseCase
import com.tokopedia.kol.feature.post.domain.usecase.LikeKolPostUseCase
import com.tokopedia.kol.feature.post.view.listener.KolPostListener
import com.tokopedia.kol.feature.post.view.subscriber.LikeKolPostSubscriber
import com.tokopedia.profile.domain.usecase.GetProfileFirstPage
import com.tokopedia.profile.view.listener.ProfileContract
import com.tokopedia.profile.view.subscriber.DeletePostSubscriber
import com.tokopedia.profile.view.subscriber.FollowSubscriber
import com.tokopedia.profile.view.subscriber.GetProfileFirstPageSubscriber
import com.tokopedia.profile.view.subscriber.GetProfilePostSubscriber
import javax.inject.Inject

/**
 * @author by milhamj on 9/21/18.
 */
class ProfilePresenter @Inject constructor(
        private val getProfileFirstPage: GetProfileFirstPage,
        private val getContentListUseCase: GetContentListUseCase,
        private val likeKolPostUseCase: LikeKolPostUseCase,
        private val followKolPostGqlUseCase: FollowKolPostGqlUseCase,
        private val deletePostUseCase: DeletePostUseCase)
    : BaseDaggerPresenter<ProfileContract.View>(), ProfileContract.Presenter {

    override var cursor: String = ""

    override fun detachView() {
        super.detachView()
        getProfileFirstPage.unsubscribe()
        likeKolPostUseCase.unsubscribe()
        followKolPostGqlUseCase.unsubscribe()
        deletePostUseCase.unsubscribe()
    }

    override fun getProfileFirstPage(userId: Int) {
        getProfileFirstPage.execute(
                GetProfileFirstPage.createRequestParams(userId),
                GetProfileFirstPageSubscriber(view)
        )
    }

    override fun getProfilePost(userId: Int) {
        getContentListUseCase.execute(
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
                DeletePostSubscriber(view, rowNumber)
        )
    }
}