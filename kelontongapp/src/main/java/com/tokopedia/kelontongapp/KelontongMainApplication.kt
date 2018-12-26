package com.tokopedia.kelontongapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib

import com.crashlytics.android.Crashlytics

import io.fabric.sdk.android.Fabric
import com.moengage.core.MoEngage



/**
 * Created by meta on 02/10/18.
 */
class KelontongMainApplication : Application() {


    private lateinit var sharedPref: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPref = getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
        initAppsflyer()
        initMoengage()
        initCrashlytics()
        createNotificationChannel()
    }

    private fun initCrashlytics() {
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, Crashlytics())
            Crashlytics.setUserIdentifier(getString(R.string.app_name))
        }
    }

    private fun initMoengage() {
        val moEngage = MoEngage.Builder(this, MOENGAGE_ID)
                .build()
        MoEngage.initialise(moEngage)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            mChannel.description = NOTIFICATION_CHANNEL_DESC
            val notificationManager = getSystemService(
                    Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    private fun initAppsflyer() {
        AppsFlyerLib.getInstance().init(AF_KEY, appsflyerConversionListener(), this)
        AppsFlyerLib.getInstance().setCustomerUserId(sharedPref.getString(USER_ID, ""))
        val addData = HashMap<String, Any?>()
        addData[KEY_INSTALL_SOURCE] = getInstallSource()
        AppsFlyerLib.getInstance().setAdditionalData(addData)
        AppsFlyerLib.getInstance().setDebugLog(true);
        AppsFlyerLib.getInstance().startTracking(this)
    }

    private fun appsflyerConversionListener() = object: AppsFlyerConversionListener {
        override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
            // no-op
        }

        override fun onAttributionFailure(p0: String?) {
            // no-op
        }

        override fun onInstallConversionDataLoaded(p0: MutableMap<String, String>?) {
            // no-op
        }

        override fun onInstallConversionFailure(p0: String?) {
            // no-op
        }
    }

    private fun getInstallSource(): String? {
        return packageManager.getInstallerPackageName(packageName)
    }
}
