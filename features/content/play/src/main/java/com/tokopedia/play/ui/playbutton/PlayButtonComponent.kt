package com.tokopedia.play.ui.playbutton

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
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
open class PlayButtonComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        coroutineScope: CoroutineScope
) : UIComponent<PlayButtonInteractionEvent>, CoroutineScope by coroutineScope, PlayButtonView.Listener {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container)

    init {
        uiView.hide()

        launch {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.VideoPropertyChanged -> handleVideoStateChanged(it.videoProp.type.isVod, it.videoProp.state)
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
        launch {
            bus.emit(
                    PlayButtonInteractionEvent::class.java,
                    PlayButtonInteractionEvent.PlayClicked
            )
        }
    }

    protected open fun initView(container: ViewGroup) =
            PlayButtonView(container, this)

    private fun handleVideoStateChanged(isVod: Boolean, state: TokopediaPlayVideoState) {
        if (!isVod) {
            uiView.hide()
            return
        }
        when (state) {
            TokopediaPlayVideoState.Pause -> uiView.showPlayButton()
            TokopediaPlayVideoState.Ended -> uiView.showRepeatButton()
            else -> uiView.hide()
        }
    }
}