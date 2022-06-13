package com.tokopedia.notifcenter.common.network

import android.content.SharedPreferences
import com.tokopedia.common.network.util.CommonUtil
import java.lang.reflect.Type
import javax.inject.Inject

class NotifcenterCacheManagerImpl @Inject constructor(
        private val sharedPreferences: SharedPreferences
) : NotifcenterCacheManager {

    override fun saveCache(key: String, obj: Any) {
        val cacheString = CommonUtil.toJson(obj)
        sharedPreferences.edit()
                .putString(key, cacheString)
                .apply()
    }

    override fun <T> loadCache(key: String, type: Type): T? {
        val cacheString = sharedPreferences.getString(key, "")
        return CommonUtil.fromJson(cacheString, type)
    }

}