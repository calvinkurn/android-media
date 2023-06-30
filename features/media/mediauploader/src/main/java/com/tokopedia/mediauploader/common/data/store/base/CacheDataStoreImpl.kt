package com.tokopedia.mediauploader.common.data.store.base

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private const val NAME = "media_uploader"
private val Context.dataStore by preferencesDataStore(NAME)

abstract class CacheDataStoreImpl<T> constructor(private val context: Context) : CacheDataStore<T> {

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
            it[stringPreferencesKey(key)] = write(element)
        }
    }
}
