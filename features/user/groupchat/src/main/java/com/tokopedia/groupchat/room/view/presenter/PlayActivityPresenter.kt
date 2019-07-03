package com.tokopedia.groupchat.room.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.groupchat.room.domain.usecase.GetVideoStreamUseCase
import com.tokopedia.groupchat.room.view.listener.PlayActivityContract
import com.tokopedia.groupchat.room.view.viewmodel.VideoStreamViewModel
import rx.Subscriber
import javax.inject.Inject

/**
 * @author : Steven 19/06/19
 */
class PlayActivityPresenter @Inject constructor(
        private var getVideoStreamUseCase: GetVideoStreamUseCase
) : BaseDaggerPresenter<PlayActivityContract.View>(), PlayActivityContract.Presenter {

    override fun getVideoStream(channelId: String?, onSuccessGetVideoStream: (VideoStreamViewModel) -> Unit, onErrorGetVideoStream: (String) -> Unit) {
        getVideoStreamUseCase.execute(GetVideoStreamUseCase.createParams(channelId),
                object : Subscriber<VideoStreamViewModel>() {
            override fun onNext(t: VideoStreamViewModel?) {
                t
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                e
            }

        })
    }
}