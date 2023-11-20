package com.tokopedia.inbox.universalinbox.data.datastore

import kotlinx.coroutines.flow.Flow

interface UniversalInboxDataStore {
    suspend fun saveCache(key: String, value: Any)
    suspend fun observeCache(key: String): Flow<String>
}
