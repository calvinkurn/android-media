package com.tokopedia.logisticseller.data.model

data class RescheduleDetailModel(
    val options: RescheduleOptionsModel = RescheduleOptionsModel(),
    val courierName: String = "",
    val invoice: String = "",
    val errorMessage: String = "",
    val ticker: String = "",
    val appLink: String = ""
)

data class RescheduleOptionsModel(
    val dayOptions: List<RescheduleDayOptionModel> = emptyList(),
    val reasonOptionModel: List<RescheduleReasonOptionModel> = emptyList()
)

data class RescheduleReasonOptionModel(
    val reason: String = ""
) {
    override fun toString(): String {
        return reason
    }
}

data class RescheduleTimeOptionModel(
    val time: String = "",
    val formattedTime: String = "",
    val etaPickup: String = ""
) {
    override fun toString(): String {
        return formattedTime
    }
}

data class RescheduleDayOptionModel(
    val day: String = "",
    val timeOptions: List<RescheduleTimeOptionModel> = emptyList()
) {
    override fun toString(): String {
        return day
    }
}
