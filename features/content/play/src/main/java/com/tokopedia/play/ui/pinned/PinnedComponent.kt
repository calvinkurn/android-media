package com.tokopedia.play.ui.pinned

import android.view.ViewGroup
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.ui.pinned.interaction.PinnedInteractionEvent
import com.tokopedia.play.view.event.ScreenStateEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by jegul on 03/12/19
 */
class PinnedComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        coroutineScope: CoroutineScope
) : UIComponent<PinnedInteractionEvent>, PinnedView.Listener, CoroutineScope by coroutineScope {

    private val uiView = initView(container)

    init {
        launch {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.SetPinned -> uiView.setPinnedMessage(it.author, it.message)
                        }
                    }
        }
    }

    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Flow<PinnedInteractionEvent> {
        return bus.getSafeManagedFlow(PinnedInteractionEvent::class.java)
    }

    override fun onPinnedActionClicked(pinnedView: PinnedView) {
        launch {
            bus.emit(
                    PinnedInteractionEvent::class.java,
                    PinnedInteractionEvent.ActionClicked
            )
        }
    }

    private fun initView(container: ViewGroup): PinnedView =
            PinnedView(container, this)
}