package com.tokopedia.checkout.data.model.response.prescription


import com.google.gson.annotations.SerializedName

data class GetPrescriptionIdsResponse(
    @SerializedName("checkoutId")
    val checkoutId: String?,
    @SerializedName("prescriptions")
    val prescriptions: List<Prescription?>?
) {
    data class Prescription(
        @SerializedName("prescriptionId")
        val prescriptionId: String?,
        @SerializedName("status")
        val status: String?
    )
}