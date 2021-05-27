package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalTokoNow {

    @JvmField
    val HOST_TOKOMART = "tokonow"

    @JvmField
    val INTERNAL_TOKOMART = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_TOKOMART}"

    //TokoMartHomeActivity
    @JvmField
    val HOME = INTERNAL_TOKOMART

    //TokoMartCategoryListActivity
    @JvmField
    val CATEGORY_LIST = "$INTERNAL_TOKOMART/category-list?warehouse_id={warehouse_id}"
}