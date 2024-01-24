package com.tokopedia.content.product.preview.view.uimodel.product

import android.os.Parcelable
import com.tokopedia.content.product.preview.view.uimodel.MediaType
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductContentUiModel(
    val contentId: String = "",
    val selected: Boolean = false,
    val variantName: String = "",
    val type: MediaType = MediaType.Unknown,
    val url: String = "",
    val thumbnailUrl: String = "",
    val videoLastDuration: Long = 0L,
    val videoTotalDuration: Long = 0L
) : Parcelable
