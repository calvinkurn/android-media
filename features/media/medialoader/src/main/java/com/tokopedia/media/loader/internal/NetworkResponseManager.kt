package com.tokopedia.media.loader.internal

import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.kotlin.extensions.clear
import com.tokopedia.media.loader.data.Header
import com.tokopedia.media.loader.data.toModel
import okhttp3.Headers

//class NetworkResponseManager private constructor() {
//    private val headers = mutableMapOf<String, String>()
//
//    fun set(url: String, header: Headers) {
//        if (header.size <= 0) return
//        if (hasReachedThreshold()) forceResetCache()
//
//        headers[url] = header
//            .toMultimap()
//            .toModel()
//            .toJson()
//    }
//
//    fun header(url: String): List<Header> {
//        val header = headers[url] ?: return emptyList()
//
//        return try {
//            header.toModel()
//        } catch (t: Throwable) {
//            FirebaseCrashlytics.getInstance().recordException(t)
//            emptyList()
//        }
//    }
//
//    fun forceResetCache() {
//        headers.clear()
//    }
//
//    private fun hasReachedThreshold(): Boolean {
//        return headers.size == CACHE_THRESHOLD
//    }
//
//    private fun List<Header>.toJson(): String {
//        return Gson().toJson(this)
//    }
//
//    private fun String.toModel(): List<Header> {
//        return Gson().fromJson(
//            this,
//            object : TypeToken<List<Header>>() {}.type
//        )
//    }
//
//    companion object {
//        private const val CACHE_THRESHOLD = 100 // 100 images cache limit
//
//        @Volatile
//        private var manager: NetworkResponseManager? = null
//
//        fun instance(context: Context): NetworkResponseManager {
//            return manager ?: synchronized(this) {
//                NetworkResponseManager().also {
//                    manager = it
//                }
//            }
//        }
//    }
//}
class NetworkResponseManager(context: Context) {

    private val editor = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    fun set(url: String, header: Headers) {
        if (header.size <= 0) return
        if (hasReachedThreshold()) forceResetCache()

        val value = header
            .toMultimap()
            .toModel()
            .toJson()

        editor.edit().putString(url, value).apply()
    }

    fun header(url: String): List<Header> {
        val header = editor.getString(url, "") ?: return emptyList()
        if (header.isEmpty()) return emptyList()

        return try {
            header.toModel()
        } catch (t: Throwable) {
            FirebaseCrashlytics.getInstance().recordException(t)
            emptyList()
        }
    }

    fun forceResetCache() {
        editor.edit().clear().apply()
    }

    private fun hasReachedThreshold(): Boolean {
        return editor.all.size == CACHE_THRESHOLD
    }

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
                return manager ?: NetworkResponseManager(context).also {
                    manager = it
                }
            }
        }
    }
}
