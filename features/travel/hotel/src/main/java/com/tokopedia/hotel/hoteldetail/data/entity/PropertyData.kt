package com.tokopedia.hotel.hoteldetail.data.entity

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 26/04/19
 */
@Parcelize
class PropertyData(@SerializedName("id")
                   @Expose
                   val id: Long = 0,
                   @SerializedName("regionId")
                   @Expose
                   val regionId: Long = 0,
                   @SerializedName("districtId")
                   @Expose
                   val districtId: Long = 0,
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
                   val cityId: Int = 0) : Parcelable