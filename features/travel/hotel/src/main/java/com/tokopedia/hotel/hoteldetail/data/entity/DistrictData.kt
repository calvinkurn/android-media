package com.tokopedia.hotel.hoteldetail.data.entity

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 26/04/19
 */
@Parcelize
class DistrictData(@SerializedName("id")
                   @Expose
                   val id: Long = 0,
                   @SerializedName("cityId")
                   @Expose
                   val cityId: Long = 0,
                   @SerializedName("latitude")
                   @Expose
                   val latitude: Double = 0.0,
                   @SerializedName("longitude")
                   @Expose
                   val longitude: Double = 0.0,
                   @SerializedName("name")
                   @Expose
                   val name: String = "") : Parcelable