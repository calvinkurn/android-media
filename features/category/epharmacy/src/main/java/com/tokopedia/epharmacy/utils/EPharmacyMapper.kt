package com.tokopedia.epharmacy.utils

import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.epharmacy.component.model.EPharmacyAttachmentDataModel
import com.tokopedia.kotlin.extensions.view.toLongOrZero

object EPharmacyMapper {

    fun mapGroupsToAttachmentComponents(
        group: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup,
        info: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo?,
        shopIndex: Int,
        isLastGroup: Boolean
    ): EPharmacyAttachmentDataModel {
        return EPharmacyAttachmentDataModel(
            getUniqueModelName(group.epharmacyGroupId, shopIndex), GROUP_COMPONENT,
            group.epharmacyGroupId,
            group.consultationSource?.enablerName,
            group.shopInfo?.firstOrNull()?.partnerLogoUrl,
            group.consultationSource?.enablerLogoUrl,
            info,
            group.consultationData?.consultationStatus,
            group.consultationData?.consultationString,
            group.consultationData?.prescription,
            group.consultationData?.partnerConsultationId,
            group.consultationData?.tokoConsultationId.toLongOrZero(),
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

    private fun getUniqueModelName(ePharmacyGroupId: String?, index: Int): String {
        return "${ePharmacyGroupId}_$index"
    }

    fun isLastIndex(list: List<Any?>?, index: Int): Boolean {
        return index == ((list?.size ?: 0) - 1)
    }
}
