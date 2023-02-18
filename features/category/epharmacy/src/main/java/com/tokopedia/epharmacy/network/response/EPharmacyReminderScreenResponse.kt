package com.tokopedia.epharmacy.network.response

import com.google.gson.annotations.SerializedName

data class EPharmacyReminderScreenResponse(
    @SerializedName("submitEpharmacyUserReminder")
    val submitEpharmacyUserReminderData: SubmitEPharmacyUserReminderData? = null
) {
    data class SubmitEPharmacyUserReminderData(
        @SerializedName("header")
        val header: EPharmacyHeader? = null,
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
