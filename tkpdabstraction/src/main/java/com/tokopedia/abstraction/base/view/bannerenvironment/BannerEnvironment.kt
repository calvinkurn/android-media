package com.tokopedia.abstraction.base.view.bannerenvironment

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import com.tokopedia.abstraction.R
import com.tokopedia.graphql.interceptor.BannerDebugInterceptor.Companion.isBeta
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.url.TokopediaUrl.Companion.getInstance

/**
 * Created by irpan on 17/04/23.
 */
class BannerEnvironment {

    companion object {
        private const val DEV_OPT_ON_BANNER_ENVIRONMENT_ENABLED = "DEV_OPT_ON_BANNER_ENVIRONMENT_ENABLED"
        private const val IS_DEV_OPT_ON_BANNER_ENVIRONMENT_ENABLED = "IS_DEV_OPT_ON_BANNER_ENVIRONMENT_ENABLED"
    }

    fun initializeBannerEnvironment(activity: Activity) {
        val enableBannerEnv = FirebaseRemoteConfigImpl(activity).getBoolean(RemoteConfigKey.ENABLE_BANNER_ENVIRONMENT, true)
        if (enableBannerEnv && isBannerEnvironmentEnabled(activity)) {
            addBanner((activity), getLiveStatus(activity))
        } else {
            removeBanner()
        }
    }

    fun isBannerEnvironmentEnabled(context: Context): Boolean {
        val cache = context.getSharedPreferences(DEV_OPT_ON_BANNER_ENVIRONMENT_ENABLED, Context.MODE_PRIVATE)
        return cache.getBoolean(IS_DEV_OPT_ON_BANNER_ENVIRONMENT_ENABLED, false)
    }

    fun setBannerEnvironmentEnabled(context: Context, isDevOptOnNotifEnabled: Boolean) {
        context.getSharedPreferences(DEV_OPT_ON_BANNER_ENVIRONMENT_ENABLED, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(IS_DEV_OPT_ON_BANNER_ENVIRONMENT_ENABLED, isDevOptOnNotifEnabled)
            .apply()
    }

    private var bannerEnvironmentView: BannerEnvironmentView? = null
    private var decorView: ViewGroup? = null

    private fun addBanner(activity: Activity, liveStatus: String) {
        decorView = activity.window.decorView as ViewGroup
        bannerEnvironmentView = BannerEnvironmentView(activity).apply {
            updateText(liveStatus, R.color.Unify_NN0)
            updateBannerColor(R.color.Unify_G500)
            bannerGravity = BannerEnvironmentGravity.END
        }
        if (liveStatus.isNotEmpty()) {
            val bannerSize = activity.resources.getDimension(R.dimen.banner_default_size_debug).toInt()
            val params = ViewGroup.MarginLayoutParams(bannerSize, bannerSize)
            decorView?.addView(bannerEnvironmentView, params)
        }
    }

    private fun removeBanner() {
        if (bannerEnvironmentView != null && decorView != null) {
            decorView?.removeView(bannerEnvironmentView)
        }
    }

    private fun getLiveStatus(context: Context): String {
        return if (getInstance().GQL.contains("staging")) {
            "STAGING"
        } else if (isBeta(context)) {
            "BETA"
        } else {
            "LIVE"
        }
    }
}
