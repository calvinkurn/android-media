package com.tokopedia.stories.creation.view.viewmodel

import androidx.lifecycle.ViewModel
import com.tokopedia.stories.creation.domain.repository.StoriesCreationRepository
import com.tokopedia.stories.creation.view.model.StoriesMediaType
import com.tokopedia.stories.creation.view.model.action.StoriesCreationAction
import com.tokopedia.stories.creation.view.model.event.StoriesCreationUiEvent
import com.tokopedia.stories.creation.view.model.state.StoriesCreationUiState
import com.tokopedia.stories.creation.view.model.state.StoriesManualConfiguration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 06, 2023
 */
class StoriesCreationViewModel @Inject constructor(
    private val repo: StoriesCreationRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(StoriesCreationUiState.Empty)
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<StoriesCreationUiEvent>()
    val uiEvent: Flow<StoriesCreationUiEvent> = _uiEvent

    val maxStoriesConfig: StoriesManualConfiguration.MaxStories
        get() = _uiState.value.config.maxStories

    fun submitAction(action: StoriesCreationAction) {
        when (action) {
            is StoriesCreationAction.SetMedia -> handleSetMedia(action.mediaFilePath, action.mediaType)
            is StoriesCreationAction.ClickAddProduct -> handleClickAddProduct()
            is StoriesCreationAction.ClickUpload -> handleClickUpload()
        }
    }

    private fun handleSetMedia(
        mediaFilePath: String,
        mediaType: StoriesMediaType,
    ) {
        _uiState.update {
            it.copy(
                mediaFilePath = mediaFilePath,
                mediaType = mediaType,
            )
        }
    }

    private fun handleClickAddProduct() {

    }

    private fun handleClickUpload() {

    }
}
