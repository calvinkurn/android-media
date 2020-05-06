package com.tokopedia.play.ui.video

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.util.CoroutineDispatcherProvider
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
        coroutineScope: CoroutineScope,
        dispatchers: CoroutineDispatcherProvider
) : UIComponent<Unit>, CoroutineScope by coroutineScope {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container)

    private var isReadyAfterOnResume = false

    init {
        launch(dispatchers.immediate) {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            ScreenStateEvent.Init -> uiView.show()
                            is ScreenStateEvent.SetVideo -> uiView.setPlayer(it.videoPlayer)
                            is ScreenStateEvent.OnNewPlayRoomEvent -> if(it.event.isFreeze || it.event.isBanned) {
                                uiView.hide()
                                uiView.setPlayer(null)
                            }
                            is ScreenStateEvent.VideoPropertyChanged -> handleVideoStateChanged(it.videoProp.state)
                        }
                    }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        uiView.onDestroy()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        if (isReadyAfterOnResume) uiView.setThumbnail()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        isReadyAfterOnResume = false
        uiView.showThumbnail(true)
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
            PlayVideoState.NoMedia -> uiView.showThumbnail(true)
            PlayVideoState.Playing, PlayVideoState.Pause -> {
                uiView.showThumbnail(false)
                isReadyAfterOnResume = true
            }
            PlayVideoState.Ended, is PlayVideoState.Error -> uiView.showThumbnail(false)
        }
    }
}