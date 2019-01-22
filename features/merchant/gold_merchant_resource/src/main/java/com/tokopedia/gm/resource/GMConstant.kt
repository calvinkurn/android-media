package com.tokopedia.gm.resource

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import java.util.*

object GMConstant {
    const val GM_REMOTE_CONFIG_KEY = "app_enable_power_merchant_rebrand"
    val DEFAULT_IS_POWER_ACTIVE
        get() = Calendar.getInstance().get(Calendar.YEAR) == 2019
    const val BASE_SELLER_URL = "https://seller.tokopedia.com/"
    const val GOLD_MERCHANT_URL = BASE_SELLER_URL + "gold-merchant/"
    const val POWER_MERCHANT_URL = BASE_SELLER_URL + "edu/power-merchant"

    @JvmStatic
    fun getGMDrawable(context: Context?): Drawable? {
        if (context == null)
            return null
        return ContextCompat.getDrawable(context, getGMDrawableResource(context))
    }

    @JvmStatic
    fun getGMTitleResource(context: Context?): Int {
        return context?.run {
            if (isPowerMerchantEnabled(context)) {
                R.string.pm_title
            } else {
                R.string.gm_title
            }
        } ?: R.string.gm_title
    }

    @JvmStatic
    fun getGMBadgeTitleResource(context: Context?): Int {
        return context?.run {
            if (isPowerMerchantEnabled(context)) {
                R.string.pm_badge_title
            } else {
                R.string.gm_badge_title
            }
        } ?: R.string.gm_badge_title
    }

    @JvmStatic
    fun getGMDrawableResource(context: Context): Int {
        return if (isPowerMerchantEnabled(context)){
                    R.drawable.ic_power_merchant
                } else {
                    R.drawable.ic_gold_merchant
                }
    }

    @JvmStatic
    fun getGMEduUrl(context: Context): String = if (isPowerMerchantEnabled(context))
        POWER_MERCHANT_URL else GOLD_MERCHANT_URL

    @JvmStatic
    fun isPowerMerchantEnabled(context: Context) = FirebaseRemoteConfigImpl(context)
            .getBoolean(GMConstant.GM_REMOTE_CONFIG_KEY, GMConstant.DEFAULT_IS_POWER_ACTIVE)

    @JvmStatic
    fun getGMPointerDrawable(context: Context?): Drawable? {
        if (context == null)
            return null
        return ContextCompat.getDrawable(context, getGMPointerDrawableResource(context))
    }

    @JvmStatic
    private fun getGMPointerDrawableResource(context: Context): Int {
        return if (isPowerMerchantEnabled(context)){
            R.drawable.ic_pointer_power_merchant
        } else {
            R.drawable.ic_pointer_gold_badge
        }
    }

    @JvmStatic
    fun getGMSubscribeBadgeDrawable(context: Context?): Drawable? {
        if (context == null)
            return null
        return ContextCompat.getDrawable(context, getGMSubscribeBadgeDrawableResource(context))
    }

    @JvmStatic
    private fun getGMSubscribeBadgeDrawableResource(context: Context): Int {
        return if (isPowerMerchantEnabled(context)){
            R.drawable.ic_pmsubscribe_feature_badge
        } else {
            R.drawable.ic_gmsubscribe_feature_badge
        }
    }

    @JvmStatic
    fun getGMRegularBadgeDrawable(context: Context?): Drawable? {
        if (context == null)
            return null
        return ContextCompat.getDrawable(context, getGMRegularBadgeDrawableResource(context))
    }

    @JvmStatic
    private fun getGMRegularBadgeDrawableResource(context: Context): Int {
        return if (isPowerMerchantEnabled(context)){
            R.drawable.ic_pm_badge_shop_regular
        } else {
            R.drawable.ic_badge_shop_regular
        }
    }

    @JvmStatic
    fun getGMDrawerDrawable(context: Context?): Drawable? {
        if (context == null)
            return null
        return ContextCompat.getDrawable(context, getGMDrawerDrawableResource(context))
    }

    @JvmStatic
    fun getGMDrawerDrawableResource(context: Context): Int {
        return if (isPowerMerchantEnabled(context)){
            R.drawable.ic_pm_badge_shop_regular
        } else {
            R.drawable.ic_goldmerchant_drawer
        }
    }
}
