package com.tokopedia.sellerorder.reschedule_pickup.data.model

data class RescheduleDetailModel(
    val options: RescheduleOptionsModel = RescheduleOptionsModel(),
    val shipperId: Long = 0,
    val invoice: String = "",
    val errorMessage: String = ""
)

data class RescheduleOptionsModel(
    val dayOptions: List<RescheduleDayOptionModel> = emptyList(),
    val reasonOptionModel: List<RescheduleReasonOptionModel> = emptyList()
)

data class RescheduleReasonOptionModel(
    val reason: String = ""
)

data class RescheduleTimeOptionModel(
    val time: String = "",
    val formattedTime: String = ""
)


data class RescheduleDayOptionModel(
    val formattedDay: String = "",
    val day: String = "",
    val timeOptions: List<RescheduleTimeOptionModel> = emptyList()
)
