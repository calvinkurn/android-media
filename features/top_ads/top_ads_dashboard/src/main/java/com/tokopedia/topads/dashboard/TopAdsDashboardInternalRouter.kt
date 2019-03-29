package com.tokopedia.topads.dashboard

import android.content.Context
import android.content.Intent
import com.tokopedia.topads.dashboard.view.activity.TopAdsAddCreditActivity
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity

object TopAdsDashboardInternalRouter{

    @JvmStatic
    fun getTopAdsdashboardIntent(context: Context): Intent = TopAdsDashboardActivity.getCallingIntent(context)

    @JvmStatic
    fun getTopAdsAddCreditIntent(context: Context): Intent = TopAdsAddCreditActivity.getCallingIntent(context)
}