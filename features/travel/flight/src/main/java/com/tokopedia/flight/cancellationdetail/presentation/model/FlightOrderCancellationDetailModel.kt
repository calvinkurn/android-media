package com.tokopedia.flight.cancellationdetail.presentation.model

import android.os.Parcelable
import com.tokopedia.flight.orderdetail.data.OrderDetailCancellation
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailJourneyModel
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 06/01/2021
 */
@Parcelize
data class FlightOrderCancellationDetailModel(
        val refundId: Int = 0,
        val createTime: String = "",
        val realRefund: String = "",
        val status: Int = 0,
        val passengers: List<FlightOrderCancellationDetailPassengerModel> = arrayListOf(),
        val journeys: List<FlightOrderDetailJourneyModel> = arrayListOf(),
        val statusStr: String = "",
        val statusType: String = "",
        val refundInfo: String = "",
        val refundDetail: OrderDetailCancellation.OrderDetailRefundDetail = OrderDetailCancellation.OrderDetailRefundDetail()
) : Parcelable