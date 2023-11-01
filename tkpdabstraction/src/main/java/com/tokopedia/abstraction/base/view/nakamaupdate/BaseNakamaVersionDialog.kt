package com.tokopedia.abstraction.base.view.nakamaupdate

import android.app.Activity
import com.tokopedia.graphql.interceptor.BannerEnvironmentInterceptor.Companion.isBeta
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import java.lang.ref.WeakReference

abstract class BaseAppDistributionDialog(
    private val weakActivity: WeakReference<Activity>
) {

    protected val remoteConfig = FirebaseRemoteConfigImpl(weakActivity.get())

    abstract fun showDialog()

    abstract fun isExpired(): Boolean

    protected fun isWhitelistByRollence(): Boolean {
        return RemoteConfigInstance.getInstance().abTestPlatform?.getString(
            RollenceKey.ANDROID_INTERNAL_TEST,
            ""
        ) == RollenceKey.ANDROID_INTERNAL_TEST
    }

    protected fun isBetaNetwork(): Boolean {
        return weakActivity.get()?.let { isBeta(it) } == true
    }
}
