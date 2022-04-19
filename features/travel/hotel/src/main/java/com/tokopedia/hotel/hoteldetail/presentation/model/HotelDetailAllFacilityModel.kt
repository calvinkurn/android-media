package com.tokopedia.hotel.hoteldetail.presentation.model

import android.os.Parcelable
import com.tokopedia.hotel.hoteldetail.data.entity.FacilityData
import com.tokopedia.hotel.hoteldetail.data.entity.PropertyDetailData
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 08/05/19
 */
@Parcelize
class HotelDetailAllFacilityModel(
        var facilityListData: List<FacilityData> = listOf(),
        var policyData: HotelDetailPolicyModel = HotelDetailPolicyModel(),
        var importantInfo: String = "",
        var description: String = "") : Parcelable {

    companion object {

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