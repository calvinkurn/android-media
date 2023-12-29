package com.tokopedia.tokopedianow.search.analytics

import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.search.analytics.SearchTracking.Category.TOKONOW_DASH_SEARCH_RESULT_PAGE
import com.tokopedia.tokopedianow.searchcategory.analytics.ProductAdsCarouselAnalytics
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Ads Slot Tracker
 * https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/3991
 */
class SearchProductAdsAnalytics @Inject constructor(
    userSession: UserSessionInterface,
    addressData: TokoNowLocalAddress
) : ProductAdsCarouselAnalytics(userSession, addressData) {

    companion object {
        private const val TRACKER_ID_PRODUCT_IMPRESSION = "44060"
        private const val TRACKER_ID_PRODUCT_CLICK = "44061"
        private const val TRACKER_ID_PRODUCT_ATC = "44062"
    }

    override val trackerIdImpression = TRACKER_ID_PRODUCT_IMPRESSION
    override val trackerIdClick = TRACKER_ID_PRODUCT_CLICK
    override val trackerIdAddToCart = TRACKER_ID_PRODUCT_ATC
    override val eventCategory = TOKONOW_DASH_SEARCH_RESULT_PAGE
}
