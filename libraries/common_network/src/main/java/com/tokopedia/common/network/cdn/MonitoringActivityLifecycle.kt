package com.tokopedia.common.network.cdn

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Build
import android.os.Bundle
import com.google.android.gms.security.ProviderInstaller
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.net.ssl.SSLContext

class MonitoringActivityLifecycle(val context: Context) : ActivityLifecycleCallbacks {

    private var cdnConfig: DataConfig? = null

    companion object {
        const val CDN_MONITORING_KEY = "android_cdn_monitoring_static_v2"
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        // no op
    }

    fun initConfig() {
        if (cdnConfig == null) {
            val config = FirebaseRemoteConfigImpl(context).getString(CDN_MONITORING_KEY)
            config?.let {
                try {
                    Gson().fromJson(config, DataConfig::class.java)?.let { cdnConfig = it }
                } catch (ignore: Exception) { }
            }
        }
    }

    private fun process(context: Context, config: DataConfig) {
        val interceptor = CdnInterceptor(context)
        config.urlList?.let {
            it.forEach { item ->
                if (config.sendSuccess) {
                    val client = OkHttpClient.Builder()
                        .dns(CdnDns(item.cname))
                        .addInterceptor(interceptor)
                        .hostnameVerifier { hostname, session -> true }
                        .build()
                    val request = Request.Builder()
                        .url(item.url)
                        .addHeader("host", item.host)
                        .addHeader("user-agent", getUserAgent())
                        .addHeader("cname", item.cname)
                        .build()
                    client.newCall(request).execute()
                }
            }
        }
    }

    fun getUserAgent(): String {
        return "TkpdConsumer/${GlobalConfig.VERSION_NAME} (Android ${Build.VERSION.RELEASE};)"
    }

    override fun onActivityStarted(activity: Activity) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val className = activity.javaClass.canonicalName ?: return@launch
                initConfig()
                cdnConfig?.let { config ->
                    if (config.pageNameList?.contains(className) == true) {
                        process(activity, config)
                    }
                }
            } catch (ignore: Exception) {
            }
        }
    }

    override fun onActivityResumed(activity: Activity) {
        // no-op
    }

    override fun onActivityPaused(activity: Activity) {
        // no-op
    }

    override fun onActivityStopped(activity: Activity) {
        // no-op
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        // no-op
    }

    override fun onActivityDestroyed(activity: Activity) {
        // no-op
    }
}
