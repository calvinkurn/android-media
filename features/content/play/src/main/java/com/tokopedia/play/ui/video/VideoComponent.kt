package com.tokopedia.play.ui.video

import android.view.ViewGroup
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.unifycomponents.dpToPx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

/**
 * Created by6jegul on 02/12/19
 */
class VideoComponent(
        container: ViewGroup,
        bus: EventBusFactory,
        coroutineScope: CoroutineScope
) : UIComponent<Unit>, CoroutineScope by coroutineScope {

    private val uiView = initUiView(container)
    private val cornerRadius = 16f.dpToPx()

    init {
        launch {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.SetVideo -> uiView.setPlayer(it.videoPlayer)
                            is ScreenStateEvent.KeyboardStateChanged -> uiView.setCornerRadius(if (it.isShown) cornerRadius else 0f)
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

    private fun initUiView(container: ViewGroup): VideoView =
            VideoView(container)
}