package com.tokopedia.shop.common.router

import android.app.Activity
import android.content.Context
import android.content.Intent

interface ShopSettingRouter{
    fun goToShopEditor(context: Context)

    fun goToManageShopEtalase(context: Context)

    fun goToManageShopNotes(context: Context)

    fun getManageShopLocationIntent(context: Context): Intent

    fun getDistrictRecommendationIntent(activity: Activity): Intent

    fun goToMerchantRedirect(context: Context)
}