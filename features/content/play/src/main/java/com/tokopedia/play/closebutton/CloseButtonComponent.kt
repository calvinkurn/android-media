package com.tokopedia.play.ui.closebutton

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.extensions.isKeyboardShown
import com.tokopedia.play.ui.closebutton.interaction.CloseButtonInteractionEvent
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by jegul on 05/05/20
 */
class CloseButtonComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        private val scope: CoroutineScope,
        dispatchers: CoroutineDispatcherProvider
) : UIComponent<CloseButtonInteractionEvent>, CloseButtonView.Listener {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container)

    init {
        scope.launch(dispatchers.immediate) {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.Init -> uiView.invisible()
                            is ScreenStateEvent.BottomInsetsChanged -> {
                                if (it.insetsViewMap.isKeyboardShown) uiView.show()
                                else uiView.invisible()
                            }
                        }
                    }
        }
    }

    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Flow<CloseButtonInteractionEvent> {
        return bus.getSafeManagedFlow(CloseButtonInteractionEvent::class.java)
    }

    override fun onCloseButtonClicked(view: CloseButtonView) {
        scope.launch {
            bus.emit(CloseButtonInteractionEvent::class.java, CloseButtonInteractionEvent.OnClicked)
        }
    }

    protected open fun initView(container: ViewGroup) =
            CloseButtonView(container, this)
}