package com.tokopedia.stories.widget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.util.UiEventManager
import com.tokopedia.stories.widget.domain.StoriesEntryPoint
import com.tokopedia.stories.widget.domain.StoriesWidgetRepository
import com.tokopedia.stories.widget.domain.StoriesWidgetState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by kenny.hadisaputra on 25/07/23
 */
internal class StoriesWidgetViewModel @AssistedInject constructor(
    @Assisted private val entryPoint: StoriesEntryPoint,
    private val repository: StoriesWidgetRepository
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(entryPoint: StoriesEntryPoint): StoriesWidgetViewModel
    }

    private val _storiesMap = MutableStateFlow(emptyMap<String, StoriesWidgetState>())
    val stories: StateFlow<Map<String, StoriesWidgetState>> get() = _storiesMap.asStateFlow()

    private val uiMessageManager = UiEventManager<StoriesWidgetMessage>()
    val uiMessage: Flow<StoriesWidgetMessage?> get() = uiMessageManager.event

    private var mCoachMarkText = ""

    init {}

    fun onIntent(intent: StoriesWidgetIntent) {
        when (intent) {
            is StoriesWidgetIntent.GetStoriesStatus -> onGetStories(intent.shopIds)
            is StoriesWidgetIntent.GetLatestStoriesStatus -> onGetLatestStoriesStatus()
            StoriesWidgetIntent.ShowCoachMark -> onShowCoachMark()
            StoriesWidgetIntent.HasSeenCoachMark -> onHasSeenCoachMark()
        }
    }

    fun getStoriesState(shopId: String): Flow<StoriesWidgetState?> {
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
            val storiesInfo = repository.getStoriesWidgetState(entryPoint, shopIds)
            mCoachMarkText = storiesInfo.coachMarkText

            val storiesStateMap = storiesInfo.widgetStates.associateBy { it.shopId }
            _storiesMap.update { it + storiesStateMap }
        }
    }

    private fun onGetLatestStoriesStatus() {
        viewModelScope.launch {
            _storiesMap.update {
                it.mapValues { entry ->
                    val isStoriesSeen = repository.getUpdatedSeenStatus(
                        entry.key,
                        entry.value.updatedAt
                    )

                    entry.value.copy(
                        status = if (entry.value.status != StoriesStatus.NoStories && isStoriesSeen) {
                            StoriesStatus.AllStoriesSeen
                        } else {
                            entry.value.status
                        }
                    )
                }
            }
        }
    }

    private fun onShowCoachMark() {
        viewModelScope.launch {
            if (repository.hasSeenCoachMark()) return@launch

            runCatching {
                _storiesMap.value.firstNotNullOf {
                    if (it.value.status != StoriesStatus.NoStories) {
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
        uiMessageManager.emitEvent(StoriesWidgetMessage.ShowCoachMark(shopId, mCoachMarkText))
    }
}
