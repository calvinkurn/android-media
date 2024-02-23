package com.tokopedia.epharmacy.utils

import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.common_epharmacy.network.response.EPharmacyPrepareProductsGroupResponse
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyAccordionProductDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyAttachmentDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyShimmerDataModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.toZeroIfNull

class EPharmacyAttachmentUiUpdater(var mapOfData: LinkedHashMap<String, BaseEPharmacyDataModel>) {

    fun updateModel(model: BaseEPharmacyDataModel) {
        updateData(model.name(), model)
    }

    private fun updateData(key: String, baseCatalogDataModel: BaseEPharmacyDataModel) {
        mapOfData[key] = baseCatalogDataModel
    }

    fun addShimmer() {
        mapOfData.clear()
        updateModel(EPharmacyShimmerDataModel(SHIMMER_COMPONENT_1, SHIMMER_COMPONENT))
        updateModel(EPharmacyShimmerDataModel(SHIMMER_COMPONENT_2, SHIMMER_COMPONENT))
    }

    fun addQuantityEditorShimmer() {
        mapOfData.clear()
        updateModel(EPharmacyShimmerDataModel(SHIMMER_COMPONENT_1, SHIMMER_COMPONENT))
    }

    fun getUpdateCartParams(userCartContent: List<EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.UpdateCart>?): List<UpdateCartRequest> {
        if(userCartContent == null)
            return emptyList()

        val cartsRequest = arrayListOf<UpdateCartRequest>()
        val mapUserCartContent : MutableMap<Long, EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.UpdateCart> = userCartContent.associateBy{ it.productId}.toMutableMap()
        mapOfData.values.filterIsInstance<EPharmacyAttachmentDataModel>().forEach { epDataModel ->
            val currProductId : Long = epDataModel.shopInfo?.products?.firstOrNull()?.productId.orZero()
            if(mapUserCartContent.contains(currProductId)){
                mapUserCartContent[currProductId] =
                    EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.UpdateCart(
                        productId = currProductId,
                        cartId = epDataModel.shopInfo?.products?.firstOrNull()?.cartId.orZero(),
                        currentQuantity = epDataModel.product?.qtyComparison?.currentQty ?: (epDataModel.shopInfo?.products?.firstOrNull()?.quantity?.toIntOrZero())
                    )
            }

            epDataModel.subProductsDataModel?.filterIsInstance<EPharmacyAccordionProductDataModel>()?.forEach { epPDM ->
                val currSubProductId : Long = epPDM.product?.productId.orZero()
                if(mapUserCartContent.contains(currSubProductId)){
                    mapUserCartContent[currSubProductId] =
                        EPharmacyPrepareProductsGroupResponse.EPharmacyPrepareProductsGroupData.GroupData.UpdateCart(
                            productId = currSubProductId,
                            cartId = epPDM.product?.cartId.orZero(),
                            currentQuantity = epPDM.product?.qtyComparison?.currentQty ?: (epPDM.product?.quantity?.toIntOrZero() ?: 0)
                        )
                }
            }
        }

        for (cart in mapUserCartContent){
            cartsRequest.add(
                UpdateCartRequest(
                    productId = cart.value.productId.toString(),
                    cartId = cart.value.cartId.toString(),
                    quantity =cart.value.currentQuantity.toZeroIfNull()
                )
            )
        }

        return cartsRequest
    }

    fun getTotalAmount(): Double {
        var subTotalAmount = 0.0
        mapOfData.values.forEach {
            (it as? EPharmacyAttachmentDataModel)?.let { ePharmacyAttachmentDataModel ->
                var quantity = ePharmacyAttachmentDataModel.product?.quantity.toIntOrZero()
                if(ePharmacyAttachmentDataModel.product?.qtyComparison != null) {
                    quantity = ePharmacyAttachmentDataModel.product?.qtyComparison?.recommendedQty.orZero()
                }
                subTotalAmount += (quantity) * (ePharmacyAttachmentDataModel.product?.price.orZero())
                ePharmacyAttachmentDataModel.subProductsDataModel?.forEach { model ->
                    (model as? EPharmacyAccordionProductDataModel)?.let { pModel ->
                        var pQuantity = pModel.product?.quantity.toIntOrZero()
                        if(pModel.product?.qtyComparison != null) {
                            pQuantity = pModel.product.qtyComparison?.recommendedQty.orZero()
                        }
                        subTotalAmount += (pQuantity) * (pModel.product?.price.orZero())
                    }
                }
            }
        }
        return subTotalAmount
    }
}
