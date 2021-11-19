package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.purchase_platform.common.feature.promo.view.model.PromoCheckoutErrorDefault
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel

data class OrderPromo(
        var lastApply: LastApplyUiModel = LastApplyUiModel(),
        var promoErrorDefault: PromoCheckoutErrorDefault = PromoCheckoutErrorDefault(),
        var state: OccButtonState = OccButtonState.DISABLE,
        var isDisabled: Boolean = false
)