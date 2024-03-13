package com.tokopedia.epharmacy.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse

data class EPharmacyConsultationDetailsResponse(
    @SerializedName("getEpharmacyConsultationDetails")
    @Expose
    var getEpharmacyConsultationDetails: EPharmacyConsultationDetails? = null
)

data class EPharmacyConsultationDetails(
    @SerializedName("data")
    @Expose
    val epharmacyConsultationDetailsData: EPharmacyConsultationDetailsData?,
    @SerializedName("error")
    @Expose
    val error: String? = "",
) {
    data class EPharmacyConsultationDetailsData(
        @SerializedName("consultation_data")
        @Expose
        val consultationData: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ConsultationData?
    )
}
