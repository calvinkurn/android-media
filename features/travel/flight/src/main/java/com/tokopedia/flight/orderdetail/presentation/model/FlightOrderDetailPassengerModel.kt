package com.tokopedia.flight.orderdetail.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 16/10/2020
 */
@Parcelize
data class FlightOrderDetailPassengerModel(
        val passengerNo: Int,
        val id: Int,
        val type: Int,
        val typeString: String,
        val title: Int,
        val titleString: String,
        val firstName: String,
        val lastName: String,
        val dob: String,
        val nationality: String,
        val passportNo: String,
        val passportCountry: String,
        val passportExpiry: String,
        val amenities: List<FlightOrderDetailAmenityModel>,
        val cancelStatus: List<FlightOrderDetailPassengerCancelStatusModel>
) : Parcelable