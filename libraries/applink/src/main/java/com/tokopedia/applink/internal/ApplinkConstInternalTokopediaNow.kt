package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalTokopediaNow {

    @JvmField
    val HOST_TOKOPEDIA_NOW = "now"

    @JvmField
    val INTERNAL_TOKOPEDIA_NOW = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_TOKOPEDIA_NOW}"

    //TokoNowHomeActivity
    @JvmField
    val HOME = "$INTERNAL_TOKOPEDIA_NOW/home"

    //TokoNowCategoryListActivity
    @JvmField
    val CATEGORY_LIST = "$INTERNAL_TOKOPEDIA_NOW/category-list?warehouse_id={warehouse_id}"

    //TokoNowSortFilterActivity
    val SORT_FILTER = "$INTERNAL_TOKOPEDIA_NOW/sort-filter?sort_value={sort_value}"

    @JvmField
    val SEARCH = "$INTERNAL_TOKOPEDIA_NOW/search"

    @JvmField
    val CATEGORY = "$INTERNAL_TOKOPEDIA_NOW/category"

    @JvmField
    val OLD_TOKOMART = "${ApplinkConstInternalGlobal.DISCOVERY}/tokomart"

    //TokoNowRecentPurchaseActivity
    @JvmField
    val RECENT_PURCHASE = "$INTERNAL_TOKOPEDIA_NOW/recent-purchase"
}