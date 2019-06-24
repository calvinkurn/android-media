package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalCategory {

    const val HOST_CATEGORY = "category"
    val INTERNAL_CATEGORY = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_CATEGORY}"

    val AGE_RESTRICTION = "$INTERNAL_CATEGORY/age_restriction"
}