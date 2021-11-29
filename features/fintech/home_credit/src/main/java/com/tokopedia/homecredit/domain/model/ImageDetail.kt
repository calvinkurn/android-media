package com.tokopedia.homecredit.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ImageDetail(
    var bitMapWidth: Int,
    var bitmapHeight: Int,
    val imagePath: String?
) : Parcelable
