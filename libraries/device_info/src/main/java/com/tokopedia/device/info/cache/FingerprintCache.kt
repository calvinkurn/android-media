package com.tokopedia.device.info.cache

import android.content.Context

object FingerprintCache{
    private const val FINGERPRINT_KEY_NAME = "FINGERPRINT_KEY_NAME"
    private const val FINGERPRINT_USE_CASE = "FINGERPRINT_USE_CASE"

    fun clearFingerprintCache(context: Context){
        val sp = context.getSharedPreferences(FINGERPRINT_KEY_NAME, Context.MODE_PRIVATE)
        sp.edit().putString(FINGERPRINT_USE_CASE, null).apply()
    }
}