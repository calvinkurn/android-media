package com.tokopedia.reviewcommon.feature.media.player.image.presentation.viewmodel

import android.os.Bundle
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.reviewcommon.extension.getSavedState
import com.tokopedia.reviewcommon.feature.media.player.image.presentation.uistate.ReviewImagePlayerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class ReviewImagePlayerViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    companion object {
        private const val FLOW_TIMEOUT_MILLIS = 5000L

        private const val SAVED_STATE_IMAGE_URI = "savedStateImageUri"
        private const val SAVED_STATE_SHOW_SEE_MORE = "savedStateShowSeeMore"
        private const val SAVED_STATE_TOTAL_MEDIA_COUNT = "savedStateTotalMediaCount"
    }

    private val _imageUri = MutableStateFlow("")
    private val _showSeeMore = MutableStateFlow(false)
    private val _totalMediaCount = MutableStateFlow(Int.ZERO)
    val uiState: StateFlow<ReviewImagePlayerUiState> = combine(
        _imageUri, _showSeeMore, _totalMediaCount
    ) { imageUri, showSeeMore, totalMediaCount ->
        if (showSeeMore && totalMediaCount.isMoreThanZero()) {
            ReviewImagePlayerUiState.ShowingSeeMore(imageUri, totalMediaCount)
        } else {
            ReviewImagePlayerUiState.Showing(imageUri)
        }
    }.stateIn(
        scope = this,
        started = SharingStarted.WhileSubscribed(FLOW_TIMEOUT_MILLIS),
        initialValue = ReviewImagePlayerUiState.Showing(_imageUri.value)
    )

    fun setImageUri(imageUri: String) {
        _imageUri.value = imageUri
    }

    fun setShowSeeMore(showSeeMore: Boolean) {
        _showSeeMore.value = showSeeMore
    }

    fun setTotalMediaCount(totalMediaCount: Int) {
        _totalMediaCount.value = totalMediaCount
    }

    fun saveState(outState: Bundle) {
        outState.putString(SAVED_STATE_IMAGE_URI, _imageUri.value)
        outState.putBoolean(SAVED_STATE_SHOW_SEE_MORE, _showSeeMore.value)
        outState.putInt(SAVED_STATE_TOTAL_MEDIA_COUNT, _totalMediaCount.value)
    }

    fun restoreSavedState(savedState: Bundle?) {
        savedState?.run {
            _imageUri.value = savedState.getSavedState(
                SAVED_STATE_IMAGE_URI, _imageUri.value
            ) ?: _imageUri.value
            _showSeeMore.value = savedState.getSavedState(
                SAVED_STATE_SHOW_SEE_MORE, _showSeeMore.value
            ) ?: _showSeeMore.value
            _totalMediaCount.value = savedState.getSavedState(
                SAVED_STATE_TOTAL_MEDIA_COUNT, _totalMediaCount.value
            ) ?: _totalMediaCount.value
        }
    }
}