package com.tokopedia.play.ui.videosettings

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.extensions.isAnyShown
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
                            is ScreenStateEvent.Init -> {
                                uiView.setFullscreen(false)
                                if (!it.stateHelper.videoOrientation.isHorizontal) uiView.hide()
                                else if (it.stateHelper.videoPlayer.isGeneral) uiView.show()
                            }
                            is ScreenStateEvent.VideoStreamChanged -> {
                                if (
                                        it.videoStream.orientation.isHorizontal &&
                                        it.stateHelper.videoPlayer.isGeneral &&
                                        !it.stateHelper.bottomInsets.isAnyShown
                                ) uiView.show() else uiView.hide()
                            }
                            is ScreenStateEvent.ImmersiveStateChanged -> if (it.shouldImmersive) uiView.fadeOut() else uiView.fadeIn()
                            is ScreenStateEvent.BottomInsetsChanged ->
                                if (!it.isAnyShown && it.stateHelper.videoOrientation.isHorizontal && it.stateHelper.videoPlayer.isGeneral) uiView.show() else uiView.hide()
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