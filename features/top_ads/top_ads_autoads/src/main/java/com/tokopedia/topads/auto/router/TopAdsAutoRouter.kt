package com.tokopedia.topads.auto.router

import android.app.Activity
import android.content.Context
import android.content.Intent

interface TopAdsAutoRouter {
    fun getTopAdsDashboardIntent(context: Context): Intent
    fun getSellerWebViewIntent(context: Context, url: String): Intent
    fun getTopAdsAddingPromoOptionIntent(context: Context): Intent
    fun getTopAdsGroupNewPromoIntent(context: Context): Intent
    fun goToAddProduct(activity: Activity)
    fun openTopAdsDashboardApplink(context: Context)
    fun getTopAdsGroupAdListIntent(context: Context): Intent
    fun getTopAdsKeywordNewChooseGroupIntent(context: Context, isPositive: Boolean, groupId: String?): Intent
    fun getTopAdsAddCreditIntent(context: Context): Intent
}