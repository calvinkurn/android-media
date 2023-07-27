package com.tokopedia.stories.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.util.UiEventManager
import com.tokopedia.stories.common.domain.ShopStoriesState
import com.tokopedia.stories.common.domain.StoriesAvatarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 25/07/23
 */
internal class StoriesAvatarViewModel @Inject constructor(
    private val repository: StoriesAvatarRepository
) : ViewModel() {

    private val _storiesMap = MutableStateFlow(emptyMap<String, StoriesAvatarState>())

    private val uiMessageManager = UiEventManager<StoriesAvatarMessage>()

    private val mutex = Mutex()

    init {
    }

    fun onIntent(intent: StoriesAvatarIntent) {
        when (intent) {
            is StoriesAvatarIntent.GetStoriesStatus -> onGetStories(intent.shopIds)
            is StoriesAvatarIntent.OpenStoriesDetail -> onOpenStoriesDetail(intent.shopId)
        }
    }

    fun getStoriesState(shopId: String): Flow<StoriesAvatarState?> {
        return _storiesMap.map { it[shopId] }.distinctUntilChanged()
    }

    fun getStoriesMessage(shopId: String): Flow<StoriesAvatarMessage?> {
        return uiMessageManager.event.filter { it?.shopId == shopId }
    }

    fun clearMessage(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearEvent(id)
        }
    }

    private fun onGetStories(shopIds: List<String>) {
        if (shopIds.isEmpty()) return

        viewModelScope.launch {
            val storiesState = repository.getShopStoriesState(shopIds)
            val storiesStateMap = storiesState.associate {
                it.shopId to StoriesAvatarState(
                    it.shopId,
                    it.getStoriesStatus(),
                    it.appLink
                )
            }
            _storiesMap.update {
                it + storiesStateMap
            }

            showCoachMark(shopIds.first())
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

    private suspend fun showCoachMark(shopId: String) {
        if (repository.hasSeenCoachMark()) return

        uiMessageManager.emitEvent(StoriesAvatarMessage.ShowCoachMark(shopId))
        repository.setHasSeenCoachMark()
    }

    private fun ShopStoriesState.getStoriesStatus(): StoriesStatus {
        return when {
            !anyStoryExisted -> StoriesStatus.NoStories
            hasUnseenStories -> StoriesStatus.HasUnseenStories
            else -> StoriesStatus.AllStoriesSeen
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
