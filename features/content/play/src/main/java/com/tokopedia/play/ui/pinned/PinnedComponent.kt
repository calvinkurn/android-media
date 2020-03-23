package com.tokopedia.play.ui.pinned

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.ui.pinned.interaction.PinnedInteractionEvent
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.uimodel.PinnedMessageUiModel
import com.tokopedia.play.view.uimodel.PinnedProductUiModel
import com.tokopedia.play.view.uimodel.PinnedRemoveUiModel
import com.tokopedia.play.view.uimodel.PinnedUiModel
import kotlinx.coroutines.CoroutineScope
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

    init {
        launch(dispatchers.immediate) {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            ScreenStateEvent.Init -> uiView.hide()
                            is ScreenStateEvent.SetPinned -> setPinned(it.pinned, it.stateHelper.bottomInsets.isAnyShown)
                            is ScreenStateEvent.BottomInsetsChanged -> {
                                if (!it.isAnyShown && it.stateHelper.shouldShowPinned) uiView.show() else uiView.hide()
                            }
                            is ScreenStateEvent.OnNewPlayRoomEvent -> if(it.event.isFreeze || it.event.isBanned) uiView.hide()
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

    override fun onPinnedMessageActionClicked(view: PinnedView, applink: String, message: String) {
        launch {
            bus.emit(PinnedInteractionEvent::class.java, PinnedInteractionEvent.PinnedMessageClicked(applink, message))
        }
    }

    override fun onPinnedProductActionClicked(view: PinnedView) {
        launch {
            bus.emit(PinnedInteractionEvent::class.java, PinnedInteractionEvent.PinnedProductClicked)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        uiView.onDestroy()
    }

    private fun setPinned(pinnedUiModel: PinnedUiModel, isBottomInsetsShown: Boolean) {
        when (pinnedUiModel) {
            is PinnedMessageUiModel -> {
                uiView.setPinnedMessage(pinnedUiModel)

                if (!isBottomInsetsShown) uiView.show()
                else uiView.hide()
            }
            is PinnedProductUiModel -> {
                uiView.setPinnedProduct(pinnedUiModel)

                if (!isBottomInsetsShown) uiView.show()
                else uiView.hide()
            }
            is PinnedRemoveUiModel -> {
                uiView.hide()
            }
        }
    }

    open protected fun initView(container: ViewGroup): PinnedView =
            PinnedView(container, this)
}