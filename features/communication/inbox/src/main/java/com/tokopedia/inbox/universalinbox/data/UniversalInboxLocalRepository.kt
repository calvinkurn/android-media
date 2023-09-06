package com.tokopedia.inbox.universalinbox.data

import com.tokopedia.inbox.universalinbox.data.datastore.UniversalInboxDataStore
import com.tokopedia.inbox.universalinbox.data.entity.UniversalInboxWrapperResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UniversalInboxLocalRepository @Inject constructor(
    private val dataStore: UniversalInboxDataStore
) {
    suspend fun getInboxMenuCache(): Flow<UniversalInboxWrapperResponse?> {
        return dataStore.loadCache(KEY_INBOX_MENU, UniversalInboxWrapperResponse::class.java)
    }

    suspend fun setInboxMenuCache(value: UniversalInboxWrapperResponse) {
        dataStore.saveCache(KEY_INBOX_MENU, value)
    }

    companion object {
        const val KEY_INBOX_MENU = "universal_inbox_menu_cache"
    }
}
