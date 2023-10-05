package com.tokopedia.tokopedianow.category.analytic

import com.tokopedia.tokopedianow.category.analytic.CategoryAnalytic.CATEGORY.EVENT_CATEGORY_PAGE
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.searchcategory.analytics.ProductAdsCarouselAnalytics
import com.tokopedia.user.session.UserSessionInterface

/**
 * Ads Slot Tracker
 * https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/3991
 */
class CategoryProductAdsAnalytic(
    userSession: UserSessionInterface,
    addressData: TokoNowLocalAddress,
) : ProductAdsCarouselAnalytics(userSession, addressData) {

    companion object {
        private const val TRACKER_ID_PRODUCT_IMPRESSION = "44063"
        private const val TRACKER_ID_PRODUCT_CLICK = "44064"
        private const val TRACKER_ID_PRODUCT_ATC = "44065"
    }

    override val trackerIdImpression = TRACKER_ID_PRODUCT_IMPRESSION
    override val trackerIdClick = TRACKER_ID_PRODUCT_CLICK
    override val trackerIdAddToCart = TRACKER_ID_PRODUCT_ATC
    override val eventCategory = EVENT_CATEGORY_PAGE
}
