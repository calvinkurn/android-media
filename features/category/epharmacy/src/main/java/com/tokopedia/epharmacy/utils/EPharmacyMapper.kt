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
            group.consultationSource?.price,
            group.consultationSource?.operatingSchedule?.duration,
            getTickerData(group,shopIndex),
            getQuantityChangedModel(info),
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

    private fun getQuantityChangedModel(group: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup): EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.QuantityChangedModel? {
        if(group.consultationData?.medicalRecommendation.isNullOrEmpty()){
    private fun getTickerData(group: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup, shopIndex: Int): EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.Ticker? {
        return if(shopIndex == 0 && group.ticker?.title?.isNotBlank() == true){
            group.ticker
        } else null
    }

    private fun getQuantityChangedModel(info: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo?): EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.QuantityChangedModel? {
        return getQuantityChangedModelProduct(info?.products?.firstOrNull())
    }

    private fun getQuantityChangedModelProduct(product: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product?) : EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.QuantityChangedModel?{
        if(product?.qtyComparison == null){
            return null
        }
        return EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.QuantityChangedModel(
            product.qtyComparison?.initialQty,100.0,
            product.qtyComparison?.recommendedQty,
            product.qtyComparison?.initialQty
        )
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
