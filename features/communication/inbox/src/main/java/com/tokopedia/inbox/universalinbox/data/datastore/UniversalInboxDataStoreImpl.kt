package com.tokopedia.inbox.universalinbox.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.lang.reflect.Type
import javax.inject.Inject

private const val NAME = "inbox"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = NAME)

class UniversalInboxDataStoreImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UniversalInboxDataStore {

    private val gson: Gson by lazy {
        Gson()
    }

    override suspend fun saveCache(key: String, value: Any) {
        val cacheKey = stringPreferencesKey(key)
        val cacheString = gson.toJson(value)
        context.dataStore.edit { preferences ->
            preferences[cacheKey] = cacheString
        }
    }

    override suspend fun <T>loadCache(key: String, type: Type): Flow<T?> {
        val cacheKey = stringPreferencesKey(key)
        return context.dataStore.data
            .catch { exception ->
                // dataStore.data throws an IOException when an error is encountered when reading data
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val cache = preferences[cacheKey] ?: ""
                gson.fromJson(cache, type)
            }
    }
}
