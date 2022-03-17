package com.tokopedia.user.session.datastore

import android.content.Context

object UserSessionAbTestPlatform {
    fun isDataStoreEnable(context: Context): Boolean {
	val sharedPreferences = context.getSharedPreferences(UserSessionDataStoreImpl.SHARED_PREFERENCE_AB_TEST_PLATFORM, Context.MODE_PRIVATE)
	val cacheValue: String = sharedPreferences.getString(UserSessionDataStoreImpl.USER_SESSION_AB_TEST_KEY, "") ?: ""
	return cacheValue.isNotEmpty()
    }
}