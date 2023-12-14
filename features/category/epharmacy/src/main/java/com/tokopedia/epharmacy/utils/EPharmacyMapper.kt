package com.tokopedia.epharmacy.utils

import com.tokopedia.common_epharmacy.EPHARMACY_PPG_QTY_CHANGE
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyAccordionProductDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyAttachmentDataModel
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup as EGroup
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo as EProductInfo

object EPharmacyMapper {

    fun mapGroupsToAttachmentComponents(
        source: String?,
        group: EGroup,
        orderNumber: Int,
        info: EGroup.ProductsInfo?,
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
            group.consultationSource?.priceStr,
            group.consultationSource?.operatingSchedule,
            group.consultationSource?.note,
            getTickerData(group, shopIndex),
            getProductModel(info),
            group.consultationData?.prescription,
            group.consultationData?.partnerConsultationId,
            group.consultationData?.tokoConsultationId,
            group.prescriptionImages,
            group.consultationSource,
            group.consultationData,
            false,
            group.prescriptionCTA,
            getSubProductsModel(
                info,
                group.consultationSource?.enablerName,
                group.consultationData?.tokoConsultationId,
                group.epharmacyGroupId
            ),
            isLastIndex(group.shopInfo, shopIndex),
            (isLastIndex(group.shopInfo, shopIndex) && isLastGroup).not(),
            isAccordionEnable = getIsProductExpanded(source),
        )
    }

    private fun getIsProductExpanded(source: String?): Boolean {
        return source == EPHARMACY_PPG_QTY_CHANGE
    }

    private fun getSubProductsModel(
        shopInfo: EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo?,
        enablerName: String?,
        tConsultationId: String?,
        eGroupId: String?
    ): List<BaseEPharmacyDataModel> {
        return getProductVisitablesWithoutFirst(shopInfo, enablerName, tConsultationId, eGroupId)
    }

    private fun getProductVisitablesWithoutFirst(
        shopInfo: EProductInfo?,
        enablerName: String?,
        tConsultationId: String?,
        eGroupId: String?
    ): List<BaseEPharmacyDataModel> {
        val productSubList = arrayListOf<EPharmacyAccordionProductDataModel>()
        shopInfo?.products?.forEachIndexed { index, product ->
            if (index != 0) {
                productSubList.add(
                    EPharmacyAccordionProductDataModel(
                        "${PRODUCT_COMPONENT}_${product?.productId}",
                        PRODUCT_COMPONENT,
                        product,
                        enablerName,
                        tConsultationId,
                        eGroupId
                    )
                )
            }
        }
        return productSubList
    }

    private fun getPartnerLogo(
        group: EGroup,
        info: EProductInfo?
    ): String? {
        return info?.partnerLogoUrl
    }

    private fun getTickerData(group: EGroup, shopIndex: Int): EGroup.Ticker? {
        return if (shopIndex == 0 && group.ticker?.message?.isNotBlank() == true) {
            group.ticker
        } else {
            null
        }
    }

    private fun getProductModel(info: EGroup.ProductsInfo?): EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product? {
        return info?.products?.firstOrNull()
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
