package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalCategory {

    const val HOST_CATEGORY = "category"
    const val AGE_RESTRICTION_REQUEST_CODE = 5838
    const val RESULT_CODE_DOB_VERIFICATION_SUCCESS = 980
    const val PARAM_EXTRA_SUCCESS = "VERIFICATION_SUCCESS"
    val INTERNAL_CATEGORY = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_CATEGORY}"

    val AGE_RESTRICTION = "$INTERNAL_CATEGORY/age_restriction"
}