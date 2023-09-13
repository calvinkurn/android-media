package com.tokopedia.common.network.cdn

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Build
import android.os.Bundle
import com.google.android.gms.security.ProviderInstaller
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.net.ssl.SSLContext

class MonitoringActivityLifecycle(val context: Context) : ActivityLifecycleCallbacks {

    private val remoteConfig: RemoteConfig by lazy {
        FirebaseRemoteConfigImpl(context)
    }

    private var cdnConfig: DataConfig? = null

    companion object {
        const val CDN_MONITORING_KEY = "android_cdn_monitoring_static"
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        try {
            // Google Play will install latest OpenSSL
            ProviderInstaller.installIfNeeded(context)
            val sslContext: SSLContext
            sslContext = SSLContext.getInstance("TLSv1.2")
            sslContext.init(null, null, null)
            sslContext.createSSLEngine()
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
        val config = remoteConfig.getString(CDN_MONITORING_KEY)
        config?.let {
            try {
                Gson().fromJson(config, DataConfig::class.java)?.let { cdnConfig = it }
            } catch (ignore: Exception) {
            }
        }
    }

    private fun process(context: Context, config: DataConfig) {
        val restRepository: RestRepository by lazy {
            RestRequestInteractor.getInstance().restRepository.apply {
                updateInterceptors(mutableListOf(CdnInterceptor(context)), context)
            }
        }
        config.imageUrlList?.let {
            it.forEach { url ->
                CoroutineScope(Dispatchers.IO).launch {
                    if (config.sendSuccess) {
                        val token = object : TypeToken<DataResponse<String>>() {}.type
                        val restRequest = RestRequest.Builder(url, token)
                            .setHeaders(mapOf("host" to "images.tokopedia.net", "user-agent" to getUserAgent()))
                            .setRequestType(RequestType.GET)
                            .build()
                        restRepository.getResponse(restRequest)
                    }
                }
            }
        }
    }

    fun getUserAgent(): String {
        return "TkpdConsumer/${GlobalConfig.VERSION_NAME} (Android ${Build.VERSION.RELEASE};)"
    }

    override fun onActivityStarted(activity: Activity) {
        val className = activity.javaClass.canonicalName ?: return
        cdnConfig?.let { config ->
            if (config.pageNameList?.contains(className) == true) {
                process(activity, config)
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
