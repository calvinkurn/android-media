package com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.uistate

import java.io.Serializable

sealed interface OrientationUiState : Serializable {

    fun isPortrait(): Boolean {
        return this is Portrait
    }

    fun isLandscape(): Boolean {
        return this is Landscape
    }

    object Portrait : OrientationUiState {
        override fun equals(other: Any?): Boolean {
            return other is Portrait
        }
    }

    object Landscape : OrientationUiState {
        override fun equals(other: Any?): Boolean {
            return other is Landscape
        }
    }
}