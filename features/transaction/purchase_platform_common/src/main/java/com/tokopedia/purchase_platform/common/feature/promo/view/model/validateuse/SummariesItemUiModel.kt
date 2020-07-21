package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcel
import android.os.Parcelable

data class SummariesItemUiModel(
        var amount: Int = -1,
        var sectionName: String = "",
        var description: String = "",
        var details: List<DetailsItemUiModel> = listOf(),
        var sectionDescription: String = "",
        var type: String = "",
        var amountStr: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Int::class.java.classLoader) as Int,
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.createTypedArrayList(DetailsItemUiModel) ?: arrayListOf(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(amount)
        parcel.writeString(sectionName)
        parcel.writeString(description)
        parcel.writeTypedList(details)
        parcel.writeString(sectionDescription)
        parcel.writeString(type)
        parcel.writeString(amountStr)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SummariesItemUiModel> {
        override fun createFromParcel(parcel: Parcel): SummariesItemUiModel {
            return SummariesItemUiModel(parcel)
        }

        override fun newArray(size: Int): Array<SummariesItemUiModel?> {
            return arrayOfNulls(size)
        }
    }
}