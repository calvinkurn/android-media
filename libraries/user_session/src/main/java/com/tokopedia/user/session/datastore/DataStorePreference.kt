package com.tokopedia.user.session.datastore

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

open class DataStorePreference @Inject constructor(private val context: Context) {

    private val dsPref: SharedPreferences by lazy {
        context.getSharedPreferences(
            DATA_STORE_PREF, Context.MODE_PRIVATE
        )
    }

    open fun isDataStoreEnabled(): Boolean {
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
