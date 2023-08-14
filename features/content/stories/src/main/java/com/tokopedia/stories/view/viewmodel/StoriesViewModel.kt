package com.tokopedia.stories.view.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.stories.data.StoriesRepository
import com.tokopedia.stories.view.model.StoriesDetailUiModel
import com.tokopedia.stories.view.model.StoriesDetailUiModel.StoriesDetailUiEvent
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

    private var shopId: String = ""
    private var storiesId: String = ""
    var mGroupPosition = 0
    var mDetailMaxInGroup = 0
    var mDetailPosition = 0

    private var _storiesGroup = MutableStateFlow(listOf<StoriesGroupUiModel>())
    private var _storiesDetail = MutableStateFlow(StoriesDetailUiModel.Empty)

    private val _uiEvent = MutableSharedFlow<StoriesUiEvent>(extraBufferCapacity = 100)
    val uiEvent: Flow<StoriesUiEvent>
        get() = _uiEvent

    val uiState = combine(
        _storiesGroup,
        _storiesDetail,
    ) { storiesCategories, storiesItem ->
        StoriesUiState(
            storiesGroup = storiesCategories,
            storiesDetail = storiesItem,
        )
    }

    init {
        val response = repository.getStoriesData()
        _storiesGroup.value = response.groups
    }

    fun submitAction(action: StoriesUiAction) {
        when (action) {
            is StoriesUiAction.SetArgumentsData -> handleSetInitialData(action.data)
            is StoriesUiAction.SetGroupMainData -> handleGroupMainData(action.selectedGroup)
            StoriesUiAction.NextDetail -> handleNext()
            StoriesUiAction.PreviousDetail -> handlePrevious()
            StoriesUiAction.PauseStories -> handleOnPauseStories()
            StoriesUiAction.ResumeStories -> handleOnResumeStories()
        }
    }

    private fun handleSetInitialData(data: Bundle?) {
        shopId = data?.getString(SHOP_ID, "").orEmpty()
        storiesId = data?.getString(STORIES_ID, "").orEmpty()
    }

    private fun handleGroupMainData(selectedGroup: Int) {
        viewModelScope.launch {
            setGroupData(selectedGroup)
            selectGroup(selectedGroup)
        }
    }

    private fun handleNext() {
        val newDetailPosition = mDetailPosition + 1
        val newGroupPosition = mGroupPosition + 1

        if (newDetailPosition < mDetailMaxInGroup) {
            selectDetail(position = newDetailPosition)
        } else {
            selectGroup(newGroupPosition)
        }
    }

    private fun handlePrevious() {
        updateStoriesDetailData(detailPosition = mDetailPosition - 1)
    }

    private fun handleOnPauseStories() {
        updateStoriesDetailData(event = StoriesDetailUiEvent.PAUSE)
    }

    private fun handleOnResumeStories() {
        updateStoriesDetailData(event = StoriesDetailUiEvent.RESUME)
    }

    private fun setGroupData(groupPosition: Int) {
        mGroupPosition = groupPosition
        updateStoriesGroupSelectedIndicator(groupPosition)

        val selectedDetail = _storiesGroup.value[groupPosition].selectedDetail
        mDetailMaxInGroup = _storiesGroup.value[groupPosition].details.size
        updateStoriesDetailData(
            groupPosition = groupPosition,
            detailPosition = selectedDetail,
        )
    }

    private fun selectGroup(position: Int) {
        viewModelScope.launch {
            _uiEvent.emit(StoriesUiEvent.SelectGroup(position))
        }
    }

    private fun selectDetail(position: Int) {
        updateStoriesDetailData(detailPosition = position)
    }

    private fun updateStoriesGroupSelectedIndicator(position: Int) {
        _storiesGroup.update { group ->
            group.mapIndexed { index, storiesGroupUiModel ->
                storiesGroupUiModel.copy(selected = index == position)
            }
        }
    }

    private fun updateStoriesDetailData(
        groupPosition: Int = mGroupPosition,
        detailPosition: Int = mDetailPosition,
        event: StoriesDetailUiEvent = StoriesDetailUiEvent.START,
    ) {
        mDetailPosition = detailPosition
        _storiesDetail.update {
            val currentDetail = _storiesGroup.value[groupPosition]
            currentDetail.details[mDetailPosition].copy(
                selected = mDetailPosition + 1,
                event = event,
            )
        }
    }

    companion object {
        private const val SHOP_ID = "shop_id"
        private const val STORIES_ID = "stories_id"
    }

}
