package com.tokopedia.stories.view.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.stories.data.repository.StoryRepository
import com.tokopedia.stories.domain.model.StoryAuthorType
import com.tokopedia.stories.domain.model.StoryRequestModel
import com.tokopedia.stories.domain.model.StorySource
import com.tokopedia.stories.utils.getRandomNumber
import com.tokopedia.stories.view.model.StoryDetailItemUiModel.StoryDetailItemUiEvent
import com.tokopedia.stories.view.model.StoryDetailItemUiModel.StoryDetailItemUiEvent.PAUSE
import com.tokopedia.stories.view.model.StoryDetailItemUiModel.StoryDetailItemUiEvent.RESUME
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

    val mGroupId: String
        get() {
            val currPosition = mGroupPosition.value
            return if (currPosition < 0) ""
            else _storyGroup.value.groupItems[currPosition].id
        }
    private var shopId: String = ""

    private val _storyGroup = MutableStateFlow(StoryGroupUiModel())
    private val _storyDetail = MutableStateFlow(StoryDetailUiModel())

    private val mGroupPosition = MutableStateFlow(-1)
    private val mDetailPosition = MutableStateFlow(-1)
    private val mResetValue = MutableStateFlow(-1)

    private val mGroupSize: Int
        get() = _storyGroup.value.groupItems.size
    private val mGroupItem: StoryGroupItemUiModel
        get() {
            val currPosition = mGroupPosition.value
            return if (currPosition < 0) StoryGroupItemUiModel()
            else _storyGroup.value.groupItems[currPosition]
        }
    private val mDetailSize: Int
        get() {
            val currPosition = mGroupPosition.value
            return _storyGroup.value.groupItems[currPosition].detail.detailItems.size
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
            mGroupPosition.value = _storyGroup.value.selectedPosition
        }) { exception ->
            Timber.d("fail fetch main data $exception")
        }
    }

    private fun handleGroupMainData(selectedGroup: Int) {
        mGroupPosition.update { selectedGroup }
        setInitialDetailData()
    }

    private fun handleNext() {
        val newGroupPosition = mGroupPosition.value.plus(1)
        val newDetailPosition = mDetailPosition.value.plus(1)

        when {
            newDetailPosition < mDetailSize -> updateStoryDetailData(position = newDetailPosition)
            newGroupPosition < mGroupSize -> handleSetGroup(position = newGroupPosition)
            else -> viewModelScope.launch { _uiEvent.emit(StoryUiEvent.FinishedAllStory) }
        }
    }

    private fun handlePrevious() {
        val newGroupPosition = mGroupPosition.value.minus(1)
        val newDetailPosition = mDetailPosition.value.minus(1)

        when {
            newDetailPosition > -1 -> updateStoryDetailData(position = newDetailPosition)
            newGroupPosition > -1 -> handleSetGroup(position = newGroupPosition)
            else -> updateStoryDetailData(event = RESUME, isReset = true)
        }
    }

    private fun handleOnPauseStory() {
        updateStoryDetailData(event = PAUSE)
    }

    private fun handleOnResumeStory() {
        updateStoryDetailData(event = RESUME)
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
            else requestStoryDetailData()

            updateGroupData(detail = detailData)

            val isReset = detailData.selectedPositionCached == detailData.detailItems.size.minus(1)
            updateStoryDetailData(position = detailData.selectedPositionCached, isReset = isReset)
        }) { exception ->
            Timber.d("fail fetch new detail $exception")
        }
    }

    private fun updateGroupData(detail: StoryDetailUiModel) {
        val newData = _storyGroup.value.copy(
            selectedPosition = mGroupPosition.value,
            groupHeader = _storyGroup.value.groupHeader.mapIndexed { index, storyGroupHeader ->
                storyGroupHeader.copy(isSelected = index == mGroupPosition.value)
            },
            groupItems = _storyGroup.value.groupItems.mapIndexed { index, storyGroupItemUiModel ->
                if (index == mGroupPosition.value) storyGroupItemUiModel.copy(detail = detail)
                else storyGroupItemUiModel
            }
        )
        _storyGroup.value = newData
    }

    private fun updateStoryDetailData(
        position: Int = mDetailPosition.value,
        event: StoryDetailItemUiEvent = PAUSE,
        isReset: Boolean = false,
    ) {
        mDetailPosition.value = position
        val positionCached = mGroupItem.detail.selectedPositionCached
        val currentDetail = mGroupItem.detail.copy(
            selectedPosition = position,
            selectedPositionCached = if (positionCached <= position) position else positionCached,
            detailItems = mGroupItem.detail.detailItems.mapIndexed { _, item ->
                item.copy(
                    event = event,
                    resetValue = if (isReset) {
                        mResetValue.value = mResetValue.value.getRandomNumber()
                        mResetValue.value
                    } else mResetValue.value,
                )
            }
        )

        _storyDetail.value = currentDetail
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
