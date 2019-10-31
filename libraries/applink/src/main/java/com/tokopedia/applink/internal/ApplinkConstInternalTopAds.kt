package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia://hotel/dashboard"
 */

object ApplinkConstInternalTopAds {

    @JvmField
    val HOST_TOPADS = "topads"
    @JvmField
    val INTERNAL_TOPADS = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_TOPADS}"


    // TopAdsDashboard
    @JvmField
    val TOPADS_DASHBOARD_SELLER = "${DeeplinkConstant.SCHEME_SELLERAPP}://${HOST_TOPADS}"
    @JvmField
    val TOPADS_DASHBOARD_CUSTOMER = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://${HOST_TOPADS}/dashboard"
    @JvmField
    val TOPADS_DASHBOARD_INTERNAL = "${INTERNAL_TOPADS}/dashboard"
    @JvmField
    val TOPADS_KEYWORD_LIST = "${INTERNAL_TOPADS}/keyword-list"
    @JvmField
    val TOPADS_ADD_PROMO_OPTION = "${INTERNAL_TOPADS}/add-promo-option"
    @JvmField
    val TOPADS_PRODUCT_ADS_LIST = "${INTERNAL_TOPADS}/product-ads-list"
    @JvmField
    val TOPADS_GROUP_ADS_LIST = "${INTERNAL_TOPADS}/group-ads-list"
    @JvmField
    val TOPADS_GROUP_NEW_PROMO = "${INTERNAL_TOPADS}/group-new-promo"
    @JvmField
    val TOPADS_BUY_CREDIT = "${INTERNAL_TOPADS}/buy"
    @JvmField
    val TOPADS_HISTORY_CREDIT = "${INTERNAL_TOPADS}/history-credit"
    @JvmField
    val TOPADS_AUTOADS = "${DeeplinkConstant.SCHEME_SELLERAPP}://${HOST_TOPADS}/autoads"
    @JvmField
    val TOPADS_KEYWORD_NEW_CHOOSE_GROUP = "${INTERNAL_TOPADS}/keyword-new-choose-group/?is_pos={isPos}&EXTRA_CHOOSEN_GROUP={groupId}"

}
