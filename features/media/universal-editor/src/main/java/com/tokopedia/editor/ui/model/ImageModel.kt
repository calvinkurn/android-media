package com.tokopedia.editor.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageModel(
    var placement: ImagePlacementModel? = null,

    /**
     * 1. text
     * 2. configuration
     */
    var texts: Map<String, InputTextModel> = emptyMap()
) : Parcelable
