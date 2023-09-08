package com.tokopedia.feedplus.browse.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.feedplus.browse.data.FeedBrowseRepository
import com.tokopedia.feedplus.browse.data.model.WidgetRequestModel
import com.tokopedia.feedplus.browse.presentation.model.ChannelUiState
import com.tokopedia.feedplus.browse.presentation.model.ChipUiState
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChipUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiAction
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by meyta.taliti on 11/08/23.
 */
class FeedBrowseViewModel @Inject constructor(
    private val repository: FeedBrowseRepository
) : ViewModel() {

    private val _title = MutableStateFlow("")
    private val _slots = MutableStateFlow<ResultState>(ResultState.Loading)
    private val _widgets = MutableStateFlow<List<FeedBrowseUiModel>>(emptyList())

    val uiState: StateFlow<FeedBrowseUiState> = combine(_title, _slots, _widgets) { title, slots, widgets ->
        if (slots is ResultState.Fail) {
            FeedBrowseUiState.Error(
                slots.error
            )
        } else {
            FeedBrowseUiState.Success(
                title = title,
                widgets = widgets
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FeedBrowseUiState.Placeholder
    )

    fun submitAction(action: FeedBrowseUiAction) {
        when (action) {
            FeedBrowseUiAction.LoadInitialPage -> handleInitialPage()
            is FeedBrowseUiAction.FetchCards -> {
                handleFetchWidget(action.extraParam, action.widgetId)
            }
            is FeedBrowseUiAction.SelectChip -> handleSelectChip(action.model, action.widgetId)
        }
    }

    private fun handleInitialPage() {
        handleFetchTitle()
        handleFetchSlots()
    }

    private fun handleFetchTitle() {
        viewModelScope.launch {
            val title = repository.getTitle()
            _title.update {
                title
            }
        }
    }

    private fun handleFetchSlots() {
        viewModelScope.launch {
            try {
                val slots = repository.getSlots()
                _widgets.value = slots
                _slots.value = ResultState.Success

                slots.forEach { slot ->
                    if (slot is FeedBrowseUiModel.Channel) {
                        handleFetchWidget(slot.extraParam, slot.id)
                    }
                }
            } catch (err: Throwable) {
                _slots.value = ResultState.Fail(err)
            }
        }
    }

    private fun handleFetchWidget(extraParam: WidgetRequestModel, widgetId: String) {
        viewModelScope.launch {
            val response = repository.getWidget(extraParam)
            updateChannelWidget(widgetId) { prevWidget ->
                replaceContent(prevWidget, response)
            }
        }
    }

    private fun handleSelectChip(chip: FeedBrowseChipUiModel, widgetId: String) {
        viewModelScope.launch {
            updateChannelWidget(widgetId) { prevWidget ->
                prevWidget.copy(
                    chipUiState = if (prevWidget.chipUiState is ChipUiState.Data) {
                        prevWidget.chipUiState.copy(
                            items = prevWidget.chipUiState.items.map {
                                it.copy(isSelected = it.id == chip.id)
                            }
                        )
                    } else {
                        prevWidget.chipUiState
                    },
                    channelUiState = ChannelUiState.Placeholder
                )
            }
        }
    }

    private fun updateChannelWidget(
        widgetId: String,
        onUpdate: (FeedBrowseUiModel.Channel) -> FeedBrowseUiModel // only handle channel for now
    ) {
        _widgets.update { existingWidgets ->
            existingWidgets.map { prevWidget ->
                if (prevWidget is FeedBrowseUiModel.Channel && prevWidget.id == widgetId) {
                    return@map onUpdate(prevWidget)
                } else {
                    prevWidget
                }
            }
        }
    }

    private fun replaceContent(
        widget: FeedBrowseUiModel.Channel,
        newContent: FeedBrowseItemUiModel
    ): FeedBrowseUiModel {
        return widget.copy(
            chipUiState = if (newContent is ChipUiState) {
                newContent
            } else {
                widget.chipUiState
            },
            channelUiState = if (newContent is ChannelUiState) {
                newContent
            } else {
                widget.channelUiState
            }
        )
    }
}
