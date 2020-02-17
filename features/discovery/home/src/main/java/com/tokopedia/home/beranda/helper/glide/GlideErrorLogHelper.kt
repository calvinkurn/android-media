package com.tokopedia.home.beranda.helper.glide

import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import android.text.TextUtils
import com.bumptech.glide.load.engine.GlideException
import com.tokopedia.config.GlobalConfig
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.wandroid.traceroute.TraceRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class GlideErrorLogHelper(): CoroutineScope {

    protected val masterJob = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + masterJob

    fun logError(context: Context, e: GlideException?, url: String) {
        launch {
            logErrorSync(context, e, url)
        }
    }

    private fun logErrorSync(context: Context, e: GlideException?, url: String) {
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
            Timber.w("P2#IMAGE_TRACEROUTE#%s: url= %s message= %s traceroute= %s",
                    traceResult?.code?.toString() ?: "",
                    url,
                    e?.message ?: "",
                    traceResult?.message ?: "")
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager: ConnectivityManager? = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        return connectivityManager?.activeNetworkInfo?.isConnected ?: true
    }
}
