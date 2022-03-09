package com.tokopedia.sellerorder.reschedule_pickup.data.mapper

import com.tokopedia.sellerorder.reschedule_pickup.data.model.GetReschedulePickupParam
import com.tokopedia.sellerorder.reschedule_pickup.data.model.GetReschedulePickupResponse
import com.tokopedia.sellerorder.reschedule_pickup.data.model.RescheduleDetailModel

object ReschedulePickupMapper {
    fun mapToGetReschedulePickupParam(orderIds: List<String>) : GetReschedulePickupParam {
        return GetReschedulePickupParam(
            orderIds = orderIds.joinToString("~")
        )
    }

    fun mapToRescheduleDetailModel(data: GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem) : RescheduleDetailModel {
        return RescheduleDetailModel(order = data.orderData.first(), shippedId = data.shipperId)
    }
}