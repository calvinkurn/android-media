package com.tokopedia.tokopoints.notification.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BottomSheetModel(
    var bottomSheetTitle:String? = "",
    var contentTitle: String? = "",
    var contentDescription: String? = "",
    var buttonText: String? = "",
    var remoteImage: Pair<String,String>? = null,
    var imageUrl: String? = "",
    var applink:String = ""
) : Parcelable
