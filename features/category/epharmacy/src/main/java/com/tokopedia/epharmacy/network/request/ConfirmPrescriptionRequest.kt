package com.tokopedia.epharmacy.network.request


import com.google.gson.annotations.SerializedName

data class ConfirmPrescriptionRequest(
    @SerializedName("input")
    val input: Input?
) {
    data class Input(
        @SerializedName("checkout_id")
        val id: String?,
        @SerializedName("prescriptions")
        val prescriptions: List<Prescription?>?
    ) {
        data class Prescription(
            @SerializedName("prescription_id")
            val prescriptionId: Long?
        )
    }
}