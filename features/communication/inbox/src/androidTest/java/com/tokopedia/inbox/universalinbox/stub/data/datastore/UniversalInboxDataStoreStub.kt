package com.tokopedia.inbox.universalinbox.stub.data.datastore

import com.google.gson.Gson
import com.tokopedia.inbox.universalinbox.data.datastore.UniversalInboxDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class UniversalInboxDataStoreStub : UniversalInboxDataStore {

    private val gson = Gson()
    private val cacheMap: MutableMap<String, String> = mutableMapOf()
    private val cacheFlow = MutableStateFlow("")

    override suspend fun saveCache(key: String, value: Any) {
        cacheMap[key] = gson.toJson(value)
        cacheFlow.emit(cacheMap[key].toString())
    }

    override suspend fun observeCache(key: String): Flow<String> {
        return cacheFlow
    }
}
