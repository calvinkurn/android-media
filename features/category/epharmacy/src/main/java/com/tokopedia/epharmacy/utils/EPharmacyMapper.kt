package com.tokopedia.epharmacy.utils

import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.epharmacy.component.model.EPharmacyAttachmentDataModel
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup as EGroup
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo as EProductInfo

object EPharmacyMapper {

    fun mapGroupsToAttachmentComponents(
        group: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup,
        orderNumber: Int,
        info: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo?,
        shopIndex: Int,
        isLastGroup: Boolean
    ): EPharmacyAttachmentDataModel {
        return EPharmacyAttachmentDataModel(
            getUniqueModelName(group.epharmacyGroupId, shopIndex), GROUP_COMPONENT,
            group.epharmacyGroupId,
            getOrderTitle(orderNumber),
            group.consultationSource?.enablerName,
            getPartnerLogo(group, info),
            group.consultationSource?.enablerLogoUrl,
            info,
            group.consultationData?.consultationStatus,
            group.consultationData?.consultationString,
            "Rp0",
            "15 Merit",
            group.consultationData?.prescription,
            group.consultationData?.partnerConsultationId,
            group.consultationData?.tokoConsultationId,
            group.prescriptionImages,
            group.prescriptionSource,
            group.consultationSource,
            group.consultationData,
            false,
            group.prescriptionCTA,
            isLastIndex(group.shopInfo, shopIndex),
            (isLastIndex(group.shopInfo, shopIndex) && isLastGroup).not()
        )
    }

    private fun getPartnerLogo(
        group: EGroup,
        info: EProductInfo?
    ): String? {
        return info?.partnerLogoUrl
    }

    private fun getUniqueModelName(ePharmacyGroupId: String?, index: Int): String {
        return "${ePharmacyGroupId}_$index"
    }

    fun isLastIndex(list: List<Any?>?, index: Int): Boolean {
        return index == ((list?.size ?: 0) - 1)
    }

    private fun getOrderTitle(orderNumber: Int): String {
        return "Pesanan $orderNumber"
    }
}
