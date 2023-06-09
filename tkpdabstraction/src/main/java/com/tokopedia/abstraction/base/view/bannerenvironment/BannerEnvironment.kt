package com.tokopedia.abstraction.base.view.bannerenvironment

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import com.tokopedia.abstraction.R
import com.tokopedia.config.GlobalConfig
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

    fun setupBannerEnvironment(activity: Activity) {
        val enableBannerEnv = FirebaseRemoteConfigImpl(activity).getBoolean(RemoteConfigKey.ENABLE_BANNER_ENVIRONMENT, true)
        if (enableBannerEnv && isBannerEnvironmentEnabled(activity) && GlobalConfig.isAllowDebuggingTools()) {
            initView((activity), getLiveStatus(activity))
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

    private fun initView(activity: Activity, liveStatus: String) {
        if (liveStatus.isNotEmpty()) {
            val decorView = activity.window.decorView as ViewGroup

            val bannerEnvironmentView = BannerEnvironmentView(activity).apply {
                updateText(liveStatus, R.color.Unify_NN0)
                updateBannerColor(R.color.Unify_G500)
                bannerGravity = BannerEnvironmentGravity.START
            }

            val bannerSize = activity.resources.getDimension(R.dimen.banner_default_size_debug).toInt()
            val params = ViewGroup.MarginLayoutParams(bannerSize, bannerSize)
            decorView.addView(bannerEnvironmentView, params)
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
