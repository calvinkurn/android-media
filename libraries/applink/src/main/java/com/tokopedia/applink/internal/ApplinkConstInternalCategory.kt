package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalCategory {

    const val HOST_CATEGORY = "category"
    const val HOST_HOTLIST = "hot"
    const val HOST_HOTLIST_REVAMP = "hotlist"
    const val HOST_FIND = "find"
    const val AGE_RESTRICTION_REQUEST_CODE = 5838
    const val RESULT_CODE_DOB_VERIFICATION_SUCCESS = 980
    const val TRADEIN_HOME_REQUEST = 22345
    const val FINAL_PRICE_REQUEST_CODE = 22456
    const val PARAM_EXTRA_SUCCESS = "VERIFICATION_SUCCESS"
    const val PARAM_TRADEIN_TYPE ="TRADEIN_TYPE"
    val INTERNAL_CATEGORY = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_CATEGORY}"

    val AGE_RESTRICTION = "$INTERNAL_CATEGORY/age_restriction"
    val TRADEIN = "$INTERNAL_CATEGORY/tradein"
    val FINAL_PRICE = "$INTERNAL_CATEGORY/host_final_price"
    val DEVICE_VALIDATION = "device_validation"

    val MONEYIN_INTERNAL = "${DeeplinkConstant.SCHEME_INTERNAL}://money_in/${DEVICE_VALIDATION}"

    val INTERNAL_HOTLIST = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_HOTLIST}"
    val INTERNAL_HOTLIST_REVAMP = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_HOTLIST_REVAMP}"
    const val INTERNAL_FIND = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_FIND}"
}