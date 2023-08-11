package com.tokopedia.stories.view.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.stories.data.StoriesRepository
import com.tokopedia.stories.view.model.StoriesDetailUiModel
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

    private var _storiesGroup = MutableStateFlow(listOf<StoriesGroupUiModel>())
    private var _storiesDetail = MutableStateFlow(listOf<StoriesDetailUiModel>())

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
        _storiesGroup.update { response.group }
    }

    fun submitAction(action: StoriesUiAction) {
        when (action) {
            is StoriesUiAction.SetInitialData -> handleSetInitialData(action.data)
            is StoriesUiAction.SelectGroup -> handleSelectGroup(action.selectedGroup)
            StoriesUiAction.NextGroup -> handleNextGroup()
            StoriesUiAction.PreviousGroup -> handlePreviousGroup()
            StoriesUiAction.NextDetail -> handleNextDetail()
            StoriesUiAction.PreviousDetail -> handlePreviousDetail()
            StoriesUiAction.PauseStories -> handleOnPauseStories()
            StoriesUiAction.ResumeStories -> handleOnResumeStories()
        }
    }

    private fun handleSetInitialData(data: Bundle?) {
        shopId = data?.getString(SHOP_ID, "").orEmpty()
        storiesId = data?.getString(STORIES_ID, "").orEmpty()
    }

    private fun handleSelectGroup(selectedGroup: Int) {
        mGroupPosition = selectedGroup
        _storiesDetail.update {
            _storiesGroup.value[selectedGroup].stories
        }
    }

    private fun handleNextGroup() {
        viewModelScope.launch {
            _uiEvent.emit(StoriesUiEvent.SelectCategories(mGroupPosition + 1))
        }
    }

    private fun handlePreviousGroup() {
        viewModelScope.launch {
            _uiEvent.emit(StoriesUiEvent.SelectCategories(mGroupPosition - 1))
        }
    }

    private fun handleNextDetail() {
        _storiesDetail.update { data ->
            data.mapIndexed { index, item ->
                if (index == mGroupPosition) item.copy(position = item.position + 1)
                else item
            }
        }
    }

    private fun handlePreviousDetail() {
        _storiesDetail.update { data ->
            data.mapIndexed { index, item ->
                if (index == mGroupPosition) item.copy(position = item.position - 1)
                else item
            }
        }
    }

    private fun handleOnPauseStories() {
        _storiesDetail.update { data ->
            data.mapIndexed { index, item ->
                if (mGroupPosition == index) item.copy(isPause = true)
                else item
            }
        }
    }

    private fun handleOnResumeStories() {
        _storiesDetail.update { data ->
            data.mapIndexed { index, item ->
                if (mGroupPosition == index) item.copy(isPause = false)
                else item
            }
        }
    }

    companion object {
        private const val SHOP_ID = "shop_id"
        private const val STORIES_ID = "stories_id"
    }

}
