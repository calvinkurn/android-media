package com.tokopedia.sellerorder.reschedule_pickup.data.mapper

import com.tokopedia.sellerorder.reschedule_pickup.data.model.GetReschedulePickupParam
import com.tokopedia.sellerorder.reschedule_pickup.data.model.GetReschedulePickupResponse
import com.tokopedia.sellerorder.reschedule_pickup.data.model.RescheduleDayOptionModel
import com.tokopedia.sellerorder.reschedule_pickup.data.model.RescheduleDetailModel
import com.tokopedia.sellerorder.reschedule_pickup.data.model.RescheduleOptionsModel
import com.tokopedia.sellerorder.reschedule_pickup.data.model.RescheduleReasonOptionModel
import com.tokopedia.sellerorder.reschedule_pickup.data.model.RescheduleTimeOptionModel
import com.tokopedia.sellerorder.reschedule_pickup.data.model.SaveRescheduleModel
import com.tokopedia.sellerorder.reschedule_pickup.data.model.SaveReschedulePickupParam
import com.tokopedia.sellerorder.reschedule_pickup.data.model.SaveReschedulePickupResponse

object ReschedulePickupMapper {
    fun mapToGetReschedulePickupParam(orderIds: List<String>): GetReschedulePickupParam {
        return GetReschedulePickupParam(
            orderIds = orderIds.joinToString("~")
        )
    }

    fun mapToRescheduleDetailModel(data: GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup): RescheduleDetailModel {
        val orderData = data.data.first().orderData
        val shipperName = data.data.first().shipperName
        return RescheduleDetailModel(
            options = mapOrderDataToOptionModel(orderData.firstOrNull() ?: GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData()),
            courierName = "${orderData.firstOrNull()?.shipperProductName ?: ""} - $shipperName",
            invoice = orderData.firstOrNull()?.invoice ?: "",
            errorMessage = orderData.firstOrNull()?.errorMessage ?: "",
            ticker = data.orderDetailTicker,
            appLink = data.appLink
        )
    }

    private fun mapOrderDataToOptionModel(orderData: GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData): RescheduleOptionsModel {
        return RescheduleOptionsModel(
            dayOptions = mapDayOptionToModel(orderData.chooseDay),
            reasonOptionModel = mapReasonOptionToModel(orderData.chooseReason)
        )
    }

    private fun mapReasonOptionToModel(reasons: List<GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData.ReasonOption>): List<RescheduleReasonOptionModel> {
        return reasons.map { RescheduleReasonOptionModel(reason = it.reason) }
    }

    private fun mapDayOptionToModel(days: List<GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData.DayOption>): List<RescheduleDayOptionModel> {
        return days.map {
            RescheduleDayOptionModel(
                day = it.day,
                timeOptions = mapTimeOptionToModel(it.chooseTime)
            )
        }
    }

    private fun mapTimeOptionToModel(times: List<GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData.DayOption.TimeOption>): List<RescheduleTimeOptionModel> {
        return times.map {
            RescheduleTimeOptionModel(
                time = it.time,
                formattedTime = "${it.time} WIB",
                etaPickup = it.etaPickup
            )
        }
    }

    fun mapToSaveReschedulePickupParam(
        orderId: String,
        date: String,
        time: String,
        reason: String
    ): SaveReschedulePickupParam {
        return SaveReschedulePickupParam(listOf(orderId), date, time, reason)
    }

    fun mapToSaveRescheduleModel(data: SaveReschedulePickupResponse.Data, etaPickup: String, orderId: String): SaveRescheduleModel {
        return SaveRescheduleModel(
            success = data.mpLogisticInsertReschedulePickup.status == "200" && data.mpLogisticInsertReschedulePickup.errors.isEmpty(),
            message = mapSaveRescheduleMessage(data, orderId),
            status = data.mpLogisticInsertReschedulePickup.status,
            etaPickup = etaPickup,
            errors = data.mpLogisticInsertReschedulePickup.errors
        )
    }

    fun mapSaveRescheduleMessage(data: SaveReschedulePickupResponse.Data, orderId: String) : String {
        return if (data.mpLogisticInsertReschedulePickup.status == "200" && data.mpLogisticInsertReschedulePickup.errors.isNotEmpty()) {
            data.mpLogisticInsertReschedulePickup.errors.first().replaceFirst("$orderId: ", "")
        } else data.mpLogisticInsertReschedulePickup.message
    }
}