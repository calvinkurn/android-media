package com.tokopedia.referral.interfaces

import android.app.Activity
import android.content.Intent

interface ReferralOtpRouter {

    fun getReferralPhoneNumberActivityIntent(activity: Activity): Intent
}
