package com.tokopedia.topchat.chatlist.data.datastore

import kotlinx.coroutines.flow.Flow

interface TopChatListDataStore {
    suspend fun saveCache(key: String, value: Any)
    suspend fun observeCache(key: String): Flow<String>
}
