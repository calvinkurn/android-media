package com.tokopedia.stories.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.util.UiEventManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Created by kenny.hadisaputra on 25/07/23
 */
internal class StoriesAvatarViewModel : ViewModel() {

    private val _storiesMap = MutableStateFlow(emptyMap<String, StoriesAvatarState>())

    private val uiMessageManager = UiEventManager<StoriesAvatarMessage>()

    private val mutex = Mutex()

    init {
    }

    fun onIntent(intent: StoriesAvatarIntent) {
        when (intent) {
            is StoriesAvatarIntent.GetStoriesStatus -> onGetStories(intent.shopId)
            is StoriesAvatarIntent.OpenStoriesDetail -> onOpenStoriesDetail(intent.shopId)
        }
    }

    fun getStoriesState(shopId: String): Flow<StoriesAvatarState> {
        return _storiesMap.mapNotNull { it[shopId] }.distinctUntilChanged()
    }

    fun getStoriesMessage(shopId: String): Flow<StoriesAvatarMessage?> {
        return uiMessageManager.event.filter { it?.shopId == shopId }
    }

    fun clearMessage(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearEvent(id)
        }
    }

    private fun onGetStories(shopId: String) {
        viewModelScope.launch {
            delay(5000)
            val hasStories = if (shopId.toLong() % 2 == 0L) StoriesStatus.HasUnseenStories else StoriesStatus.AllStoriesSeen
            updateStoriesForId(shopId, StoriesAvatarState(shopId, hasStories, "a"))

            uiMessageManager.emitEvent(StoriesAvatarMessage.ShowCoachMark(shopId))
        }
    }

    private fun onOpenStoriesDetail(shopId: String) {
        viewModelScope.launch {
            val state = getStoriesStateById(shopId)
            if (state == null || state.status == StoriesStatus.NoStories || state.appLink.isBlank()) {
                uiMessageManager.emitEvent(StoriesAvatarMessage.OpenDetailWithNoStories(shopId))
            } else {
                uiMessageManager.emitEvent(StoriesAvatarMessage.OpenStoriesDetail(shopId, state.appLink))
            }
        }
    }

    private suspend fun updateStoriesForId(shopId: String, state: StoriesAvatarState) = mutex.withLock {
        _storiesMap.update { it + (shopId to state) }
    }

    private suspend fun getStoriesStateById(shopId: String) = mutex.withLock {
        val storiesMap = _storiesMap.value
        storiesMap[shopId]
    }
}
