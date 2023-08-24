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
    var mGroupPosition = 0
    var mDetailMaxInGroup = 0
    var mDetailPosition = 0

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
            Timber.d("fail $exception")
        }
    }

    private fun handleGroupMainData(selectedGroup: Int) {
        viewModelScope.launch {
            selectGroup(selectedGroup)
            setGroupData(selectedGroup)
        }
    }

    private fun handleNext() {
        val newDetailPosition = mDetailPosition.plus(1)
        val newGroupPosition = mGroupPosition.plus(1)

        if (newDetailPosition < mDetailMaxInGroup) {
            selectDetail(position = newDetailPosition)
        } else {
            selectGroup(newGroupPosition)
        }
    }

    private fun handlePrevious() {
        if (mDetailPosition > 0) {
            updateStoryDetailData(detailPosition = mDetailPosition - 1)
        } else {
            if (mGroupPosition > 0) selectGroup(mGroupPosition - 1)
            else updateStoryDetailData(detailPosition = mDetailPosition)
        }
    }

    private fun handleOnPauseStory() {
        updateStoryDetailData(event = StoryDetailItemUiEvent.PAUSE)
    }

    private fun handleOnResumeStory() {
        updateStoryDetailData(event = StoryDetailItemUiEvent.START)
    }

    private fun selectGroup(position: Int) {
        viewModelScope.launch {
            _uiEvent.emit(StoryUiEvent.SelectGroup(position))
        }
    }

    private fun setGroupData(groupPosition: Int) {
        mGroupPosition = groupPosition
        updateStoryGroupSelectedIndicator(groupPosition)

        val selectedDetail = _storyGroup.value.groupItems[groupPosition].detail.selectedDetail
        mDetailMaxInGroup = _storyGroup.value.groupItems[groupPosition].detail.detailItems.size
        updateStoryDetailData(
            groupPosition = groupPosition,
            detailPosition = selectedDetail,
        )
    }

    private fun selectDetail(position: Int) {
        updateStoryDetailData(detailPosition = position)
    }

    private fun updateStoryGroupSelectedIndicator(position: Int) {
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
        event: StoryDetailItemUiEvent = StoryDetailItemUiEvent.START,
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
