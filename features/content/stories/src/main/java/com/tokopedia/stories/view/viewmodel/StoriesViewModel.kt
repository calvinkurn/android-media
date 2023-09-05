package com.tokopedia.stories.view.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.stories.data.repository.StoriesRepository
import com.tokopedia.stories.domain.model.StoriesAuthorType
import com.tokopedia.stories.domain.model.StoriesRequestModel
import com.tokopedia.stories.domain.model.StoriesSource
import com.tokopedia.stories.utils.getRandomNumber
import com.tokopedia.stories.view.model.StoriesDetailItemUiModel.StoriesDetailItemUiEvent
import com.tokopedia.stories.view.model.StoriesDetailItemUiModel.StoriesDetailItemUiEvent.PAUSE
import com.tokopedia.stories.view.model.StoriesDetailItemUiModel.StoriesDetailItemUiEvent.RESUME
import com.tokopedia.stories.view.model.StoriesDetailUiModel
import com.tokopedia.stories.view.model.StoriesGroupItemUiModel
import com.tokopedia.stories.view.model.StoriesGroupUiModel
import com.tokopedia.stories.view.model.StoriesUiState
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction
import com.tokopedia.stories.view.viewmodel.event.StoriesUiEvent
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class StoriesViewModel @Inject constructor(
    private val repository: StoriesRepository,
) : ViewModel() {

    private var mShopId: String = ""

    private val _storiesGroup = MutableStateFlow(StoriesGroupUiModel())
    private val _storiesDetail = MutableStateFlow(StoriesDetailUiModel())

    private val mGroupPos = MutableStateFlow(-1)
    private val mDetailPos = MutableStateFlow(-1)
    private val mResetValue = MutableStateFlow(-1)

    val mGroupId: String
        get() {
            val currPosition = mGroupPos.value
            return if (currPosition < 0) ""
            else _storiesGroup.value.groupItems[currPosition].groupId
        }

    private val mGroupSize: Int
        get() = _storiesGroup.value.groupItems.size

    private val mGroupItem: StoriesGroupItemUiModel
        get() {
            val currPosition = mGroupPos.value
            return if (currPosition < 0) StoriesGroupItemUiModel()
            else _storiesGroup.value.groupItems[currPosition]
        }

    private val mDetailSize: Int
        get() {
            val currPosition = mGroupPos.value
            return _storiesGroup.value.groupItems[currPosition].detail.detailItems.size
        }

    private val _uiEvent = MutableSharedFlow<StoriesUiEvent>(extraBufferCapacity = 100)
    val uiEvent: Flow<StoriesUiEvent>
        get() = _uiEvent

    val uiState = combine(
        _storiesGroup,
        _storiesDetail,
    ) { storiesGroup, storiesDetail ->
        StoriesUiState(
            storiesGroup = storiesGroup,
            storiesDetail = storiesDetail,
        )
    }

    fun submitAction(action: StoriesUiAction) {
        when (action) {
            is StoriesUiAction.SetArgumentsData -> handleSetInitialData(action.data)
            is StoriesUiAction.SetGroupMainData -> handleGroupMainData(action.selectedGroup)
            is StoriesUiAction.SetGroup -> handleSetGroup(action.selectedGroup, action.showAnimation)
            StoriesUiAction.NextDetail -> handleNext()
            StoriesUiAction.PreviousDetail -> handlePrevious()
            StoriesUiAction.PauseStories -> handleOnPauseStories()
            StoriesUiAction.ResumeStories -> handleOnResumeStories()
            StoriesUiAction.ContentIsLoaded -> handleContentIsLoaded()
        }
    }

    private fun handleSetInitialData(data: Bundle?) {
        mShopId = data?.getString(SHOP_ID, "").orEmpty()

        viewModelScope.launchCatchError(block = {
            _storiesGroup.value = requestStoriesInitialData()
            mGroupPos.value = _storiesGroup.value.selectedGroupPosition
        }) { exception ->
            _uiEvent.emit(StoriesUiEvent.ErrorGroupPage(exception))
        }
    }

    private fun handleGroupMainData(selectedGroup: Int) {
        mGroupPos.update { selectedGroup }
        viewModelScope.launchCatchError(block = {
            setInitialDetailData()
            fetchAndCacheNextGroupDetail()
        }) { exception ->
            _uiEvent.emit(StoriesUiEvent.ErrorDetailPage(exception))
        }
    }

    private fun handleSetGroup(position: Int, showAnimation: Boolean) {
        viewModelScope.launch {
            _uiEvent.emit(StoriesUiEvent.SelectGroup(position, showAnimation))
        }
    }

    private fun handleNext() {
        val newGroupPosition = mGroupPos.value.plus(1)
        val newDetailPosition = mDetailPos.value.plus(1)

        when {
            newDetailPosition < mDetailSize -> updateDetailData(position = newDetailPosition)
            newGroupPosition < mGroupSize -> handleSetGroup(position = newGroupPosition, true)
            else -> viewModelScope.launch { _uiEvent.emit(StoriesUiEvent.FinishedAllStories) }
        }
    }

    private fun handlePrevious() {
        val newGroupPosition = mGroupPos.value.minus(1)
        val newDetailPosition = mDetailPos.value.minus(1)

        when {
            newDetailPosition > -1 -> updateDetailData(position = newDetailPosition)
            newGroupPosition > -1 -> handleSetGroup(position = newGroupPosition, true)
            else -> updateDetailData(isReset = true)
        }
    }

    private fun handleOnPauseStories() {
        updateDetailData(event = PAUSE, isSameContent = true)
    }

    private fun handleOnResumeStories() {
        updateDetailData(event = RESUME, isSameContent = true)
    }

    private fun handleContentIsLoaded() {
        updateDetailData(event = RESUME, isSameContent = true)
    }

    private suspend fun setInitialDetailData() {
        val isCached = mGroupItem.detail.detailItems.isNotEmpty()
        val detail = if (isCached) mGroupItem.detail
        else {
            val detailData = requestStoriesDetailData(mGroupId)
            updateGroupData(detail = detailData, groupPosition = mGroupPos.value)
            detailData
        }

        updateDetailData(position = detail.selectedDetailPositionCached, isReset = true)
    }

    private suspend fun fetchAndCacheNextGroupDetail() {
        val nextGroupPos = mGroupPos.value.plus(1)
        val isNextGroupExist = nextGroupPos < mGroupSize
        if (!isNextGroupExist) return

        val nextGroupId = _storiesGroup.value.groupItems[nextGroupPos].groupId

        viewModelScope.launchCatchError(block = {
            val nextGroupData = requestStoriesDetailData(nextGroupId)
            updateGroupData(detail = nextGroupData, groupPosition = nextGroupPos)
        }) { }
    }

    private fun updateGroupData(detail: StoriesDetailUiModel, groupPosition: Int) {
        _storiesGroup.update { group ->
            group.copy(
                selectedGroupId = mGroupId,
                selectedGroupPosition = mGroupPos.value,
                groupHeader = group.groupHeader.mapIndexed { index, storiesGroupHeader ->
                    storiesGroupHeader.copy(isSelected = index == mGroupPos.value)
                },
                groupItems = group.groupItems.mapIndexed { index, storiesGroupItemUiModel ->
                    if (index == groupPosition) {
                        storiesGroupItemUiModel.copy(
                            detail = detail.copy(
                                selectedGroupId = storiesGroupItemUiModel.groupId,
                            )
                        )
                    } else storiesGroupItemUiModel
                }
            )
        }
    }

    private fun updateDetailData(
        position: Int = mDetailPos.value,
        event: StoriesDetailItemUiEvent = PAUSE,
        isReset: Boolean = false,
        isSameContent: Boolean = false,
    ) {
        mDetailPos.value = position
        val positionCached = mGroupItem.detail.selectedDetailPositionCached
        val currentDetail = mGroupItem.detail.copy(
            selectedGroupId = mGroupId,
            selectedDetailPosition = position,
            selectedDetailPositionCached = if (positionCached <= position) position else positionCached,
            detailItems = mGroupItem.detail.detailItems.map { item ->
                item.copy(
                    event = event,
                    resetValue = if (isReset) {
                        mResetValue.value = mResetValue.value.getRandomNumber()
                        mResetValue.value
                    } else mResetValue.value,
                    isSameContent = isSameContent,
                )
            }
        )

        updateGroupData(detail = currentDetail, groupPosition = mGroupPos.value)
        _storiesDetail.update { currentDetail }
    }

    private suspend fun requestStoriesInitialData(): StoriesGroupUiModel {
        val request = StoriesRequestModel(
            authorID = mShopId,
            authorType = StoriesAuthorType.SHOP.value,
            source = StoriesSource.SHOP_ENTRY_POINT.value,
            sourceID = "",
        )
        return repository.getStoriesInitialData(request)
    }

    private suspend fun requestStoriesDetailData(sourceId: String): StoriesDetailUiModel {
        val request = StoriesRequestModel(
            authorID = mShopId,
            authorType = StoriesAuthorType.SHOP.value,
            source = StoriesSource.STORY_GROUP.value,
            sourceID = sourceId,
        )
        return repository.getStoriesDetailData(request)
    }

    companion object {
        private const val SHOP_ID = "shop_id"
    }

}
