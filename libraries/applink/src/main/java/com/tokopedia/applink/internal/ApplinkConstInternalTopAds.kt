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

    //credit
    @JvmField
    val TOPADS_BUY_CREDIT = "${INTERNAL_TOPADS}/buy"

    @JvmField
    val TOPADS_HISTORY_CREDIT = "${INTERNAL_TOPADS}/history-credit"

    @JvmField
    val TOPADS_AUTO_TOPUP = "${INTERNAL_TOPADS}/auto-topup"

    //edit-manualads
    @JvmField
    val TOPADS_EDIT_ADS = "${INTERNAL_TOPADS}/edit-ads"

    @JvmField
    val TOPADS_EDIT_WITHOUT_GROUP = "${INTERNAL_TOPADS}/edit-without-group"

    //edit-autoads
    @JvmField
    val TOPADS_EDIT_AUTOADS = "${INTERNAL_TOPADS}/edit-autoads"

    //create-autoads
    @JvmField
    val TOPADS_ADS_SELECTION = "${INTERNAL_TOPADS}/ad-selection"

    @JvmField
    val TOPADS_AUTOADS_CREATE = "${INTERNAL_TOPADS}/create-autoads"

    //onboarding
    @JvmField
    val TOPADS_CREATE_ADS = "${INTERNAL_TOPADS}/create-ads"

    @JvmField
    val TOPADS_CREATE_CHOOSER = "${INTERNAL_TOPADS}/ad-picker"

    @JvmField
    val TOPADS_CREATION_ONBOARD = "${INTERNAL_TOPADS}/creation-onboard"

    @JvmField
    val TOPADS_AUTOADS_ONBOARDING = "${INTERNAL_TOPADS}/autoads-onboarding"

    //headline
    @JvmField
    val TOPADS_HEADLINE_ADS_CREATION = "${INTERNAL_TOPADS}/headline-ad-creation"

    @JvmField
    val TOPADS_HEADLINE_DETAIL = "${INTERNAL_TOPADS}/headline-ad-detail"

    @JvmField
    val TOPADS_HEADLINE_ADS_EDIT = "${INTERNAL_TOPADS}/headline-ad-edit"

    @JvmField
    val TOPADS_NEGATIVE_KEYWORD_EDIT = "${INTERNAL_TOPADS}/edit-ads-negative-keyword"

}
