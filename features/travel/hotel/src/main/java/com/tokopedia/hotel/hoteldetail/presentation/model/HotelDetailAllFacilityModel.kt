package com.tokopedia.hotel.hoteldetail.presentation.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.hotel.hoteldetail.data.entity.FacilityData
import com.tokopedia.hotel.hoteldetail.data.entity.PropertyDetailData

/**
 * @author by furqan on 08/05/19
 */
class HotelDetailAllFacilityModel(
        var facilityListData: List<FacilityData> = listOf(),
        var policyData: HotelDetailPolicyModel = HotelDetailPolicyModel(),
        var importantInfo: String = "",
        var description: String = "") : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.createTypedArrayList(FacilityData),
            parcel.readParcelable(HotelDetailPolicyModel::class.java.classLoader),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(facilityListData)
        parcel.writeParcelable(policyData, flags)
        parcel.writeString(importantInfo)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HotelDetailAllFacilityModel> {
        override fun createFromParcel(parcel: Parcel): HotelDetailAllFacilityModel {
            return HotelDetailAllFacilityModel(parcel)
        }

        override fun newArray(size: Int): Array<HotelDetailAllFacilityModel?> {
            return arrayOfNulls(size)
        }

        fun transform(propertyDetailData: PropertyDetailData): HotelDetailAllFacilityModel {
            val detailAllFacilityModel = HotelDetailAllFacilityModel()

            if (propertyDetailData.facility.isNotEmpty()) {
                detailAllFacilityModel.facilityListData = propertyDetailData.facility
            }

            detailAllFacilityModel.policyData = HotelDetailPolicyModel(propertyDetailData.property.checkInFrom,
                    propertyDetailData.property.checkinTo, propertyDetailData.property.checkoutFrom,
                    propertyDetailData.property.checkoutTo, propertyDetailData.propertyPolicy)

            detailAllFacilityModel.importantInfo = propertyDetailData.property.importantInformation
            detailAllFacilityModel.description = propertyDetailData.property.description

            return detailAllFacilityModel
        }
    }

}