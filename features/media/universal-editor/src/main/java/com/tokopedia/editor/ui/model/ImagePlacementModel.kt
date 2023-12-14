package com.tokopedia.editor.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImagePlacementModel(
    var path: String,
    var scale: Float,
    var angle: Float,
    var translateX: Float,
    var translateY: Float
) : Parcelable
