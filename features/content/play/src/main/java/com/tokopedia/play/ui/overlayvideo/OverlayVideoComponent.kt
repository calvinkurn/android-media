package com.tokopedia.play.ui.overlayvideo

import android.view.ViewGroup
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play_common.state.TokopediaPlayVideoState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

/**
 * Created by jegul on 20/01/20
 */
class OverlayVideoComponent(
        container: ViewGroup,
        bus: EventBusFactory,
        coroutineScope: CoroutineScope
) : UIComponent<Unit>, CoroutineScope by coroutineScope {

    private val uiView = initView(container)

    init {
        launch {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            ScreenStateEvent.Init -> uiView.hide()
                            is ScreenStateEvent.VideoPropertyChanged -> handleVideoStateChanged(it.videoProp.type.isVod, it.videoProp.state)
                        }
                    }
        }
    }

    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Flow<Unit> {
        return emptyFlow()
    }

    private fun initView(container: ViewGroup) =
            OverlayVideoView(container)

    private fun handleVideoStateChanged(isVod: Boolean, state: TokopediaPlayVideoState) {
        if (!isVod) {
            uiView.hide()
            return
        }
        when (state) {
            TokopediaPlayVideoState.Ended -> uiView.show()
            else -> uiView.hide()
        }
    }
}