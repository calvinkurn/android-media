package com.tokopedia.epharmacy.network.response

import com.google.gson.annotations.SerializedName

data class EPharmacyReminderScreenResponse(
    @SerializedName("submitEpharmacyUserReminder")
    val submitEpharmacyUserReminderData: SubmitEpharmacyUserReminderData? = null
) {
    data class SubmitEpharmacyUserReminderData(
        @SerializedName("header")
        val header: EPharmacyPrescriptionUploadResponse.Header? = null,
        @SerializedName("data")
        val data: ReminderScreenData? = null
    ) {
        data class ReminderScreenData(
            @SerializedName("is_success")
            val isSuccess: Boolean? = false,
            @SerializedName("error")
            val error: String? = ""
        )
    }
}
