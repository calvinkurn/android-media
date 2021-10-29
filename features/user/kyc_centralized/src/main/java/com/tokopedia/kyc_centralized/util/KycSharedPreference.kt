package com.tokopedia.kyc_centralized.util

import android.content.SharedPreferences
import com.tokopedia.common.network.util.CommonUtil
import java.lang.reflect.Type
import javax.inject.Inject

class KycSharedPreference @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    fun saveCache(key: String, obj: Any) {
        val cacheString = CommonUtil.toJson(obj)
        sharedPreferences.edit()
            .putString(key, cacheString)
            .apply()
    }

    fun <T> getCache(key: String, type: Type): T {
        val cacheString = sharedPreferences.getString(key, "")
        return CommonUtil.fromJson(cacheString, type)
    }

    fun removeCache(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
}