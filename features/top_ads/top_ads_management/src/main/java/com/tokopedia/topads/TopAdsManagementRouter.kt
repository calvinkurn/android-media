package com.tokopedia.topads

import android.content.Context
import android.content.Intent

interface TopAdsManagementRouter {
    fun getTopAdsDashboardIntent(context: Context): Intent
    fun getTopAdsAddCreditIntent(context: Context): Intent
}