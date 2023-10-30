package com.tokopedia.epharmacy.utils

import com.tokopedia.epharmacy.component.BaseEPharmacyDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyAccordionProductDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyAttachmentDataModel
import com.tokopedia.epharmacy.component.model.EPharmacyShimmerDataModel
import com.tokopedia.epharmacy.network.request.EPharmacyUpdateCartParam
import com.tokopedia.kotlin.extensions.view.orZero

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

    fun getUpdateCartParams(tConsultationID: Long): EPharmacyUpdateCartParam {
        val productList = arrayListOf<EPharmacyUpdateCartParam.Input.CartProduct>()
        mapOfData.values.filterIsInstance<EPharmacyAttachmentDataModel>().forEach { epDataModel ->
            productList.add(
                EPharmacyUpdateCartParam.Input.CartProduct(
                    cartID = epDataModel.shopInfo?.products?.firstOrNull()?.cartId.orZero(),
                    quantity = epDataModel.quantityChangedModel?.currentQty.orZero()
                )
            )
            epDataModel.subProductsDataModel?.filterIsInstance<EPharmacyAccordionProductDataModel>()?.forEach { epPDM ->
                productList.add(
                    EPharmacyUpdateCartParam.Input.CartProduct(
                        cartID = epPDM.product?.cartId.orZero(),
                        quantity = epPDM.product?.qtyComparison?.currentQty.orZero()
                    )
                )
            }
        }
        return EPharmacyUpdateCartParam(EPharmacyUpdateCartParam.Input(tConsultationID, productList))
    }
}
