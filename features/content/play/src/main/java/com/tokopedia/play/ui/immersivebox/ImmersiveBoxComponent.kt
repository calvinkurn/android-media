package com.tokopedia.play.ui.immersivebox

import android.view.ViewGroup
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.ui.immersivebox.interaction.ImmersiveBoxInteractionEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Created by jegul on 13/12/19
 */
class ImmersiveBoxComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        coroutineScope: CoroutineScope
) : UIComponent<ImmersiveBoxInteractionEvent>, CoroutineScope by coroutineScope, ImmersiveBoxView.Listener {

    private val uiView = initView(container)

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

    private fun initView(container: ViewGroup) =
            ImmersiveBoxView(container, this)
}