package com.tokopedia.appdownloadmanager_common.presentation.util

import android.app.Activity
import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.tokopedia.appdownloadmanager_common.nakamaupdate.DownloadManagerUpdateModel
import com.tokopedia.appdownloadmanager_common.presentation.bottomsheet.AppDownloadingBottomSheet
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.interceptor.BannerEnvironmentInterceptor
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.remoteconfig.RollenceKey
import java.lang.ref.WeakReference

abstract class BaseDownloadManagerHelper(
     val activityRef: WeakReference<Activity>
) {

    protected val sharePref by lazy {
        activityRef.get()?.getSharedPreferences(DOWNLOAD_MANAGER_NAKAMA_PREF, Context.MODE_PRIVATE)
    }

    protected val remoteConfig = FirebaseRemoteConfigImpl(activityRef.get())

    protected var downloadManagerUpdateModel: DownloadManagerUpdateModel? = null

    init {
        initDownloadManagerUpdateConfig()
    }

    abstract fun showAppDownloadManagerBottomSheet()

    open fun isEnableShowBottomSheet(): Boolean {
        val canShowToday = isExpired()
//        return canShowToday && isBetaNetwork() && isWhitelistByRollence()
        return true
    }

    open fun isExpired(): Boolean {
        val interval = sharePref?.getInt(DOWNLOAD_MANAGER_EXPIRED_TIME, 0) ?: 0
        val time = sharePref?.getLong(DOWNLOAD_MANAGER_TIMESTAMP, 0) ?: 0L
        val currTime = System.currentTimeMillis() / 1000
        return currTime - time > interval
    }
    open fun isWhitelistByRollence(): Boolean {
        return RemoteConfigInstance.getInstance().abTestPlatform?.getString(
            RollenceKey.ANDROID_INTERNAL_TEST,
            ""
        ) == RollenceKey.ANDROID_INTERNAL_TEST
    }

    open fun setCacheExpire() {
        val expireTime = downloadManagerUpdateModel?.expireTime.orZero()

        val sharePrefEditor = sharePref?.edit()
        sharePrefEditor?.putInt(
            AppDownloadingBottomSheet.DOWNLOAD_MANAGER_EXPIRED_TIME,
            expireTime
        )
        val currTime = System.currentTimeMillis() / 1000
        sharePrefEditor?.putLong(
            AppDownloadingBottomSheet.DOWNLOAD_MANAGER_TIMESTAMP,
            currTime
        )

        sharePrefEditor?.apply()
    }

    open fun isBetaNetwork(): Boolean {
        return activityRef.get()?.let { BannerEnvironmentInterceptor.isBeta(it) } == true
    }

    private fun initDownloadManagerUpdateConfig() {
        val internalTestConfigJson =
            remoteConfig.getString(RemoteConfigKey.ANDROID_INTERNAL_TEST_UPDATE_CONFIG)

        if (internalTestConfigJson.isNotBlank()) {
            try {
                val internalTestConfigModel =
                    Gson().fromJson(internalTestConfigJson, DownloadManagerUpdateModel::class.java)
                this.downloadManagerUpdateModel = internalTestConfigModel
            } catch (e: Exception) {
                if (GlobalConfig.isAllowDebuggingTools()) {
                    e.printStackTrace()
                } else {
                    FirebaseCrashlytics.getInstance().recordException(e)
                }
            }
        }
    }

    companion object {
        const val DOWNLOAD_MANAGER_NAKAMA_PREF = "DownloadManagerNakamaVersionPref"
        const val DOWNLOAD_MANAGER_EXPIRED_TIME = "expired_time"
        const val DOWNLOAD_MANAGER_TIMESTAMP = "timestamp"

        const val APK_MIME_TYPE = "application/vnd.android.package-archive"
    }
}
