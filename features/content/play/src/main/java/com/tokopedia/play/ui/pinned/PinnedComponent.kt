package com.tokopedia.play.ui.pinned

import android.view.ViewGroup
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.ui.pinned.interaction.PinnedInteractionEvent
import com.tokopedia.play.view.event.ScreenStateEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

/**
 * Created by jegul on 03/12/19
 */
class PinnedComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        coroutineScope: CoroutineScope
) : UIComponent<PinnedInteractionEvent>, CoroutineScope by coroutineScope, PinnedView.Listener {

    private val uiView = initView(container)

    //temp state
    private var shouldShow: Boolean = false
    private var isKeyboardShown: Boolean = false

    init {
        uiView.hide()

        launch {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.SetPinned -> {
                                shouldShow = if (it.pinnedMessage.shouldRemove) {
                                    uiView.hide()
                                    false
                                } else {
                                    uiView.setPinnedMessage(it.pinnedMessage)
                                    if (!isKeyboardShown) uiView.show()
                                    true
                                }
                            }
                            is ScreenStateEvent.KeyboardStateChanged -> {
                                if (!it.isShown && shouldShow) uiView.show() else uiView.hide()
                                isKeyboardShown = it.isShown
                            }
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

    override fun onPinnedActionClicked(view: PinnedView, applink: String, message: String) {
        launch {
            bus.emit(PinnedInteractionEvent::class.java, PinnedInteractionEvent.PinnedActionClicked(applink, message))
        }
    }

    private fun initView(container: ViewGroup): PinnedView =
            PinnedView(container, this)
}