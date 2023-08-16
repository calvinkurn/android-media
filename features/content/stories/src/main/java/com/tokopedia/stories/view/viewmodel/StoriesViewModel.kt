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

    val currentStoryId : String
        get() = _storiesGroup.value.firstOrNull { story -> story.selected }?.id.orEmpty()

    init {
        val response = repository.getStoriesData()
        _storiesGroup.update { response.groups }
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
            StoriesUiAction.OpenKebabMenu -> handleOpenKebab()
        }
    }

    private fun handleSetInitialData(data: Bundle?) {
        shopId = data?.getString(SHOP_ID, "").orEmpty()
        storiesId = data?.getString(STORIES_ID, "").orEmpty()
    }

    private fun handleSelectGroup(selectedGroup: Int) {
        viewModelScope.launch {
            handleResetDetail()
            mGroupPosition = selectedGroup
            _storiesGroup.update { group ->
                group.mapIndexed { index, storiesGroupUiModel ->
                    if (index == selectedGroup) storiesGroupUiModel.copy(selected = true)
                    else storiesGroupUiModel.copy(selected = false)
                }
            }
            _storiesDetail.update {
                val currentDetail = _storiesGroup.value[selectedGroup]
                currentDetail.details[currentDetail.selectedDetail]
            }
            _uiEvent.emit(StoriesUiEvent.SelectGroup(mGroupPosition))
        }
    }

    private fun handleNextGroup() {
        viewModelScope.launch {
            handleResetDetail()
            _uiEvent.emit(StoriesUiEvent.SelectGroup(mGroupPosition + 1))
        }
    }

    private fun handlePreviousGroup() {
        viewModelScope.launch {
            handleResetDetail()
            _uiEvent.emit(StoriesUiEvent.SelectGroup(mGroupPosition - 1))
        }
    }

    private fun handleResetDetail() {
        _storiesDetail.update { data ->
            data.copy(event = StoriesDetailUiEvent.RESTART)
        }
    }

    private fun handleNextDetail() {
        handleResetDetail()
        _storiesDetail.update { data ->
            data.copy(
                selected = data.selected + 1,
                event = StoriesDetailUiEvent.START,
            )
        }
    }

    private fun handlePreviousDetail() {
        handleResetDetail()
        _storiesDetail.update { data ->
            data.copy(
                selected = data.selected - 1,
                event = StoriesDetailUiEvent.START,
            )
        }
    }

    private fun handleOnPauseStories() {
        _storiesDetail.update { data ->
            data.copy(event = StoriesDetailUiEvent.PAUSE)
        }
    }

    private fun handleOnResumeStories() {
        _storiesDetail.update { data ->
            data.copy(event = StoriesDetailUiEvent.START)
        }
    }

    private fun handleOpenKebab() {
        viewModelScope.launch {
            _uiEvent.emit(StoriesUiEvent.OpenKebab)
        }
    }

    companion object {
        private const val SHOP_ID = "shop_id"
        private const val STORIES_ID = "stories_id"
    }

}
