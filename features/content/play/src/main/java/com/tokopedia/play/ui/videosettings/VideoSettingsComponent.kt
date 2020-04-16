package com.tokopedia.play.ui.videosettings

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.ui.videosettings.interaction.VideoSettingsInteractionEvent
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by jegul on 14/04/20
 */
open class VideoSettingsComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        private val coroutineScope: CoroutineScope,
        dispatchers: CoroutineDispatcherProvider
) : UIComponent<VideoSettingsInteractionEvent>, VideoSettingsView.Listener, CoroutineScope by coroutineScope {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container)

    init {
        launch(dispatchers.immediate) {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            ScreenStateEvent.Init -> uiView.hide()
                            is ScreenStateEvent.VideoStreamChanged -> if (it.videoStream.orientation.isLandscape) uiView.show() else uiView.hide()
                            is ScreenStateEvent.ScreenOrientationChanged -> uiView.setFullscreen(it.orientation.isLandscape)
                            is ScreenStateEvent.ImmersiveStateChanged -> if (it.shouldImmersive) uiView.fadeOut() else uiView.fadeIn()
                        }
                    }
        }
    }

    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Flow<VideoSettingsInteractionEvent> {
        return bus.getSafeManagedFlow(VideoSettingsInteractionEvent::class.java)
    }

    override fun onEnterFullscreen(view: VideoSettingsView) {
        launch {
            bus.emit(VideoSettingsInteractionEvent::class.java, VideoSettingsInteractionEvent.EnterFullScreenClicked)
        }
    }

    override fun onExitFullscreen(view: VideoSettingsView) {
        launch {
            bus.emit(VideoSettingsInteractionEvent::class.java, VideoSettingsInteractionEvent.ExitFullScreenClicked)
        }
    }

    protected open fun initView(container: ViewGroup) =
            VideoSettingsView(container, this)
}