package com.tokopedia.epharmacy.network.request


import com.google.gson.annotations.SerializedName

data class EpharmacyConfirmPrescriptionParam(
    @SerializedName("input")
    val input: Input?
) {
    data class Input(
        @SerializedName("order_id")
        val id: Long,
        @SerializedName("prescriptions")
        val prescriptions: List<PrescriptionRequest?>?
    )
}