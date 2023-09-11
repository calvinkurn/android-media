package com.tokopedia.checkout.revamp.view.uimodel

import com.tokopedia.promousage.domain.entity.PromoEntryPointInfo
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel

data class CheckoutPromoModel(
    override val cartStringGroup: String = "",
    val promo: LastApplyUiModel,
    val entryPointInfo: PromoEntryPointInfo? = null,

    val isPromoRevamp: Boolean = false,
    val isEnable: Boolean = true,
    val isLoading: Boolean = false,
    val isAnimateWording: Boolean = false,
) : CheckoutItem
