package com.tokopedia.play.ui.productsheet.interaction

import com.tokopedia.play.component.ComponentEvent
import com.tokopedia.play.view.uimodel.ProductLineUiModel

/**
 * Created by jegul on 03/03/20
 */
sealed class ProductSheetInteractionEvent : ComponentEvent {

    object OnCloseProductSheet : ProductSheetInteractionEvent()
    data class OnBuyProduct(val product: ProductLineUiModel) : ProductSheetInteractionEvent()
    data class OnAtcProduct(val product: ProductLineUiModel) : ProductSheetInteractionEvent()
}