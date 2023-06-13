package com.tokopedia.epharmacy.network.request


import com.google.gson.annotations.SerializedName

data class UploadPrescriptionRequest(
    @SerializedName("prescriptions")
    val prescriptionRequests: List<PrescriptionRequest?>?
) {
    data class PrescriptionRequest(
        @SerializedName("data")
        val `data`: String?,
        @SerializedName("format")
        val format: String?,
        @SerializedName("id")
        val id: Long?,
        @SerializedName("source")
        val source: String?
    )
}