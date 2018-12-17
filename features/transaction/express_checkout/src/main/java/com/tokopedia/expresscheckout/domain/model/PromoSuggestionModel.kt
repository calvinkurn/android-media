package com.tokopedia.expresscheckout.domain.model

/**
 * Created by Irfan Khoirul on 30/11/18.
 */

data class PromoSuggestionModel(
        var cta: String? = null,
        var ctaColor: String? = null,
        var isVisible: Int = 0,
        var promoCode: String? = null,
        var text: String? = null
)