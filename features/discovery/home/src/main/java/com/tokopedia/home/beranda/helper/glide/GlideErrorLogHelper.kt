package com.tokopedia.home.beranda.helper.glide

import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import android.text.TextUtils
import com.tokopedia.config.GlobalConfig
import com.tokopedia.media.loader.utils.MediaException
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.wandroid.traceroute.TraceRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class GlideErrorLogHelper : CoroutineScope {

    protected val masterJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + masterJob

    fun logError(context: Context, e: MediaException?, url: String) {
        launch {
            logErrorSync(context, e, url)
        }
    }

    private fun logErrorSync(context: Context, e: MediaException?, url: String) {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        val traceRouteMinVersion = remoteConfig.getLong(RemoteConfigKey.ENABLE_TRACEROUTE_MIN_VERSION, 999999999)
        if (GlobalConfig.VERSION_CODE < traceRouteMinVersion) {
            return
        }
        if (!isNetworkAvailable(context)) {
            return
        }
        val host = Uri.parse(url).host
        if (!TextUtils.isEmpty(host)) {
            val traceResult = TraceRoute.traceRoute(host!!)
            ServerLogger.log(Priority.P2, "IMAGE_TRACEROUTE",
                    mapOf(
                            "type" to traceResult?.code?.toString().orEmpty(),
                            "url" to url,
                            "traceroute" to traceResult?.message.orEmpty(),
                            "message" to e?.message.orEmpty()
                    ))
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager: ConnectivityManager? = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        return connectivityManager?.activeNetworkInfo?.isConnected ?: true
    }
}
