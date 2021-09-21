package com.tokopedia.sellerorder.requestpickup.data.mapper

import com.tokopedia.sellerorder.requestpickup.data.model.*

class SchedulePickupMapper {

    fun mapSchedulePickup(response: List<SomConfirmReqPickup.Data.MpLogisticPreShipInfo.DataSuccess.ScheduleTime.ScheduleResponse>, day: String): List<ScheduleTime> {
        return response.map {
            ScheduleTime(
                    key = it.key,
                    start = it.start,
                    end = it.end,
                    day = day
            )
        }
    }

}