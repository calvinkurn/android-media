package com.tokopedia.hotel.hoteldetail.data.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.hotel.hoteldetail.presentation.adapter.HotelDetailFacilityAdapterTypeFactory

/**
 * @author by furqan on 26/04/19
 */
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

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(FacilityItem))

    override fun type(typeFactory: HotelDetailFacilityAdapterTypeFactory): Int =
            typeFactory.type(this)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(groupName)
        parcel.writeString(groupIconUrl)
        parcel.writeTypedList(item)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FacilityData> {
        override fun createFromParcel(parcel: Parcel): FacilityData {
            return FacilityData(parcel)
        }

        override fun newArray(size: Int): Array<FacilityData?> {
            return arrayOfNulls(size)
        }
    }

}