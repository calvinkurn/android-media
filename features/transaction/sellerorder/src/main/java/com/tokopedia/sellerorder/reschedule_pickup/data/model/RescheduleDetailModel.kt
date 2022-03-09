package com.tokopedia.sellerorder.reschedule_pickup.data.model

data class RescheduleDetailModel(
    val order: GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData = GetReschedulePickupResponse.Data.MpLogisticGetReschedulePickup.DataItem.OrderData(),
    val shippedId: Long = 0
)