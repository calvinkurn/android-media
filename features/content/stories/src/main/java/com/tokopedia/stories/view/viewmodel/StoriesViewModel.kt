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
import com.tokopedia.stories.domain.model.StoriesTrackActivityActionType
import com.tokopedia.stories.domain.model.StoriesTrackActivityRequestModel
import com.tokopedia.stories.view.model.StoriesDetail
import com.tokopedia.stories.view.model.StoriesDetailItem
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesDetailItemUiEvent
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesDetailItemUiEvent.PAUSE
import com.tokopedia.stories.view.model.StoriesDetailItem.StoriesDetailItemUiEvent.RESUME
import com.tokopedia.stories.view.model.StoriesGroupItem
import com.tokopedia.stories.view.model.StoriesUiModel
import com.tokopedia.stories.view.utils.getRandomNumber
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction
import com.tokopedia.stories.view.viewmodel.event.StoriesUiEvent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StoriesViewModel @AssistedInject constructor(
    @Assisted private val authorId: String,
    private val repository: StoriesRepository,
) : ViewModel() {

    @AssistedFactory
    interface Factory  {
        fun create(authorId: String) : StoriesViewModel
    }

    private val _storiesMainData = MutableStateFlow(StoriesUiModel())
    val storiesMainData: Flow<StoriesUiModel>
        get() = _storiesMainData

    private val _storiesEvent = MutableSharedFlow<StoriesUiEvent>(extraBufferCapacity = 100)
    val storiesEvent: Flow<StoriesUiEvent>
        get() = _storiesEvent

    private val _groupPos = MutableStateFlow(-1)
    private val _detailPos = MutableStateFlow(-1)
    private val _resetValue = MutableStateFlow(-1)

    val mGroup: StoriesGroupItem
        get() {
            val groupPosition = _groupPos.value
            return if (groupPosition < 0) StoriesGroupItem()
            else {
                if (_storiesMainData.value.groupItems.size <= groupPosition) StoriesGroupItem()
                else _storiesMainData.value.groupItems[groupPosition]
            }
        }

    val mDetail: StoriesDetailItem
        get() {
            val groupPosition = _groupPos.value
            val detailPosition = _detailPos.value
            return if (groupPosition < 0 || detailPosition < 0) StoriesDetailItem()
            else {
                when {
                    _storiesMainData.value.groupItems.size <= groupPosition -> StoriesDetailItem()
                    _storiesMainData.value.groupItems[groupPosition].detail.detailItems.size <= detailPosition -> StoriesDetailItem()
                    else -> _storiesMainData.value.groupItems[groupPosition].detail.detailItems[detailPosition]
                }
            }
        }

    private val mStoriesMainData: StoriesUiModel
        get() = _storiesMainData.value

    private val mGroupPos: Int
        get() = _groupPos.value

    private val mDetailPos: Int
        get() = _detailPos.value

    private val mGroupSize: Int
        get() = _storiesMainData.value.groupItems.size

    private val mGroupItem: StoriesGroupItem
        get() {
            val groupPosition = _groupPos.value
            return if (groupPosition < 0) StoriesGroupItem()
            else {
                if (_storiesMainData.value.groupItems.size <= groupPosition) StoriesGroupItem()
                else _storiesMainData.value.groupItems[groupPosition]
            }
        }

    private val mDetailSize: Int
        get() {
            val groupPosition = _groupPos.value
            return if (groupPosition < 0) 0
            else {
                if (_storiesMainData.value.groupItems.size <= groupPosition) 0
                else _storiesMainData.value.groupItems[groupPosition].detail.detailItems.size
            }
        }

    private val mResetValue: Int
        get() = _resetValue.value

    private var mLatestTrackerPosition = -1

    fun submitAction(action: StoriesUiAction) {
        when (action) {
            is StoriesUiAction.SetInitialData -> handleSetInitialData(action.bundle)
            is StoriesUiAction.SaveInstanceStateData -> handleSaveInstanceStateData(action.bundle)
            is StoriesUiAction.GetSavedInstanceStateData -> handleGetSavedInstanceStateData(action.bundle)
            is StoriesUiAction.SetMainData -> handleMainData(action.selectedGroup)
            is StoriesUiAction.SelectGroup -> handleSelectGroup(action.selectedGroup, action.showAnimation)
            StoriesUiAction.NextDetail -> handleNext()
            StoriesUiAction.PreviousDetail -> handlePrevious()
            StoriesUiAction.PauseStories -> handleOnPauseStories()
            StoriesUiAction.ResumeStories -> handleOnResumeStories()
            StoriesUiAction.ContentIsLoaded -> handleContentIsLoaded()
        }
    }

    private fun handleSetInitialData(bundle: Bundle?) {
        if (bundle == null) return

        launchRequestInitialData()
    }

    private fun handleSaveInstanceStateData(bundle: Bundle?) {
        if (bundle == null) return

        bundle.apply {
            putParcelable(SAVED_INSTANCE_STORIES_MAIN_DATA, mStoriesMainData)
            putInt(SAVED_INSTANCE_STORIES_GROUP_POSITION, mGroupPos)
            putInt(SAVED_INSTANCE_STORIES_DETAIL_POSITION, mDetailPos)
        }
    }

    @SuppressLint("DeprecatedMethod")
    private fun handleGetSavedInstanceStateData(bundle: Bundle?) {
        if (bundle == null) return

        val storiesMainData = if (SDK_INT >= TIRAMISU) {
            bundle.getParcelable(SAVED_INSTANCE_STORIES_MAIN_DATA, StoriesUiModel::class.java)
        } else bundle.getParcelable(SAVED_INSTANCE_STORIES_MAIN_DATA)
        val groupPosition = bundle.getInt(SAVED_INSTANCE_STORIES_GROUP_POSITION, 0)
        val detailPosition = bundle.getInt(SAVED_INSTANCE_STORIES_DETAIL_POSITION, 0)

        if (storiesMainData == null) launchRequestInitialData()
        else {
            _storiesMainData.value = storiesMainData
            _groupPos.value = groupPosition
            _detailPos.value = detailPosition
        }
    }

    private fun handleMainData(selectedGroup: Int) {
        mLatestTrackerPosition = -1
        _groupPos.update { selectedGroup }
        viewModelScope.launchCatchError(block = {
            setInitialData()
            setCachingData()
        }) { exception ->
            _storiesEvent.emit(StoriesUiEvent.ErrorDetailPage(exception))
        }
    }

    private fun handleSelectGroup(position: Int, showAnimation: Boolean) {
        viewModelScope.launch {
            _storiesEvent.emit(StoriesUiEvent.SelectGroup(position, showAnimation))
        }
    }

    private fun handleNext() {
        val newGroupPosition = mGroupPos.plus(1)
        val newDetailPosition = mDetailPos.plus(1)

        when {
            newDetailPosition < mDetailSize -> updateDetailData(position = newDetailPosition)
            newGroupPosition < mGroupSize -> handleSelectGroup(position = newGroupPosition, true)
            else -> viewModelScope.launch { _storiesEvent.emit(StoriesUiEvent.FinishedAllStories) }
        }
    }

    private fun handlePrevious() {
        val newGroupPosition = mGroupPos.minus(1)
        val newDetailPosition = mDetailPos.minus(1)

        when {
            newDetailPosition > -1 -> updateDetailData(position = newDetailPosition)
            newGroupPosition > -1 -> handleSelectGroup(position = newGroupPosition, true)
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
        checkAndHitTrackActivity()
    }

    private suspend fun setInitialData() {
        val isCached = mGroupItem.detail.detailItems.isNotEmpty()
        val detail = if (isCached) mGroupItem.detail
        else {
            val detailData = requestStoriesDetailData(mGroup.groupId)
            updateMainData(detail = detailData, groupPosition = mGroupPos)
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
        val prevGroupPos = mGroupPos.minus(1)
        val prevGroupItem = mStoriesMainData.groupItems.getOrNull(prevGroupPos) ?: return
        val isNotFirstGroup = prevGroupPos > -1
        val isPrevGroupCached = prevGroupItem.detail.detailItems.isNotEmpty()
        if (isNotFirstGroup && isPrevGroupCached) return

        val prevGroupId = prevGroupItem.groupId

        try {
            val prevGroupData = requestStoriesDetailData(prevGroupId)
            updateMainData(detail = prevGroupData, groupPosition = prevGroupPos)
        } catch (_: Throwable) { }
    }

    private suspend fun fetchAndCacheNextGroupDetail() {
        val nextGroupPos = mGroupPos.plus(1)
        val nextGroupItem = mStoriesMainData.groupItems.getOrNull(nextGroupPos) ?: return
        val isNotLastGroup = nextGroupPos < mGroupSize
        val isNextGroupCached = nextGroupItem.detail.detailItems.isNotEmpty()
        if (isNotLastGroup && isNextGroupCached) return

        val nextGroupId = nextGroupItem.groupId

        try {
            val nextGroupData = requestStoriesDetailData(nextGroupId)
            updateMainData(detail = nextGroupData, groupPosition = nextGroupPos)
        } catch (_: Throwable) { }
    }

    private fun updateMainData(detail: StoriesDetail, groupPosition: Int) {
        val sameDetail = mStoriesMainData.groupItems[groupPosition].detail == detail
        if (sameDetail) return

        _storiesMainData.update { group ->
            group.copy(
                selectedGroupId = mGroup.groupId,
                selectedGroupPosition = mGroupPos,
                groupHeader = group.groupHeader.mapIndexed { index, storiesGroupHeader ->
                    storiesGroupHeader.copy(isSelected = index == mGroupPos)
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
        position: Int = mDetailPos,
        event: StoriesDetailItemUiEvent = PAUSE,
        isReset: Boolean = false,
        isSameContent: Boolean = false,
    ) {
        if (position == -1) return
        _detailPos.update { position }
        val positionCached = mGroupItem.detail.selectedDetailPositionCached
        val currentDetail = mGroupItem.detail.copy(
            selectedGroupId = mGroup.groupId,
            selectedDetailPosition = position,
            selectedDetailPositionCached = if (positionCached <= position) position else positionCached,
            detailItems = mGroupItem.detail.detailItems.map { item ->
                item.copy(
                    event = event,
                    resetValue = if (isReset) {
                        _resetValue.update { it.getRandomNumber() }
                        mResetValue
                    } else mResetValue,
                    isSameContent = isSameContent,
                )
            }
        )

        updateMainData(detail = currentDetail, groupPosition = mGroupPos)
    }

    private fun launchRequestInitialData() {
        viewModelScope.launchCatchError(block = {
            _storiesMainData.value = requestStoriesInitialData()
            _groupPos.value = mStoriesMainData.selectedGroupPosition
        }) { exception ->
            _storiesEvent.emit(StoriesUiEvent.ErrorGroupPage(exception))
        }
    }

    private fun checkAndHitTrackActivity() {
        viewModelScope.launchCatchError(block = {
            val detailItem = mGroupItem.detail
            if (mDetailPos <= mLatestTrackerPosition) return@launchCatchError
            mLatestTrackerPosition = mDetailPos
            val trackerId = detailItem.detailItems[mLatestTrackerPosition].meta.activityTracker
            requestSetStoriesTrackActivity(trackerId)
        }) {}
    }

    private suspend fun requestStoriesInitialData(): StoriesUiModel {
        val request = StoriesRequestModel(
            authorID = authorId,
            authorType = StoriesAuthorType.SHOP.value,
            source = StoriesSource.SHOP_ENTRY_POINT.value,
            sourceID = "",
        )
        return repository.getStoriesInitialData(request)
    }

    private suspend fun requestStoriesDetailData(sourceId: String): StoriesDetail {
        val request = StoriesRequestModel(
            authorID = authorId,
            authorType = StoriesAuthorType.SHOP.value,
            source = StoriesSource.STORY_GROUP.value,
            sourceID = sourceId,
        )
        return repository.getStoriesDetailData(request)
    }

    private suspend fun requestSetStoriesTrackActivity(trackerId: String): Boolean {
        val request = StoriesTrackActivityRequestModel(
            id = trackerId,
            action = StoriesTrackActivityActionType.LAST_SEEN.value,
        )
        return repository.setStoriesTrackActivity(request)
    }

    companion object {
        private const val SAVED_INSTANCE_STORIES_MAIN_DATA = "stories_main_data"
        private const val SAVED_INSTANCE_STORIES_GROUP_POSITION = "stories_group_position"
        private const val SAVED_INSTANCE_STORIES_DETAIL_POSITION = "stories_detail_position"
    }

}
