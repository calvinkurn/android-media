package com.tokopedia.play.ui.variantsheet

import android.view.ViewGroup
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.ui.variantsheet.interaction.VariantSheetInteractionEvent
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.BottomInsetsState
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.wrapper.PlayResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by jegul on 05/03/20
 */
class VariantSheetComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        coroutineScope: CoroutineScope,
        dispatchers: CoroutineDispatcherProvider
) : UIComponent<VariantSheetInteractionEvent>, CoroutineScope by coroutineScope, VariantSheetView.Listener {

    private val uiView = initView(container)

    init {
        launch(dispatchers.immediate) {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            ScreenStateEvent.Init -> uiView.hide()
                            is ScreenStateEvent.BottomInsetsChanged -> { it.insetsViewMap[BottomInsetsType.VariantSheet]?.let(::handleShowHideVariantSheet) }
                            is ScreenStateEvent.SetVariantSheet -> when (it.variantResult) {
                                is PlayResult.Loading -> if (it.variantResult.showPlaceholder) uiView.showPlaceholder(true)
                                is PlayResult.Success -> uiView.setVariantSheet(it.variantResult.data)
                            }
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

    override fun onCloseButtonClicked(view: VariantSheetView) {
        launch {
            bus.emit(VariantSheetInteractionEvent::class.java, VariantSheetInteractionEvent.OnCloseVariantSheet)
        }
    }

    override fun onAddToCartClicked(view: VariantSheetView, productId: String) {
        launch {
            bus.emit(VariantSheetInteractionEvent::class.java, VariantSheetInteractionEvent.OnAddProductToCart(productId))
        }
    }

    override fun onBuyClicked(view: VariantSheetView, productId: String) {
        launch {
            bus.emit(VariantSheetInteractionEvent::class.java, VariantSheetInteractionEvent.OnBuyProduct(productId))
        }
    }

    override fun onVariantGuideLinedClicked(url: String) {
        launch {
            bus.emit(VariantSheetInteractionEvent::class.java, VariantSheetInteractionEvent.OnClickVariantGuideline(url))
        }
    }

    private fun initView(container: ViewGroup) =
            VariantSheetView(container, this)

    private fun handleShowHideVariantSheet(state: BottomInsetsState) {
        if (state is BottomInsetsState.Shown) uiView.showWithHeight(state.estimatedInsetsHeight)
        else uiView.hide()
    }
}