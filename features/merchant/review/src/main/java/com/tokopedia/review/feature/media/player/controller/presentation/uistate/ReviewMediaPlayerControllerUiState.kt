package com.tokopedia.review.feature.media.player.controller.presentation.uistate

import com.tokopedia.review.feature.media.gallery.detailed.presentation.uistate.OrientationUiState

data class ReviewMediaPlayerControllerUiState(
    val shouldShowVideoPlayerController: Boolean,
    val shouldShowMediaCounter: Boolean,
    val shouldShowMediaCounterLoader: Boolean,
    val muted: Boolean,
    val orientationUiState: OrientationUiState,
    val overlayVisibility: Boolean,
    val currentGalleryPosition: Int,
    val totalMedia: Int
)
