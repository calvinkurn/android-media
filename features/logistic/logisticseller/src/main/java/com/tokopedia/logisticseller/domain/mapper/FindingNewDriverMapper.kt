package com.tokopedia.logisticseller.domain.mapper

import com.tokopedia.logisticseller.data.model.FindingNewDriverModel
import com.tokopedia.logisticseller.data.response.NewDriverAvailabilityResponse
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.toDate
import java.util.*
import javax.inject.Inject

class FindingNewDriverMapper @Inject constructor() {

    fun map(newDriverAvailabilityData: NewDriverAvailabilityResponse.NewDriverAvailabilityData): FindingNewDriverModel {
        return FindingNewDriverModel(
            invoice = newDriverAvailabilityData.invoice,
            message = newDriverAvailabilityData.message,
            availableTime = newDriverAvailabilityData.availableTime
        ).apply {
            isEnable = newDriverAvailabilityData.availabilityRetry
            if (isEnable.not()) {
                calendar = availableTime.convertToCalendar()
            }
        }
    }

    private fun String.convertToCalendar(): Calendar {
        val availableTime = toDate("yyyy-MM-dd'T'HH:mm:ssZ")
        val calendar = DateUtil.getCurrentCalendar()
        calendar.time = availableTime
        return calendar
    }
}
