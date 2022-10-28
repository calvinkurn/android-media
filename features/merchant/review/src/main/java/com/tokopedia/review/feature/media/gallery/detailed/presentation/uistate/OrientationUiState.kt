package com.tokopedia.review.feature.media.gallery.detailed.presentation.uistate

import androidx.annotation.IntDef

data class OrientationUiState(
    @Orientation val orientation: Int = Orientation.PORTRAIT
) {
    fun isPortrait(): Boolean {
        return orientation == Orientation.PORTRAIT
    }

    fun isLandscape(): Boolean {
        return orientation == Orientation.LANDSCAPE
    }

    @Retention(AnnotationRetention.SOURCE)
    @Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
    @IntDef(value = [Orientation.PORTRAIT, Orientation.LANDSCAPE])
    annotation class Orientation {
        companion object {
            const val PORTRAIT = 0
            const val LANDSCAPE = 1
        }
    }
}