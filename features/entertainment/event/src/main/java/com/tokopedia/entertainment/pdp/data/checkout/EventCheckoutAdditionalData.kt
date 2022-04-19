package com.tokopedia.entertainment.pdp.data.checkout

import android.os.Parcelable
import com.tokopedia.entertainment.pdp.data.Form
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EventCheckoutAdditionalData(
        val idItemMap : String = "",
        val idPackage : String = "",
        val idItem : String = "",
        val titleItem : String = "",
        var additionalType: AdditionalType = AdditionalType.NULL_DATA,
        var listForm : List<Form> = emptyList(),
        var position : Int = 0
): Parcelable

enum class AdditionalType(val type:Int){
    NULL_DATA(0),
    PACKAGE_UNFILL(1),
    PACKAGE_FILLED(2),
    ITEM_UNFILL(3),
    ITEM_FILLED(4)
}

