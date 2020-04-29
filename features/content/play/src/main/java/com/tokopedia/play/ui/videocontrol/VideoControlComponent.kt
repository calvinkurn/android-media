package com.tokopedia.play.ui.videocontrol

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.ui.videocontrol.interaction.VideoControlInteractionEvent
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.uimodel.General
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by jegul on 05/12/19
 */
open class VideoControlComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        private val scope: CoroutineScope,
        dispatchers: CoroutineDispatcherProvider
) : UIComponent<VideoControlInteractionEvent>, VideoControlView.Listener {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container)

    init {
        scope.launch(dispatchers.immediate) {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.Init -> uiView.hide()
                            is ScreenStateEvent.SetVideo -> if (it.videoPlayer is General) uiView.setPlayer(it.videoPlayer.exoPlayer)
                            is ScreenStateEvent.VideoStreamChanged -> {
                                if (
                                        it.videoStream.channelType.isVod &&
                                        it.stateHelper.videoPlayer.isGeneral &&
                                        !it.stateHelper.bottomInsets.isAnyShown
                                ) uiView.show()
                                else uiView.hide()
                            }
                            is ScreenStateEvent.OnNewPlayRoomEvent -> if (it.event.isFreeze || it.event.isBanned) {
                                uiView.hide()
                                uiView.setPlayer(null)
                            }
                            is ScreenStateEvent.BottomInsetsChanged -> {
                                if (!it.isAnyShown && it.stateHelper.channelType.isVod && it.stateHelper.videoPlayer.isGeneral) uiView.show()
                                else uiView.hide()
                            }
                        }
                    }
        }
    }

    override fun onStartSeeking(view: VideoControlView) {
        scope.launch {
            bus.emit(VideoControlInteractionEvent::class.java, VideoControlInteractionEvent.VideoScrubStarted)
        }
    }

    override fun onEndSeeking(view: VideoControlView) {
        scope.launch {
            bus.emit(VideoControlInteractionEvent::class.java, VideoControlInteractionEvent.VideoScrubEnded)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        uiView.onDestroy()
    }

    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Flow<VideoControlInteractionEvent> {
        return bus.getSafeManagedFlow(VideoControlInteractionEvent::class.java)
    }

    protected open fun initView(container: ViewGroup) =
            VideoControlView(container, this)
}