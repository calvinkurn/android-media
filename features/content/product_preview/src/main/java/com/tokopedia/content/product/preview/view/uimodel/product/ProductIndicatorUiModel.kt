package com.tokopedia.content.product.preview.view.uimodel.product

import android.os.Parcelable
import com.tokopedia.content.product.preview.view.uimodel.MediaType
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductIndicatorUiModel(
    val indicatorId: String = "",
    val selected: Boolean = false,
    val variantName: String = "",
    val type: MediaType = MediaType.Unknown,
    val thumbnailUrl: String = ""
) : Parcelable
