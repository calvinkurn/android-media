package com.tokopedia.abstraction.base.view.nakamaupdate

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.remoteconfig.RemoteConfigKey
import java.lang.ref.WeakReference

class DownloadManagerNakamaDialog(
    val weakActivity: WeakReference<Activity>
) : BaseAppDistributionDialog(weakActivity) {

    init {
        initDownloadManagerUpdateConfig()
    }

    private val sharePref by lazy {
        val sharedPreferences = weakActivity.get()
            ?.getSharedPreferences(PRE_APP_DISTRIBUTION_PREF, Context.MODE_PRIVATE)
        sharedPreferences
    }

    private val downloadManagerProgressDialog by lazy {
        DownloadManagerNakamaProgressDialog(
            ProgressDialog(weakActivity.get()),
            weakActivity
        )
    }

    private var downloadManagerUpdateModel: DownloadManagerUpdateModel? = null

    override fun showDialog() {
        if (isAndroidInternalTestEnabled()) {
            downloadManagerUpdateModel?.let {
                weakActivity.get()?.let { activity ->
                    val downloadManagerUpdateDialog =
                        DownloadManagerUpdateDialogBuilder(it).build(
                            activity,
                            onPositiveButtonClicked = {
                                downloadManagerProgressDialog.startDownload(APK_URL)
                                it.dismiss()
                                setCacheExpire()
                            },
                            onNegativeButtonClicked = {
                                it.dismiss()
                                setCacheExpire()
                            },
                            onCancelableClicked = {
                                setCacheExpire()
                            }
                        )

                    downloadManagerUpdateDialog?.show()
                }
            }
        }
    }

    override fun isExpired(): Boolean {
        val interval = sharePref?.getInt(PRE_APP_DISTRIBUTION_EXPIRED_TIME, 0) ?: 0
        val time = sharePref?.getLong(PRE_APP_DISTRIBUTION_TIMESTAMP, 0) ?: 0L
        val currTime = System.currentTimeMillis() / 1000
        return currTime - time > interval
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

    private fun isAndroidInternalTestEnabled(): Boolean {
        val isEnabled = downloadManagerUpdateModel?.isEnabled.orFalse()
//        val canShowToday = isExpired()
//        val isEligible = isWhitelistByRollence() && isBetaNetwork() &&
//            canShowToday &&
//            isEnabled
        return isEnabled
    }

    private fun setCacheExpire() {
        val expireTime = downloadManagerUpdateModel?.expireTime.orZero()

        val sharePrefEditor = sharePref?.edit()
        sharePrefEditor?.putInt(PRE_APP_DISTRIBUTION_EXPIRED_TIME, expireTime)
        val currTime = System.currentTimeMillis() / 1000
        sharePrefEditor?.putLong(PRE_APP_DISTRIBUTION_TIMESTAMP, currTime)

        sharePrefEditor?.apply()
    }

    companion object {
        const val PRE_APP_DISTRIBUTION_PREF = "DownloadManagerNakamaVersionPref"
        const val PRE_APP_DISTRIBUTION_EXPIRED_TIME = "expired_time"
        const val PRE_APP_DISTRIBUTION_TIMESTAMP = "timestamp"

        const val APK_URL = "https://jenkins-android.tokopedia.net/job/bundle-internal-nakama/52/artifact/android_repo/apks/universal.apk"
    }
}
