package com.tokopedia.stories.creation.view.viewmodel

import androidx.lifecycle.ViewModel
import com.tokopedia.stories.creation.view.model.action.StoriesCreationAction
import com.tokopedia.stories.creation.view.model.state.StoriesCreationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 06, 2023
 */
class StoriesCreationViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState = MutableStateFlow(StoriesCreationUiState.Empty)

    val uiState: StateFlow<StoriesCreationUiState> = _uiState

    fun submitAction(action: StoriesCreationAction) {
        when (action) {
            is StoriesCreationAction.SetMedia -> handleSetMedia(action.mediaFilePath)
        }
    }

    private fun handleSetMedia(mediaFilePath: String) {
        _uiState.update {
            it.copy(
                mediaFilePath = mediaFilePath
            )
        }
    }
}
