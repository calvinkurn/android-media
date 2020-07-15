package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model

import com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model.PromoCheckoutErrorDefault
import com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model.last_apply.LastApplyUiModel

data class OrderPromo(
        var lastApply: LastApplyUiModel? = null,
        var promoErrorDefault: PromoCheckoutErrorDefault? = null,
        var state: ButtonBayarState = ButtonBayarState.DISABLE
)