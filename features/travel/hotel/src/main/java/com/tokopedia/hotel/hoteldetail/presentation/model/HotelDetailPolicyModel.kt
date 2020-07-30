package com.tokopedia.hotel.hoteldetail.presentation.model

import android.os.Parcelable
import com.tokopedia.hotel.hoteldetail.data.entity.PropertyPolicyData
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 08/05/19
 */
@Parcelize
class HotelDetailPolicyModel (
        var checkInFrom: String = "",
        var checkInTo: String = "",
        var checkOutFrom: String = "",
        var checkOutTo: String = "",
        var propertyPolicy: List<PropertyPolicyData> = listOf()
) : Parcelable