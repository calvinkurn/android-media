package com.tokopedia.search.result.presentation.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.discovery.common.constants.SearchConstant.ProductCardLabel.LABEL_INTEGRITY

data class LabelGroupDataView(
    val position: String,
    val type: String,
    val title: String,
    val imageUrl: String = ""
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

    companion object CREATOR : Parcelable.Creator<LabelGroupDataView> {
        override fun createFromParcel(parcel: Parcel): LabelGroupDataView {
            return LabelGroupDataView(parcel)
        }

        override fun newArray(size: Int): Array<LabelGroupDataView?> {
            return arrayOfNulls(size)
        }
    }
}