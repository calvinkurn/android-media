package com.tokopedia.logisticseller.ui.requestpickup.data.mapper

import com.tokopedia.logisticseller.ui.requestpickup.data.model.ScheduleTime
import com.tokopedia.logisticseller.ui.requestpickup.data.model.SomConfirmReqPickup

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
