package com.tokopedia.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment

/**
 * @author by milhamj on 9/17/18.
 */
interface ProfileModuleRouter {
    fun getLoginIntent(context: Context): Intent

    fun openRedirectUrl(activity: Activity, url: String)

    fun getFavoritedShopFragment(userId: String): Fragment
}