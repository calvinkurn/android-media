package com.tokopedia.checkout.data.model.response.prescription


import com.google.gson.annotations.SerializedName

data class GetPrescriptionIdsResponse(
    @SerializedName("getEpharmacyCheckoutData")
    val detailData : EPharmacyPrescriptionDetailData?
) {

    data class EPharmacyPrescriptionDetailData(
        @SerializedName("checkoutId")
        val checkoutId: String?,
        @SerializedName("prescription_images")
        val prescriptions: List<Prescription?>?
    )

    data class Prescription(
        @SerializedName("prescription_id")
        val prescriptionId: String?,
        @SerializedName("status")
        val status: String?,
    )
}