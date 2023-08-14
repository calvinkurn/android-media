package com.tokopedia.editor.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TextPlacementModel(
    val deltaX: Float,
    val deltaY: Float,
    val deltaScale: Float,
    val deltaAngle: Float,
    val pivotX: Float,
    val pivotY: Float,
    val minScale: Float,
    val maxScale: Float
) : Parcelable
