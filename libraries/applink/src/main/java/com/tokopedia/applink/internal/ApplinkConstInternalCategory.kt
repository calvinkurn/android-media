package com.tokopedia.applink.internal

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalCategory {

    private const val HOST_CATEGORY = "category"
    const val HOST_HOTLIST = "hot"
    const val HOST_HOTLIST_REVAMP = "hotlist"
    const val HOST_FIND = "find"
    private const val HOST_EXPLORE_CATEGORY = "category-explore"
    private const val HOST_BELANJA_CATEGORY = "category_belanja"
    private const val HOST_CATALOG = "catalog"
    const val AGE_RESTRICTION_REQUEST_CODE = 5838
    const val RESULT_CODE_DOB_VERIFICATION_SUCCESS = 980
    const val TRADEIN_HOME_REQUEST = 22345
    const val FINAL_PRICE_REQUEST_CODE = 22456
    const val PARAM_EXTRA_SUCCESS = "VERIFICATION_SUCCESS"
    const val PARAM_TRADEIN_TYPE ="TRADEIN_TYPE"
    const val INTERNAL_CATEGORY = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_CATEGORY}"

    val AGE_RESTRICTION = "$INTERNAL_CATEGORY/age_restriction"
    const val TRADEIN = "$INTERNAL_CATEGORY/tradein"
    const val FINAL_PRICE = "$INTERNAL_CATEGORY/host_final_price"
    const val DEVICE_VALIDATION = "device_validation"

    const val MONEYIN_INTERNAL = "${DeeplinkConstant.SCHEME_INTERNAL}://money_in/${DEVICE_VALIDATION}"

    const val INTERNAL_HOTLIST = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_HOTLIST}"
    const val INTERNAL_HOTLIST_REVAMP = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_HOTLIST_REVAMP}"
    const val INTERNAL_FIND = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_FIND}"
    const val INTERNAL_EXPLORE_CATEGORY = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_EXPLORE_CATEGORY}"
    const val INTERNAL_BELANJA_CATEGORY = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_BELANJA_CATEGORY}"
    const val INTERNAL_CATALOG = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_CATALOG}"

    fun getDiscoveryDeeplink(deeplink: String): String {
        return deeplink.replace(ApplinkConst.DISCOVERY, ApplinkConstInternalGlobal.DISCOVERY)
    }

    @JvmField
    val INTERNAL_CATEGORY_DETAIL = "$INTERNAL_CATEGORY/{DEPARTMENT_ID}"
}