package com.tokopedia.media.common.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

open class MediaPreferences constructor(
    private val context: Context?
) {

    private fun pref(): SharedPreferences? {
        return context?.getSharedPreferences(MEDIA_QUALITY_PREF, Context.MODE_PRIVATE)
    }

    protected fun insert(key: String, `value`: Any) {
        val editor = pref()?.edit()

        when (`value`) {
            is Int -> editor?.putInt(key, value)?.apply()
            is String -> editor?.putString(key, value)?.apply()
            is Long -> editor?.putLong(key, value)?.apply()
            is Float -> editor?.putFloat(key, value)?.apply()
            else -> {
                val valueToJson = Gson().toJson(`value`)
                editor?.putString(key, valueToJson)?.apply()
            }
        }
    }

    protected fun getInt(key: String): Int {
        return pref()?.getInt(key, 0)?: 0
    }

    protected fun getBoolean(key: String): Boolean {
        return pref()?.getBoolean(key, false)?: false
    }

    protected fun getString(key: String): String {
        return pref()?.getString(key, "")?: ""
    }

    protected fun isExist(key: String): Boolean {
        return pref()?.contains(key)?: false
    }

    companion object {
        private const val MEDIA_QUALITY_PREF = "media_image_quality"
    }

}