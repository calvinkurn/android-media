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
    val SORT_FILTER = "$INTERNAL_TOKOPEDIA_NOW/sort-filter"

    //TokoNowDateFilterActivity
    val DATE_FILTER = "$INTERNAL_TOKOPEDIA_NOW/date-filter"

    //TokoNowEducationalInfoActivity
    val EDUCATIONAL_INFO = "$INTERNAL_TOKOPEDIA_NOW/educational-info"

    @JvmField
    val SEARCH = "$INTERNAL_TOKOPEDIA_NOW/search"

    @JvmField
    val CATEGORY = "$INTERNAL_TOKOPEDIA_NOW/category"

    //TokoNowRepurchaseActivity
    @JvmField
    val REPURCHASE = "$INTERNAL_TOKOPEDIA_NOW/repurchase-page"

    //TokoNowCategoryFilterActivity
    @JvmField
    val CATEGORY_FILTER = "$INTERNAL_TOKOPEDIA_NOW/category-filter?warehouse_id={warehouse_id}"
}