package com.tokopedia.epharmacy.network.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class EpharmacyUserReminderParam(
    @SerializedName("input")
    val input: Input
) : GqlParam {
    data class Input(
        @SerializedName("reminder_type")
        val reminderType: Int,
        @SerializedName("params")
        val params: EpharmacyConsultationInfoParams? = null
    ) {
        data class EpharmacyConsultationInfoParams(
            @SerializedName("consultation_source_id")
            val consultationSourceId: Long? = null,
            @SerializedName("source_time")
            val sourceTime: String = ""
        )
    }
}
