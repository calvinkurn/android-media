package com.tokopedia.play.ui.productsheet

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.ui.productsheet.interaction.ProductSheetInteractionEvent
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.BottomInsetsState
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.uimodel.ProductLineUiModel
import com.tokopedia.play.view.wrapper.PlayResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * Created by jegul on 02/03/20
 */
open class ProductSheetComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        private val scope: CoroutineScope,
        dispatchers: CoroutineDispatcherProvider
) : UIComponent<ProductSheetInteractionEvent>, ProductSheetView.Listener {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container)

    init {
        scope.launch(dispatchers.immediate) {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.Init -> uiView.hide()
                            is ScreenStateEvent.BottomInsetsChanged -> { it.insetsViewMap[BottomInsetsType.ProductSheet]?.let(::handleShowHideProductSheet) }
                            is ScreenStateEvent.SetProductSheet -> when (it.productResult) {
                                is PlayResult.Loading -> if (it.productResult.showPlaceholder) uiView.showPlaceholder()
                                is PlayResult.Success -> {
                                    if (it.productResult.data.productList.isNotEmpty()) {
                                        uiView.setProductSheet(it.productResult.data)
                                    } else {
                                        uiView.showEmpty(it.productResult.data.partnerId)
                                    }
                                }
                                is PlayResult.Failure -> uiView.showError(
                                        isConnectionError = it.productResult.error is ConnectException || it.productResult.error is UnknownHostException,
                                        onError = it.productResult.onRetry
                                )
                            }
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
        scope.launch {
            bus.emit(ProductSheetInteractionEvent::class.java, ProductSheetInteractionEvent.OnCloseProductSheet)
        }
    }

    override fun onBuyButtonClicked(view: ProductSheetView, product: ProductLineUiModel) {
        scope.launch {
            bus.emit(ProductSheetInteractionEvent::class.java, ProductSheetInteractionEvent.OnBuyProduct(product))
        }
    }

    override fun onAtcButtonClicked(view: ProductSheetView, product: ProductLineUiModel) {
        scope.launch {
            bus.emit(ProductSheetInteractionEvent::class.java, ProductSheetInteractionEvent.OnAtcProduct(product))
        }
    }

    override fun onProductCardClicked(view: ProductSheetView, product: ProductLineUiModel, position: Int) {
        scope.launch {
            bus.emit(ProductSheetInteractionEvent::class.java, ProductSheetInteractionEvent.OnProductCardClicked(product, position))
        }
    }

    override fun onVoucherScrolled(lastPositionViewed: Int) {
        scope.launch {
            bus.emit(ProductSheetInteractionEvent::class.java, ProductSheetInteractionEvent.OnVoucherScrolled(lastPositionViewed))
        }
    }

    override fun onEmptyButtonClicked(partnerId: Long) {
        scope.launch {
            bus.emit(ProductSheetInteractionEvent::class.java, ProductSheetInteractionEvent.OnEmptyButtonClicked(partnerId))
        }
    }

    open fun initView(container: ViewGroup) =
            ProductSheetView(container, this)

    private fun handleShowHideProductSheet(state: BottomInsetsState) {
        if (state is BottomInsetsState.Shown) uiView.showWithHeight(state.estimatedInsetsHeight)
        else uiView.hide()
    }

}