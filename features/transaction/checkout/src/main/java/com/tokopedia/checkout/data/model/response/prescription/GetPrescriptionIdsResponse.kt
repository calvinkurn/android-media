package com.tokopedia.checkout.data.model.response.prescription


import com.google.gson.annotations.SerializedName

data class GetPrescriptionIdsResponse(
    @SerializedName("getEpharmacyCheckoutData")
    val detailData : EPharmacyCheckoutData?
) {

    data class EPharmacyCheckoutData(
        @SerializedName("data")
        val prescriptionData: EPharmacyPrescriptionDetailData?
    ) {
        data class EPharmacyPrescriptionDetailData(
            @SerializedName("checkout_id")
            val checkoutId: String?,
            @SerializedName("prescription_images")
            val prescriptions: List<Prescription?>?
        )

        data class Prescription(
            @SerializedName("prescription_id")
            val prescriptionId: String?
        )
    }

}