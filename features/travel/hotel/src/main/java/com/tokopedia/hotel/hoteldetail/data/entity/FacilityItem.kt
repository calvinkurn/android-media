package com.tokopedia.hotel.hoteldetail.data.entity

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 26/04/19
 */
@Parcelize
class FacilityItem(@SerializedName("id")
                   @Expose
                   val id: Int = 0,
                   @SerializedName("name")
                   @Expose
                   val name: String = "",
                   @SerializedName("icon")
                   @Expose
                   val icon: String = "",
                   @SerializedName("iconUrl")
                   @Expose
                   val iconUrl: String = "",
                   @SerializedName("availability")
                   @Expose
                   val availability: Int = 0) : Parcelable