package com.tokopedia.product.manage.common.feature.datasource

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.manage.common.feature.datasource.LocalDataSourceImpl.PreferencesKeys.PRODUCT_ID
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
        const val PREFERENCE_KEY_PRODUCT_ID = "PRODUCT_ID"
    }

    private object PreferencesKeys {
        val STATUS = intPreferencesKey(PREFERENCE_KEY_STATUS)
        val PRODUCT_ID = stringPreferencesKey(PREFERENCE_KEY_PRODUCT_ID)
    }

    override fun getDataSourceFlow(): Flow<UserPreferences> =
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
                val productId = preferences[PRODUCT_ID].orEmpty()
                UserPreferences(status, productId)
            }

    override suspend fun updateDataSource(status: Int, productId: String) {
        context.dataStore.edit { preferences ->
            preferences[STATUS] = status
            preferences[PRODUCT_ID] = productId
        }
    }

    override suspend fun clearDataStore() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}