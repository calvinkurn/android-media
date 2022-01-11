package com.tokopedia.tokopoints.view.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BottomSheetModel(
    var bottomSheetTitle:String? = "",
    var contentTitle: String? = "",
    var contentDescription: String? = "",
    var buttonText: String? = "",
    var remoteImage: String? = "",
    var imageUrl: String? = ""
) : Parcelable
