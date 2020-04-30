package com.tokopedia.play.ui.video

import android.graphics.Bitmap
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.util.video.PlayVideoUtil
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play_common.state.PlayVideoState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

/**
 * Created by jegul on 02/12/19
 */
open class VideoComponent(
        container: ViewGroup,
        bus: EventBusFactory,
        scope: CoroutineScope,
        dispatchers: CoroutineDispatcherProvider,
        private val playVideoUtil: PlayVideoUtil
) : UIComponent<Unit> {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container)

    init {
        scope.launch(dispatchers.immediate) {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.Init -> {
                                uiView.setOrientation(it.screenOrientation, it.stateHelper.videoOrientation)
                                uiView.hide()
                                if (it.stateHelper.videoState == PlayVideoState.Ended) showEndImage()
                            }
                            is ScreenStateEvent.SetVideo -> uiView.setPlayer(it.videoPlayer)
                            is ScreenStateEvent.OnNewPlayRoomEvent -> if (it.event.isFreeze || it.event.isBanned) {
                                uiView.hide()
                                uiView.setPlayer(null)
                            }
                            is ScreenStateEvent.VideoPropertyChanged -> handleVideoStateChanged(it.videoProp.state)
                            is ScreenStateEvent.VideoStreamChanged -> {
                                uiView.setOrientation(it.stateHelper.screenOrientation, it.videoStream.orientation)
                            }
                        }
                    }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        uiView.onDestroy()
    }

    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Flow<Unit> {
        return emptyFlow()
    }

    protected open fun initView(container: ViewGroup): VideoView =
            VideoView(container)

    private fun handleVideoStateChanged(state: PlayVideoState) {
        when (state) {
            PlayVideoState.Playing, PlayVideoState.Pause -> {
                uiView.show()
                uiView.showThumbnail(null)
            }
            PlayVideoState.Ended -> {
                uiView.show()
                uiView.getCurrentBitmap()?.let { saveEndImage(it) }
            }
        }
    }

    private fun saveEndImage(bitmap: Bitmap) {
        playVideoUtil.saveEndImage(bitmap)
    }

    private fun showEndImage() {
        uiView.showThumbnail(playVideoUtil.getEndImage())
    }
}