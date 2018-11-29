package com.tokopedia.topads.dashboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle

interface TopAdsDashboardRouter {

    fun sendEventTracking(event: String, category: String, action: String, label: String)

    fun getTopAdsDetailShopIntent(context: Context): Intent
    fun getTopAdsKeywordListIntent(context: Context): Intent
    fun getTopAdsAddingPromoOptionIntent(context: Context): Intent
    fun getTopAdsProductAdListIntent(context: Context): Intent
    fun getTopAdsGroupAdListIntent(context: Context): Intent
    fun getTopAdsGroupNewPromoIntent(context: Context): Intent
    fun getTopAdsKeywordNewChooseGroupIntent(context: Context, isPositive: Boolean, groupId: String?): Intent
    fun getHomeIntent(context: Context): Intent

    @Throws(ClassNotFoundException::class)
    fun getHomeClass(context: Context): Class<*>
    fun isSupportedDelegateDeepLink(url: String): Boolean
    fun actionNavigateByApplinksUrl(activity: Activity, url: String, bundle: Bundle)

    fun eventTopAdsProductClickKeywordDashboard()
    fun eventTopAdsProductClickProductDashboard()
    fun eventTopAdsProductClickGroupDashboard()
    fun eventTopAdsProductAddBalance()
    fun eventTopAdsShopChooseDateCustom()
    fun eventTopAdsShopDatePeriod(label: String)
    fun eventTopAdsProductStatisticBar(label: String)
    fun eventTopAdsShopStatisticBar(label: String)
    fun eventOpenTopadsPushNotification(label: String)

    fun openTopAdsDashboardApplink(context: Context)
}