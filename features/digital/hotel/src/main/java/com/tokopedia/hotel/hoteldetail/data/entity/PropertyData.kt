package com.tokopedia.hotel.hoteldetail.data.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 26/04/19
 */
class PropertyData(@SerializedName("id")
                   @Expose
                   val id: Int = 0,
                   @SerializedName("regionId")
                   @Expose
                   val regionId: Int = 0,
                   @SerializedName("districtId")
                   @Expose
                   val districtId: Int = 0,
                   @SerializedName("typeId")
                   @Expose
                   val typeId: Int = 0,
                   @SerializedName("typeName")
                   @Expose
                   val typeName: String = "",
                   @SerializedName("name")
                   @Expose
                   val name: String = "",
                   @SerializedName("slug")
                   @Expose
                   val slug: String = "",
                   @SerializedName("address")
                   @Expose
                   val address: String = "",
                   @SerializedName("zipCode")
                   @Expose
                   val zipCode: String = "",
                   @SerializedName("email")
                   @Expose
                   val email: String = "",
                   @SerializedName("phoneNumber")
                   @Expose
                   val phoneNumber: String = "",
                   @SerializedName("latitude")
                   @Expose
                   val latitude: Double = 0.0,
                   @SerializedName("longitude")
                   @Expose
                   val longitude: Double = 0.0,
                   @SerializedName("locationDescription")
                   @Expose
                   val locationDescription: String = "",
                   @SerializedName("locationImageStatic")
                   @Expose
                   val locationImageStatic: String = "",
                   @SerializedName("images")
                   @Expose
                   val images: List<PropertyImageItem> = listOf(),
                   @SerializedName("isClosed")
                   @Expose
                   val isClosed: Int = 0,
                   @SerializedName("checkinFrom")
                   @Expose
                   val checkInFrom: String = "",
                   @SerializedName("checkinTo")
                   @Expose
                   val checkinTo: String = "",
                   @SerializedName("checkinInfo")
                   @Expose
                   val checkinInfo: String = "",
                   @SerializedName("checkoutFrom")
                   @Expose
                   val checkoutFrom: String = "",
                   @SerializedName("checkoutTo")
                   @Expose
                   val checkoutTo: String = "",
                   @SerializedName("checkoutInfo")
                   @Expose
                   val checkoutInfo: String = "",
                   @SerializedName("star")
                   @Expose
                   val star: Int = 0,
                   @SerializedName("description")
                   @Expose
                   val description: String = "",
                   @SerializedName("importantInformation")
                   @Expose
                   val importantInformation: String = "",
                   @SerializedName("welcomeMessage")
                   @Expose
                   val welcomeMessage: String = "",
                   @SerializedName("licenseNumber")
                   @Expose
                   val licenseNumber: String = "",
                   @SerializedName("chainId")
                   @Expose
                   val chainId: Int = 0,
                   @SerializedName("currency")
                   @Expose
                   val currency: String = "IDR",
                   @SerializedName("boost")
                   @Expose
                   val boost: Int = 0,
                   @SerializedName("cityId")
                   @Expose
                   val cityId: Int = 0) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(PropertyImageItem),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(regionId)
        parcel.writeInt(districtId)
        parcel.writeInt(typeId)
        parcel.writeString(typeName)
        parcel.writeString(name)
        parcel.writeString(slug)
        parcel.writeString(address)
        parcel.writeString(zipCode)
        parcel.writeString(email)
        parcel.writeString(phoneNumber)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeString(locationDescription)
        parcel.writeString(locationImageStatic)
        parcel.writeTypedList(images)
        parcel.writeInt(isClosed)
        parcel.writeString(checkInFrom)
        parcel.writeString(checkinTo)
        parcel.writeString(checkinInfo)
        parcel.writeString(checkoutFrom)
        parcel.writeString(checkoutTo)
        parcel.writeString(checkoutInfo)
        parcel.writeInt(star)
        parcel.writeString(description)
        parcel.writeString(importantInformation)
        parcel.writeString(welcomeMessage)
        parcel.writeString(licenseNumber)
        parcel.writeInt(chainId)
        parcel.writeString(currency)
        parcel.writeInt(boost)
        parcel.writeInt(cityId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PropertyData> {
        override fun createFromParcel(parcel: Parcel): PropertyData {
            return PropertyData(parcel)
        }

        override fun newArray(size: Int): Array<PropertyData?> {
            return arrayOfNulls(size)
        }
    }

}