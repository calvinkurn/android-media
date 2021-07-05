package com.tokopedia.flight.orderdetail.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 16/10/2020
 */
@Parcelize
data class FlightOrderDetailCancellationModel(
        val cancelId: Int,
        val cancelDetails: List<OrderDetailCancellationDetail>,
        val createTime: String,
        val updateTime: String,
        val estimatedRefund: String,
        val estimatedRefundNumeric: Long,
        val realRefund: String,
        val realRefundNumeric: Long,
        val status: Int,
        val statusStr: String,
        val statusType: String,
        val refundInfo: String,
        val refundDetail: OrderDetailRefundDetailModel
) : Parcelable {

    @Parcelize
    data class OrderDetailRefundDetailModel(
            val topInfo: List<OrderDetailRefundKeyValueModel>,
            val middleInfo: List<OrderDetailRefundTitleContentModel>,
            val bottomInfo: List<OrderDetailRefundKeyValueModel>,
            val notes: List<OrderDetailRefundKeyValueModel>
    ) : Parcelable

    @Parcelize
    data class OrderDetailRefundKeyValueModel(
            val key: String,
            val value: String
    ) : Parcelable

    @Parcelize
    data class OrderDetailRefundTitleContentModel(
            val title: String,
            val content: List<OrderDetailRefundKeyValueModel>
    ) : Parcelable

    @Parcelize
    data class OrderDetailCancellationDetail(
            val journeyId: Int,
            val passengerId: Int,
            val refundedGateway: String,
            val refundedTime: String
    ) : Parcelable

}