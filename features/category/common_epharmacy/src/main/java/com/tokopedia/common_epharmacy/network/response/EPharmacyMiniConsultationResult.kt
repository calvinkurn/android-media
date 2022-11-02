package com.tokopedia.common_epharmacy.network.response

import com.google.gson.annotations.SerializedName

data class EPharmacyMiniConsultationResult(
    @SerializedName("products_info")
    val shopInfo: List<EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo?>?,
    @SerializedName("consultation_status")
    val consultationStatus: Int?,
    @SerializedName("consultation_string")
    val consultationString: String?,
    @SerializedName("prescription")
    val prescription: List<EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ConsultationData.Prescription?>?,
    @SerializedName("partner_consultation_id")
    val partnerConsultationId: String?,
    @SerializedName("toko_consultation_id")
    val tokoConsultationId: String?,
    @SerializedName("prescription_images")
    val prescriptionImages: List<EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.PrescriptionImage?>?,
) {

}
