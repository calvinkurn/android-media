package com.tokopedia.hotel.hoteldetail.data.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 26/04/19
 */
class PropertyPolicyData(@SerializedName("name")
                         @Expose
                         val name: String = "",
                         @SerializedName("content")
                         @Expose
                         val content: String = "",
                         @SerializedName("propertyPolicyId")
                         @Expose
                         val propertyPolicyId: String = "") : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(content)
        parcel.writeString(propertyPolicyId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PropertyPolicyData> {
        override fun createFromParcel(parcel: Parcel): PropertyPolicyData {
            return PropertyPolicyData(parcel)
        }

        override fun newArray(size: Int): Array<PropertyPolicyData?> {
            return arrayOfNulls(size)
        }
    }
}