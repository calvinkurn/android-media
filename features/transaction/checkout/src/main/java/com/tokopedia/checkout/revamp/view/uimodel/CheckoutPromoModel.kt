package com.tokopedia.checkout.revamp.view.uimodel

import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel

data class CheckoutPromoModel(
    override val cartStringGroup: String = "",
    val promo: LastApplyUiModel
) : CheckoutItem
