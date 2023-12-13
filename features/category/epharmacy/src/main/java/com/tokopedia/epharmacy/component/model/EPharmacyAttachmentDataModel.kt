package com.tokopedia.epharmacy.component.model

import android.os.Bundle
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.epharmacy.adapters.factory.EPharmacyAdapterFactory
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup as EG

data class EPharmacyAttachmentDataModel(
    override val name: String,
    override val type: String,
    val epharmacyGroupId: String?,
    val orderTitle: String?,
    val enablerName: String?,
    val enablerLogo: String?,
    val chooserLogo: String?,
    val shopInfo: EG.ProductsInfo?,
    val consultationStatus: Int?,
    val consultationString: String?,
    val price: String?,
    val operatingSchedule: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ConsultationSource.OperatingSchedule?,
    val note: String?,
    val ticker: EG.Ticker?,
    var product: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product?,
    val prescription: List<EG.ConsultationData.Prescription?>?,
    val partnerConsultationId: String?,
    val tokoConsultationId: String?,
    var prescriptionImages: List<EG.PrescriptionImage?>?,
    val consultationSource: EG.ConsultationSource?,
    val consultationData: EG.ConsultationData?,
    var productsIsExpanded: Boolean = false,
    var prescriptionCTA: EG.PrescriptionCTA?,
    var subProductsDataModel: List<BaseEPharmacyDataModel>?,
    val showUploadWidget: Boolean = false,
    val showDivider: Boolean = true,
    var isError: Boolean = false,
    var isFirstError: Boolean = false,
    var isAccordionEnable: Boolean = true
) :
    BaseEPharmacySimpleDataModelImpl(name, type) {

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
