package com.tokopedia.product.detail.data.model.datamodel

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Yehezkiel on 2020-02-10
 */

data class ComponentTrackDataModel(
        val componentType: String = "",
        val componentName: String = "",
        val adapterPosition: Int = 0
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(componentType)
        parcel.writeString(componentName)
        parcel.writeInt(adapterPosition)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ComponentTrackDataModel> {
        override fun createFromParcel(parcel: Parcel): ComponentTrackDataModel {
            return ComponentTrackDataModel(parcel)
        }

        override fun newArray(size: Int): Array<ComponentTrackDataModel?> {
            return arrayOfNulls(size)
        }
    }
}