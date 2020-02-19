package com.tokopedia.play.ui.pinned

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.ui.pinned.interaction.PinnedInteractionEvent
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by jegul on 03/12/19
 */
open class PinnedComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        coroutineScope: CoroutineScope,
        dispatchers: CoroutineDispatcherProvider
) : UIComponent<PinnedInteractionEvent>, CoroutineScope by coroutineScope, PinnedView.Listener {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container)

    //temp state
    private var shouldShow: Boolean = false
    private var isKeyboardShown: Boolean = false

    init {
        launch(dispatchers.immediate) {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            ScreenStateEvent.Init -> uiView.hide()
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
                            is ScreenStateEvent.OnNewPlayRoomEvent -> if(it.event.isFreeze) uiView.hide()
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

    open protected fun initView(container: ViewGroup): PinnedView =
            PinnedView(container, this)
}