package com.tokopedia.user.session.datastore

import android.content.Context
import com.tokopedia.user.session.datastore.AbPlatformInteractor.Companion.USER_SESSION_AB_TEST_KEY
import javax.inject.Inject

object UserSessionAbTestPlatform {
    fun isDataStoreEnable(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(
            UserSessionDataStoreImpl.SHARED_PREFERENCE_AB_TEST_PLATFORM,
            Context.MODE_PRIVATE
        )
        val cacheValue: String = sharedPreferences.getString(USER_SESSION_AB_TEST_KEY, "") ?: ""
        return cacheValue.isNotEmpty()
    }
}

class AbPlatformInteractor @Inject constructor(private val context: Context) {

    fun isDataStoreEnabled(): Boolean {
        val sharedPreferences = context.getSharedPreferences(
            UserSessionDataStoreImpl.SHARED_PREFERENCE_AB_TEST_PLATFORM,
            Context.MODE_PRIVATE
        )
        val cacheValue: String = sharedPreferences.getString(USER_SESSION_AB_TEST_KEY, "") ?: ""
        return cacheValue.isNotEmpty()
    }

    companion object {
        const val USER_SESSION_AB_TEST_KEY = "android_data_store_v2"
    }
}