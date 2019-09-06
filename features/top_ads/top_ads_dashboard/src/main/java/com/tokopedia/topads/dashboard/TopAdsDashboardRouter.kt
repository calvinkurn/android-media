package com.tokopedia.topads.dashboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle

interface TopAdsDashboardRouter {

    fun getTopAdsDetailShopIntent(context: Context): Intent //ok
    fun getTopAdsKeywordListIntent(context: Context): Intent //ok
    fun getTopAdsAddingPromoOptionIntent(context: Context): Intent //ok
    fun getTopAdsProductAdListIntent(context: Context): Intent //ok
    fun getTopAdsGroupAdListIntent(context: Context): Intent //ok
    fun getTopAdsGroupNewPromoIntent(context: Context): Intent //ok
    fun getTopAdsKeywordNewChooseGroupIntent(context: Context, isPositive: Boolean, groupId: String?): Intent //ok
    fun getHomeIntent(context: Context): Intent

    @Throws(ClassNotFoundException::class)
    fun getHomeClass(context: Context): Class<*> //ok
    fun isSupportedDelegateDeepLink(url: String): Boolean
    fun actionNavigateByApplinksUrl(activity: Activity, url: String, bundle: Bundle) //ok

    fun openTopAdsDashboardApplink(context: Context) //ok
}