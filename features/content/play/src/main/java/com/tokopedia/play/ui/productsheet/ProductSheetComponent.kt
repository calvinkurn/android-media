package com.tokopedia.play.ui.productsheet

import android.view.ViewGroup
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

/**
 * Created by jegul on 02/03/20
 */
class ProductSheetComponent(
        container: ViewGroup,
        bus: EventBusFactory,
        coroutineScope: CoroutineScope,
        dispatchers: CoroutineDispatcherProvider
) : UIComponent<Unit>, CoroutineScope by coroutineScope {

    private val uiView = initView(container)

    init {
        launch(dispatchers.immediate) {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            ScreenStateEvent.Init -> uiView.hide()
                            is ScreenStateEvent.BottomInsetsView -> if (it.type.isBottomSheet) {
                                if (it.isShown) uiView.show() else uiView.hide()
                            }
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

    private fun initView(container: ViewGroup) =
            ProductSheetView(container)
}