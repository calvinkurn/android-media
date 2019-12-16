package com.tokopedia.play.ui.playbutton

import android.view.ViewGroup
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.ui.playbutton.interaction.PlayButtonInteractionEvent
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play_common.state.TokopediaPlayVideoState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by jegul on 10/12/19
 */
class PlayButtonComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        coroutineScope: CoroutineScope
) : UIComponent<PlayButtonInteractionEvent>, CoroutineScope by coroutineScope, PlayButtonView.Listener {

    private val uiView = initView(container)

    init {
        launch {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.VideoPropertyChanged -> handleVideoStateChanged(it.videoProp.type.isLive, it.videoProp.state)
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

    override fun onPlayButtonClicked(view: PlayButtonView) {
        launch {
            bus.emit(
                    PlayButtonInteractionEvent::class.java,
                    PlayButtonInteractionEvent.PlayClicked
            )
        }
    }

    private fun initView(container: ViewGroup) =
            PlayButtonView(container, this)

    private fun handleVideoStateChanged(isLive: Boolean, state: TokopediaPlayVideoState) {
        if (isLive) {
            uiView.hide()
            return
        }
        when (state) {
            TokopediaPlayVideoState.Playing -> uiView.hide()
            TokopediaPlayVideoState.Pause -> uiView.show()
        }
    }
}