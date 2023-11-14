package com.tokopedia.topchat.stub.chatlist.data

import com.google.gson.Gson
import com.tokopedia.topchat.chatlist.data.datastore.TopChatListDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TopChatListDataStoreStub : TopChatListDataStore {

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
