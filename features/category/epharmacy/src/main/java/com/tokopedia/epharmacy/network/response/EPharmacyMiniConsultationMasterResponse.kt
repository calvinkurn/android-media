package com.tokopedia.epharmacy.network.response


import com.google.gson.annotations.SerializedName

data class GetEpharmacyMiniConsultationStaticData(
    @SerializedName("getEpharmacyStaticData")
    var getEpharmacyStaticData: EPharmacyMiniConsultationMasterResponse? = null
)

data class EPharmacyMiniConsultationMasterResponse(
    @SerializedName("data")
    val `data`: EPharmacyMiniConsultationData?,
    @SerializedName("error")
    val error: String? = "",
    @SerializedName("header")
    val header: Header? = null
) {
    data class EPharmacyMiniConsultationData(
        @SerializedName("info_title")
        val infoTitle: String? = "",
        @SerializedName("info_text")
        val infoText: String? = "",
        @SerializedName("step_title")
        val stepTitle: String? = "",
        @SerializedName("steps")
        val steps: List<ConsultationSteps?>? = null,
        @SerializedName("logo_url")
        val logoUrl: String? = "",
        @SerializedName("logo_title")
        val logoTitle: String? = "",
    ) {
        data class ConsultationSteps(
            @SerializedName("image_url")
            val imageUrl: String? = "",
            @SerializedName("title")
            val title: String? = "",
            @SerializedName("subtitle")
            val subtitle: String? = ""
        )
    }

    data class Header(
        @SerializedName("code")
        val code: Int? = null,
        @SerializedName("server_process_time")
        val serverProcessTime: String? = ""
    )
}
