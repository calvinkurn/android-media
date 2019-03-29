package com.tokopedia.shop.common.router

import android.app.Activity
import android.content.Context
import android.content.Intent

interface ShopSettingRouter {

    fun getManageShopBasicDataIntent(context: Context): Intent

    fun getManageShopEtalaseIntent(context: Context): Intent

    fun getManageShopNotesIntent(context: Context): Intent

    fun getManageShopLocationIntent(context: Context): Intent

    fun getDistrictRecommendationIntent(activity: Activity): Intent

    fun goToMerchantRedirect(context: Context)

    fun goToGmSubscribeMembershipRedirect(context: Context)

    fun goToGMSubscribe(activity: Activity)
}