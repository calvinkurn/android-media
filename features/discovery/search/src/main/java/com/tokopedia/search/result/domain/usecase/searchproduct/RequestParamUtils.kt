package com.tokopedia.search.result.domain.usecase.searchproduct

import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.*
import com.tokopedia.usecase.RequestParams

internal fun RequestParams.isSkipProductAds()
        = getBoolean(SEARCH_PRODUCT_SKIP_PRODUCT_ADS, false)

internal fun RequestParams.isSkipHeadlineAds()
        = getBoolean(SEARCH_PRODUCT_SKIP_HEADLINE_ADS, false)

internal fun RequestParams.isSkipGlobalNav()
        = getBoolean(SEARCH_PRODUCT_SKIP_GLOBAL_NAV, false)

internal fun RequestParams.isSkipInspirationCarousel()
        = getBoolean(SEARCH_PRODUCT_SKIP_INSPIRATION_CAROUSEL, false)

internal fun RequestParams.isSkipInspirationWidget()
        = getBoolean(SEARCH_PRODUCT_SKIP_INSPIRATION_WIDGET, false)