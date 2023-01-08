package com.tokopedia.epharmacy.network.response


import com.google.gson.annotations.SerializedName

data class EPharmacyPrescriptionUploadResponse(
    @SerializedName("data")
    val `data`: List<EPharmacyPrescriptionData?>?,
    @SerializedName("error")
    val error: String?,
    @SerializedName("header")
    val header: Header?
) {
    data class EPharmacyPrescriptionData(
        @SerializedName("error_msg")
        val errorMsg: String?,
        @SerializedName("id")
        val id: Long?,
        @SerializedName("prescription_id")
        val prescriptionId: Long?
    )

    data class Header(
        @SerializedName("code")
        val code: Int?,
        @SerializedName("server_prosess_time")
        val serverProsessTime: String?
    )
}