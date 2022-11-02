package com.tokopedia.epharmacy.component.model

import android.os.Bundle
import com.google.gson.annotations.SerializedName
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.epharmacy.adapters.factory.EPharmacyAdapterFactory
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.network.response.EPharmacyProduct
import com.tokopedia.epharmacy.network.response.PrescriptionImage

data class EPharmacyAttachmentDataModel(val name : String = "", val type : String = "",
                                        val orderName : String?,
                                        val epharmacyGroupId : String?,
                                        val partnerName : String?, val partnerLogo: String?,
                                        val shopInfo : EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo?,
                                        val consultationStatus: Int?,
                                        val consultationString: String?,
                                        val prescription: List<EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ConsultationData.Prescription?>?,
                                        val partnerConsultationId: String?,
                                        val tokoConsultationId: String?,
                                        val prescriptionImages: List<EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.PrescriptionImage?>?,
                                        val prescriptionSource: List<String?>?,
                                        var productsIsExpanded : Boolean = false,
                                        val uploadWidgetText : String?, val uploadWidgetIcon : String?,
                                        val uploadWidgetAppLink : String?,
                                        val uploadWidget : Boolean = false
)
    : BaseEPharmacyDataModel {

    override fun name(): String = name

    override fun type(): String  = type

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
