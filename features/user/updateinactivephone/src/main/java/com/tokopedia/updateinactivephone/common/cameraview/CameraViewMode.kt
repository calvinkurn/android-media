package com.tokopedia.updateinactivephone.common.cameraview

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class CameraViewMode(val id: Int) : Parcelable {
    ID_CARD(1),
    SELFIE(2)
}

@Parcelize
enum class FileType(val id: Int) : Parcelable {
    ID_CARD(1),
    SELFIE(2)
}