package com.tokopedia.play.ui.playbutton

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.ui.playbutton.interaction.PlayButtonInteractionEvent
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play_common.state.PlayVideoState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by jegul on 10/12/19
 */
open class PlayButtonComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        private val scope: CoroutineScope,
        dispatchers: CoroutineDispatcherProvider
) : UIComponent<PlayButtonInteractionEvent>, PlayButtonView.Listener {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container)

    init {
        scope.launch(dispatchers.immediate) {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.Init -> uiView.hide()
                            is ScreenStateEvent.VideoPropertyChanged -> handleVideoStateChanged(it.stateHelper.channelType.isVod, it.videoProp.state)
                            is ScreenStateEvent.OnNewPlayRoomEvent -> if(it.event.isFreeze) uiView.hide()
                        }
                    }
        }
    }

    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Flow<PlayButtonInteractionEvent> {
        return bus.getSafeManagedFlow(PlayButtonInteractionEvent::class.java)
    }

    override fun onButtonClicked(view: PlayButtonView) {
        scope.launch {
            bus.emit(
                    PlayButtonInteractionEvent::class.java,
                    PlayButtonInteractionEvent.PlayClicked
            )
        }
    }

    protected open fun initView(container: ViewGroup) =
            PlayButtonView(container, this)

    private fun handleVideoStateChanged(isVod: Boolean, state: PlayVideoState) {
        if (!isVod) {
            uiView.hide()
            return
        }
        when (state) {
            PlayVideoState.Pause -> uiView.showPlayButton()
            PlayVideoState.Ended -> uiView.showRepeatButton()
            else -> uiView.hide()
        }
    }
}