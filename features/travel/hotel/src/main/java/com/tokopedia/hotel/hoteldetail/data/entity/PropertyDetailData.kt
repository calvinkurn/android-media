package com.tokopedia.hotel.hoteldetail.data.entity

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 26/04/19
 */
class PropertyDetailData(@SerializedName("property")
                         @Expose
                         val property: PropertyData = PropertyData(),
                         @SerializedName("city")
                         @Expose
                         val city: RegionData = RegionData(),
                         @SerializedName("region")
                         @Expose
                         val region: RegionData = RegionData(),
                         @SerializedName("district")
                         @Expose
                         val district: DistrictData = DistrictData(),
                         @SerializedName("facility")
                         @Expose
                         val facility: List<FacilityData> = listOf(),
                         @SerializedName("mainFacility")
                         @Expose
                         val mainFacility: List<FacilityItem> = listOf(),
                         @SerializedName("propertyPolicy")
                         @Expose
                         val propertyPolicy: List<PropertyPolicyData> = listOf())
    : Parcelable {


    constructor(parcel: Parcel) : this(
            parcel.readParcelable(PropertyData::class.java.classLoader),
            parcel.readParcelable(RegionData::class.java.classLoader),
            parcel.readParcelable(RegionData::class.java.classLoader),
            parcel.readParcelable(DistrictData::class.java.classLoader),
            parcel.createTypedArrayList(FacilityData),
            parcel.createTypedArrayList(FacilityItem),
            parcel.createTypedArrayList(PropertyPolicyData))

    class Response(
            @SerializedName("propertyDetail")
            @Expose
            val propertyDetailData: PropertyDetailData = PropertyDetailData())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(property, flags)
        parcel.writeParcelable(city, flags)
        parcel.writeParcelable(region, flags)
        parcel.writeParcelable(district, flags)
        parcel.writeTypedList(facility)
        parcel.writeTypedList(mainFacility)
        parcel.writeTypedList(propertyPolicy)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PropertyDetailData> {
        override fun createFromParcel(parcel: Parcel): PropertyDetailData {
            return PropertyDetailData(parcel)
        }

        override fun newArray(size: Int): Array<PropertyDetailData?> {
            return arrayOfNulls(size)
        }
    }

}