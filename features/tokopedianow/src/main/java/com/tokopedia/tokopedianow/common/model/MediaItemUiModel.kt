package com.tokopedia.tokopedianow.common.model

import android.os.Parcelable
import com.tokopedia.tokopedianow.recipedetail.constant.MediaType
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaItemUiModel(
    val url: String,
    val thumbnailUrl: String,
    val type: MediaType,
    val position: Int
): Parcelable