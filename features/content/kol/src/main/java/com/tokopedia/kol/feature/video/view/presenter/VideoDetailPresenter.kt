package com.tokopedia.kol.feature.video.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.kol.feature.post.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.kol.feature.post.domain.usecase.LikeKolPostUseCase
import com.tokopedia.kol.feature.post.view.listener.KolPostListener
import com.tokopedia.kol.feature.video.domain.usecase.GetVideoDetailUseCase
import com.tokopedia.kol.feature.video.view.listener.VideoDetailContract
import com.tokopedia.kol.feature.video.view.subscriber.FollowSubscriber
import com.tokopedia.kol.feature.video.view.subscriber.LikeSubscriber
import com.tokopedia.kol.feature.video.view.subscriber.VideoDetailSubscriber
import javax.inject.Inject

/**
 * @author by yfsx on 26/03/19.
 */
class VideoDetailPresenter
@Inject constructor(private val likeKolPostUseCase: LikeKolPostUseCase,
                    private val followKolPostGqlUseCase: FollowKolPostGqlUseCase,
                    private val getVideoDetailUseCase: GetVideoDetailUseCase)
    : VideoDetailContract.Presenter, BaseDaggerPresenter<VideoDetailContract.View>() {

    override fun attachView(view: VideoDetailContract.View?) {
        super.attachView(view)
    }

    override fun detachView() {
        super.detachView()
        likeKolPostUseCase.unsubscribe()
        followKolPostGqlUseCase.unsubscribe()
        getVideoDetailUseCase.unsubscribe()
    }

    override fun checkViewAttached() {
        super.checkViewAttached()
    }

    override fun getView(): VideoDetailContract.View {
        return super.getView()
    }

    override fun getFeedDetail(detailId: String) {
        getVideoDetailUseCase.execute(GetVideoDetailUseCase.createRequestParams(getUserId(), detailId),
                VideoDetailSubscriber(view))
    }

    override fun followKol(id: Int) {
        followKolPostGqlUseCase.clearRequest()
        followKolPostGqlUseCase.addRequest(
                followKolPostGqlUseCase.getRequest(id, FollowKolPostGqlUseCase.PARAM_FOLLOW)
        )
        followKolPostGqlUseCase.execute(FollowSubscriber(view))
    }

    override fun unFollowKol(id: Int) {
        followKolPostGqlUseCase.clearRequest()
        followKolPostGqlUseCase.addRequest(
                followKolPostGqlUseCase.getRequest(id, FollowKolPostGqlUseCase.PARAM_UNFOLLOW)
        )
        followKolPostGqlUseCase.execute(FollowSubscriber(view))
    }

    override fun likeKol(id: Int, rowNumber: Int, likeListener: KolPostListener.View.Like) {
        likeKolPostUseCase.execute(
                LikeKolPostUseCase.getParam(id, LikeKolPostUseCase.ACTION_LIKE),
                LikeSubscriber(likeListener, rowNumber)
        )
    }

    override fun unlikeKol(id: Int, rowNumber: Int, likeListener: KolPostListener.View.Like) {
        likeKolPostUseCase.execute(
                LikeKolPostUseCase.getParam(id, LikeKolPostUseCase.ACTION_UNLIKE),
                LikeSubscriber(likeListener, rowNumber)
        )
    }
    private fun getUserId(): String {
        var userId = "0"
        if (view.getUserSession().userId.isNotEmpty()) {
            userId = view.getUserSession().userId
        }

        return userId
    }
}