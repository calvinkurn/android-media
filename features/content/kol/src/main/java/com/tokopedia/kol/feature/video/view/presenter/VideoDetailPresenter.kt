package com.tokopedia.kol.feature.video.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.feedcomponent.domain.model.DynamicFeedDomainModel
import com.tokopedia.kol.feature.video.domain.usecase.GetVideoDetailUseCase
import com.tokopedia.kol.feature.video.view.listener.VideoDetailContract
import com.tokopedia.kol.feature.video.view.subscriber.FollowSubscriber
import com.tokopedia.kol.feature.video.view.subscriber.LikeSubscriber
import com.tokopedia.kolcommon.domain.usecase.FollowKolPostGqlUseCase
import com.tokopedia.kolcommon.domain.usecase.LikeKolPostUseCase
import com.tokopedia.kolcommon.view.listener.KolPostLikeListener
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * @author by yfsx on 26/03/19.
 */
class VideoDetailPresenter
@Inject constructor(private val baseDispatcher: CoroutineDispatchers,
                    private val likeKolPostUseCase: LikeKolPostUseCase,
                    private val followKolPostGqlUseCase: FollowKolPostGqlUseCase,
                    private val getVideoDetailUseCase: GetVideoDetailUseCase
                   )
    : VideoDetailContract.Presenter, BaseDaggerPresenter<VideoDetailContract.View>() ,CoroutineScope{

    var getFeedNextPageResp = DynamicFeedDomainModel()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()


    override fun attachView(view: VideoDetailContract.View?) {
        super.attachView(view)
    }

    override fun detachView() {
        super.detachView()
        likeKolPostUseCase.unsubscribe()
        followKolPostGqlUseCase.unsubscribe()
    }

    override fun getFeedDetail(detailId: String) {

        launchCatchError(context = baseDispatcher.main,block = {
            getFeedNextPageResp = withContext(baseDispatcher.io) {
               getFeedDataResult(detailId)
            }

            view.onSuccessGetVideoDetail(getFeedNextPageResp.postList)

        }) {
            view.onErrorGetVideoDetail(it.message?:"")
        }
    }

    private suspend fun getFeedDataResult(detailId: String): DynamicFeedDomainModel {
        try {
            return getVideoDetailUseCase.execute(cursor = "", detailId = detailId)
        } catch (e: Throwable) {
            Timber.e(e)
            throw e
        }
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

    override fun likeKol(id: Long, rowNumber: Int, likeListener: KolPostLikeListener) {
        likeKolPostUseCase.execute(
                LikeKolPostUseCase.getParam(id, LikeKolPostUseCase.LikeKolPostAction.Like),
                LikeSubscriber(likeListener, rowNumber, LikeKolPostUseCase.LikeKolPostAction.Like)
        )
    }

    override fun unlikeKol(id: Long, rowNumber: Int, likeListener: KolPostLikeListener) {
        likeKolPostUseCase.execute(
                LikeKolPostUseCase.getParam(id, LikeKolPostUseCase.LikeKolPostAction.Unlike),
                LikeSubscriber(likeListener, rowNumber, LikeKolPostUseCase.LikeKolPostAction.Unlike)
        )
    }

}
