package com.tokopedia.topchat.chatlist.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topchat.chatlist.data.datastore.TopChatListDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class GetChatListLastVisitedTabUseCase @Inject constructor(
    private val dataStore: TopChatListDataStore,
    private val dispatchers: CoroutineDispatchers
) {
    suspend fun setLastVisitedTab(position: Int) {
        withContext(dispatchers.io) {
            dataStore.saveCache(KEY_LAST_POSITION, position)
        }
    }

    suspend fun observeLastVisitedTab(): Flow<Int> {
        return dataStore.observeCache(KEY_LAST_POSITION).map {
            return@map it.toIntOrZero()
        }.catch {
            Timber.d(it)
            emit(-1)
        }
            .flowOn(dispatchers.io)
    }

    companion object {
        const val KEY_LAST_POSITION = "key_last_seen_tab_position"
    }
}
