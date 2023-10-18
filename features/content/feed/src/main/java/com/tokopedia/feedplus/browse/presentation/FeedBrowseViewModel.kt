package com.tokopedia.feedplus.browse.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.feedplus.browse.data.FeedBrowseRepository
import com.tokopedia.feedplus.browse.data.model.ContentSlotModel
import com.tokopedia.feedplus.browse.data.model.FeedBrowseModel
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.data.model.WidgetRecommendationModel
import com.tokopedia.feedplus.browse.data.model.WidgetRequestModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChipUiModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseIntent
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiModel2
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseUiState
import com.tokopedia.feedplus.browse.presentation.model.ItemListState
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
private typealias FeedBrowseModelMap = Map<String, FeedBrowseUiModel2>
internal class FeedBrowseViewModel @Inject constructor(
    private val repository: FeedBrowseRepository
) : ViewModel() {

    private val _title = MutableStateFlow("")
    private val _slots = MutableStateFlow<ResultState>(ResultState.Loading)
    private val _widgets = MutableStateFlow<FeedBrowseModelMap>(emptyMap())

    private val updateMutex = Mutex()

    val uiState: StateFlow<FeedBrowseUiState> = combine(
        _title,
        _slots,
        _widgets
    ) { title, slots, widgets ->
        if (slots is ResultState.Fail) {
            FeedBrowseUiState.Error(
                slots.error
            )
        } else {
            FeedBrowseUiState.Success(
                title = title,
                widgets = widgets.values.toList()
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FeedBrowseUiState.Placeholder
    )

    fun onIntent(action: FeedBrowseIntent) {
        when (action) {
            FeedBrowseIntent.LoadInitialPage -> handleInitialPage()
            is FeedBrowseIntent.FetchCards -> {
                handleFetchWidget(action.extraParam, action.widgetId)
            }
            is FeedBrowseIntent.SelectChip -> handleSelectChip(action.model, action.widgetId)
            is FeedBrowseIntent.FetchCardsWidget -> {
                handleFetchWidget(WidgetRequestModel.Empty, action.slotId)
            }
            is FeedBrowseIntent.SelectChipWidget -> {
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
                _widgets.value = slots.associate {
                    it.slotId to FeedBrowseUiModel2(ResultState.Loading, it)
                }
                _slots.value = ResultState.Success

                slots.forEach { slot -> launch { getAndUpdateData(slot) } }
            } catch (err: Throwable) {
                _slots.value = ResultState.Fail(err)
            }
        }
    }

    private fun handleFetchWidget(extraParam: WidgetRequestModel, slotId: String) {
        viewModelScope.launch {
            val widgets = _widgets.value
            val widget = widgets[slotId]?.model ?: return@launch
            getAndUpdateData(widget)
        }
    }

    private fun handleSelectChip(chip: FeedBrowseChipUiModel, widgetId: String) {
        viewModelScope.launch {
            updateWidget<FeedBrowseModel.ChannelsWithMenus>(widgetId, ResultState.Success) {
                it.copy(selectedMenuId = chip.id)
            }
        }
    }

    private fun handleSelectChip(chip: WidgetMenuModel, slotId: String) {
        viewModelScope.launch {
            updateWidget<FeedBrowseModel.ChannelsWithMenus>(slotId, ResultState.Success) {
                it.copy(
                    selectedMenuId = chip.id,
                    menus = it.menus + (chip to ItemListState.Loading)
                )
            }
        }
    }

    private suspend fun getAndUpdateData(model: FeedBrowseModel) {
        when (model) {
            is FeedBrowseModel.ChannelsWithMenus -> model.getAndUpdateData()
            is FeedBrowseModel.InspirationBanner -> model.getAndUpdateData()
        }
    }

    private suspend fun FeedBrowseModel.ChannelsWithMenus.getAndUpdateData() {
        val menuKeys = menus.keys
        val selectedMenu = menuKeys.firstOrNull { it.id == selectedMenuId }

        val requestModel = if (menus.isEmpty()) {
            Log.d("FeedBrowse", "Requesting: Slot $slotId, Group: $group")
            WidgetRequestModel(group = group)
        } else {
            val menu = selectedMenu ?: menuKeys.first()
            WidgetRequestModel(
                group = menu.group,
                sourceType = menu.sourceType,
                sourceId = menu.sourceId
            )
        }

        try {
            when (val response = repository.getWidgetContentSlot(requestModel)) {
                is ContentSlotModel.TabMenus -> {
                    updateWidget<FeedBrowseModel.ChannelsWithMenus>(slotId, ResultState.Success) {
                        it.copy(
                            menus = response.menu.associateWith { ItemListState.Loading },
                            selectedMenuId = response.menu.firstOrNull()?.id.orEmpty()
                        )
                    }
                }
                is ContentSlotModel.ChannelBlock -> {
                    if (menuKeys.isEmpty()) {
                        updateWidget<FeedBrowseModel.ChannelsWithMenus>(slotId, ResultState.Success) {
                            it.copy(
                                type = FeedBrowseModel.ChannelsWithMenus.Type.ChannelBlock,
                                menus = mapOf(
                                    WidgetMenuModel.Default to ItemListState.HasContent(response.channels)
                                )
                            )
                        }
                    } else {
                        updateWidget<FeedBrowseModel.ChannelsWithMenus>(slotId, ResultState.Success) {
                            val menu = selectedMenu ?: menuKeys.first()
                            it.copy(
                                type = FeedBrowseModel.ChannelsWithMenus.Type.ChannelBlock,
                                menus = menus + (menu to ItemListState.HasContent(response.channels))
                            )
                        }
                    }
                }
                is ContentSlotModel.ChannelRecommendation -> {
                    if (menuKeys.isEmpty()) {
                        updateWidget<FeedBrowseModel.ChannelsWithMenus>(slotId, ResultState.Success) {
                            it.copy(
                                type = FeedBrowseModel.ChannelsWithMenus.Type.ChannelRecommendation,
                                menus = mapOf(
                                    WidgetMenuModel.Default to ItemListState.HasContent(response.channels)
                                )
                            )
                        }
                    } else {
                        updateWidget<FeedBrowseModel.ChannelsWithMenus>(slotId, ResultState.Success) {
                            val menu = selectedMenu ?: menuKeys.first()
                            it.copy(
                                type = FeedBrowseModel.ChannelsWithMenus.Type.ChannelRecommendation,
                                menus = menus + (menu to ItemListState.HasContent(response.channels))
                            )
                        }
                    }
                }
            }
        } catch (e: IllegalStateException) { this }
    }

    private suspend fun FeedBrowseModel.InspirationBanner.getAndUpdateData() {
        val response = repository.getWidgetRecommendation(identifier)
        if (response !is WidgetRecommendationModel.Banners) return
        updateWidget<FeedBrowseModel.InspirationBanner>(slotId, ResultState.Success) {
            it.copy(bannerList = response.banners)
        }
    }

    private suspend inline fun <reified T : FeedBrowseModel> updateWidget(
        slotId: String,
        state: ResultState,
        onUpdate: (T) -> FeedBrowseModel
    ) = updateMutex.withLock {
        _widgets.update {
            val widget = it[slotId] ?: return@update it
            if (widget.model !is T) return@update it
            it + (slotId to FeedBrowseUiModel2(state, onUpdate(widget.model)))
        }
    }
}
