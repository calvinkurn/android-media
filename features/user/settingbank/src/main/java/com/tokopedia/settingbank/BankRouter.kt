package com.tokopedia.settingbank

import android.content.Context
import android.content.Intent

/**
 * @author by nisie on 7/23/18.
 */
interface BankRouter {
    fun getPhoneVerificationActivityIntent(context: Context): Intent

}