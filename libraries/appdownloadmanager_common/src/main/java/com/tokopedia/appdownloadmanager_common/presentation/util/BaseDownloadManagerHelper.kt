package com.tokopedia.appdownloadmanager_common.presentation.util

import android.app.Activity
import android.content.Context
import android.os.Environment
import androidx.fragment.app.FragmentActivity
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.appdownloadmanager_common.domain.model.AppVersionBetaInfoModel
import com.tokopedia.appdownloadmanager_common.domain.service.GetDownloadVersionList
import com.tokopedia.appdownloadmanager_common.presentation.bottomsheet.AppDownloadingBottomSheet
import com.tokopedia.appdownloadmanager_common.presentation.model.DownloadManagerUpdateModel
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.interceptor.BannerEnvironmentInterceptor
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.ref.WeakReference

abstract class BaseDownloadManagerHelper(
    val activityRef: WeakReference<Activity>
) {

    protected val sharePref by lazy {
        activityRef.get()?.getSharedPreferences(DOWNLOAD_MANAGER_NAKAMA_PREF, Context.MODE_PRIVATE)
    }

    protected val remoteConfig = FirebaseRemoteConfigImpl(activityRef.get())

    protected var downloadManagerUpdateModel: DownloadManagerUpdateModel? = null

    protected var appVersionBetaInfoModel: AppVersionBetaInfoModel? = null

    init {
        initDownloadManagerUpdateConfig()
    }

    abstract fun showAppDownloadManagerBottomSheet(shouldSkipRollenceCheck: Boolean)

    open suspend fun isEnableShowBottomSheet(isTriggeredViaApplink: Boolean): Boolean {
        val canShowToday = isTriggeredViaApplink || isExpired()
        val isBetaNetwork = isBetaNetwork()

        val shouldUpgradeVersion = if (canShowToday && isBetaNetwork) {
            coroutineScope {
                async(Dispatchers.IO) {
                    isNeedToUpgradeVersion()
                }
            }.await()
        } else {
            false
        }

        return isAppDownloadingBottomSheetNotShow()
            && isBetaNetwork
            && canShowToday
            && shouldUpgradeVersion
            && downloadManagerUpdateModel?.isEnabled == true
    }

    protected fun isExpired(): Boolean {
        val interval = sharePref?.getInt(DOWNLOAD_MANAGER_EXPIRED_TIME, 0) ?: 0
        val time = sharePref?.getLong(DOWNLOAD_MANAGER_TIMESTAMP, 0) ?: 0L
        val currTime = System.currentTimeMillis() / 1000
        return currTime - time > interval
    }

    protected fun setCacheExpire() {
        val expireTime = downloadManagerUpdateModel?.expireTime.orZero()

        val sharePrefEditor = sharePref?.edit()
        sharePrefEditor?.putInt(
            DOWNLOAD_MANAGER_EXPIRED_TIME,
            expireTime
        )
        val currTime = System.currentTimeMillis() / 1000
        sharePrefEditor?.putLong(
            DOWNLOAD_MANAGER_TIMESTAMP,
            currTime
        )

        sharePrefEditor?.apply()
    }

    protected fun isBetaNetwork(): Boolean {
        return activityRef.get()?.let { BannerEnvironmentInterceptor.isBeta(it) } == true
    }

    private suspend fun isNeedToUpgradeVersion(): Boolean {
        val typeToken = object : TypeToken<List<AppVersionBetaInfoModel>>() {}.type
        val appVersionBetaInfoModel =
            GetDownloadVersionList.getApiResponse<List<AppVersionBetaInfoModel>>(
                TKPD_VERSION_LIST_URL,
                typeToken
            )?.firstOrNull()

        this@BaseDownloadManagerHelper.appVersionBetaInfoModel = AppVersionBetaInfoModel(
            appVersionBetaInfoModel?.versionName.orEmpty(),
            appVersionBetaInfoModel?.versionCode.orEmpty()
        )

        return GlobalConfig.VERSION_CODE < appVersionBetaInfoModel?.versionCode.toIntSafely()
    }

    private fun isAppDownloadingBottomSheetNotShow(): Boolean {
        val appDownloadingTag = AppDownloadingBottomSheet::class.java.simpleName
        (activityRef.get() as? FragmentActivity)?.let {
            if (it.supportFragmentManager.findFragmentByTag(appDownloadingTag) == null) {
                return true
            }
        }
        return false
    }

    private fun initDownloadManagerUpdateConfig() {
        val configKey =
            if (GlobalConfig.IS_NAKAMA_VERSION) RemoteConfigKey.ANDROID_INTERNAL_NAKAMA_VERSION_DIALOG_CONFIG else RemoteConfigKey.ANDROID_INTERNAL_PUBLIC_VERSION_DIALOG_CONFIG

        val internalTestConfigJson =
            remoteConfig.getString(configKey)

        if (internalTestConfigJson.isNotBlank()) {
            try {
                val internalTestConfigModel =
                    Gson().fromJson(internalTestConfigJson, DownloadManagerUpdateModel::class.java)
                this.downloadManagerUpdateModel = internalTestConfigModel
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
            }
        }
    }

    companion object {
        const val DOWNLOAD_MANAGER_NAKAMA_PREF = "DownloadManagerNakamaVersionPref"
        const val DOWNLOAD_MANAGER_EXPIRED_TIME = "expired_time"
        const val DOWNLOAD_MANAGER_TIMESTAMP = "timestamp"

        const val DOWNLOAD_MANAGER_APPLINK_PARAM = "show_internal_update_dialog"
        const val DOWNLOAD_MANAGER_PARAM_TRUE = "true"

        const val APK_MIME_TYPE = "application/vnd.android.package-archive"

        const val TKPD_VERSION_LIST_URL =
            "https://docs-android.tokopedia.net/versionList?packagename=com.tokopedia.tkpd"

        const val APK_URL =
            "https://docs-android.tokopedia.net/downloadApk?packagename=com.tokopedia.tkpd&versionname=%s&versioncode=%s"

        const val TOKOPEDIA_APK_PATH = "Tokopedia-Apk"

        val TKPD_DOWNLOAD_APK_DIR =
            "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/$TOKOPEDIA_APK_PATH"
    }
}
