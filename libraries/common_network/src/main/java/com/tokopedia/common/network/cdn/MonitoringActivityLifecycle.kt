package com.tokopedia.common.network.cdn

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.cdn.CdnTracker.pageName
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestCacheStrategy
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Objects

class MonitoringActivityLifecycle(val context: Context) : ActivityLifecycleCallbacks {

    private val remoteConfig: RemoteConfig by lazy {
        FirebaseRemoteConfigImpl(context)
    }

    private var cdnConfig: DataConfig? = null

    companion object {
        const val CDN_MONITORING_KEY = "android_cdn_monitoring_static"
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        val config = remoteConfig.getString(CDN_MONITORING_KEY)
        config?.let {
            try {
                Gson().fromJson(config, DataConfig::class.java)?.let { cdnConfig = it }
            } catch (ignore: Exception) {
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun process(context: Context, config: DataConfig) {
        val restRepository: RestRepository by lazy {
            RestRequestInteractor.getInstance().restRepository.apply {
                updateInterceptors(mutableListOf(CdnInterceptor(context)), context)
            }
        }
        config.imageUrlList?.let {
            it.forEach { url ->
                GlobalScope.launch(Dispatchers.IO) {
                    if (config.sendSuccess) {
                        val token = object : TypeToken<DataResponse<String>>() {}.type
                        val restRequest = RestRequest.Builder(url, token)
                            .setHeaders(mapOf("host" to "images.tokopedia.net"))
                            .build()
                        restRepository.getResponse(restRequest)
                    }
                }
            }
        }
    }

    override fun onActivityStarted(activity: Activity) {
        val className = activity.javaClass.canonicalName ?: return
        cdnConfig?.let { config ->
            if (config.pageNameList?.contains(className) == true) {
                process(activity, config)
            }
        }
    }

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}
}
