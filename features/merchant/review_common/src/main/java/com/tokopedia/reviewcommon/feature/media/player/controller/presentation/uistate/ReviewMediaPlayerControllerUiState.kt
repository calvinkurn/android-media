package com.tokopedia.reviewcommon.feature.media.player.controller.presentation.uistate

import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.uistate.DetailedReviewMediaGalleryOrientationUiState

data class ReviewMediaPlayerControllerUiState(
    val shouldShowVideoPlayerController: Boolean,
    val shouldShowMediaCounter: Boolean,
    val shouldShowMediaCounterLoader: Boolean,
    val muted: Boolean,
    val orientationUiState: DetailedReviewMediaGalleryOrientationUiState,
    val overlayVisibility: Boolean,
    val currentGalleryPosition: Int,
    val totalMedia: Int
)
