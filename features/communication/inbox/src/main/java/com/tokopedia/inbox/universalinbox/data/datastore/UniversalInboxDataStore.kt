package com.tokopedia.inbox.universalinbox.data.datastore

import kotlinx.coroutines.flow.Flow
import java.lang.reflect.Type

interface UniversalInboxDataStore {
    suspend fun saveCache(key: String, value: Any)
    suspend fun <T>loadCache(key: String, type: Type): Flow<T?>
}
