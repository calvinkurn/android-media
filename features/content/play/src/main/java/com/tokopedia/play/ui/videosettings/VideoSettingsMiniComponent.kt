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
 * Created by jegul on 08/05/20
 */
open class VideoSettingsMiniComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        private val scope: CoroutineScope,
        dispatchers: CoroutineDispatcherProvider
) : UIComponent<VideoSettingsInteractionEvent>, VideoSettingsView.Listener {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container)

    init {
        scope.launch(dispatchers.immediate) {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.Init -> uiView.setFullscreen(true)
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
        scope.launch {
            bus.emit(VideoSettingsInteractionEvent::class.java, VideoSettingsInteractionEvent.EnterFullscreenClicked)
        }
    }

    override fun onExitFullscreen(view: VideoSettingsView) {
        scope.launch {
            bus.emit(VideoSettingsInteractionEvent::class.java, VideoSettingsInteractionEvent.ExitFullscreenClicked)
        }
    }

    protected open fun initView(container: ViewGroup) =
            VideoSettingsView(container, this)
}