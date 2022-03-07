package com.tokopedia.gopay.kyc.domain.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CameraImageResult(
    val bitmapWidth: Int,
    val bitmapHeight: Int,
    val finalCameraResultPath: String?,
    val compressedByteArray: List<Byte>?,
) : Parcelable