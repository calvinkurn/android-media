package com.tokopedia.play.ui.youtube

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.youtube.player.YouTubeInitializationResult
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.ui.youtube.interaction.YouTubeInteractionEvent
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.uimodel.YouTube
import com.tokopedia.play_common.state.PlayVideoState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by jegul on 27/04/20
 */
open class YouTubeComponent(
        container: ViewGroup,
        fragmentManager: FragmentManager,
        private val bus: EventBusFactory,
        private val scope: CoroutineScope,
        dispatchers: CoroutineDispatcherProvider
) : UIComponent<YouTubeInteractionEvent>, YouTubeView.Listener {

    private var videoId: String? = null
    private var currentPosition: Int = 0
    private var shouldPlayOnReady: Boolean = true

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container, fragmentManager)

    init {
        scope.launch(dispatchers.immediate) {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.Init -> {
                                uiView.hide()
                            }
                            is ScreenStateEvent.OnNewPlayRoomEvent -> if (it.event.isFreeze || it.event.isBanned) {
                                uiView.hide()
                                uiView.release()
                            }
                            is ScreenStateEvent.SetVideo -> if (it.videoPlayer is YouTube) {
                                videoId = it.videoPlayer.youtubeId

                                playVideoById(it.videoPlayer.youtubeId)
                                uiView.show()
                            }
                            is ScreenStateEvent.OrientationChanged -> uiView.setFullScreenButton(it.orientation.isLandscape)
                            is ScreenStateEvent.YouTubeAnalyticsRequired -> {
                                val youTubePlayer = uiView.getPlayer()
                                youTubePlayer?.let { player -> it.analyticsBlock(player) }
                            }
                        }
                    }
        }
    }

    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Flow<YouTubeInteractionEvent> {
        return bus.getSafeManagedFlow(YouTubeInteractionEvent::class.java)
    }

    override fun onInitFailure(view: YouTubeView, result: YouTubeInitializationResult) {
    }

    override fun onEnterFullscreen(view: YouTubeView) {
        scope.launch {
            bus.emit(YouTubeInteractionEvent::class.java, YouTubeInteractionEvent.EnterFullscreenClicked)
        }
    }

    override fun onExitFullscreen(view: YouTubeView) {
        scope.launch {
            bus.emit(YouTubeInteractionEvent::class.java, YouTubeInteractionEvent.ExitFullscreenClicked)
        }
    }

    override fun onVideoStateChanged(view: YouTubeView, state: PlayVideoState) {
        scope.launch {
            bus.emit(YouTubeInteractionEvent::class.java, YouTubeInteractionEvent.YouTubeVideoStateChanged(state))
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        uiView.release()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        try {
            currentPosition = uiView.getCurrentPosition()
            shouldPlayOnReady = uiView.isPlaying() ?: true
        } catch (e: Throwable) { /*Not Used*/ }

        uiView.release()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        videoId?.let { playVideoById(it) }
    }

    private fun playVideoById(videoId: String) {
        uiView.setYouTubeId(videoId, currentPosition, shouldPlayOnReady)
    }

    protected open fun initView(container: ViewGroup, fragmentManager: FragmentManager) =
            YouTubeView(container, fragmentManager, this)
}