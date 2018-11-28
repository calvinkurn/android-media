package com.tokopedia.topads.dashboard

import android.content.Context
import android.content.Intent

interface TopAdsDashboardRouter {

    fun sendEventTracking(event: String, category: String, action: String, label: String)

    fun getTopAdsDetailShopIntent(context: Context): Intent
    fun getTopAdsKeywordListIntent(context: Context): Intent
    fun getTopAdsAddingPromoOptionIntent(context: Context): Intent
    fun getTopAdsProductAdListIntent(context: Context): Intent
    fun getTopAdsGroupAdListIntent(context: Context): Intent
    fun getTopAdsGroupNewPromoIntent(context: Context): Intent
    fun getTopAdsKeywordNewChooseGroupIntent(context: Context, isPositive: Boolean, groupId: String?): Intent

    fun eventTopAdsProductClickKeywordDashboard()
    fun eventTopAdsProductClickProductDashboard()
    fun eventTopAdsProductClickGroupDashboard()
    fun eventTopAdsProductAddBalance()
    fun eventTopAdsShopChooseDateCustom()
    fun eventTopAdsShopDatePeriod(label: String)
    fun eventTopAdsProductStatisticBar(label: String)
    fun eventTopAdsShopStatisticBar(label: String)
}