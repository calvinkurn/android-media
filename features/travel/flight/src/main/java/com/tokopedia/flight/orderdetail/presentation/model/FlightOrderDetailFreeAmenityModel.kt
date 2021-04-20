package com.tokopedia.flight.orderdetail.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 16/10/2020
 */
@Parcelize
data class FlightOrderDetailFreeAmenityModel(
        val cabinBaggage: OrderDetailBaggageModel,
        val freeBaggage: OrderDetailBaggageModel,
        val meal: Boolean,
        val usbPort: Boolean,
        val wifi: Boolean,
        val others: List<String>
) : Parcelable {
    @Parcelize
    data class OrderDetailBaggageModel(
            val isUpTo: Boolean,
            val unit: String,
            val value: Int
    ) : Parcelable
}