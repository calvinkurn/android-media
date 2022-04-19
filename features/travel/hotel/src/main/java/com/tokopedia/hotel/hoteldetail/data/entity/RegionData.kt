package com.tokopedia.hotel.hoteldetail.data.entity

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 26/04/19
 */
@Parcelize
class RegionData(@SerializedName("id")
                 @Expose
                 val id: Long = 0,
                 @SerializedName("name")
                 @Expose
                 val name: String = "",
                 @SerializedName("countryName")
                 @Expose
                 val countryName: String = "") : Parcelable