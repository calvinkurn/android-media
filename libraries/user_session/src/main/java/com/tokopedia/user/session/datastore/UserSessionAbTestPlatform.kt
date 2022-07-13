package com.tokopedia.user.session.datastore

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.user.session.datastore.DataStorePreference.Companion.SHARED_PREFERENCE_AB_TEST_PLATFORM
import com.tokopedia.user.session.datastore.DataStorePreference.Companion.USER_SESSION_AB_TEST_KEY
import com.tokopedia.user.session.datastore.workmanager.DataStoreMigrationWorker
import okhttp3.internal.cache2.Relay.Companion.edit
import javax.inject.Inject

object UserSessionAbTestPlatform {
    fun isDataStoreEnable(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(
            SHARED_PREFERENCE_AB_TEST_PLATFORM,
            Context.MODE_PRIVATE
        )
        val cacheValue: String = sharedPreferences.getString(USER_SESSION_AB_TEST_KEY, "") ?: ""
        return cacheValue.isNotEmpty()
    }
}

open class DataStorePreference @Inject constructor(private val context: Context) {

    private val dsPref: SharedPreferences by lazy {
        context.getSharedPreferences(
            DATA_STORE_PREF, Context.MODE_PRIVATE
        )
    }

    fun isDataStoreEnabled(): Boolean {
        val sharedPreferences = context.getSharedPreferences(
            SHARED_PREFERENCE_AB_TEST_PLATFORM,
            Context.MODE_PRIVATE
        )
        val cacheValue: String = sharedPreferences.getString(USER_SESSION_AB_TEST_KEY, "") ?: ""
        return cacheValue.isNotEmpty()
    }

    fun isMigrationSuccess(): Boolean {
        return dsPref.getBoolean(KEY_MIGRATION_STATUS, false)
    }

    fun setMigrationStatus(isSuccess: Boolean) {
        dsPref.edit().putBoolean(KEY_MIGRATION_STATUS, isSuccess).apply()
    }

    companion object {
        const val USER_SESSION_AB_TEST_KEY = "android_data_store_v2"
        const val DATA_STORE_PREF = "DATA_STORE_MIGRATION_PREF"
        const val KEY_MIGRATION_STATUS = "data_store_migration_status"
        const val SHARED_PREFERENCE_AB_TEST_PLATFORM = "tkpd-ab-test-platform"
    }
}