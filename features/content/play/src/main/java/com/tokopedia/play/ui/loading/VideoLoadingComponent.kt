package com.tokopedia.play.ui.loading

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
 * Created by jegul on 09/12/19
 */
class VideoLoadingComponent(
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
                            is ScreenStateEvent.VideoPropertyChanged -> handleVideoStateChanged(it.videoProp.state)
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
            VideoLoadingView(container)

    private fun handleVideoStateChanged(state: TokopediaPlayVideoState) {
        when (state) {
            TokopediaPlayVideoState.Buffering -> uiView.show()
            TokopediaPlayVideoState.Playing, TokopediaPlayVideoState.Ended, TokopediaPlayVideoState.NoMedia, TokopediaPlayVideoState.Pause -> uiView.hide()
        }
    }
}