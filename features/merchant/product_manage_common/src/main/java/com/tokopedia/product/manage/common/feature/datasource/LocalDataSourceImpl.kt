package com.tokopedia.product.manage.common.feature.datasource

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.manage.common.feature.datasource.LocalDataSourceImpl.PreferencesKeys.STATUS
import com.tokopedia.product.manage.common.feature.datasource.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val USER_PREFERENCES_NAME = "USER_PREFERENCES"

private val Context.dataStore by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

class LocalDataSourceImpl(
    @ApplicationContext private val context: Context,
) :LocalDataSource {

    companion object {
        const val PREFERENCE_KEY_STATUS = "STATUS"
    }

    private object PreferencesKeys {
        val STATUS = intPreferencesKey(PREFERENCE_KEY_STATUS)
    }

    override fun userPreferencesFlow(): Flow<UserPreferences> =
        context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .distinctUntilChanged()
            .map { preferences ->
                val status = preferences[STATUS].orZero()
                UserPreferences(status)
            }

    override suspend fun updateStatus(status: Int) {
        context.dataStore.edit { preferences ->
            preferences[STATUS] = status
        }
    }

    override suspend fun clearDataStore() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}