package com.tokopedia.feedplus.browse.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.util.UiEventManager
import com.tokopedia.feedplus.browse.data.FeedBrowseRepository
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiAction
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiEvent
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiState
import com.tokopedia.feedplus.browse.presentation.view.FeedBrowsePlaceholderView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseViewModel @Inject constructor(
    private val repository: FeedBrowseRepository,
    private val uiEventManager: UiEventManager<FeedBrowseUiEvent>
): ViewModel() {

    private val _pageTitle = MutableStateFlow("")
    private val _widgets = MutableStateFlow<List<FeedBrowseUiModel>>(
        listOf(
            FeedBrowseUiModel.Placeholder(FeedBrowsePlaceholderView.Type.Title),
            FeedBrowseUiModel.Placeholder(FeedBrowsePlaceholderView.Type.Chips),
            FeedBrowseUiModel.Placeholder(FeedBrowsePlaceholderView.Type.Cards),
            FeedBrowseUiModel.Placeholder(FeedBrowsePlaceholderView.Type.Title),
            FeedBrowseUiModel.Placeholder(FeedBrowsePlaceholderView.Type.Cards),
        )
    )

    /**
     * 1. getFeedXHome, returns slots -> showing Placeholder
     * 2. getContentSlot, returns -> still showing Placeholder
     *  2.1 title + chips -> showing data + placeholder channels
     *  2.2 title + channels -> showing data
     *
     * edge cases:
     * 1. error when getFeedXHome()
     * 2. error when getContentSlot()
     *
     */
    val uiState: StateFlow<FeedBrowseUiState>
        get() = kotlinx.coroutines.flow.combine(_pageTitle, _widgets) { title, widgets ->
            FeedBrowseUiState(
                title = title,
                widgets = widgets
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            FeedBrowseUiState.Empty
        )

    val uiEvent: Flow<FeedBrowseUiEvent?>
        get() = uiEventManager.event

    fun submitAction(action: FeedBrowseUiAction) {
        when (action) {
            FeedBrowseUiAction.FetchTitle -> handleFetchTitle()
            FeedBrowseUiAction.FetchSlots -> handleFetchSlots()
        }
    }

    private fun handleFetchTitle() {
        viewModelScope.launch {
            val title = repository.getTitle()
            _pageTitle.value = title
        }
    }

    private fun handleFetchSlots() {
        viewModelScope.launch {
            val slots = repository.getSlots()

            // this works, async, but the position might not be the same
            slots.forEach { slot ->
                getWidget(slot.type)
            }
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
