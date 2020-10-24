package com.tokopedia.phoneverification

import com.tokopedia.url.TokopediaUrl.Companion.getInstance

/**
 * @author by alvinatin on 12/10/18.
 */
class PhoneVerificationConst {

    companion object {
        const val MUTATION_USER_MSISDN_ADD = "mutation_user_msisdn_add"
        const val EXTRA_IS_MANDATORY = "EXTRA_IS_MANDATORY"
        const val EXTRA_IS_LOGOUT_ON_BACK = "EXTRA_LOGOUT_ON_BACK"
        const val EXTRA_INIT_FRAGMENT = "EXTRA_INIT_FRAGMENT"
        const val INIT_STATE_FRAGMENT_HOME = 0
        const val SCREEN_CHANGE_PHONE_NUMBER = "Change Phone Number Page"
        const val SCREEN_PHONE_VERIFICATION = "Phone Verification Screen"
        @JvmField
        var BASE_URL = getInstance().ACCOUNTS
        const val CHANGE_PHONE_NUMBER = "/api/msisdn/change-msisdn"
        const val VERIFY_PHONE_NUMBER = "/api/msisdn/verify-msisdn"
        const val URL_TOKOCASH_SHARE = "https://ecs7.tokopedia.net/img/android/others/ic_referral_tokocash.png"
    }
}