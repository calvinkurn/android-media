package com.tokopedia.referral

import android.app.Activity
import android.content.Context
import android.content.Intent

import java.util.HashMap

interface ReferralRouter {
    fun getLoginIntent(context: Context): Intent
    fun eventReferralAndShare(context: Context, action: String, label: String)
    fun setBranchReferralCode(referralCode: String)
    fun sendMoEngageReferralScreenOpen(context: Context, screenName: String)
    fun executeDefaultShare(activity: Activity, keyValueMap: HashMap<String, String>)
    fun executeShareSocmedHandler(activity: Activity, keyValueMap: HashMap<String, String>, packageName: String)
    fun sendAnalyticsToGTM(context: Context, type: String, channel: String)
    fun getReferralPhoneNumberActivityIntent(activity: Activity): Intent
}
