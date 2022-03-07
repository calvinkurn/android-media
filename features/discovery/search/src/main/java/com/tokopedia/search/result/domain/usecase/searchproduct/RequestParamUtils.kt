package com.tokopedia.search.result.domain.usecase.searchproduct

import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_PRODUCT_ADS
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_HEADLINE_ADS
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_GLOBAL_NAV
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_INSPIRATION_CAROUSEL
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_INSPIRATION_WIDGET
import com.tokopedia.discovery.common.constants.SearchConstant.SearchProduct.SEARCH_PRODUCT_SKIP_GET_LAST_FILTER_WIDGET
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

internal fun RequestParams.isSkipGetLastFilterWidget()
    = getBoolean(SEARCH_PRODUCT_SKIP_GET_LAST_FILTER_WIDGET, false)