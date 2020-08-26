package com.tokopedia.updateinactivephone.revamp.common.cameraview

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class CameraViewMode(val id: Int) : Parcelable {
    ID_CARD(1),
    SELFIE(2),
    SAVING_BOOK(3)
}