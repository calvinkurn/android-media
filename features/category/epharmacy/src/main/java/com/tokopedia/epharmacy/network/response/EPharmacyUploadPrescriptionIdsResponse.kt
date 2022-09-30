package com.tokopedia.epharmacy.network.response


import com.google.gson.annotations.SerializedName

data class EPharmacyUploadPrescriptionIdsResponse(
    @SerializedName("confirmPrescriptionIDs")
    val confirmPrescriptionIDs : EPharmacyUploadPrescriptionData?,
) {
    data class EPharmacyUploadPrescriptionData(
        @SerializedName("success")
        val success: Boolean?
    )
}