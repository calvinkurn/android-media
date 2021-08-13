package com.tokopedia.promocheckoutmarketplace.presentation

import com.tokopedia.promocheckoutmarketplace.presentation.analytics.PromoCheckoutAnalytics
import com.tokopedia.promocheckoutmarketplace.presentation.viewmodel.PromoCheckoutViewModel

// Helper class to provide getter for global variable
class PromoCheckoutMarketplaceHanselHelper(val promoCheckoutViewModel: PromoCheckoutViewModel,
                                           val promoCheckoutAnalytics: PromoCheckoutAnalytics) {

    fun getViewModel(): PromoCheckoutViewModel {
        return promoCheckoutViewModel
    }

    fun getAnalytics(): PromoCheckoutAnalytics {
        return promoCheckoutAnalytics
    }

}