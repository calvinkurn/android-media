package com.tokopedia.purchase_platform.express_checkout.domain.model.atc

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class PromoSuggestionModel(
        var cta: String? = null,
        var ctaColor: String? = null,
        var isVisible: Int? = 0,
        var promoCode: String? = null,
        var text: String? = null
)