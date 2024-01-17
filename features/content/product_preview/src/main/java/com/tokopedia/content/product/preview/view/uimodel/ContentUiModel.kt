package com.tokopedia.content.product.preview.view.uimodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContentUiModel(
    val contentId: String = "",
    val selected: Boolean = false,
    val type: MediaType = MediaType.Unknown,
    val url: String = ""
) : Parcelable
