package com.tokopedia.content.product.preview.view.uimodel.product

import android.os.Parcelable
import com.tokopedia.content.product.preview.view.uimodel.ContentUiModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductContentUiModel(
    val productId: String = "",
    val content: List<ContentUiModel> = emptyList(),
    val indicator: List<IndicatorUiModel> = emptyList()
) : Parcelable
