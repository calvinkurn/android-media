package com.tokopedia.play.view.wrapper

import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.view.uimodel.ProductLineUiModel

/**
 * Created by jegul on 18/12/19
 */
sealed class InteractionEvent {

    abstract val needLogin: Boolean

    object SendChat : InteractionEvent() {
        override val needLogin: Boolean = true
    }
    data class Like(val shouldLike: Boolean, override val needLogin: Boolean = true) : InteractionEvent()
    data class Follow(val partnerId: Long, val partnerAction: PartnerFollowAction, override val needLogin: Boolean = true) : InteractionEvent()
    data class AtcProduct(val product: ProductLineUiModel, override val needLogin: Boolean = true) : InteractionEvent()
    data class BuyProduct(val product: ProductLineUiModel, override val needLogin: Boolean = true) : InteractionEvent()
    object CartPage : InteractionEvent() {
        override val needLogin: Boolean = true
    }
}