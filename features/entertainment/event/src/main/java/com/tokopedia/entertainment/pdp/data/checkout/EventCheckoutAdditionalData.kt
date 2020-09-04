package com.tokopedia.entertainment.pdp.data.checkout

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EventCheckoutAdditionalData(
    val idPackage : String = "",
    val idItem : String = "",
    val titleItem : String = "",
    val additionalType: AdditionalType
): Parcelable

enum class AdditionalType(val type:Int){
    NULL_DATA(0),
    PACKAGE_UNFILL(1),
    PACKAGE_FILLED(2),
    ITEM_UNFILL(3),
    ITEM_FILLED(4)
}

