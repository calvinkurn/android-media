package com.tokopedia.checkout.domain.mapper

import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.purchase_platform.common.constant.AddOnConstant
import com.tokopedia.purchase_platform.common.feature.addons.data.model.AddOnProductDataItemModel
import com.tokopedia.purchase_platform.common.feature.addons.data.request.AddOnDataRequest
import com.tokopedia.purchase_platform.common.feature.addons.data.request.AddOnRequest
import com.tokopedia.purchase_platform.common.feature.addons.data.request.CartProduct
import com.tokopedia.purchase_platform.common.feature.addons.data.request.SaveAddOnStateRequest

object ShipmentAddOnProductServiceMapper {
    fun generateSaveAddOnProductRequestParams(addOnProductData: AddOnProductDataItemModel, cartItemModel: CartItemModel, isChecked: Boolean): SaveAddOnStateRequest {
        val listAddOnRequest = arrayListOf<AddOnDataRequest>()
        cartItemModel.addOnProduct.listAddOnProductData.forEach { addOn ->
            val addOnRequest = AddOnDataRequest()
            addOnRequest.addOnId = addOn.addOnDataId
            addOnRequest.addOnQty = 1
            addOnRequest.addOnUniqueId = addOn.addOnDataUniqueId
            addOnRequest.addOnType = addOn.addOnDataType
            if (addOnProductData.addOnDataId == addOn.addOnDataId) {
                if (isChecked) {
                    addOnRequest.addOnStatus = 1
                } else {
                    addOnRequest.addOnStatus = 2
                }
            } else {
                addOnRequest.addOnStatus = addOn.addOnDataStatus
            }
            listAddOnRequest.add(addOnRequest)
        }
        return SaveAddOnStateRequest().apply {
            source = AddOnConstant.SOURCE_NORMAL_CHECKOUT
            featureType = 1
            addOns = listOf(
                    AddOnRequest().apply {
                        addOnLevel = AddOnConstant.ADD_ON_LEVEL_PRODUCT
                        addOnKey = ""
                        cartProducts = listOf(CartProduct(
                                cartId = cartItemModel.cartId,
                                productId = cartItemModel.productId,
                                productName = cartItemModel.name,
                                productParentId = cartItemModel.variantParentId
                        ))
                        addOnData = listAddOnRequest
                    }
            )
        }
    }
}