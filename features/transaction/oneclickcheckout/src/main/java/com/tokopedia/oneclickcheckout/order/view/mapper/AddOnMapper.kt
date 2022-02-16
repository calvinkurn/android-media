package com.tokopedia.oneclickcheckout.order.view.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.logisticcart.shipping.model.*
import com.tokopedia.oneclickcheckout.order.view.model.OrderCart
import com.tokopedia.oneclickcheckout.order.view.model.OrderProduct
import com.tokopedia.oneclickcheckout.order.view.model.OrderShop
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.*
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.Product

object AddOnMapper {

    fun mapAddOnBottomSheetParam(addOn: AddOnsDataModel,
                                 orderProduct: OrderProduct,
                                 orderShop: OrderShop,
                                 orderCart: OrderCart): AddOnProductData {
        return AddOnProductData(
                bottomSheetType = AddOnProductData.ADD_ON_BOTTOM_SHEET,
                bottomSheetTitle = addOn.addOnsBottomSheetModel.headerTitle,
                source = AddOnProductData.SOURCE_ONE_CLICK_CHECKOUT,
                availableBottomSheetData = AvailableBottomSheetData(
                        products = listOf(Product(
                                cartId = orderProduct.cartId,
                                productId = orderProduct.productId.toString(),
                                productName = orderProduct.productName,
                                productImageUrl = orderProduct.productImageUrl,
                                productPrice = orderProduct.productPrice
                        )),
                        isTokoCabang = orderShop.isFulfillment,
                        cartString = orderCart.cartString,
                        warehouseId = orderShop.warehouseId.toString(),
                        shopName = orderShop.shopName,
                        addOnInfoWording = orderCart.addOnWordingData,
                        addOnSavedStates = addOn.addOnsDataItemModelList.map {
                            AddOnData(
                                    addOnId = it.addOnId.toString(),
                                    addOnPrice = it.addOnPrice,
                                    addOnQty = it.addOnQty.toInt(),
                                    addOnMetadata = AddOnMetadata(
                                            addOnNote = AddOnNote(
                                                    from = "",
                                                    to = "",
                                                    notes = "",
                                                    isCustomNote = true
                                            )
                                    )
                            )
                        }
                )
        )
    }

    fun mapAddOnBottomSheetResult(addOnResult: AddOnResult): AddOnsDataModel {
        return AddOnsDataModel(
                status = addOnResult.status,
                addOnsDataItemModelList = addOnResult.addOnData.map { mapAddOnDataItem(it) },
                addOnsButtonModel = mapAddOnButton(addOnResult.addOnButton),
                addOnsBottomSheetModel = mapAddOnBottomSheet(addOnResult.addOnBottomSheet),
        )
    }

    private fun mapAddOnDataItem(addOnData: AddOnData): AddOnDataItemModel {
        return AddOnDataItemModel(
                addOnPrice = addOnData.addOnPrice,
                addOnId = addOnData.addOnId.toLongOrZero(),
                addOnQty = addOnData.addOnQty.toLong(),
//                addOnMetadata =
        )
    }

    private fun mapAddOnButton(addOnButtonResult: AddOnButtonResult): AddOnButtonModel {
        return AddOnButtonModel(
                leftIconUrl = addOnButtonResult.leftIconUrl,
                rightIconUrl = addOnButtonResult.rightIconUrl,
                description = addOnButtonResult.description,
                action = addOnButtonResult.action,
                title = addOnButtonResult.title
        )
    }

    private fun mapAddOnBottomSheet(addOnBottomSheetResult: AddOnBottomSheetResult): AddOnBottomSheetModel {
        return AddOnBottomSheetModel(
                headerTitle = addOnBottomSheetResult.headerTitle,
                description = addOnBottomSheetResult.description,
                ticker = mapAddOnTicker(addOnBottomSheetResult.ticker),
                products = addOnBottomSheetResult.products.map { mapAddOnProduct(it) }
        )
    }

    private fun mapAddOnTicker(tickerResult: TickerResult): AddOnTickerModel {
        return AddOnTickerModel(
                text = tickerResult.text
        )
    }

    private fun mapAddOnProduct(productResult: ProductResult): AddOnProductItemModel {
        return AddOnProductItemModel(
                productName = productResult.productName,
                productImageUrl = productResult.productImageUrl
        )
    }

}