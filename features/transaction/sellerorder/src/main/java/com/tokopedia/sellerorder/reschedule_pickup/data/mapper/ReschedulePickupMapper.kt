package com.tokopedia.sellerorder.reschedule_pickup.data.mapper

import com.tokopedia.sellerorder.reschedule_pickup.data.model.GetReschedulePickupParam
import com.tokopedia.sellerorder.reschedule_pickup.data.model.GetReschedulePickupResponse
import com.tokopedia.sellerorder.reschedule_pickup.data.model.RescheduleDetailModel
import com.tokopedia.sellerorder.reschedule_pickup.data.model.SaveRescheduleModel
import com.tokopedia.sellerorder.reschedule_pickup.data.model.SaveReschedulePickupParam
import com.tokopedia.sellerorder.reschedule_pickup.data.model.SaveReschedulePickupResponse

object ReschedulePickupMapper {
    fun mapToGetReschedulePickupParam(orderIds: List<String>) : GetReschedulePickupParam {
        return GetReschedulePickupParam(
            orderIds = orderIds.joinToString("~")
        )
    }

    fun mapToRescheduleDetailModel(data: GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem) : RescheduleDetailModel {
        return RescheduleDetailModel(order = data.orderData.first(), shippedId = data.shipperId)
    }

    fun mapToSaveReschedulePickupParam(orderId: String, date: String, time: String, reason: String) : SaveReschedulePickupParam {
        return SaveReschedulePickupParam(listOf(orderId), date, time, reason)
    }

    fun mapToSaveRescheduleModel(data: SaveReschedulePickupResponse.Data) : SaveRescheduleModel {
        return SaveRescheduleModel(message = data.mpLogisticInsertReschedulePickup.message, status = data.mpLogisticInsertReschedulePickup.status)
    }
}