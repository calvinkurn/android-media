package com.tokopedia.epharmacy.network.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse

data class EPharmacyInitiateConsultationResponse(
    @SerializedName("initiateConsultation")
    var getInitiateConsultation: InitiateConsultation? = null,
    @SerializedName("epharmacy_group_id")
    var epharmacyGroupId: String?
)

data class InitiateConsultation(
    @SerializedName("data")
    val initiateConsultationData: InitiateConsultationData?,
    @SerializedName("error")
    val error: String? = "",
    @SerializedName("header")
    val header: EPharmacyHeader? = null
) {
    data class InitiateConsultationData(
        @SerializedName("toko_consultation_id")
        val tokoConsultationId: String?,
        @SerializedName("consultation_source")
        val consultationSource: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ConsultationSource?
    )
}
