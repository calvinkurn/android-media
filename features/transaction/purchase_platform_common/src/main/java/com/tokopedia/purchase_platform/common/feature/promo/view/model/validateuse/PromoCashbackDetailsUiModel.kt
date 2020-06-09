package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by fwidjaja on 2020-03-05.
 */
data class PromoCashbackDetailsUiModel (
        var amountId: Float? = 0.0F,
        var amountPoints: Float? = 0.0F,
        var benefitType: String? = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Float::class.java.classLoader) as? Float,
            parcel.readValue(Float::class.java.classLoader) as? Float,
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(amountId)
        parcel.writeValue(amountPoints)
        parcel.writeString(benefitType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PromoCashbackDetailsUiModel> {
        override fun createFromParcel(parcel: Parcel): PromoCashbackDetailsUiModel {
            return PromoCashbackDetailsUiModel(parcel)
        }

        override fun newArray(size: Int): Array<PromoCashbackDetailsUiModel?> {
            return arrayOfNulls(size)
        }
    }
}