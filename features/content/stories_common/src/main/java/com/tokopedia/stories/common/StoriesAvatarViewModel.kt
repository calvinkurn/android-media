package com.tokopedia.stories.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.util.UiEventManager
import com.tokopedia.stories.common.domain.ShopStoriesState
import com.tokopedia.stories.common.domain.StoriesAvatarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 25/07/23
 */
internal class StoriesAvatarViewModel @Inject constructor(
    private val repository: StoriesAvatarRepository
) : ViewModel() {

    private val _storiesMap = MutableStateFlow(emptyMap<String, StoriesAvatarState>())
    val stories: StateFlow<Map<String, StoriesAvatarState>> get() = _storiesMap.asStateFlow()

    private val uiMessageManager = UiEventManager<StoriesAvatarMessage>()
    val uiMessage: Flow<StoriesAvatarMessage?> get() = uiMessageManager.event

    init {}

    fun onIntent(intent: StoriesAvatarIntent) {
        when (intent) {
            is StoriesAvatarIntent.GetStoriesStatus -> onGetStories(intent.shopIds)
            StoriesAvatarIntent.ShowCoachMark -> onShowCoachMark()
            StoriesAvatarIntent.HasSeenCoachMark -> onHasSeenCoachMark()
        }
    }

    fun getStoriesState(shopId: String): Flow<StoriesAvatarState?> {
        return _storiesMap.map { it[shopId] }.distinctUntilChanged()
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
                    it.getStoriesStatus(),
                    it.appLink
                )
            }
            _storiesMap.update {
                it + storiesStateMap
            }
        }
    }

    private fun onShowCoachMark() {
        viewModelScope.launch {
            if (repository.hasSeenCoachMark()) return@launch

            runCatching {
                _storiesMap.value.firstNotNullOf {
                    if (it.value.status == StoriesStatus.HasUnseenStories) {
                        it.key
                    } else {
                        null
                    }
                }
            }.onSuccess { shopId -> showCoachMark(shopId) }
        }
    }

    private fun onHasSeenCoachMark() {
        viewModelScope.launch {
            repository.setHasSeenCoachMark()
        }
    }

    private suspend fun showCoachMark(shopId: String) {
        uiMessageManager.emitEvent(StoriesAvatarMessage.ShowCoachMark(shopId))
    }

    private fun ShopStoriesState.getStoriesStatus(): StoriesStatus {
        return when {
            !anyStoryExisted -> StoriesStatus.NoStories
            hasUnseenStories -> StoriesStatus.HasUnseenStories
            else -> StoriesStatus.AllStoriesSeen
        }
    }
}
