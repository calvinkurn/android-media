package com.tokopedia.play.ui.immersivebox

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.ui.immersivebox.interaction.ImmersiveBoxInteractionEvent
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by jegul on 13/12/19
 */
open class ImmersiveBoxComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        coroutineScope: CoroutineScope,
        dispatchers: CoroutineDispatcherProvider
) : UIComponent<ImmersiveBoxInteractionEvent>, CoroutineScope by coroutineScope, ImmersiveBoxView.Listener {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container)

    init {
        launch(dispatchers.immediate) {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            ScreenStateEvent.Init -> uiView.show()
                            is ScreenStateEvent.BottomInsetsChanged -> if (it.isAnyShown) uiView.hide() else uiView.show()
                            is ScreenStateEvent.OnNewPlayRoomEvent -> if(it.event.isFreeze || it.event.isBanned) uiView.hide()
                        }
                    }
        }
    }

    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Flow<ImmersiveBoxInteractionEvent> {
        return bus.getSafeManagedFlow(ImmersiveBoxInteractionEvent::class.java)
    }

    override fun onImmersiveBoxClicked(view: ImmersiveBoxView) {
        launch {
            bus.emit(ImmersiveBoxInteractionEvent::class.java, ImmersiveBoxInteractionEvent.BoxClicked)
        }
    }

    protected open fun initView(container: ViewGroup) =
            ImmersiveBoxView(container, this)
}