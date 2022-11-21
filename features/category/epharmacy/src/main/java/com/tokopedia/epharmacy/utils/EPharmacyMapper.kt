package com.tokopedia.epharmacy.utils

import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.epharmacy.component.model.EPharmacyAttachmentDataModel
import com.tokopedia.epharmacy.network.response.PrescriptionStatusCount

object EPharmacyMapper {

    fun mapGroupsToAttachmentComponents(
        group: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup,
        info: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo?,
        index: Int,
        isLastShopOfGroup: Boolean,
        isLastGroup: Boolean
    ): EPharmacyAttachmentDataModel {
        return EPharmacyAttachmentDataModel(
            getUniqueModelName(group.epharmacyGroupId, index, isLastShopOfGroup), GROUP_COMPONENT,
            group.epharmacyGroupId,
            group.consultationSource?.enablerName,
            group.consultationSource?.enablerLogoUrl,
            info,
            group.consultationData?.consultationStatus,
            group.consultationData?.consultationString,
            group.consultationData?.prescription,
            group.consultationData?.partnerConsultationId,
            group.consultationData?.tokoConsultationId,
            group.prescriptionImages,
            group.prescriptionSource,
            group.consultationSource,
            false,
            group.prescriptionCTA,
            isLastIndex(group.shopInfo, index),
            (isLastIndex(group.shopInfo, index) && isLastGroup).not()
        )
    }

    fun getUniqueModelName(ePharmacyGroupId: String?, index: Int, isLastShopOfGroup: Boolean): String {
        return if (isLastShopOfGroup) {
            "${ePharmacyGroupId}_$index"
        } else {
            "${ePharmacyGroupId}_N"
        }
    }

    fun isLastIndex(list: List<Any?>?, index: Int): Boolean {
        return index == ((list?.size ?: 0) - 1)
    }

    fun getPrescriptionCount(consultationStatus: ArrayList<Int>): PrescriptionStatusCount {
        val statusCount = PrescriptionStatusCount(0, 0, 0)
        consultationStatus.forEach { status ->
            when (status) {
                EPharmacyConsultationStatus.REJECTED.status, EPharmacyConsultationStatus.EXPIRED.status -> {
                    statusCount.rejected += 1
                }
                EPharmacyConsultationStatus.APPROVED.status -> {
                    statusCount.approved += 1
                }
                EPharmacyConsultationStatus.ACTIVE.status -> {
                    statusCount.active += 1
                }
            }
        }
        return statusCount
    }
}
