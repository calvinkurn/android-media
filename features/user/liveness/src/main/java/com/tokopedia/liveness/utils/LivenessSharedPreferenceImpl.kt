package com.tokopedia.liveness.utils

import android.content.SharedPreferences
import javax.inject.Inject

class LivenessSharedPreferenceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
): LivenessSharedPreference {

    override fun getStringCache(key: String): String {
        return sharedPreferences.getString(key, "").orEmpty()
    }
}

interface LivenessSharedPreference {
    fun getStringCache(key: String): String
}
