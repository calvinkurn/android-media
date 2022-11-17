package com.tokopedia.epharmacy.network.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class EPharmacyReminderScreenParam(
    @SerializedName("input")
    val input: Input
) : GqlParam {
    data class Input(
        @SerializedName("reminder_type")
        val reminderType: Long,
        @SerializedName("params")
        val prescriptions: EpharmacyConsultationInfoParams? = null
    ) {
        data class EpharmacyConsultationInfoParams(
            @SerializedName("consultation_source_id")
            val consultationSourceId: Long? = null
        )
    }
}
