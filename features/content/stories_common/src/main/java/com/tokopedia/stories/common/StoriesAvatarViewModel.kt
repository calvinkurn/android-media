package com.tokopedia.stories.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Created by kenny.hadisaputra on 25/07/23
 */
internal class StoriesAvatarViewModel : ViewModel() {

    private val _storiesMap = MutableStateFlow(emptyMap<String, StoriesState>())

    private val mutex = Mutex()

    init {

    }

    fun onIntent(intent: StoriesIntent) {
        when (intent) {
            is StoriesIntent.getStoriesStatus -> onGetStories(intent.shopId)
        }
    }

    fun getStories(shopId: String): Flow<StoriesState> {
        return _storiesMap.mapNotNull { it[shopId] }.distinctUntilChanged()
    }

    private fun onGetStories(shopId: String) {
        viewModelScope.launch {
            delay(2000)
            val hasStories = if (shopId.toLong() % 2 == 0L) StoriesStatus.HasUnseenStories else StoriesStatus.AllStoriesSeen
            updateStoriesForId(shopId, StoriesState(shopId, hasStories))
        }
    }

    private suspend fun updateStoriesForId(shopId: String, state: StoriesState) = mutex.withLock {
        _storiesMap.update { it + (shopId to state) }
    }
}
