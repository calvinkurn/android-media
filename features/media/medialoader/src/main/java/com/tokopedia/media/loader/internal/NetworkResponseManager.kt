package com.tokopedia.media.loader.internal

import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.media.loader.data.Header
import com.tokopedia.media.loader.data.failureTypeKey
import com.tokopedia.media.loader.data.toModel
import okhttp3.Headers

class NetworkResponseManager(context: Context) {

    private val editor = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    private val caches = mutableMapOf<String, String>()

    /**
     * A setter the cache of a response header.
     *
     * This setter maintain the caching mechanism to share the response header from [CustomOkHttpStreamFetcher]
     * into [MediaListenerBuilder]. The setter has two pipelines to maintain the performance and
     * efficiency, such shared-preferences for persistent data and Map literals for temporary data.
     *
     * If the url got hit at first, then we will gather the header response from Map. But if
     * the second request occurred, then we will read the data from shared preferences.
     */
    fun set(url: String, header: Headers) {
        val headerMap = header.toMultimap()
        if (header.size <= 0) return

        if (caches[url]?.isNotEmpty() == true || header(url).isNotEmpty()) return
        if (headerMap.containsKey(failureTypeKey()).not()) return
        if (hasReachedThreshold()) forceResetCache()

        try {
            val value = headerMap.toModel().toJson()

            caches[url] = value
            editor.edit().putString(url, value).apply()
        } catch (t: Throwable) {
            FirebaseCrashlytics.getInstance().recordException(t)
        }
    }

    /**
     * A getter of cache header.
     *
     * Get a cache of network response header. The data priority comes from Map literals. If the
     * data from Map doesn't exist, then fetch from shared preferences.
     */
    fun header(url: String): List<Header> {
        val header = caches[url] ?: editor.getString(url, null) ?: return emptyList()

        return try {
            header.toModel()
        } catch (t: Throwable) {
            FirebaseCrashlytics.getInstance().recordException(t)
            emptyList()
        }
    }

    // clear cache if needed, it will be triggered by [properties.isForceClearHeaderCache]
    fun forceResetCache() {
        caches.clear()

        if (size().isMoreThanZero()) {
            editor.edit().clear().apply()
        }
    }

    // to mitigate the over heavy computation and storage, we have to limit the amount of caches.
    private fun hasReachedThreshold(): Boolean {
        return size() == CACHE_THRESHOLD
    }

    private fun size() = editor.all.size

    private fun List<Header>.toJson(): String {
        return Gson().toJson(this)
    }

    private fun String.toModel(): List<Header> {
        return Gson().fromJson(
            this,
            object : TypeToken<List<Header>>() {}.type
        )
    }

    companion object {
        private const val NAME = "media_loader_network_header.pref"
        private const val CACHE_THRESHOLD = 100 // 100 images cache limit

        @Volatile private var manager: NetworkResponseManager? = null

        fun instance(context: Context): NetworkResponseManager {
            synchronized(this) {
                return manager ?: NetworkResponseManager(context.applicationContext)
                    .also { manager = it }
            }
        }
    }
}
