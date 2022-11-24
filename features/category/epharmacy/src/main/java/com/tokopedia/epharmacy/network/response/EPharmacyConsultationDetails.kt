package com.tokopedia.epharmacy.network.response


import com.google.gson.annotations.SerializedName
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse

data class EPharmacyConsultationDetailsResponse(
    @SerializedName("getEpharmacyConsultationDetails")
    var getEpharmacyConsultationDetails: EPharmacyConsultationDetails? = null
)

data class EPharmacyConsultationDetails(
    @SerializedName("data")
    val epharmacyConsultationDetailsData: EPharmacyConsultationDetailsData?,
    @SerializedName("error")
    val error: String? = "",
    @SerializedName("header")
    val header: Header? = null
) {
    data class EPharmacyConsultationDetailsData(
        @SerializedName("consultation_data")
        val consultationData: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ConsultationData?
    )

    data class Header(
        @SerializedName("code")
        val code: Int? = null,
        @SerializedName("server_process_time")
        val serverProcessTime: String? = ""
    )
}
