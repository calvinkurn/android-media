package com.tokopedia.settingbank

import android.content.Context
import android.content.Intent
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker

/**
 * @author by nisie on 7/23/18.
 */
interface BankRouter{

    fun getPhoneVerificationActivityIntent(context: Context) : Intent

    fun getAnalyticTracker() : AnalyticTracker

}