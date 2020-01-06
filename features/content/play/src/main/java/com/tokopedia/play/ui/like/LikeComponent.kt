package com.tokopedia.play.ui.like

import android.view.ViewGroup
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.component.UIView
import com.tokopedia.play.ui.like.interaction.LikeInteractionEvent
import com.tokopedia.play.view.event.ScreenStateEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by jegul on 02/12/19
 */
class LikeComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        coroutineScope: CoroutineScope
) : UIComponent<LikeInteractionEvent>, LikeView.Listener, CoroutineScope by coroutineScope {

    private val uiView = initView(container)

    init {
        launch {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.KeyboardStateChanged -> if (it.isShown) uiView.hide() else uiView.show()
                            is ScreenStateEvent.LikeContent -> uiView.playLikeAnimation(it.shouldLike)
                        }
                    }
        }
    }

    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Flow<LikeInteractionEvent> {
        return bus.getSafeManagedFlow(LikeInteractionEvent::class.java)
    }

    override fun onLikeClicked(view: LikeView, shouldLike: Boolean) {
        launch {
            bus.emit(
                    LikeInteractionEvent::class.java,
                    LikeInteractionEvent.LikeClicked(shouldLike)
            )
        }
    }

    private fun initView(container: ViewGroup) =
            LikeView(container, this)
}