package com.tokopedia.epharmacy.component.model

import android.os.Bundle
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.epharmacy.adapters.factory.EPharmacyAdapterFactory
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel

data class EPharmacyAttachmentDataModel(
    val name: String = "",
    val type: String = "",
    val epharmacyGroupId: String?,
    val enablerName: String?,
    val enablerLogo: String?,
    val shopInfo: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo?,
    val consultationStatus: Int?,
    val consultationString: String?,
    val prescription: List<EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ConsultationData.Prescription?>?,
    val partnerConsultationId: String?,
    val tokoConsultationId: Long?,
    var prescriptionImages: List<EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.PrescriptionImage?>?,
    val prescriptionSource: List<String?>?,
    val consultationSource: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ConsultationSource?,
    val consultationData: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ConsultationData?,
    var productsIsExpanded: Boolean = false,
    var prescriptionCTA: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.PrescriptionCTA?,
    val showUploadWidget: Boolean = false,
    val showDivider: Boolean = true,
    var isError: Boolean = false
) :
    BaseEPharmacyDataModel {

    override fun name(): String = name

    override fun type(): String = type

    override fun type(typeFactory: EPharmacyAdapterFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsWith(newData: BaseEPharmacyDataModel): Boolean {
        return this == newData
    }

    override fun getChangePayload(newData: BaseEPharmacyDataModel): Bundle? {
        return null
    }
}
