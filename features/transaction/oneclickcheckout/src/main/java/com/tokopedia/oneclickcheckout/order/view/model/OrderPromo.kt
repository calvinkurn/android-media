package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.promousage.domain.entity.PromoEntryPointInfo
import com.tokopedia.purchase_platform.common.feature.promo.view.model.PromoCheckoutErrorDefault
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel

data class OrderPromo(
    var lastApply: LastApplyUiModel = LastApplyUiModel(),
    var promoErrorDefault: PromoCheckoutErrorDefault = PromoCheckoutErrorDefault(),
    var entryPointInfo: PromoEntryPointInfo? = null,
    var state: OccButtonState = OccButtonState.DISABLE,

    var isPromoRevamp: Boolean = false,
    var isDisabled: Boolean = false,
    var isAnimateWording: Boolean = false
)
