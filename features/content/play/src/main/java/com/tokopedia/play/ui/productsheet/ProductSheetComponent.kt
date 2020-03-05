package com.tokopedia.play.ui.productsheet

import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.ui.productsheet.interaction.ProductSheetInteractionEvent
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.BottomInsetsType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by jegul on 02/03/20
 */
class ProductSheetComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        coroutineScope: CoroutineScope,
        dispatchers: CoroutineDispatcherProvider
) : UIComponent<ProductSheetInteractionEvent>, CoroutineScope by coroutineScope, ProductSheetView.Listener {

    private val uiView = initView(container)

    init {
        launch(dispatchers.immediate) {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            ScreenStateEvent.Init -> uiView.setStateHidden()
                            is ScreenStateEvent.BottomInsetsView -> if (it.type is BottomInsetsType.BottomSheet.Product) handleIfProductSheet(it.type.height.orZero(), it.isShown)
                            is ScreenStateEvent.SetProductSheet -> uiView.setProductSheet(it.productSheetModel)
                        }
                    }
        }
    }

    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Flow<ProductSheetInteractionEvent> {
        return bus.getSafeManagedFlow(ProductSheetInteractionEvent::class.java)
    }

    override fun onCloseButtonClicked(view: ProductSheetView) {
        launch {
            bus.emit(ProductSheetInteractionEvent::class.java, ProductSheetInteractionEvent.OnCloseProductSheet)
        }
    }

    override fun onBuyButtonClicked(view: ProductSheetView, productId: String) {
        launch {
            bus.emit(ProductSheetInteractionEvent::class.java, ProductSheetInteractionEvent.OnBuyProduct(productId))
        }
    }

    override fun onAtcButtonClicked(view: ProductSheetView, productId: String) {
        launch {
            bus.emit(ProductSheetInteractionEvent::class.java, ProductSheetInteractionEvent.OnAtcProduct(productId))
        }
    }

    private fun initView(container: ViewGroup) =
            ProductSheetView(container, this)

    private fun handleIfProductSheet(height: Int, isShown: Boolean) {
        if (isShown) uiView.showWithHeight(height) else uiView.hide()
    }
}