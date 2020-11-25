package com.tokopedia.play.view.wrapper

import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.uimodel.ProductLineUiModel

/**
 * Created by jegul on 18/12/19
 */
sealed class InteractionEvent {

    abstract val needLogin: Boolean

    object SendChat : InteractionEvent() {
        override val needLogin: Boolean = true
    }
    data class Like(val shouldLike: Boolean) : InteractionEvent() {
        override val needLogin: Boolean = true
    }
    data class Follow(val partnerId: Long, val partnerAction: PartnerFollowAction) : InteractionEvent() {
        override val needLogin: Boolean = true
    }
    data class DoActionProduct(val product: ProductLineUiModel, val action: ProductAction, val type: BottomInsetsType) : InteractionEvent() {
        override val needLogin: Boolean = true
    }
    data class OpenProductDetail(val product: ProductLineUiModel, val position: Int) : InteractionEvent() {
        override val needLogin: Boolean = false
    }
    object CartPage : InteractionEvent() {
        override val needLogin: Boolean = true
    }
    object ClickPinnedProduct : InteractionEvent() {
        override val needLogin: Boolean = false
    }
}