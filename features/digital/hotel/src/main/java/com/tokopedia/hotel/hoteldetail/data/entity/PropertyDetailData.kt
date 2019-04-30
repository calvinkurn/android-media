package com.tokopedia.hotel.hoteldetail.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 26/04/19
 */
class PropertyDetailData(
        @SerializedName("property")
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
        @SerializedName("MAIN_FACILITY")
        @Expose
        val mainFacility: List<FacilityItem> = listOf(),
        @SerializedName("propertyPolicy")
        @Expose
        val propertyPolicy: List<PropertyPolicyData> = listOf()) {
    class Response(
            @SerializedName("propertyDetail")
            @Expose
            val propertyDetailData: PropertyDetailData = PropertyDetailData())
}