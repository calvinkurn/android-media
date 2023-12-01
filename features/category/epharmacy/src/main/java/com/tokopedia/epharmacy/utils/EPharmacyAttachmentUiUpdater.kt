package com.tokopedia.epharmacy.utils

import com.tokopedia.cartcommon.data.request.updatecart.UpdateCartRequest
import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyAccordionProductDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyAttachmentDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyShimmerDataModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero

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

    fun getUpdateCartParams(): List<UpdateCartRequest> {
        val cartsRequest = arrayListOf<UpdateCartRequest>()
        mapOfData.values.filterIsInstance<EPharmacyAttachmentDataModel>().forEach { epDataModel ->
            cartsRequest.add(
                UpdateCartRequest(
                    productId = epDataModel.shopInfo?.products?.firstOrNull()?.productId.orZero().toString(),
                    cartId = epDataModel.shopInfo?.products?.firstOrNull()?.cartId.orZero().toString(),
                    quantity = epDataModel.product?.qtyComparison?.currentQty ?: (epDataModel.shopInfo?.products?.firstOrNull()?.quantity?.toIntOrZero() ?: 0)
                )
            )
            epDataModel.subProductsDataModel?.filterIsInstance<EPharmacyAccordionProductDataModel>()?.forEach { epPDM ->
                cartsRequest.add(
                    UpdateCartRequest(
                        productId = epPDM.product?.productId.orZero().toString(),
                        cartId = epPDM.product?.cartId.orZero().toString(),
                        quantity = epPDM.product?.qtyComparison?.currentQty ?: (epPDM.product?.quantity?.toIntOrZero() ?: 0)
                    )
                )
            }
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
                            pQuantity = pModel.product?.qtyComparison?.recommendedQty.orZero()
                        }
                        subTotalAmount += (pQuantity) * (pModel.product?.price.orZero())
                    }
                }
            }
        }
        return subTotalAmount
    }
}
