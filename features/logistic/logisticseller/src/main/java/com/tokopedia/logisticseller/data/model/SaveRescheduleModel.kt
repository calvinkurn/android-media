package com.tokopedia.logisticseller.data.model

data class SaveRescheduleModel(
    val success: Boolean = false,
    val message: String = "",
    val status: String = "",
    val etaPickup: String = "",
    val errors: List<String> = emptyList(),
    val openDialog: Boolean = false
)
