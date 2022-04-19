package com.tokopedia.hotel.hoteldetail.data.entity

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.hotel.hoteldetail.presentation.adapter.HotelDetailFacilityAdapterTypeFactory
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 26/04/19
 */
@Parcelize
class PropertyPolicyData(@SerializedName("name")
                         @Expose
                         val name: String = "",
                         @SerializedName("content")
                         @Expose
                         val content: String = "",
                         @SerializedName("icon")
                         @Expose
                         val icon: String = "",
                         @SerializedName("iconUrl")
                         @Expose
                         val iconUrl: String = "",
                         @SerializedName("propertyPolicyId")
                         @Expose
                         val propertyPolicyId: String = "")
    : Parcelable, Visitable<HotelDetailFacilityAdapterTypeFactory> {

    override fun type(typeFactory: HotelDetailFacilityAdapterTypeFactory): Int =
        typeFactory.type(this)
}