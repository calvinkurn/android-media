package com.tokopedia.logisticcart.scheduledelivery.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.scheduledelivery.ValidationMetadata

data class ScheduleDeliveryParam(
    @SerializedName("wh_id")
    val warehouseId: Long = 0,
    @SerializedName("validation_metadata")
    val validationMetadata: ValidationMetadata = ValidationMetadata(),
    @SerializedName("cat_id")
    val catId: String = "",
) {
    fun toMap() = mapOf(
        "wh_id" to warehouseId,
        "validation_metadata" to validationMetadata,
        "cat_id" to catId,
    )
}
