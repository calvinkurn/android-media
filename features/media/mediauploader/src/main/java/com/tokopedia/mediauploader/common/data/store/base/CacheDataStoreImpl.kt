package com.tokopedia.mediauploader.common.data.store.base

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tokopedia.config.GlobalConfig
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

abstract class CacheDataStoreImpl<T> constructor(
    private val context: Context,
    preferencesName: String
) : CacheDataStore<T> {

    private val Context.dataStore by preferencesDataStore(preferencesName)

    override suspend fun get(key: String): T? {
        return context.dataStore.data
            .map {
                val content = it[stringPreferencesKey(key)] ?: ""
                read(content)
            }.firstOrNull()
    }

    override suspend fun set(key: String, cache: T.() -> Unit) {
        context.dataStore.edit {
            val element = get(key)?.apply(cache) ?: default(cache)
            val json = write(element)

            it[stringPreferencesKey(key)] = json

            if (GlobalConfig.isAllowDebuggingTools()) {
                println("MEDIA-UPLOADER: (store) $json")
            }
        }
    }
}
