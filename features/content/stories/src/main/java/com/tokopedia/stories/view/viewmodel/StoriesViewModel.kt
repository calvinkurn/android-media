package com.tokopedia.stories.view.viewmodel

import android.annotation.SuppressLint
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
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
    private val _groupPos = MutableStateFlow(-1)
    private val _detailPos = MutableStateFlow(-1)
    private val _resetValue = MutableStateFlow(-1)

    val mGroupId: String
        get() {
            val currPosition = _groupPos.value
            return if (currPosition < 0) ""
            else _storiesGroup.value.groupItems[currPosition].groupId
        }

    private val mGroupSize: Int
        get() = _storiesGroup.value.groupItems.size

    private val mGroupItem: StoriesGroupItemUiModel
        get() {
            val currPosition = _groupPos.value
            return if (currPosition < 0) StoriesGroupItemUiModel()
            else _storiesGroup.value.groupItems[currPosition]
        }

    private val mDetailSize: Int
        get() {
            val currPosition = _groupPos.value
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
            is StoriesUiAction.SetArgumentsData -> handleSetInitialData(action.bundle)
            is StoriesUiAction.SaveInstanceStateData -> handleSaveInstanceStateData(action.bundle)
            is StoriesUiAction.GetSavedInstanceStateData -> handleGetSavedInstanceStateData(action.bundle)
            is StoriesUiAction.SetGroupMainData -> handleGroupMainData(action.selectedGroup)
            is StoriesUiAction.SetGroup -> handleSetGroup(action.selectedGroup, action.showAnimation)
            StoriesUiAction.NextDetail -> handleNext()
            StoriesUiAction.PreviousDetail -> handlePrevious()
            StoriesUiAction.PauseStories -> handleOnPauseStories()
            StoriesUiAction.ResumeStories -> handleOnResumeStories()
            StoriesUiAction.ContentIsLoaded -> handleContentIsLoaded()
        }
    }

    private fun handleSetInitialData(bundle: Bundle?) {
        if (bundle == null) return

        mShopId = bundle.getString(SHOP_ID, "").orEmpty()
        launchRequestInitialData()
    }

    private fun handleSaveInstanceStateData(bundle: Bundle?) {
        if (bundle == null) return

        bundle.apply {
            putString(SAVED_INSTANCE_STORIES_SHOP_ID, mShopId)
            putParcelable(SAVED_INSTANCE_STORIES_GROUP_DATA, _storiesGroup.value)
            putInt(SAVED_INSTANCE_STORIES_GROUP_POSITION, _groupPos.value)
            putInt(SAVED_INSTANCE_STORIES_DETAIL_POSITION, _detailPos.value)
        }
    }

    @SuppressLint("DeprecatedMethod")
    private fun handleGetSavedInstanceStateData(bundle: Bundle?) {
        if (bundle == null) return

        val shopId = bundle.getString(SAVED_INSTANCE_STORIES_SHOP_ID, "")
        val groupData = if (SDK_INT >= TIRAMISU) {
            bundle.getParcelable(SAVED_INSTANCE_STORIES_GROUP_DATA, StoriesGroupUiModel::class.java)
        } else bundle.getParcelable(SAVED_INSTANCE_STORIES_GROUP_DATA)
        val groupPosition = bundle.getInt(SAVED_INSTANCE_STORIES_GROUP_POSITION, 0)
        val detailPosition = bundle.getInt(SAVED_INSTANCE_STORIES_DETAIL_POSITION, 0)

        if (groupData == null) {
            mShopId = shopId
            launchRequestInitialData()
        } else {
            mShopId = shopId
            _storiesGroup.value = groupData
            _groupPos.value = groupPosition
            _detailPos.value = detailPosition
        }
    }

    private fun handleGroupMainData(selectedGroup: Int) {
        _groupPos.update { selectedGroup }
        viewModelScope.launchCatchError(block = {
            setInitialData()
            setCachingData()
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
        val newGroupPosition = _groupPos.value.plus(1)
        val newDetailPosition = _detailPos.value.plus(1)

        when {
            newDetailPosition < mDetailSize -> updateDetailData(position = newDetailPosition)
            newGroupPosition < mGroupSize -> handleSetGroup(position = newGroupPosition, true)
            else -> viewModelScope.launch { _uiEvent.emit(StoriesUiEvent.FinishedAllStories) }
        }
    }

    private fun handlePrevious() {
        val newGroupPosition = _groupPos.value.minus(1)
        val newDetailPosition = _detailPos.value.minus(1)

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

    private suspend fun setInitialData() {
        val isCached = mGroupItem.detail.detailItems.isNotEmpty()
        val detail = if (isCached) mGroupItem.detail
        else {
            val detailData = requestStoriesDetailData(mGroupId)
            updateGroupData(detail = detailData, groupPosition = _groupPos.value)
            detailData
        }

        updateDetailData(position = detail.selectedDetailPositionCached, isReset = true)
    }

    private fun setCachingData() {
        viewModelScope.launchCatchError(block = {
            val prevRequest = asyncCatchError(block = {
                fetchAndCachePreviousGroupDetail()
            }) { it }
            val nextRequest = asyncCatchError(block = {
                fetchAndCacheNextGroupDetail()
            }) { it }
            prevRequest.await()
            nextRequest.await()
        }) { }
    }

    private suspend fun fetchAndCachePreviousGroupDetail() {
        val prevGroupPos = _groupPos.value.minus(1)
        val prevGroupItem = _storiesGroup.value.groupItems.getOrNull(prevGroupPos) ?: return
        val isNotFirstGroup = prevGroupPos > -1
        val isPrevGroupCached = prevGroupItem.detail.detailItems.isNotEmpty()
        if (isNotFirstGroup && isPrevGroupCached) return

        val prevGroupId = prevGroupItem.groupId

        try {
            val prevGroupData = requestStoriesDetailData(prevGroupId)
            updateGroupData(detail = prevGroupData, groupPosition = prevGroupPos)
        } catch (_: Throwable) { }
    }

    private suspend fun fetchAndCacheNextGroupDetail() {
        val nextGroupPos = _groupPos.value.plus(1)
        val nextGroupItem = _storiesGroup.value.groupItems.getOrNull(nextGroupPos) ?: return
        val isNotLastGroup = nextGroupPos < mGroupSize
        val isNextGroupCached = nextGroupItem.detail.detailItems.isNotEmpty()
        if (isNotLastGroup && isNextGroupCached) return

        val nextGroupId = nextGroupItem.groupId

        try {
            val nextGroupData = requestStoriesDetailData(nextGroupId)
            updateGroupData(detail = nextGroupData, groupPosition = nextGroupPos)
        } catch (_: Throwable) { }
    }

    private fun updateGroupData(detail: StoriesDetailUiModel, groupPosition: Int) {
        val sameDetail = _storiesGroup.value.groupItems[groupPosition].detail == detail
        if (sameDetail) return

        _storiesGroup.update { group ->
            group.copy(
                selectedGroupId = mGroupId,
                selectedGroupPosition = _groupPos.value,
                groupHeader = group.groupHeader.mapIndexed { index, storiesGroupHeader ->
                    storiesGroupHeader.copy(isSelected = index == _groupPos.value)
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
        position: Int = _detailPos.value,
        event: StoriesDetailItemUiEvent = PAUSE,
        isReset: Boolean = false,
        isSameContent: Boolean = false,
    ) {
        if (position == -1) return
        _detailPos.value = position
        val positionCached = mGroupItem.detail.selectedDetailPositionCached
        val currentDetail = mGroupItem.detail.copy(
            selectedGroupId = mGroupId,
            selectedDetailPosition = position,
            selectedDetailPositionCached = if (positionCached <= position) position else positionCached,
            detailItems = mGroupItem.detail.detailItems.map { item ->
                item.copy(
                    event = event,
                    resetValue = if (isReset) {
                        _resetValue.value = _resetValue.value.getRandomNumber()
                        _resetValue.value
                    } else _resetValue.value,
                    isSameContent = isSameContent,
                )
            }
        )

        updateGroupData(detail = currentDetail, groupPosition = _groupPos.value)
        _storiesDetail.update { currentDetail }
    }

    private fun launchRequestInitialData() {
        viewModelScope.launchCatchError(block = {
            _storiesGroup.value = requestStoriesInitialData()
            _groupPos.value = _storiesGroup.value.selectedGroupPosition
        }) { exception ->
            _uiEvent.emit(StoriesUiEvent.ErrorGroupPage(exception))
        }
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
        private const val SAVED_INSTANCE_STORIES_SHOP_ID = "stories_shop_id"
        private const val SAVED_INSTANCE_STORIES_GROUP_DATA = "stories_group_data"
        private const val SAVED_INSTANCE_STORIES_GROUP_POSITION = "stories_group_position"
        private const val SAVED_INSTANCE_STORIES_DETAIL_POSITION = "stories_detail_position"
    }

}
