package com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.uistate

import java.io.Serializable

sealed interface DetailedReviewMediaGalleryOrientationUiState : Serializable {

    fun isPortrait(): Boolean {
        return this is Portrait
    }

    fun isLandscape(): Boolean {
        return this is Landscape
    }

    object Portrait : DetailedReviewMediaGalleryOrientationUiState {
        override fun equals(other: Any?): Boolean {
            return other is Portrait
        }
    }

    object Landscape : DetailedReviewMediaGalleryOrientationUiState {
        override fun equals(other: Any?): Boolean {
            return other is Landscape
        }
    }
}