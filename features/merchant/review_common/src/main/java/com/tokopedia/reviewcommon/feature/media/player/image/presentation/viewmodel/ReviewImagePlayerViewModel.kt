package com.tokopedia.reviewcommon.feature.media.player.image.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.reviewcommon.feature.media.player.image.presentation.uistate.ReviewImagePlayerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ReviewImagePlayerViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {
    private val _uiState: MutableStateFlow<ReviewImagePlayerUiState> =
        MutableStateFlow(ReviewImagePlayerUiState.Showing())
    val uiState: StateFlow<ReviewImagePlayerUiState>
        get() = _uiState

    fun setImageUri(imageUri: String) {
        _uiState.value = ReviewImagePlayerUiState.Showing(imageUri)
    }

    fun restoreSavedState(savedState: ReviewImagePlayerUiState?) {
        savedState?.run { _uiState.value = this }
    }
}