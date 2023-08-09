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

    var storiesSelectedPage = MutableStateFlow(StoriesDataUiModel.Empty)

    private val _storiesUiEvent = MutableSharedFlow<StoriesUiEvent>(extraBufferCapacity = 100)
    val storiesUiEvent: Flow<StoriesUiEvent>
        get() = _storiesUiEvent

    private var _storiesUiState = MutableStateFlow(StoriesUiModel.Empty)
    val storiesUiState: Flow<StoriesUiModel>
        get() = _storiesUiState

    init {
        _storiesUiState.update {
            repository.getStoriesData()
        }
    }

    fun submitAction(action: StoriesUiAction) {
        when (action) {
            is StoriesUiAction.SelectPage -> handleSelectPage(action.selectedPage)
            is StoriesUiAction.SetInitialData -> handleSetInitialData(action.data)
            StoriesUiAction.NextIndicator -> handleNextIndicator()
            StoriesUiAction.NextPage -> handleNextPage()
            StoriesUiAction.PreviousPage -> handlePreviousPage()
            StoriesUiAction.OnPauseStories -> handleOnPauseStories()
            StoriesUiAction.OnResumeStories -> handleOnResumeStories()
        }
    }

    private fun handleSelectPage(selectedPage: Int) {
        mCounter = selectedPage
        storiesSelectedPage.update {
            _storiesUiState.value.stories[selectedPage]
        }
    }

    private fun handleSetInitialData(data: Bundle?) {
        shopId = data?.getString(SHOP_ID, "").orEmpty()
        storiesId = data?.getString(STORIES_ID, "").orEmpty()
    }

    private fun handleNextIndicator() {
        storiesSelectedPage.update { data ->
            data.copy(selected = data.selected + 1)
        }
    }

    private fun handleNextPage() {
        viewModelScope.launch {
            _storiesUiEvent.emit(StoriesUiEvent.NextPage(mCounter + 1))
        }

    }

    private fun handlePreviousPage() {
    }

    private fun handleOnPauseStories() {
        storiesSelectedPage.update { data ->
            data.copy(isPause = true)
        }
    }

    private fun handleOnResumeStories() {
        storiesSelectedPage.update { data ->
            data.copy(isPause = false)
        }
    }

    companion object {
        private const val SHOP_ID = "shop_id"
        private const val STORIES_ID = "stories_id"
    }

}
