package com.tokopedia.epharmacy.utils

import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyAccordionProductDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyAttachmentDataModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup as EGroup
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo as EProductInfo

object EPharmacyMapper {

    fun mapGroupsToAttachmentComponents(
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
            group.consultationSource?.price,
            group.consultationSource?.operatingSchedule,
            group.consultationSource?.note,
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
            getSubProductsModel(info),
            isLastIndex(group.shopInfo, shopIndex),
            (isLastIndex(group.shopInfo, shopIndex) && isLastGroup).not()
        )
    }

    private fun getSubProductsModel(shopInfo: EProductInfo?): List<BaseEPharmacyDataModel>? {
        return getProductVisitablesWithoutFirst(shopInfo)
    }

    private fun getProductVisitablesWithoutFirst(shopInfo: EProductInfo?): List<BaseEPharmacyDataModel> {
        val productSubList = arrayListOf<EPharmacyAccordionProductDataModel>()
        shopInfo?.products?.forEachIndexed { index, product ->
            if (index != 0) {
                productSubList.add(EPharmacyAccordionProductDataModel("${PRODUCT_COMPONENT}_${product?.productId}", PRODUCT_COMPONENT, product))
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
        return if(shopIndex == 0 && group.ticker?.title?.isNotBlank() == true){
            group.ticker
        } else null
    }

    private fun getQuantityChangedModel(info: EGroup.ProductsInfo?): EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.EpharmacyGroup.ProductsInfo.Product.QtyComparison? {
        return getQuantityChangedModelProduct(info?.products?.firstOrNull())
    }

    private fun getQuantityChangedModelProduct(product: EGroup.ProductsInfo.Product?) : EGroup.ProductsInfo.Product.QtyComparison?{
        return product?.qtyComparison.apply { product?.qtyComparison?.subTotal = product?.price.orZero() *  product?.qtyComparison?.initialQty?.toDouble().orZero()}
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
