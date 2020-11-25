package com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by fwidjaja on 11/03/20.
 */
data class LastApplyEmptyCartInfoUiModel (
        var imgUrl: String = "",
        var message: String = "",
        var detail: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imgUrl)
        parcel.writeString(message)
        parcel.writeString(detail)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LastApplyEmptyCartInfoUiModel> {
        override fun createFromParcel(parcel: Parcel): LastApplyEmptyCartInfoUiModel {
            return LastApplyEmptyCartInfoUiModel(parcel)
        }

        override fun newArray(size: Int): Array<LastApplyEmptyCartInfoUiModel?> {
            return arrayOfNulls(size)
        }
    }
}