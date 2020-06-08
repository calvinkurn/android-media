package com.tokopedia.play.ui.variantsheet.interaction

import com.tokopedia.play.component.ComponentEvent
import com.tokopedia.play.view.uimodel.ProductLineUiModel

/**
 * Created by jegul on 05/03/20
 */
sealed class VariantSheetInteractionEvent : ComponentEvent {

    object OnCloseVariantSheet : VariantSheetInteractionEvent()
    data class OnBuyProduct(val product: ProductLineUiModel) : VariantSheetInteractionEvent()
    data class OnAddProductToCart(val product: ProductLineUiModel) : VariantSheetInteractionEvent()
}