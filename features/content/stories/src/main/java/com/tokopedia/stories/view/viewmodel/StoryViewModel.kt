package com.tokopedia.stories.view.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.stories.data.repository.StoryRepository
import com.tokopedia.stories.domain.model.StoryAuthorType
import com.tokopedia.stories.domain.model.StoryRequestModel
import com.tokopedia.stories.domain.model.StorySource
import com.tokopedia.stories.view.model.StoryDetailItemUiModel.StoryDetailItemUiEvent
import com.tokopedia.stories.view.model.StoryDetailUiModel
import com.tokopedia.stories.view.model.StoryGroupItemUiModel
import com.tokopedia.stories.view.model.StoryGroupUiModel
import com.tokopedia.stories.view.model.StoryUiState
import com.tokopedia.stories.view.viewmodel.action.StoryUiAction
import com.tokopedia.stories.view.viewmodel.event.StoryUiEvent
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class StoryViewModel @Inject constructor(
    private val repository: StoryRepository,
) : ViewModel() {

    private var shopId: String = ""

    private val _storyGroup = MutableStateFlow(StoryGroupUiModel())
    private val _storyDetail = MutableStateFlow(StoryDetailUiModel())

    private val mGroupPosition = MutableStateFlow(-1)
    private val mDetailPosition = MutableStateFlow(-1)

    private val mGroupItem: StoryGroupItemUiModel
        get() {
            val currPosition = mGroupPosition.value
            return if (currPosition < 0) StoryGroupItemUiModel()
            else _storyGroup.value.groupItems[currPosition]
        }

    private val _uiEvent = MutableSharedFlow<StoryUiEvent>(extraBufferCapacity = 100)
    val uiEvent: Flow<StoryUiEvent>
        get() = _uiEvent

    val uiState = combine(
        _storyGroup,
        _storyDetail,
    ) { storyGroup, storyDetail ->
        StoryUiState(
            storyGroup = storyGroup,
            storyDetail = storyDetail,
        )
    }

    fun submitAction(action: StoryUiAction) {
        when (action) {
            is StoryUiAction.SetArgumentsData -> handleSetInitialData(action.data)
            is StoryUiAction.SetGroupMainData -> handleGroupMainData(action.selectedGroup)
            is StoryUiAction.SetGroup -> handleSetGroup(action.selectedGroup)
            StoryUiAction.NextDetail -> handleNext()
            StoryUiAction.PreviousDetail -> handlePrevious()
            StoryUiAction.PauseStory -> handleOnPauseStory()
            StoryUiAction.ResumeStory -> handleOnResumeStory()
        }
    }

    private fun handleSetInitialData(data: Bundle?) {
        shopId = data?.getString(SHOP_ID, "").orEmpty()

        viewModelScope.launchCatchError(block = {
            _storyGroup.value = requestStoryInitialData()
        }) { exception ->
            Timber.d("fail fetch main data $exception")
        }
    }

    private fun handleGroupMainData(selectedGroup: Int) {
        mGroupPosition.value = selectedGroup
        updateGroupSelectedIndicator()
        setInitialDetailData()
    }

    private fun handleNext() {
        val newGroupPosition = mGroupPosition.value.plus(1)
        val newDetailPosition = mDetailPosition.value.plus(1)

        if (newDetailPosition < mGroupItem.detail.detailItems.size) {
            updateStoryDetailData(position = newDetailPosition)
        } else {
            handleSetGroup(newGroupPosition)
        }
    }

    private fun handlePrevious() {
        if (mDetailPosition.value > 0) {
            updateStoryDetailData(position = mDetailPosition.value.minus(1))
        } else {
            if (mGroupPosition.value > 0) handleSetGroup(mGroupPosition.value.minus(1))
            else updateStoryDetailData(position = mDetailPosition.value)
        }
    }

    private fun handleOnPauseStory() {
        updateStoryDetailData(event = StoryDetailItemUiEvent.PAUSE)
    }

    private fun handleOnResumeStory() {
        updateStoryDetailData(event = StoryDetailItemUiEvent.START)
    }

    private fun handleSetGroup(position: Int) {
        viewModelScope.launch {
            _uiEvent.emit(StoryUiEvent.SelectGroup(position))
        }
    }

    private fun setInitialDetailData() {
        viewModelScope.launchCatchError(block = {
            val isCached = mGroupItem.detail != StoryDetailUiModel()

            val detailData = if (isCached) mGroupItem.detail
            else {
                val newDetail = requestStoryDetailData()
                updateGroupData(detail = newDetail)
                newDetail
            }

            updateStoryDetailData(position = detailData.selectedPositionCached)
        }) { exception ->
            Timber.d("fail fetch new detail $exception")
        }
    }

    private fun updateGroupData(detail: StoryDetailUiModel) {
        _storyGroup.update { group ->
            group.copy(
                groupItems = group.groupItems.mapIndexed { index, storyGroupItemUiModel ->
                    if (index == mGroupPosition.value) storyGroupItemUiModel.copy(detail = detail)
                    else storyGroupItemUiModel
                }
            )
        }
    }

    private fun updateGroupSelectedIndicator() {
        _storyGroup.update { group ->
            group.copy(
                selectedPosition = mGroupPosition.value,
                groupItems = group.groupItems.mapIndexed { index, item ->
                    item.copy(isSelected = index == mGroupPosition.value,)
                }
            )
        }
    }

    private fun updateStoryDetailData(
        position: Int = mDetailPosition.value,
        event: StoryDetailItemUiEvent = StoryDetailItemUiEvent.PAUSE,
    ) {
        mDetailPosition.value = position
        val positionCached = mGroupItem.detail.selectedPositionCached
        val currentDetail = mGroupItem.detail.copy(
            selectedPosition = position,
            selectedPositionCached = if (positionCached <= position) position else positionCached,
            detailItems = mGroupItem.detail.detailItems.mapIndexed { index, item ->
                item.copy(
                    event = event,
                    isSelected = index == position,
                )
            }
        )

        _storyDetail.update { currentDetail }
        updateGroupData(detail = currentDetail)
    }

    private suspend fun requestStoryInitialData(): StoryGroupUiModel {
        val request = StoryRequestModel(
            authorID = shopId,
            authorType = StoryAuthorType.SHOP.value,
            source = StorySource.SHOP_ENTRY_POINT.value,
            sourceID = "",
        )
        return repository.getStoryInitialData(request)
    }

    private suspend fun requestStoryDetailData(): StoryDetailUiModel {
        val request = StoryRequestModel(
            authorID = shopId,
            authorType = StoryAuthorType.SHOP.value,
            source = "",
            sourceID = mGroupItem.id,
        )
        return repository.getStoryDetailData(request)
    }

    companion object {
        private const val SHOP_ID = "shop_id"
    }

}
