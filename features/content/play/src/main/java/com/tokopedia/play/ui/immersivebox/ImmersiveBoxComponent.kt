package com.tokopedia.play.ui.immersivebox

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.ui.immersivebox.interaction.ImmersiveBoxInteractionEvent
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
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
        private val scope: CoroutineScope,
        dispatchers: CoroutineDispatcherProvider
) : UIComponent<ImmersiveBoxInteractionEvent>, ImmersiveBoxView.Listener {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container)

    init {
        scope.launch(dispatchers.immediate) {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.Init -> uiView.show()
                            is ScreenStateEvent.BottomInsetsChanged -> if (it.isAnyShown) uiView.hide() else uiView.show()
                            is ScreenStateEvent.OnNewPlayRoomEvent -> if(it.event.isFreeze || it.event.isBanned) uiView.hide()
                            is ScreenStateEvent.ImmersiveStateChanged -> if (it.shouldImmersive) uiView.fadeOut() else uiView.fadeIn()
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

    override fun onImmersiveBoxClicked(view: ImmersiveBoxView, currentAlpha: Float) {
        scope.launch {
            bus.emit(ImmersiveBoxInteractionEvent::class.java, ImmersiveBoxInteractionEvent.BoxClicked(currentAlpha))
        }
    }

    protected open fun initView(container: ViewGroup) =
            ImmersiveBoxView(container, this)
}