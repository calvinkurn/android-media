package com.tokopedia.oneclickcheckout.order.view.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.oneclickcheckout.order.view.model.OrderProduct
import com.tokopedia.purchase_platform.common.constant.AddOnConstant
import com.tokopedia.purchase_platform.common.constant.AddOnConstant.SOURCE_ONE_CLICK_CHECKOUT
import com.tokopedia.purchase_platform.common.feature.addons.data.request.AddOnDataRequest
import com.tokopedia.purchase_platform.common.feature.addons.data.request.AddOnMetadataRequest
import com.tokopedia.purchase_platform.common.feature.addons.data.request.AddOnRequest
import com.tokopedia.purchase_platform.common.feature.addons.data.request.CartProduct
import com.tokopedia.purchase_platform.common.feature.addons.data.request.SaveAddOnStateRequest
import com.tokopedia.purchase_platform.common.feature.addonsproduct.data.model.AddOnsProductDataModel

internal object SaveAddOnStateMapper {
    private const val SAVE_ADD_ON_AS_PRODUCT_SERVICE_FEATURE_TYPE = 1
    const val SAVE_ADD_ON_STATE_QUANTITY = 1

    fun generateSaveAddOnStateRequestParams(
        newAddOnProductData: AddOnsProductDataModel.Data,
        product: OrderProduct
    ): SaveAddOnStateRequest {
        return SaveAddOnStateRequest(
            addOns = listOf(
                AddOnRequest(
                    addOnKey = product.cartId,
                    addOnLevel = AddOnConstant.ADD_ON_LEVEL_PRODUCT,
                    cartProducts = listOf(
                        CartProduct(
                            cartId = product.cartId.toLongOrZero(),
                            productId = product.productId.toLongOrZero(),
                            warehouseId = product.warehouseId.toLongOrZero(),
                            productName = product.productName,
                            productImageUrl = product.productImageUrl,
                            productParentId = product.parentId
                        )
                    ),
                    addOnData = product.addOnsProductData.data.map { addOnProductData ->
                        if (addOnProductData.id == newAddOnProductData.id) {
                            AddOnDataRequest(
                                addOnId = newAddOnProductData.id.toLongOrZero(),
                                addOnQty = SAVE_ADD_ON_STATE_QUANTITY,
                                addOnMetadata = AddOnMetadataRequest(),
                                addOnUniqueId = newAddOnProductData.uniqueId,
                                addOnType = newAddOnProductData.type,
                                addOnStatus = newAddOnProductData.status
                            )
                        } else {
                            AddOnDataRequest(
                                addOnId = addOnProductData.id.toLongOrZero(),
                                addOnQty = SAVE_ADD_ON_STATE_QUANTITY,
                                addOnMetadata = AddOnMetadataRequest(),
                                addOnUniqueId = addOnProductData.uniqueId,
                                addOnType = addOnProductData.type,
                                addOnStatus = addOnProductData.status
                            )
                        }
                    }
                )
            ),
            source = SOURCE_ONE_CLICK_CHECKOUT,
            featureType = SAVE_ADD_ON_AS_PRODUCT_SERVICE_FEATURE_TYPE
        )
    }

    fun generateSaveAllAddOnsStateRequestParams(
        products: List<OrderProduct>
    ): SaveAddOnStateRequest {
        return SaveAddOnStateRequest(
            addOns = products.filter { it.addOnsProductData.data.isNotEmpty() }.map { product ->
                AddOnRequest(
                    addOnKey = product.cartId,
                    addOnLevel = AddOnConstant.ADD_ON_LEVEL_PRODUCT,
                    cartProducts = listOf(
                        CartProduct(
                            cartId = product.cartId.toLongOrZero(),
                            productId = product.productId.toLongOrZero(),
                            warehouseId = product.warehouseId.toLongOrZero(),
                            productName = product.productName,
                            productImageUrl = product.productImageUrl,
                            productParentId = product.parentId
                        )
                    ),
                    addOnData = product.addOnsProductData.data.map { addOnProductData ->
                        AddOnDataRequest(
                            addOnId = addOnProductData.id.toLongOrZero(),
                            addOnQty = product.orderQuantity,
                            addOnMetadata = AddOnMetadataRequest(),
                            addOnUniqueId = addOnProductData.uniqueId,
                            addOnType = addOnProductData.type,
                            addOnStatus = addOnProductData.status
                        )
                    }
                )
            },
            source = SOURCE_ONE_CLICK_CHECKOUT,
            featureType = SAVE_ADD_ON_AS_PRODUCT_SERVICE_FEATURE_TYPE
        )
    }
}
