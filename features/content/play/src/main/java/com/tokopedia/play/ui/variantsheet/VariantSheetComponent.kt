package com.tokopedia.play.ui.variantsheet

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.ui.variantsheet.interaction.VariantSheetInteractionEvent
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
 * Created by jegul on 05/03/20
 */
open class VariantSheetComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        private val scope: CoroutineScope,
        dispatchers: CoroutineDispatcherProvider
) : UIComponent<VariantSheetInteractionEvent>, VariantSheetView.Listener {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container)

    init {
        scope.launch(dispatchers.immediate) {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.Init -> uiView.hide()
                            is ScreenStateEvent.BottomInsetsChanged -> { it.insetsViewMap[BottomInsetsType.VariantSheet]?.let(::handleShowHideVariantSheet) }
                            is ScreenStateEvent.SetVariantSheet -> when (it.variantResult) {
                                is PlayResult.Loading -> if (it.variantResult.showPlaceholder) uiView.showPlaceholder()
                                is PlayResult.Success -> uiView.setVariantSheet(it.variantResult.data)
                                is PlayResult.Failure -> uiView.showError(
                                        isConnectionError = it.variantResult.error is ConnectException || it.variantResult.error is UnknownHostException,
                                        onError = it.variantResult.onRetry
                                )
                            }
                            is ScreenStateEvent.SetVariantToaster -> uiView.showToaster(it.toasterType, it.message, it.actionText, it.actionClickListener)
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
        scope.launch {
            bus.emit(VariantSheetInteractionEvent::class.java, VariantSheetInteractionEvent.OnCloseVariantSheet)
        }
    }

    override fun onAddToCartClicked(view: VariantSheetView, productModel: ProductLineUiModel) {
        scope.launch {
            bus.emit(VariantSheetInteractionEvent::class.java, VariantSheetInteractionEvent.OnAddProductToCart(productModel))
        }
    }

    override fun onBuyClicked(view: VariantSheetView, productModel: ProductLineUiModel) {
        scope.launch {
            bus.emit(VariantSheetInteractionEvent::class.java, VariantSheetInteractionEvent.OnBuyProduct(productModel))
        }
    }

    open fun initView(container: ViewGroup) =
            VariantSheetView(container, this)

    private fun handleShowHideVariantSheet(state: BottomInsetsState) {
        if (state is BottomInsetsState.Shown) uiView.showWithHeight(state.estimatedInsetsHeight)
        else uiView.hide()
    }
}