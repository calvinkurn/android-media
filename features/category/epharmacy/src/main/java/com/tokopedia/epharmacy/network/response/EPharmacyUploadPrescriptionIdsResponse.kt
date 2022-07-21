package com.tokopedia.epharmacy.network.response


import com.google.gson.annotations.SerializedName

data class EPharmacyUploadPrescriptionIdsResponse(
    @SerializedName("confirmPrescriptionIDs")
    val confirmPrescriptionIDs : Data?,
) {
    data class Data(
        @SerializedName("success")
        val success: Boolean?,
        @SerializedName("header")
        val header: EPharmacyHeader?
    ){
        data class EPharmacyHeader(
            @SerializedName("error_code")
            val errorCode: String?,
            @SerializedName("error_message")
            val errorMessage: String?
        )
    }
}