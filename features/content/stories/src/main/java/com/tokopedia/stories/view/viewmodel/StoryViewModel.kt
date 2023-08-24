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
    private var mGroupPosition = 0
    private var mDetailMaxInGroup = 0
    private var mDetailPosition = 0

    private val _storyGroup = MutableStateFlow(StoryGroupUiModel())
    private val _storyDetail = MutableStateFlow(StoryDetailUiModel())

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
        viewModelScope.launchCatchError(block = {
            shopId = data?.getString(SHOP_ID, "").orEmpty()
            val request = StoryRequestModel(
                authorID = shopId,
                authorType = StoryAuthorType.SHOP.value,
                source = StorySource.SHOP_ENTRY_POINT.value,
                sourceID = "",
            )
            val response = repository.getInitialStoryData(request)
            _storyGroup.value = response
        }) { exception ->
            Timber.d("fail fetch main data $exception")
        }
    }

    private fun handleGroupMainData(selectedGroup: Int) {
        mGroupPosition = selectedGroup
        updateGroupSelectedIndicator(selectedGroup)
        setDetailData(selectedGroup)
    }

    private fun handleNext() {
        val newDetailPosition = mDetailPosition.plus(1)
        val newGroupPosition = mGroupPosition.plus(1)

        if (newDetailPosition < mDetailMaxInGroup) {
            selectDetail(position = newDetailPosition)
        } else {
            handleSetGroup(newGroupPosition)
        }
    }

    private fun handlePrevious() {
        if (mDetailPosition > 0) {
            updateStoryDetailData(detailPosition = mDetailPosition - 1)
        } else {
            if (mGroupPosition > 0) handleSetGroup(mGroupPosition - 1)
            else updateStoryDetailData(detailPosition = mDetailPosition)
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

    private fun setDetailData(groupPosition: Int) {
        viewModelScope.launchCatchError(block = {
        val isNeedFetch = _storyGroup.value.groupItems[groupPosition].detail == StoryDetailUiModel()

        val detailData = if (isNeedFetch) {
            val request = StoryRequestModel(
                authorID = shopId,
                authorType = "shop",
                source = "",
                sourceID = _storyGroup.value.groupItems[groupPosition].id,
            )
            val newDetail = repository.getStoryDetailData(request)
            updateGroupData(
                groupPosition = groupPosition,
                detail = newDetail,
            )
            newDetail
        }
        else _storyGroup.value.groupItems[groupPosition].detail

        val selectedDetail = detailData.selectedDetail
        mDetailMaxInGroup = detailData.detailItems.size
        updateStoryDetailData(
            groupPosition = groupPosition,
            detailPosition = selectedDetail,
        )
        }) { exception ->
            Timber.d("fail fetch new detail $exception")
        }
    }

    private fun selectDetail(position: Int) {
        updateStoryDetailData(detailPosition = position)
    }

    private fun updateGroupData(
        groupPosition: Int,
        detail: StoryDetailUiModel,
    ) {
        _storyGroup.update { data ->
            data.copy(
                groupItems = data.groupItems.mapIndexed { index, storyGroupItemUiModel ->
                    if (index == groupPosition) storyGroupItemUiModel.copy(detail = detail)
                    else storyGroupItemUiModel
                }
            )
        }
    }

    private fun updateGroupSelectedIndicator(position: Int) {
        _storyGroup.update { group ->
            group.copy(
                selectedGroup = position,
                groupItems = group.groupItems.mapIndexed { index, storyGroupItemUiModel ->
                    storyGroupItemUiModel.copy(
                        isSelected = index == position,
                    )
                }
            )
        }
    }

    private fun updateStoryDetailData(
        groupPosition: Int = mGroupPosition,
        detailPosition: Int = mDetailPosition,
        event: StoryDetailItemUiEvent = StoryDetailItemUiEvent.PAUSE,
    ) {
        mDetailPosition = detailPosition
        _storyDetail.update {
            val currentDetail = _storyGroup.value.groupItems[groupPosition].detail
            currentDetail.copy(
                selectedDetail = detailPosition,
                detailItems = currentDetail.detailItems.mapIndexed { index, storyDetailItemUiModel ->
                    storyDetailItemUiModel.copy(
                        event = event,
                        isSelected = index == detailPosition,
                    )
                }
            )
        }
    }

    companion object {
        private const val SHOP_ID = "shop_id"
    }

}
