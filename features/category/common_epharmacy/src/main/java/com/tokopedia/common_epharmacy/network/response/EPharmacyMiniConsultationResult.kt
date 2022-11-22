package com.tokopedia.common_epharmacy.network.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class EPharmacyMiniConsultationResult(
    @SerializedName("epharmacy_group_id")
    val epharmacyGroupId: String?,
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
    val prescriptionImages: List<EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.PrescriptionImage?>?

) : Parcelable
