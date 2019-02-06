package com.tokopedia.gm.resource

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import java.util.*

object GMConstant {
    const val BASE_SELLER_URL = "https://seller.tokopedia.com/"
    const val POWER_MERCHANT_URL = BASE_SELLER_URL + "edu/power-merchant"

    @JvmStatic
    fun getGMDrawable(context: Context?): Drawable? {
        if (context == null)
            return null
        return ContextCompat.getDrawable(context, getGMDrawableResource(context))
    }

    @JvmStatic
    fun getGMTitleResource(context: Context?): Int = R.string.pm_title

    @JvmStatic
    fun getGMBadgeTitleResource(context: Context?): Int = R.string.pm_badge_title

    @JvmStatic
    fun getGMDrawableResource(context: Context): Int = R.drawable.ic_power_merchant

    @JvmStatic
    fun getGMEduUrl(context: Context): String = POWER_MERCHANT_URL

    @JvmStatic
    fun isPowerMerchantEnabled(context: Context) = true

    @JvmStatic
    fun getGMPointerDrawable(context: Context?): Drawable? {
        if (context == null)
            return null
        return ContextCompat.getDrawable(context, getGMPointerDrawableResource(context))
    }

    @JvmStatic
    private fun getGMPointerDrawableResource(context: Context): Int = R.drawable.ic_pointer_power_merchant

    @JvmStatic
    fun getGMSubscribeBadgeDrawable(context: Context?): Drawable? {
        if (context == null)
            return null
        return ContextCompat.getDrawable(context, getGMSubscribeBadgeDrawableResource(context))
    }

    @JvmStatic
    private fun getGMSubscribeBadgeDrawableResource(context: Context): Int = R.drawable.ic_pmsubscribe_feature_badge

    @JvmStatic
    fun getGMRegularBadgeDrawable(context: Context?): Drawable? {
        if (context == null)
            return null
        return ContextCompat.getDrawable(context, getGMRegularBadgeDrawableResource(context))
    }

    @JvmStatic
    private fun getGMRegularBadgeDrawableResource(context: Context): Int = R.drawable.ic_pm_badge_shop_regular

    @JvmStatic
    fun getGMDrawerDrawable(context: Context?): Drawable? {
        if (context == null)
            return null
        return ContextCompat.getDrawable(context, getGMDrawerDrawableResource(context))
    }

    @JvmStatic
    fun getGMDrawerDrawableResource(context: Context): Int = R.drawable.ic_pm_badge_shop_regular
}
