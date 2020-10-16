package com.tokopedia.flight.orderdetail.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 16/10/2020
 */
@Parcelize
data class OrderDetailPassengerModel(
        val id: Int,
        val type: Int,
        val title: Int,
        val firstName: String,
        val lastName: String,
        val dob: String,
        val nationality: String,
        val passportNo: String,
        val passportCountry: String,
        val passportExpiry: String,
        val amenities: List<OrderDetailAmenityModel>,
        val cancelStatus: List<OrderDetailPassengerCancelStatusModel>
) : Parcelable