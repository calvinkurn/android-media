package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalDiscovery {

    const val HOST_DISCOVERY = "discovery"

    const val HOST_MARKETPLACE = "marketplace"

    const val INTERNAL_DISCOVERY = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_DISCOVERY"

    const val FILTER = "$INTERNAL_DISCOVERY/filter"

    const val SORT = "$INTERNAL_DISCOVERY/sort"

    const val CATALOG = "$INTERNAL_DISCOVERY/catalog"

    const val SEARCH_RESULT = "$INTERNAL_DISCOVERY/search-result"

    const val AUTOCOMPLETE = "$INTERNAL_DISCOVERY/autocomplete"

    const val SIMILAR_SEARCH_RESULT_BASE = "$INTERNAL_DISCOVERY/similar-search-result"

    const val SIMILAR_SEARCH_RESULT = "$SIMILAR_SEARCH_RESULT_BASE/{product_id}/"

    const val PRODUCT_CARD_OPTIONS = "$INTERNAL_DISCOVERY/product-card-options"

    const val DISCOVERY_CATEGORY_DETAIL_MARKETPLACE = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_MARKETPLACE}/category/{DEPARTMENT_ID}/"
}