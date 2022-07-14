package com.tokopedia.tokopedianow.common.model

import android.os.Parcelable
import com.tokopedia.tokopedianow.recipedetail.constant.MediaType
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaItemUiModel(
    val id: String,
    val url: String,
    val thumbnailUrl: String,
    val duration: String = "",
    val type: MediaType
): Parcelable