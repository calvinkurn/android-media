package com.tokopedia.logisticseller.domain.mapper

import com.tokopedia.logisticseller.data.model.RescheduleDayOptionModel
import com.tokopedia.logisticseller.data.model.RescheduleReasonOptionModel
import com.tokopedia.logisticseller.data.model.RescheduleTimeOptionModel
import com.tokopedia.logisticseller.data.model.SaveRescheduleModel
import com.tokopedia.logisticseller.data.param.GetReschedulePickupParam
import com.tokopedia.logisticseller.data.param.SaveReschedulePickupParam
import com.tokopedia.logisticseller.data.response.GetReschedulePickupResponse
import com.tokopedia.logisticseller.data.response.SaveReschedulePickupResponse
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupInfo
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupOptions
import com.tokopedia.logisticseller.ui.reschedulepickup.uimodel.ReschedulePickupState

object ReschedulePickupMapper {
    private const val ORDER_ID_SEPARATOR = "~"
    private const val INSERT_SUCCESS_STATUS = "200"
    fun mapToGetReschedulePickupParam(orderIds: List<String>): GetReschedulePickupParam {
        return GetReschedulePickupParam(
            input = GetReschedulePickupParam.MpLogisticGetReschedulePickupInputs(
                orderIds = orderIds.joinToString(ORDER_ID_SEPARATOR)
            )
        )
    }

    fun mapToState(data: GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup): ReschedulePickupState {
        val orderData = data.data.first().orderData
        val shipperName = data.data.first().shipperName
        return ReschedulePickupState(
            options = mapOrderDataToOptionState(
                orderData.firstOrNull()
                    ?: GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData()
            ),
            info = ReschedulePickupInfo(
                courier = "${orderData.firstOrNull()?.shipperProductName ?: ""} - $shipperName",
                invoice = orderData.firstOrNull()?.invoice ?: "",
                guide = data.orderDetailTicker,
                applink = data.appLink
            )
        )
    }

    private fun mapOrderDataToOptionState(orderData: GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData): ReschedulePickupOptions {
        return ReschedulePickupOptions(
            dayOptions = mapDayOptionToModel(orderData.chooseDay),
            reasonOptions = mapReasonOptionToModel(orderData.chooseReason)
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
        return SaveReschedulePickupParam(
            input = SaveReschedulePickupParam.MpLogisticInsertReschedulePickupInputs(
                orderIds = listOf(orderId),
                date = date,
                time = time,
                reason = reason
            )
        )
    }

    fun mapToSaveRescheduleModel(
        data: SaveReschedulePickupResponse.Data,
        etaPickup: String,
        orderId: String
    ): SaveRescheduleModel {
        return SaveRescheduleModel(
            success = data.mpLogisticInsertReschedulePickup.status == INSERT_SUCCESS_STATUS && data.mpLogisticInsertReschedulePickup.errors.isEmpty(),
            message = mapSaveRescheduleMessage(data, orderId),
            status = data.mpLogisticInsertReschedulePickup.status,
            etaPickup = etaPickup,
            errors = data.mpLogisticInsertReschedulePickup.errors,
            openDialog = true
        )
    }

    private fun mapSaveRescheduleMessage(
        data: SaveReschedulePickupResponse.Data,
        orderId: String
    ): String {
        return if (data.mpLogisticInsertReschedulePickup.status == INSERT_SUCCESS_STATUS && data.mpLogisticInsertReschedulePickup.errors.isNotEmpty()) {
            data.mpLogisticInsertReschedulePickup.errors.first().replaceFirst("$orderId: ", "")
        } else {
            data.mpLogisticInsertReschedulePickup.message
        }
    }
}
