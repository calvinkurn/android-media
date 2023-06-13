package com.tokopedia.epharmacy.network.response

import com.google.gson.annotations.SerializedName

data class EPharmacyPrescriptionUploadResponse(
    @SerializedName("data")
    val `data`: List<EPharmacyPrescriptionData?>?,
    @SerializedName("error")
    val error: String?,
    @SerializedName("header")
    val header: EPharmacyHeader?
) {
    data class EPharmacyPrescriptionData(
        @SerializedName("error_msg")
        val errorMsg: String?,
        @SerializedName("id")
        val id: Long?,
        @SerializedName("prescription_id")
        val prescriptionId: Long?
    )
}
