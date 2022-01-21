package com.tokopedia.play.view.wrapper

import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.uimodel.PlayProductUiModel

/**
 * Created by jegul on 18/12/19
 */
sealed class InteractionEvent {

    abstract val needLogin: Boolean

    object SendChat : InteractionEvent() {
        override val needLogin: Boolean = true
    }
    data class DoActionProduct(val product: PlayProductUiModel.Product, val action: ProductAction, val type: BottomInsetsType) : InteractionEvent() {
        override val needLogin: Boolean = true
    }
    data class OpenProductDetail(val product: PlayProductUiModel.Product, val position: Int) : InteractionEvent() {
        override val needLogin: Boolean = false
    }
    object CartPage : InteractionEvent() {
        override val needLogin: Boolean = true
    }
    object OpenUserReport : InteractionEvent() {
        override val needLogin: Boolean = true
    }
}