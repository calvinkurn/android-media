package com.tokopedia.product.detail.data.model.shop

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.view.adapter.CourierTypeFactory

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
data class BBInfo(
        @SerializedName("bbName")
        @Expose
        val name: String = "",

        @SerializedName("bbDesc")
        @Expose
        val desc: String = "",

        @SerializedName("bbNameEN")
        @Expose
        val nameEN: String = "",

        @SerializedName("bbDescEN")
        @Expose
        val descEN: String = ""
): BlackBoxShipmentHolder(), Parcelable {

    override fun type(typeFactory: CourierTypeFactory): Int = typeFactory.type(this)

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(desc)
        parcel.writeString(nameEN)
        parcel.writeString(descEN)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BBInfo> {
        override fun createFromParcel(parcel: Parcel): BBInfo {
            return BBInfo(parcel)
        }

        override fun newArray(size: Int): Array<BBInfo?> {
            return arrayOfNulls(size)
        }
    }
}