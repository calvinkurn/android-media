package com.tokopedia.feedplus.browse.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.feedplus.browse.data.FeedBrowseRepository
import com.tokopedia.feedplus.browse.data.model.FeedBrowseModel
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.data.model.WidgetRequestModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChipUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiAction
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

/**
 * Created by meyta.taliti on 11/08/23.
 */
internal class FeedBrowseViewModel @Inject constructor(
    private val repository: FeedBrowseRepository,
    private val modelFetchers: FeedBrowseModelFetchers,
) : ViewModel() {

    private val _title = MutableStateFlow("")
    private val _slots = MutableStateFlow<ResultState>(ResultState.Loading)
    private val _widgets = MutableStateFlow<List<FeedBrowseModel>>(emptyList())

    private val updateMutex = Mutex()

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
            is FeedBrowseUiAction.FetchCardsWidget -> {
                handleFetchWidget(WidgetRequestModel.Empty, action.slotId)
            }
            is FeedBrowseUiAction.SelectChipWidget -> {
                handleSelectChip(action.model, action.slotId)
            }
        }
    }

    private fun handleInitialPage() {
        handleFetchTitle()
        handleFetchSlots()
    }

    private fun handleFetchTitle() {
        viewModelScope.launch {
            val title = repository.getTitle()
            _title.update { title }
        }
    }

    private fun handleFetchSlots() {
        viewModelScope.launch {
            try {
                val slots = repository.getSlots()
                _widgets.value = slots
                _slots.value = ResultState.Success

                slots.forEach { slot ->
                    launch {
                        Log.d("FeedBrowse", "Getting slots for Slot Id: ${slot.slotId}, Slot: $slot")
                        val updatedModel = modelFetchers.call(slot)
                        Log.d("FeedBrowse", "Updating slots for Slot Id: ${slot.slotId}, Model: $updatedModel")
                        updateWidget(updatedModel.slotId) { updatedModel }
                    }
                }
            } catch (err: Throwable) {
                _slots.value = ResultState.Fail(err)
            }
        }
    }

    private fun handleFetchWidget(extraParam: WidgetRequestModel, widgetId: String) {
        viewModelScope.launch {
            val widgets = _widgets.value
            val widget = widgets.firstOrNull { it.slotId == widgetId } ?: return@launch
            val model = modelFetchers.call(widget)

            updateWidget(widgetId) { model }
        }
    }

    private fun handleSelectChip(chip: FeedBrowseChipUiModel, widgetId: String) {
        viewModelScope.launch {
            updateWidget(widgetId) {
                if (it !is FeedBrowseModel.ChannelsWithMenus) return@updateWidget it
                it.copy(
                    menus = it.menus.mapKeys { entry ->
                        entry.key.copy(isSelected = chip.id == entry.key.id)
                    }
                )
            }
        }
    }

    private fun handleSelectChip(chip: WidgetMenuModel, widgetId: String) {
        viewModelScope.launch {
            updateWidget(widgetId) {
                if (it !is FeedBrowseModel.ChannelsWithMenus) return@updateWidget it
                it.copy(
                    menus = it.menus.mapKeys { entry ->
                        entry.key.copy(isSelected = chip.id == entry.key.id)
                    }
                )
            }
        }
    }

    private suspend fun updateWidget(
        widgetId: String,
        onUpdate: (FeedBrowseModel) -> FeedBrowseModel
    ) = updateMutex.withLock {
        _widgets.update { existingWidgets ->
            existingWidgets.map { prevWidget ->
                if (prevWidget.slotId == widgetId) {
                    return@map onUpdate(prevWidget)
                } else {
                    prevWidget
                }
            }
        }
    }
}
