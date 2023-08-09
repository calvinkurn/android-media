package com.tokopedia.stories.view.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.stories.data.StoriesRepository
import com.tokopedia.stories.view.model.StoriesDataUiModel
import com.tokopedia.stories.view.model.StoriesUiModel
import com.tokopedia.stories.view.viewmodel.action.StoriesUiAction
import com.tokopedia.stories.view.viewmodel.event.StoriesUiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class StoriesViewModel @Inject constructor(
    private val repository: StoriesRepository,
) : ViewModel() {

    private var shopId: String = ""
    private var storiesId: String = ""
    var mCounter = 0

    var storiesSelectedCategories = MutableStateFlow(StoriesDataUiModel.Empty)

    private val _uiEvent = MutableSharedFlow<StoriesUiEvent>(extraBufferCapacity = 100)
    val uiEvent: Flow<StoriesUiEvent>
        get() = _uiEvent

    private var _uiState = MutableStateFlow(StoriesUiModel.Empty)
    val uiState: Flow<StoriesUiModel>
        get() = _uiState

    init {
        _uiState.update {
            repository.getStoriesData()
        }
    }

    fun submitAction(action: StoriesUiAction) {
        when (action) {
            is StoriesUiAction.SelectCategories -> handleSelectCategories(action.selectedCategories)
            is StoriesUiAction.SetInitialData -> handleSetInitialData(action.data)
            StoriesUiAction.NextStories -> handleNextStories()
            StoriesUiAction.PreviousStories -> handlePreviousStories()
            StoriesUiAction.NextCategories -> handleNextCategories()
            StoriesUiAction.PreviousCategories -> handlePreviousCategories()
            StoriesUiAction.PauseStories -> handleOnPauseStories()
            StoriesUiAction.ResumeStories -> handleOnResumeStories()
        }
    }

    private fun handleSelectCategories(selectedPage: Int) {
        mCounter = selectedPage
        storiesSelectedCategories.update {
            _uiState.value.stories[selectedPage]
        }
    }

    private fun handleSetInitialData(data: Bundle?) {
        shopId = data?.getString(SHOP_ID, "").orEmpty()
        storiesId = data?.getString(STORIES_ID, "").orEmpty()
    }

    private fun handleNextStories() {
        storiesSelectedCategories.update { data ->
            data.copy(selected = data.selected + 1)
        }
    }

    private fun handlePreviousStories() {
        storiesSelectedCategories.update { data ->
            data.copy(selected = data.selected - 1)
        }
    }

    private fun handleNextCategories() {
        viewModelScope.launch {
            _uiEvent.emit(StoriesUiEvent.SelectCategories(mCounter + 1))
        }
    }

    private fun handlePreviousCategories() {
        viewModelScope.launch {
            _uiEvent.emit(StoriesUiEvent.SelectCategories(mCounter - 1))
        }
    }

    private fun handleOnPauseStories() {
        storiesSelectedCategories.update { data ->
            data.copy(isPause = true)
        }
    }

    private fun handleOnResumeStories() {
        storiesSelectedCategories.update { data ->
            data.copy(isPause = false)
        }
    }

    companion object {
        private const val SHOP_ID = "shop_id"
        private const val STORIES_ID = "stories_id"
    }

}
