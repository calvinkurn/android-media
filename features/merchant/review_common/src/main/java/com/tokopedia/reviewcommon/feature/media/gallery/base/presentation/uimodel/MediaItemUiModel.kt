package com.tokopedia.reviewcommon.feature.media.gallery.base.presentation.uimodel

import android.os.Parcelable

interface MediaItemUiModel : Parcelable {
    val uri: String
    val mediaNumber: Int
    fun areItemTheSame(other: MediaItemUiModel?): Boolean
    fun areContentsTheSame(other: MediaItemUiModel?): Boolean
}