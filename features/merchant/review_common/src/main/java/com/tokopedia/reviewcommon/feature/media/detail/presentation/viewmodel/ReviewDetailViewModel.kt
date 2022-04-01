package com.tokopedia.reviewcommon.feature.media.detail.presentation.viewmodel

import android.os.Bundle
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.reviewcommon.extension.getSavedState
import com.tokopedia.reviewcommon.feature.media.detail.presentation.uimodel.ReviewDetailBasicInfoUiModel
import com.tokopedia.reviewcommon.feature.media.detail.presentation.uimodel.ReviewDetailSupplementaryInfoUiModel
import com.tokopedia.reviewcommon.feature.media.detail.presentation.uimodel.ReviewDetailUiModel
import com.tokopedia.reviewcommon.feature.media.detail.presentation.uistate.ExpandedReviewDetailBottomSheetUiState
import com.tokopedia.reviewcommon.feature.media.detail.presentation.uistate.ReviewDetailBasicInfoUiState
import com.tokopedia.reviewcommon.feature.media.detail.presentation.uistate.ReviewDetailFragmentUiState
import com.tokopedia.reviewcommon.feature.media.detail.presentation.uistate.ReviewDetailSupplementaryUiState
import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uimodel.LoadingStateItemUiModel
import com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uimodel.MediaItemUiModel
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.usecase.ToggleLikeReviewUseCase
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.uistate.OrientationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ReviewDetailViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    companion object {
        private const val STATE_FLOW_STOP_TIMEOUT_MILLIS = 5000L

        private const val SAVED_STATE_EXPANDED = "savedStateExpanded"
        private const val SAVED_STATE_SHOW_EXPANDED_REVIEW_DETAIL_BOTTOM_SHEET = "savedStateShowExpandedReviewDetailBottomSheet"
    }

    private val _expanded = MutableStateFlow(false)
    private val _showExpandedReviewDetailBottomSheet = MutableStateFlow(false)
    private val _currentMediaItem = MutableStateFlow<MediaItemUiModel?>(null)
    private val _getDetailedReviewMediaResult = MutableStateFlow<ProductrevGetReviewMedia?>(null)
    private val _orientationUiState = MutableStateFlow<OrientationUiState>(OrientationUiState.Portrait)
    private val _overlayVisibility = MutableStateFlow(true)

    val currentReviewDetail: StateFlow<ReviewDetailUiModel?> = combine(
        _expanded, _currentMediaItem, _getDetailedReviewMediaResult, ::mapCurrentReviewDetail
    ).stateIn(
        scope = this,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_STOP_TIMEOUT_MILLIS),
        initialValue = null
    )
    private val _basicInfoUiState = combine(
        _orientationUiState, _overlayVisibility, _currentMediaItem, currentReviewDetail, ::mapBasicInfoUiState
    ).stateIn(
        scope = this,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_STOP_TIMEOUT_MILLIS),
        initialValue = ReviewDetailBasicInfoUiState.Hidden
    )
    private val _supplementaryInfoUiState = combine(
        _expanded, _orientationUiState, _overlayVisibility, _currentMediaItem, currentReviewDetail, ::mapSupplementaryUiState
    ).stateIn(
        scope = this,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_STOP_TIMEOUT_MILLIS),
        initialValue = ReviewDetailSupplementaryUiState.Hidden
    )
    val reviewDetailFragmentUiState = combine(
        _expanded, _basicInfoUiState, _supplementaryInfoUiState, ::mapReviewDetailFragmentUiState
    ).stateIn(
        scope = this,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_STOP_TIMEOUT_MILLIS),
        initialValue = ReviewDetailFragmentUiState.Showing(
            _basicInfoUiState.value,
            _supplementaryInfoUiState.value
        )
    )
    val expandedReviewDetailBottomSheetUiState = combine(
        _showExpandedReviewDetailBottomSheet,
        _basicInfoUiState,
        _supplementaryInfoUiState,
        ::mapExpandedReviewDetailBottomSheetUiState
    ).stateIn(
        scope = this,
        started = SharingStarted.WhileSubscribed(STATE_FLOW_STOP_TIMEOUT_MILLIS),
        initialValue = ExpandedReviewDetailBottomSheetUiState.Hidden(
            _basicInfoUiState.value,
            _supplementaryInfoUiState.value
        )
    )

    private fun mapCurrentReviewDetail(
        expanded: Boolean,
        currentMediaItem: MediaItemUiModel?,
        getDetailedReviewMediaResult: ProductrevGetReviewMedia?
    ): ReviewDetailUiModel? {
        val currentMediaItemNumber = currentMediaItem?.mediaNumber.orZero()
        return getDetailedReviewMediaResult?.let { resultData ->
            resultData.reviewMedia.find { it.mediaNumber == currentMediaItemNumber }?.let { item ->
                resultData.getReviewDetailWithID(item.feedbackId, expanded)
            }
        }
    }

    private fun mapBasicInfoUiState(
        orientationUiState: OrientationUiState,
        overlayVisibility: Boolean,
        currentMediaItem: MediaItemUiModel?,
        currentReviewDetail: ReviewDetailUiModel?
    ): ReviewDetailBasicInfoUiState {
        val isInPortraitMode = orientationUiState.isPortrait()
        return if (isInPortraitMode && overlayVisibility) {
            val showingLoading = currentMediaItem is LoadingStateItemUiModel
            if (!showingLoading) {
                currentReviewDetail?.basicInfoUiModel?.let { basicInfoData ->
                    return ReviewDetailBasicInfoUiState.Showing(basicInfoData)
                } ?: ReviewDetailBasicInfoUiState.Hidden
            } else ReviewDetailBasicInfoUiState.Loading
        } else {
            ReviewDetailBasicInfoUiState.Hidden
        }
    }

    private fun mapSupplementaryUiState(
        expanded: Boolean,
        orientationUiState: OrientationUiState,
        overlayVisibility: Boolean,
        currentMediaItem: MediaItemUiModel?,
        currentReviewDetail: ReviewDetailUiModel?
    ): ReviewDetailSupplementaryUiState {
        val isInPortraitMode = orientationUiState.isPortrait()
        return if (isInPortraitMode && overlayVisibility) {
            val showingLoading = currentMediaItem is LoadingStateItemUiModel
            if (!showingLoading) {
                if (expanded) {
                    currentReviewDetail?.supplementaryInfoUiModel?.let { supplementaryInfoUiModel ->
                        ReviewDetailSupplementaryUiState.Showing(supplementaryInfoUiModel)
                    } ?: ReviewDetailSupplementaryUiState.Hidden
                } else {
                    ReviewDetailSupplementaryUiState.Hidden
                }
            } else ReviewDetailSupplementaryUiState.Loading
        } else {
            ReviewDetailSupplementaryUiState.Hidden
        }
    }

    private fun mapReviewDetailFragmentUiState(
        expanded: Boolean,
        basicInfoUiState: ReviewDetailBasicInfoUiState,
        supplementaryUiState: ReviewDetailSupplementaryUiState
    ): ReviewDetailFragmentUiState {
        return if (basicInfoUiState is ReviewDetailBasicInfoUiState.Hidden && supplementaryUiState is ReviewDetailSupplementaryUiState.Hidden) {
            ReviewDetailFragmentUiState.Hidden
        } else if (
            (basicInfoUiState is ReviewDetailBasicInfoUiState.Loading && supplementaryUiState is ReviewDetailSupplementaryUiState.Loading) ||
            (basicInfoUiState is ReviewDetailBasicInfoUiState.Showing && supplementaryUiState is ReviewDetailSupplementaryUiState.Showing) ||
            (basicInfoUiState is ReviewDetailBasicInfoUiState.Showing && supplementaryUiState is ReviewDetailSupplementaryUiState.Hidden && !expanded)
        ) {
            ReviewDetailFragmentUiState.Showing(basicInfoUiState, supplementaryUiState)
        } else {
            reviewDetailFragmentUiState.value
        }
    }

    private fun mapExpandedReviewDetailBottomSheetUiState(
        showExpandedReviewDetailBottomSheet: Boolean,
        basicInfoUiState: ReviewDetailBasicInfoUiState,
        supplementaryUiState: ReviewDetailSupplementaryUiState
    ): ExpandedReviewDetailBottomSheetUiState {
        return if (showExpandedReviewDetailBottomSheet) {
            ExpandedReviewDetailBottomSheetUiState.Showing(basicInfoUiState, supplementaryUiState)
        } else {
            ExpandedReviewDetailBottomSheetUiState.Hidden(basicInfoUiState, supplementaryUiState)
        }
    }

    private fun ProductrevGetReviewMedia.getReviewDetailWithID(
        feedbackId: String,
        expanded: Boolean
    ): ReviewDetailUiModel? {
        return detail.reviewDetail.find { it.feedbackId == feedbackId }?.let {
            ReviewDetailUiModel(
                basicInfoUiModel = ReviewDetailBasicInfoUiModel(
                    feedbackId = it.feedbackId,
                    rating = it.rating,
                    createTimeStr = it.createTimestamp,
                    likeCount = it.totalLike,
                    isLiked = it.isLiked,
                    userId = it.user.userId,
                    anonymous = it.isAnonymous,
                    profilePicture = it.user.image,
                    reviewerName = it.user.fullName,
                    reviewerStatsSummary = it.userStats.joinToString(separator = " • ") {
                        it.formatted
                    }.let { if (it.isNotBlank()) " • $it" else it },
                    expanded = expanded
                ),
                supplementaryInfoUiModel = ReviewDetailSupplementaryInfoUiModel(
                    variant = it.variantName,
                    review = it.review,
                    complaint = it.badRatingReasonFmt
                ),
                isReportable = it.isReportable,
                feedbackID = it.feedbackId,
                shopID = it.shopId
            )
        }
    }

    fun toggleExpand() {
        _expanded.update { !it }
    }

    fun updateGetDetailedReviewMediaResult(response: ProductrevGetReviewMedia?) {
        _getDetailedReviewMediaResult.value = response
    }

    fun updateCurrentMediaItem(mediaItems: MediaItemUiModel?) {
        _currentMediaItem.value = mediaItems
    }

    fun saveState(outState: Bundle) {
        outState.putBoolean(SAVED_STATE_EXPANDED, _expanded.value)
        outState.putBoolean(
            SAVED_STATE_SHOW_EXPANDED_REVIEW_DETAIL_BOTTOM_SHEET,
            _showExpandedReviewDetailBottomSheet.value
        )
    }

    fun restoreSavedState(savedState: Bundle) {
        _expanded.value = savedState.getSavedState(
            SAVED_STATE_EXPANDED, _expanded.value
        ) ?: _expanded.value
        _showExpandedReviewDetailBottomSheet.value = savedState.getSavedState(
            SAVED_STATE_SHOW_EXPANDED_REVIEW_DETAIL_BOTTOM_SHEET,
            _showExpandedReviewDetailBottomSheet.value
        ) ?: _showExpandedReviewDetailBottomSheet.value
    }

    fun getFeedbackID(): String? {
        return currentReviewDetail.value?.basicInfoUiModel?.feedbackId
    }

    fun getInvertedLikeStatus(): Int? {
        return currentReviewDetail.value?.basicInfoUiModel?.isLiked?.let { isLiked ->
            if (isLiked) ToggleLikeReviewUseCase.NEUTRAL else ToggleLikeReviewUseCase.LIKED
        }
    }

    fun showExpandedReviewDetailBottomSheet() {
        _showExpandedReviewDetailBottomSheet.value = true
    }

    fun dismissExpandedReviewDetailBottomSheet() {
        _showExpandedReviewDetailBottomSheet.value = false
    }

    fun updateCurrentOrientation(orientationUiState: OrientationUiState) {
        _orientationUiState.value = orientationUiState
    }

    fun updateCurrentOverlayVisibility(visible: Boolean) {
        _overlayVisibility.value = visible
    }
}
