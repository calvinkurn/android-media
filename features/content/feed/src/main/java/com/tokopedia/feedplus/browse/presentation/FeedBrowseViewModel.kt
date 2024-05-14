package com.tokopedia.feedplus.browse.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.content.common.types.ResultState
import com.tokopedia.feedplus.browse.data.FeedBrowseRepository
import com.tokopedia.feedplus.browse.data.model.ContentSlotModel
import com.tokopedia.feedplus.browse.data.model.FeedBrowseSlotUiModel
import com.tokopedia.feedplus.browse.data.model.HeaderDetailModel
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.data.model.WidgetRecommendationModel
import com.tokopedia.feedplus.browse.data.model.WidgetRequestModel
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseAction
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseChannelListState
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseStatefulModel
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
private typealias FeedBrowseModelMap = Map<String, FeedBrowseStatefulModel>
internal class FeedBrowseViewModel @Inject constructor(
    private val repository: FeedBrowseRepository
) : ViewModel() {

    private val _headerDetail = MutableStateFlow(HeaderDetailModel.DEFAULT)
    private val _slots = MutableStateFlow<ResultState>(ResultState.Loading)
    private val _widgets = MutableStateFlow<FeedBrowseModelMap>(emptyMap())

    private val updateMutex = Mutex()

    val uiState: StateFlow<FeedBrowseUiState> = combine(
        _headerDetail,
        _slots,
        _widgets
    ) { headerDetail, slots, widgets ->
        if (slots is ResultState.Fail) {
            FeedBrowseUiState.Error(
                slots.error
            )
        } else {
            FeedBrowseUiState.Success(
                headerDetail = headerDetail,
                widgets = widgets.values.toList()
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FeedBrowseUiState.Placeholder
    )

    fun onAction(action: FeedBrowseAction) {
        when (action) {
            FeedBrowseAction.LoadInitialPage -> handleInitialPage()
            is FeedBrowseAction.FetchCardsWidget -> {
                handleFetchWidget(action.slotId)
            }
            is FeedBrowseAction.SelectChipWidget -> {
                handleSelectChip(action.model, action.slotId)
            }
            FeedBrowseAction.UpdateStoriesStatus -> {
                handleUpdateStoriesStatus()
            }
        }
    }

    fun getHeaderDetail(): HeaderDetailModel {
        return _headerDetail.value
    }

    private fun handleInitialPage() {
        handleFetchHeaderDetail()
        handleFetchSlots()
    }

    private fun handleFetchHeaderDetail() {
        viewModelScope.launch {
            val data = repository.getHeaderDetail()
            data?.let {
                _headerDetail.value = it
            }
        }
    }

    private fun handleFetchSlots() {
        viewModelScope.launch {
            try {
                val slots = repository.getSlots()
                _widgets.value = slots.associate {
                    it.slotId to FeedBrowseStatefulModel(ResultState.Loading, it)
                }
                _slots.value = ResultState.Success

                slots.forEach { slot -> launch { getAndUpdateData(slot) } }
            } catch (err: Throwable) {
                _slots.value = ResultState.Fail(err)
            }
        }
    }

    private fun handleFetchWidget(slotId: String) {
        viewModelScope.launch {
            val widgets = _widgets.value
            val widget = widgets[slotId]?.model ?: return@launch
            getAndUpdateData(widget)
        }
    }

    private fun handleSelectChip(chip: WidgetMenuModel, slotId: String) {
        viewModelScope.launch {
            updateWidget<FeedBrowseSlotUiModel.ChannelsWithMenus>(slotId, ResultState.Success) {
                it.copy(
                    selectedMenuId = chip.id,
                    menus = it.menus + (chip to FeedBrowseChannelListState.initLoading())
                )
            }
        }
    }

    private fun handleUpdateStoriesStatus() {
        viewModelScope.launch {
            val storiesSlots = getSlotsByInstance<FeedBrowseSlotUiModel.StoryGroups>()
            storiesSlots.forEach { slot ->
                if (slot.model !is FeedBrowseSlotUiModel.StoryGroups) return@forEach
                val newStories = slot.model.storyList.map { story ->
                    val hasSeenAllStories = repository.getUpdatedSeenStoriesStatus(
                        story.id,
                        !story.hasUnseenStory,
                        story.lastUpdatedAt
                    )
                    story.copy(
                        hasUnseenStory = if (hasSeenAllStories) false else story.hasUnseenStory,
                        lastUpdatedAt = System.currentTimeMillis()
                    )
                }

                updateWidget<FeedBrowseSlotUiModel.StoryGroups>(
                    slot.model.slotId,
                    slot.result
                ) { it.copy(storyList = newStories) }
            }
        }
    }

    private suspend fun getAndUpdateData(model: FeedBrowseSlotUiModel) {
        when (model) {
            is FeedBrowseSlotUiModel.ChannelsWithMenus -> model.getAndUpdateData()
            is FeedBrowseSlotUiModel.InspirationBanner -> model.getAndUpdateData()
            is FeedBrowseSlotUiModel.Authors -> model.getAndUpdateData()
            is FeedBrowseSlotUiModel.StoryGroups -> model.getAndUpdateData()
        }
    }

    private suspend fun FeedBrowseSlotUiModel.ChannelsWithMenus.getAndUpdateData() {
        val menuKeys = menus.keys
        val selectedMenu = menuKeys.firstOrNull { it.id == selectedMenuId }

        val requestModel = if (menus.isEmpty()) {
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
                    updateWidget<FeedBrowseSlotUiModel.ChannelsWithMenus>(slotId, ResultState.Success) {
                        it.copy(
                            menus = response.menus.associateWith { FeedBrowseChannelListState.initLoading() },
                            selectedMenuId = response.menus.firstOrNull()?.id.orEmpty()
                        )
                    }
                }
                is ContentSlotModel.ChannelBlock -> {
                    if (menuKeys.isEmpty()) {
                        updateWidget<FeedBrowseSlotUiModel.ChannelsWithMenus>(slotId, ResultState.Success) {
                            it.copy(
                                menus = mapOf(
                                    WidgetMenuModel.Empty.copy(group = it.group) to FeedBrowseChannelListState.initSuccess(response.channels, config = response.config)
                                )
                            )
                        }
                    } else {
                        updateWidget<FeedBrowseSlotUiModel.ChannelsWithMenus>(slotId, ResultState.Success) {
                            val menu = selectedMenu ?: menuKeys.first()
                            it.copy(
                                menus = menus + (menu to FeedBrowseChannelListState.initSuccess(response.channels, config = response.config))
                            )
                        }
                    }
                }
                is ContentSlotModel.NoData -> {
                    val error = IllegalStateException("Empty list for request model: $requestModel")
                    if (menuKeys.isEmpty()) {
                        updateWidget<FeedBrowseSlotUiModel.ChannelsWithMenus>(slotId, ResultState.Fail(error))
                    } else {
                        updateWidget<FeedBrowseSlotUiModel.ChannelsWithMenus>(slotId, ResultState.Success) {
                            val menu = selectedMenu ?: menuKeys.first()
                            it.copy(
                                menus = menus + (menu to FeedBrowseChannelListState.initFail(error))
                            )
                        }
                    }
                }
            }
        } catch (e: Throwable) {
            updateWidget<FeedBrowseSlotUiModel.ChannelsWithMenus>(slotId, ResultState.Fail(e)) { it }
        }
    }

    private suspend fun FeedBrowseSlotUiModel.InspirationBanner.getAndUpdateData() {
        updateWidget<FeedBrowseSlotUiModel.InspirationBanner>(slotId, ResultState.Loading)
        try {
            val response = repository.getWidgetRecommendation(identifier)
            if (response !is WidgetRecommendationModel.Banners) error("Expected $identifier to return banner, but it's not")

            updateWidget<FeedBrowseSlotUiModel.InspirationBanner>(slotId, ResultState.Success) {
                it.copy(bannerList = response.banners)
            }
        } catch (err: Throwable) {
            updateWidget<FeedBrowseSlotUiModel.InspirationBanner>(slotId, ResultState.Fail(err)) {
                it.copy(bannerList = emptyList())
            }
        }
    }

    private suspend fun FeedBrowseSlotUiModel.Authors.getAndUpdateData() {
        updateWidget<FeedBrowseSlotUiModel.Authors>(slotId, ResultState.Loading)
        try {
            val mappedResult = repository.getWidgetRecommendation(identifier)
            if (mappedResult !is WidgetRecommendationModel.Authors) error("Expected $identifier to return author, but it's not")

            updateWidget<FeedBrowseSlotUiModel.Authors>(slotId, ResultState.Success) {
                it.copy(authorList = mappedResult.channels)
            }
        } catch (err: Throwable) {
            updateWidget<FeedBrowseSlotUiModel.Authors>(slotId, ResultState.Fail(err)) {
                it.copy(authorList = emptyList())
            }
        }
    }

    private suspend fun FeedBrowseSlotUiModel.StoryGroups.getAndUpdateData() {
        updateWidget<FeedBrowseSlotUiModel.StoryGroups>(slotId, ResultState.Loading)
        try {
            val mappedResult = repository.getStoryGroups(source, nextCursor)
            updateWidget<FeedBrowseSlotUiModel.StoryGroups>(slotId, ResultState.Success) {
                it.copy(
                    storyList = mappedResult.storyList,
                    nextCursor = mappedResult.nextCursor
                )
            }
        } catch (err: Throwable) {
            updateWidget<FeedBrowseSlotUiModel.StoryGroups>(slotId, ResultState.Fail(err))
        }
    }

    private suspend inline fun <reified T : FeedBrowseSlotUiModel> updateWidget(
        slotId: String,
        state: ResultState,
        onUpdate: (T) -> FeedBrowseSlotUiModel = { it }
    ) = updateMutex.withLock {
        _widgets.update {
            val widget = it[slotId] ?: return@update it
            if (widget.model !is T) return@update it
            it + (slotId to FeedBrowseStatefulModel(state, onUpdate(widget.model)))
        }
    }

    private inline fun <reified T : FeedBrowseSlotUiModel> getSlotsByInstance(): List<FeedBrowseStatefulModel> {
        val widgets = _widgets.value
        return widgets.values.filter { it.model is T }
    }
}
