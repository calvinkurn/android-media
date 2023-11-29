package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.promousage.domain.entity.PromoEntryPointInfo
import com.tokopedia.purchase_platform.common.feature.promo.view.model.PromoCheckoutErrorDefault
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel

data class OrderPromo(
    var lastApply: LastApplyUiModel = LastApplyUiModel(),
    var promoErrorDefault: PromoCheckoutErrorDefault = PromoCheckoutErrorDefault(),
    var entryPointInfo: PromoEntryPointInfo = PromoEntryPointInfo(),
    var state: OccButtonState = OccButtonState.DISABLE,

    var enableNewInterface: Boolean = false,
    var isCartCheckoutRevamp: Boolean = false,
    var isPromoRevamp: Boolean = false,
    var isDisabled: Boolean = false,
    var isAnimateWording: Boolean = false
)
