package com.tokopedia.epharmacy.network.response


import com.google.gson.annotations.SerializedName

data class EPharmacyUploadPrescriptionIdsResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("error")
    val error: String?
) {
    data class Data(
        @SerializedName("success")
        val success: Boolean?
    )
}