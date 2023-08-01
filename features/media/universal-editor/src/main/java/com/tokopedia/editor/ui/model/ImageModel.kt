package com.tokopedia.editor.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageModel(
    var placement: ImagePlacementModel? = null,
    var texts: List<InputTextModel> = emptyList()
) : Parcelable

@Parcelize
data class ImagePlacementModel(
    var x: Int,
    var y: Int,
) : Parcelable
