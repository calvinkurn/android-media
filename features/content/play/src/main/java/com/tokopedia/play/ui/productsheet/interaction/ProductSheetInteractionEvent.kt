package com.tokopedia.play.ui.productsheet.interaction

import com.tokopedia.play.component.ComponentEvent

/**
 * Created by jegul on 03/03/20
 */
sealed class ProductSheetInteractionEvent : ComponentEvent {

    object OnCloseProductSheet : ProductSheetInteractionEvent()
    data class OnBuyProduct(val productId: String) : ProductSheetInteractionEvent()
    data class OnAtcProduct(val productId: String) : ProductSheetInteractionEvent()
}