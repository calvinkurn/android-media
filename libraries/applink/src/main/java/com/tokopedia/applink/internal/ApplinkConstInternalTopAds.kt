package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia://hotel/dashboard"
 */

object ApplinkConstInternalTopAds {

    const val HOST_TOPADS = "topads"

    const val INTERNAL_TOPADS = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_TOPADS}"

    // TopAdsDashboard
    const val TOPADS_DASHBOARD_SELLER = "${DeeplinkConstant.SCHEME_SELLERAPP}://${HOST_TOPADS}"

    const val TOPADS_DASHBOARD_CUSTOMER = "${DeeplinkConstant.SCHEME_TOKOPEDIA}://${HOST_TOPADS}/dashboard"

    const val TOPADS_DASHBOARD_INTERNAL = "${INTERNAL_TOPADS}/dashboard"

    //credit
    const val TOPADS_BUY_CREDIT = "${INTERNAL_TOPADS}/buy"

    const val TOPADS_HISTORY_CREDIT = "${INTERNAL_TOPADS}/history-credit"

    const val TOPADS_AUTO_TOPUP = "${INTERNAL_TOPADS}/auto-topup"

    //edit-manualads
    const val TOPADS_EDIT_ADS = "${INTERNAL_TOPADS}/edit-ads"

    const val TOPADS_EDIT_WITHOUT_GROUP = "${INTERNAL_TOPADS}/edit-without-group"

    //edit-autoads
    const val TOPADS_EDIT_AUTOADS = "${INTERNAL_TOPADS}/edit-autoads"

    //create-autoads
    const val TOPADS_ADS_SELECTION = "${INTERNAL_TOPADS}/ad-selection"

    const val TOPADS_AUTOADS_CREATE = "${INTERNAL_TOPADS}/create-autoads"

    //onboarding
    const val TOPADS_CREATE_ADS = "${INTERNAL_TOPADS}/create-ads"

    const val TOPADS_CREATE_CHOOSER = "${INTERNAL_TOPADS}/ad-picker"

    const val TOPADS_CREATION_ONBOARD = "${INTERNAL_TOPADS}/creation-onboard"

    const val TOPADS_AUTOADS_ONBOARDING = "${INTERNAL_TOPADS}/autoads-onboarding"

    //headline
    const val TOPADS_HEADLINE_ADS_CREATION = "${INTERNAL_TOPADS}/headline-ad-creation"

    const val TOPADS_HEADLINE_DETAIL = "${INTERNAL_TOPADS}/headline-ad-detail"

    const val TOPADS_HEADLINE_ADS_EDIT = "${INTERNAL_TOPADS}/headline-ad-edit"

    const val TOPADS_NEGATIVE_KEYWORD_EDIT = "${INTERNAL_TOPADS}/edit-ads-negative-keyword"

}
