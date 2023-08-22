package com.tokopedia.promousage.util.analytics

import javax.inject.Inject

class PromoUsageAnalytics @Inject constructor() {

    companion object {
        const val SOURCE_CART_PAGE = 1
        const val SOURCE_CHECKOUT_PAGE = 2
        const val SOURCE_OCC_PAGE = 3
    }

    fun onErrorAttemptPromo(
        attemptedPromoCode: String,
        errorMessage: String
    ) {

    }

    fun eventClickPakaiPromoSuccess(
        pageSource: Int,
        status: String,
        selectedPromoCodes: List<String>
    ) {

    }

    fun eventViewErrorAfterClickPakaiPromo(
        pageSource: Int,
        promoId: String,
        errorMessage: String
    ) {

    }

    fun eventClickSimpanPromoBaru(
        pageSource: Int
    ) {

    }

    fun eventClickSelectKupon(
        pageSource: Int,
        clickedPromoCode: String,
        causingOtherPromoClash: Boolean
    ) {

    }

    fun eventClickSelectPromo(
        pageSource: Int,
        code: String
    ) {

    }

    fun eventClickDeselectKupon(
        pageSource: Int,
        clickedPromoCode: String,
        causingOtherPromoClash: Boolean
    ) {

    }

    fun eventClickDeselectPromo(
        pageSource: Int,
        code: String
    ) {

    }
}
