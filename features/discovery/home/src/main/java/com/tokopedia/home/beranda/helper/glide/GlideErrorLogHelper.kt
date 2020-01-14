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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

object GlideErrorLogHelper {

    fun logError(context: Context, e: GlideException?, url: String) {
        GlobalScope.launch {
            logErrorSync(context, e, url)
        }
    }

    private suspend fun logErrorSync(context: Context, e: GlideException?, url: String) {
        val remoteConfig = FirebaseRemoteConfigImpl(context)
        val traceRouteMinVersion = remoteConfig.getLong(RemoteConfigKey.ENABLE_TRACEROUTE_MIN_VERSION, 999999999)
        if (GlobalConfig.VERSION_CODE < traceRouteMinVersion) {
            return
        }

        if (!isNetworkAvailable(context)) {
            Timber.w("P2#IMAGE_TRACEROUTE#network not available")
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

    private suspend fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager : ConnectivityManager? = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        return connectivityManager?.activeNetworkInfo?.isConnected ?: true
    }
}
