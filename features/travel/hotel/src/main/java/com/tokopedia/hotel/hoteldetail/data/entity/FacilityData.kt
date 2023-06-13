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
class FacilityData(@SerializedName("groupName")
                   @Expose
                   val groupName: String = "",
                   @SerializedName("groupIconUrl")
                   @Expose
                   val groupIconUrl: String = "",
                   @SerializedName("item")
                   @Expose
                   val item: List<FacilityItem> = listOf())
    : Parcelable, Visitable<HotelDetailFacilityAdapterTypeFactory> {

    override fun type(typeFactory: HotelDetailFacilityAdapterTypeFactory): Int =
            typeFactory.type(this)

}
