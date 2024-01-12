package com.tokopedia.shop.info.domain.entity

data class ShopOperationalHourWithStatus(
    val day: Day,
    val startTime: String,
    val endTime: String,
    val status: Status
) {
    enum class Day(val id: Int) {
        MONDAY(1),
        TUESDAY(2),
        WEDNESDAY(3),
        THURSDAY(4),
        FRIDAY(5),
        SATURDAY(6),
        SUNDAY(7),
        UNDEFINED(-1)
    }
    enum class Status {
        OPEN,
        CLOSED,
        OPEN24HOURS
    }

}
