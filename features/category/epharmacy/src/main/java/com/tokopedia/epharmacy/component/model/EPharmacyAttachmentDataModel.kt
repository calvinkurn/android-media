package com.tokopedia.epharmacy.component.model

import android.os.Bundle
import com.tokopedia.epharmacy.adapters.factory.EPharmacyAdapterFactory
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup as EG

data class EPharmacyAttachmentDataModel(
    val name: String = "",
    val type: String = "",
    val epharmacyGroupId: String?,
    val enablerName: String?,
    val enablerLogo: String?,
    val chooserLogo: String?,
    val shopInfo: EG.ProductsInfo?,
    val consultationStatus: Int?,
    val consultationString: String?,
    val prescription: List<EG.ConsultationData.Prescription?>?,
    val partnerConsultationId: String?,
    val tokoConsultationId: Long?,
    var prescriptionImages: List<EG.PrescriptionImage?>?,
    val prescriptionSource: List<String?>?,
    val consultationSource: EG.ConsultationSource?,
    val consultationData: EG.ConsultationData?,
    var productsIsExpanded: Boolean = false,
    var prescriptionCTA: EG.PrescriptionCTA?,
    val showUploadWidget: Boolean = false,
    val showDivider: Boolean = true,
    var isError: Boolean = false,
    var isFirstError: Boolean = false
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
