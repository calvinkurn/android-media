package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia-android-internal://global".
 */
object ApplinkConstInternalGlobal {

    @JvmField
    val HOST_GLOBAL = "global"

    //Extras
    @JvmField
    val PARAM_UUID = "uuid"
    @JvmField
    val PARAM_MSISDN = "msisdn"
    @JvmField
    val PARAM_PHONE = "phone"
    @JvmField
    val PARAM_CIPF_USER_ID = "userId"
    @JvmField
    val PARAM_CIPF_OLD_PHONE = "oldPhone"

    @JvmField
    val INTERNAL_GLOBAL = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_GLOBAL}"

    // WithdrawActivity
    // tokopedia-android-internal://global/withdraw
    @JvmField
    val WITHDRAW = "$INTERNAL_GLOBAL/withdraw"

    // ChangeInactiveFormRequestActivity
    // tokopedia-android-internal://global/change-inactive-phone-form
    @JvmField
    val CHANGE_INACTIVE_PHONE_FORM = "$INTERNAL_GLOBAL/change-inactive-phone-form"

    //ChooseTokocashAccountActivity
    // tokopedia-android-internal://global/choose-account
    @JvmField
    val CHOOSE_ACCOUNT = "$INTERNAL_GLOBAL/choose-account"


}
