package com.tokopedia.stories.widget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.util.UiEventManager
import com.tokopedia.stories.widget.domain.StoriesEntryPoint
import com.tokopedia.stories.widget.domain.StoriesWidgetInfo
import com.tokopedia.stories.widget.domain.StoriesWidgetRepository
import com.tokopedia.stories.widget.domain.StoriesWidgetState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    private val _storiesState = MutableStateFlow(StoriesWidgetInfo.Default)
    val stories: StateFlow<Map<String, StoriesWidgetState>> get() = _storiesState.map {
        it.widgetStates
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyMap()
    )

    private val uiMessageManager = UiEventManager<StoriesWidgetMessage>()
    val uiMessage: Flow<StoriesWidgetMessage?> get() = uiMessageManager.event

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
        return _storiesState.map { it.widgetStates[shopId] }.distinctUntilChanged()
    }

    fun clearMessage(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearEvent(id)
        }
    }

    private fun onGetStories(shopIds: List<String>) {
        if (shopIds.isEmpty()) return

        viewModelScope.launch {
            val storiesInfo = repository.getStoriesWidgetInfo(entryPoint, shopIds)
            _storiesState.update {
                it.copy(
                    widgetStates = it.widgetStates + storiesInfo.widgetStates,
                    coachMarkText = storiesInfo.coachMarkText
                )
            }
        }
    }

    private fun onGetLatestStoriesStatus() {
        viewModelScope.launch {
            _storiesState.update {
                it.copy(
                    widgetStates = it.widgetStates.mapValues { entry ->
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
                )
            }
        }
    }

    private fun onShowCoachMark() {
        viewModelScope.launch {
            if (repository.hasSeenCoachMark()) return@launch

            runCatching {
                _storiesState.value.widgetStates.firstNotNullOf {
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
        uiMessageManager.emitEvent(
            StoriesWidgetMessage.ShowCoachMark(
                shopId,
                _storiesState.value.coachMarkText
            )
        )
    }
}
