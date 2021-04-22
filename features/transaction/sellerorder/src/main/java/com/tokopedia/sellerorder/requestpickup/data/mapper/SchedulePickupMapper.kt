package com.tokopedia.sellerorder.requestpickup.data.mapper

import com.tokopedia.sellerorder.requestpickup.data.model.SchedulePickupModel
import com.tokopedia.sellerorder.requestpickup.data.model.SomConfirmReqPickup
import com.tokopedia.sellerorder.requestpickup.data.model.Today
import com.tokopedia.sellerorder.requestpickup.data.model.Tomorrow

class SchedulePickupMapper() {

    fun mapSchedulePickup(response: SomConfirmReqPickup.Data.MpLogisticPreShipInfo.DataSuccess.ScheduleTime): SchedulePickupModel {
        return SchedulePickupModel().apply {
            today = mapTodaySchedule(response.today)
            tomorrow = mapTomorrowSchedule(response.tomorrow)
        }
    }

    private fun mapTodaySchedule(data: List<SomConfirmReqPickup.Data.MpLogisticPreShipInfo.DataSuccess.ScheduleTime.Today>): List<Today> {
        return data.map {
            Today(
                    it.keyToday,
                    it.startToday,
                    it.endToday
            )
        }
    }

    private fun mapTomorrowSchedule(data: List<SomConfirmReqPickup.Data.MpLogisticPreShipInfo.DataSuccess.ScheduleTime.Tomorrow>): List<Tomorrow> {
        return data.map {
            Tomorrow(
                    it.keyTomorrow,
                    it.startTomorrow,
                    it.endTomorrow
            )
        }
    }
}