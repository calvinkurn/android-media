package com.tokopedia.topads.auto.router

import android.app.Activity
import android.content.Context
import android.content.Intent

interface TopAdsAutoRouter {
    fun getSellerWebViewIntent(context: Context, url: String): Intent //ok
    fun getTopAdsAddingPromoOptionIntent(context: Context): Intent //ok
    fun getTopAdsGroupNewPromoIntent(context: Context): Intent //ok
    fun goToAddProduct(activity: Activity) //ok
    fun getTopAdsDashboardIntent(context: Context): Intent //ok
    fun openTopAdsDashboardApplink(context: Context) //ok
    fun getTopAdsGroupAdListIntent(context: Context): Intent //ok
    fun getTopAdsKeywordNewChooseGroupIntent(context: Context, isPositive: Boolean, groupId: String?): Intent //ok
    fun getTopAdsAddCreditIntent(context: Context): Intent //ok
    fun goToApplinkActivity(context: Context, applink: String) //ok
}