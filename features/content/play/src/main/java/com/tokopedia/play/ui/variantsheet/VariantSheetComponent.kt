package com.tokopedia.play.ui.variantsheet

import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.ui.variantsheet.interaction.VariantSheetInteractionEvent
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.BottomInsetsType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

/**
 * Created by jegul on 05/03/20
 */
class VariantSheetComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        coroutineScope: CoroutineScope,
        dispatchers: CoroutineDispatcherProvider
) : UIComponent<VariantSheetInteractionEvent>, CoroutineScope by coroutineScope {

    private val uiView = initView(container)

    init {
        launch(dispatchers.immediate) {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            ScreenStateEvent.Init -> uiView.setStateHidden()
                            is ScreenStateEvent.BottomInsetsView -> if (it.type is BottomInsetsType.BottomSheet.Variant) handleIfVariantSheet(it.type.height.orZero(), it.isShown)
                            is ScreenStateEvent.SetProductSheet -> uiView.setProductSheet(it.productSheetModel)
                        }
                    }
        }
    }

    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Flow<VariantSheetInteractionEvent> {
        return bus.getSafeManagedFlow(VariantSheetInteractionEvent::class.java)
    }

    private fun initView(container: ViewGroup) =
            VariantSheetView(container)

    private fun handleIfVariantSheet(height: Int, isShown: Boolean) {
        if (isShown) uiView.showWithHeight(height) else uiView.hide()
    }
}