package com.tokopedia.feedplus.browse.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.util.UiEventManager
import com.tokopedia.feedplus.browse.data.FeedBrowseRepository
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiAction
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiEvent
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseViewModel @Inject constructor(
    private val repository: FeedBrowseRepository,
    private val uiEventManager: UiEventManager<FeedBrowseUiEvent>
) : ViewModel() {

    private val _widgets = MutableStateFlow<List<FeedBrowseUiModel>>(emptyList())

    /**
     * 1. getFeedXHome, returns slots -> showing Placeholder
     * 2. getContentSlot, returns -> still showing Placeholder
     *  2.1 title + chips -> showing data + placeholder channels
     *  2.2 title + channels -> showing data
     *
     * edge cases:
     * 1. error when getFeedXHome()
     * 2. error when getContentSlot()
     */
    val uiState: StateFlow<FeedBrowseUiState>
        get() = _uiState.asStateFlow()

    private val _uiState = MutableStateFlow<FeedBrowseUiState>(FeedBrowseUiState.Placeholder)

    val uiEvent: Flow<FeedBrowseUiEvent?>
        get() = uiEventManager.event

    fun submitAction(action: FeedBrowseUiAction) {
        when (action) {
            FeedBrowseUiAction.LoadInitialPage -> handleInitialPage()
            is FeedBrowseUiAction.FetchCards -> TODO()
        }
    }

    private fun handleInitialPage() {
        viewModelScope.launch {
            val title = repository.getTitle()
            val result = try {
                val slots = repository.getSlots()

                // this works, async, but the position might not be the same
                slots.forEach { getWidget(it.type) }

                FeedBrowseUiState.Success(
                    title = title,
                    widgets = emptyList()
                )
            } catch (err: Throwable) {
                FeedBrowseUiState.Error(err)
            }
            _uiState.value = result
        }
    }

    private fun getWidget(type: String) {
        viewModelScope.launch {
            val widget = try {
                repository.getWidgets(type)
            } catch (e: Throwable) {
                // todo: handle error state
                emptyList()
            }
            _widgets.update {
                it + widget
            }
        }
    }
}
