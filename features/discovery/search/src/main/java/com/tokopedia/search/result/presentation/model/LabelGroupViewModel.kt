package com.tokopedia.search.result.presentation.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.discovery.common.constants.SearchConstant.ProductCardLabel.LABEL_INTEGRITY

data class LabelGroupViewModel(
    val position: String,
    val type: String,
    val title: String,
    val imageUrl: String
) : Parcelable {

    fun isLabelIntegrity() = position == LABEL_INTEGRITY

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(position)
        parcel.writeString(type)
        parcel.writeString(title)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LabelGroupViewModel> {
        override fun createFromParcel(parcel: Parcel): LabelGroupViewModel {
            return LabelGroupViewModel(parcel)
        }

        override fun newArray(size: Int): Array<LabelGroupViewModel?> {
            return arrayOfNulls(size)
        }
    }
}