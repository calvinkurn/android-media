package com.tokopedia.checkout.domain.mapper

import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.cartshipmentform.ShipmentSummaryAddOnData
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.view.uimodel.ShipmentAddOnSummaryModel
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.purchase_platform.common.constant.AddOnConstant
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.feature.addons.data.request.AddOnDataRequest
import com.tokopedia.purchase_platform.common.feature.addons.data.request.AddOnRequest
import com.tokopedia.purchase_platform.common.feature.addons.data.request.CartProduct
import com.tokopedia.purchase_platform.common.feature.addons.data.request.SaveAddOnStateRequest
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.utils.currency.CurrencyFormatUtil

object ShipmentAddOnProductServiceMapper {

    fun generateSaveAddOnProductRequestParams(cartItemModel: CartItemModel, isOneClickCheckout: Boolean): SaveAddOnStateRequest {
        val listAddOnRequest = arrayListOf<AddOnDataRequest>()
        cartItemModel.addOnProduct.listAddOnProductData.forEach { addOn ->
            val addOnRequest = AddOnDataRequest()
            addOnRequest.addOnId = addOn.id
            addOnRequest.addOnQty = 1
            addOnRequest.addOnUniqueId = addOn.uniqueId
            addOnRequest.addOnType = addOn.type
            addOnRequest.addOnStatus = addOn.status
            listAddOnRequest.add(addOnRequest)
        }
        return SaveAddOnStateRequest().apply {
            source = if (isOneClickCheckout) AddOnConstant.SOURCE_ONE_CLICK_SHIPMENT else AddOnConstant.SOURCE_NORMAL_CHECKOUT
            featureType = 1
            addOns = listOf(
                AddOnRequest().apply {
                    addOnLevel = AddOnConstant.ADD_ON_LEVEL_PRODUCT
                    addOnKey = cartItemModel.cartId.toString()
                    cartProducts = listOf(
                        CartProduct(
                            cartId = cartItemModel.cartId,
                            productId = cartItemModel.productId,
                            productName = cartItemModel.name,
                            productParentId = cartItemModel.variantParentId
                        )
                    )
                    addOnData = listAddOnRequest
                }
            )
        }
    }

    fun generateSaveAddOnProductRequestParams(listCartItemModel: List<CartItemModel>, isOneClickCheckout: Boolean): SaveAddOnStateRequest {
        val listAddOnRequest = arrayListOf<AddOnRequest>()
        listCartItemModel.forEach { cartItem ->
            val listAddOnProduct: ArrayList<AddOnDataRequest> = arrayListOf()
            if (cartItem.addOnProduct.listAddOnProductData.isNotEmpty()) {
                cartItem.addOnProduct.listAddOnProductData.forEach { addOnProduct ->
                    val addOnDataRequest = AddOnDataRequest(
                        addOnId = addOnProduct.id,
                        addOnQty = cartItem.quantity,
                        addOnUniqueId = addOnProduct.uniqueId,
                        addOnType = addOnProduct.type,
                        addOnStatus = addOnProduct.status
                    )
                    listAddOnProduct.add(addOnDataRequest)
                }
            }

            val addOnRequest = AddOnRequest(
                addOnKey = cartItem.cartId.toString(),
                addOnLevel = AddOnConstant.ADD_ON_LEVEL_PRODUCT,
                cartProducts = listOf(
                    CartProduct(
                        cartId = cartItem.cartId,
                        productId = cartItem.productId,
                        warehouseId = cartItem.warehouseId.toLongOrZero(),
                        productName = cartItem.name,
                        productImageUrl = cartItem.imageUrl,
                        productParentId = cartItem.variantParentId
                    )
                ),
                addOnData = listAddOnProduct
            )
            listAddOnRequest.add(addOnRequest)
        }
        return SaveAddOnStateRequest().apply {
            source = if (isOneClickCheckout) AddOnConstant.SOURCE_ONE_CLICK_SHIPMENT else AddOnConstant.SOURCE_NORMAL_CHECKOUT
            featureType = 1
            addOns = listAddOnRequest
        }
    }

    fun generateSaveAddOnProductRequestParamsNew(product: CheckoutProductModel, isOneClickCheckout: Boolean): SaveAddOnStateRequest {
        val listAddOnRequest = arrayListOf<AddOnDataRequest>()
        product.addOnProduct.listAddOnProductData.forEach { addOn ->
            val addOnRequest = AddOnDataRequest()
            addOnRequest.addOnId = addOn.id
            addOnRequest.addOnQty = 1
            addOnRequest.addOnUniqueId = addOn.uniqueId
            addOnRequest.addOnType = addOn.type
            addOnRequest.addOnStatus = addOn.status
            listAddOnRequest.add(addOnRequest)
        }
        return SaveAddOnStateRequest().apply {
            source = if (isOneClickCheckout) AddOnConstant.SOURCE_ONE_CLICK_SHIPMENT else AddOnConstant.SOURCE_NORMAL_CHECKOUT
            featureType = 1
            addOns = listOf(
                AddOnRequest().apply {
                    addOnLevel = AddOnConstant.ADD_ON_LEVEL_PRODUCT
                    addOnKey = product.cartId.toString()
                    cartProducts = listOf(
                        CartProduct(
                            cartId = product.cartId,
                            productId = product.productId,
                            productName = product.name,
                            productParentId = product.variantParentId
                        )
                    )
                    addOnData = listAddOnRequest
                }
            )
        }
    }

    fun generateSaveAddOnProductRequestParamsNew(listCartItemModel: List<CheckoutProductModel>, isOneClickCheckout: Boolean): SaveAddOnStateRequest {
        val listAddOnRequest = arrayListOf<AddOnRequest>()
        listCartItemModel.forEach { cartItem ->
            val listAddOnProduct: ArrayList<AddOnDataRequest> = arrayListOf()
            if (cartItem.addOnProduct.listAddOnProductData.isNotEmpty()) {
                cartItem.addOnProduct.listAddOnProductData.forEach { addOnProduct ->
                    val addOnDataRequest = AddOnDataRequest(
                        addOnId = addOnProduct.id,
                        addOnQty = cartItem.quantity,
                        addOnUniqueId = addOnProduct.uniqueId,
                        addOnType = addOnProduct.type,
                        addOnStatus = addOnProduct.status
                    )
                    listAddOnProduct.add(addOnDataRequest)
                }
            }

            val addOnRequest = AddOnRequest(
                addOnKey = cartItem.cartId.toString(),
                addOnLevel = AddOnConstant.ADD_ON_LEVEL_PRODUCT,
                cartProducts = listOf(
                    CartProduct(
                        cartId = cartItem.cartId,
                        productId = cartItem.productId,
                        warehouseId = cartItem.warehouseId.toLongOrZero(),
                        productName = cartItem.name,
                        productImageUrl = cartItem.imageUrl,
                        productParentId = cartItem.variantParentId
                    )
                ),
                addOnData = listAddOnProduct
            )
            listAddOnRequest.add(addOnRequest)
        }
        return SaveAddOnStateRequest().apply {
            source = if (isOneClickCheckout) AddOnConstant.SOURCE_ONE_CLICK_SHIPMENT else AddOnConstant.SOURCE_NORMAL_CHECKOUT
            featureType = 1
            addOns = listAddOnRequest
        }
    }

    fun mapSummaryAddOns(cartShipmentAddressFormData: CartShipmentAddressFormData): List<ShipmentAddOnSummaryModel> {
        val countMapSummaries = hashMapOf<Int, Pair<Long, Int>>()
        val listShipmentAddOnSummary: ArrayList<ShipmentAddOnSummaryModel> = arrayListOf()

        var qtyAddOn = 0
        var totalPriceAddOn: Long
        groupAddressLoop@ for (groupAddress in cartShipmentAddressFormData.groupAddress) {
            groupShopLoop@ for (groupShop in groupAddress.groupShop) {
                groupShopV2Loop@ for (groupShopV2 in groupShop.groupShopData) {
                    productLoop@ for (product in groupShopV2.products) {
                        addOnLoop@ for (addon in product.addOnProduct.listAddOnProductData) {
                            if (addon.status == AddOnConstant.ADD_ON_PRODUCT_STATUS_CHECK || addon.status == AddOnConstant.ADD_ON_PRODUCT_STATUS_MANDATORY) {
                                qtyAddOn += product.productQuantity
                                totalPriceAddOn = (qtyAddOn * addon.price).toLong()
                                countMapSummaries[addon.type] = totalPriceAddOn to qtyAddOn
                            }
                        }
                    }
                }
            }
        }

        val mapSummary = getShoppingSummaryAddOns(cartShipmentAddressFormData.listSummaryAddons)
        for (entry in countMapSummaries) {
            val addOnWording = mapSummary[entry.key]?.replace(CartConstant.QTY_ADDON_REPLACE, entry.value.second.toString())
            val addOnPrice = CurrencyFormatUtil.convertPriceValueToIdrFormat(entry.value.first, false).removeDecimalSuffix()
            val summaryAddOn = ShipmentAddOnSummaryModel(
                wording = addOnWording ?: "",
                type = entry.key,
                qty = entry.value.second,
                priceLabel = addOnPrice,
                priceValue = entry.value.first
            )
            listShipmentAddOnSummary.add(summaryAddOn)
        }
        return listShipmentAddOnSummary
    }

    fun getShoppingSummaryAddOns(listSummaryAddOn: List<ShipmentSummaryAddOnData>): HashMap<Int, String> {
        val mapSummary = hashMapOf<Int, String>()
        for (summaryItem in listSummaryAddOn) {
            mapSummary[summaryItem.type] = summaryItem.wording
        }
        return mapSummary
    }
}
