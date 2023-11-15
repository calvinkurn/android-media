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
                    cartId = epDataModel.shopInfo?.products?.firstOrNull()?.cartId.orZero().toString(),
                    quantity = epDataModel.quantityChangedModel?.currentQty ?: (epDataModel.shopInfo?.products?.firstOrNull()?.quantity?.toIntOrZero() ?: 0)
                )
            )
            epDataModel.subProductsDataModel?.filterIsInstance<EPharmacyAccordionProductDataModel>()?.forEach { epPDM ->
                cartsRequest.add(
                    UpdateCartRequest(
                        cartId = epPDM.product?.cartId.orZero().toString(),
                        quantity = epPDM.product?.qtyComparison?.currentQty ?: (epPDM.product?.quantity?.toIntOrZero() ?: 0)
                    )
                )
            }
        }
        return cartsRequest
    }
}
