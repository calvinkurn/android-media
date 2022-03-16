package com.tokopedia.oneclickcheckout.order.view.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.oneclickcheckout.order.view.model.OrderCart
import com.tokopedia.oneclickcheckout.order.view.model.OrderProduct
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfileAddress
import com.tokopedia.oneclickcheckout.order.view.model.OrderShop
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.*
import com.tokopedia.purchase_platform.common.feature.gifting.data.response.AddOnsResponse
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.*

object AddOnMapper {

    fun mapAddOnBottomSheetParam(addOnBottomSheetType: Int,
                                 addOn: AddOnsDataModel,
                                 orderProduct: OrderProduct,
                                 orderShop: OrderShop,
                                 orderCart: OrderCart,
                                 orderProfileAddress: OrderProfileAddress,
                                 userName: String): AddOnProductData {

        val productId = if (orderProduct.parentId.isNotEmpty() && orderProduct.parentId.toLongOrZero() > 0) {
            orderProduct.parentId
        } else {
            orderProduct.productId.toString()
        }

        var defaultReceiver = ""
        if (orderProfileAddress.isAddressActive) {
            defaultReceiver = orderProfileAddress.receiverName
        }

        val bottomSheetType: Int
        var availableBottomSheetData = AvailableBottomSheetData()
        var unavailableBottomSheetData = UnavailableBottomSheetData()

        if (addOnBottomSheetType == AddOnsResponse.STATUS_SHOW_DISABLED_ADD_ON_BUTTON) {
            bottomSheetType = AddOnProductData.ADD_ON_UNAVAILABLE_BOTTOM_SHEET
            val products = addOn.addOnsBottomSheetModel.products.map {
                Product(
                        productName = it.productName,
                        productImageUrl = it.productImageUrl
                )
            }
            unavailableBottomSheetData = UnavailableBottomSheetData(
                    unavailableProducts = products,
                    description = addOn.addOnsBottomSheetModel.description,
                    tickerMessage = addOn.addOnsBottomSheetModel.ticker.text,
            )
        } else {
            bottomSheetType = AddOnProductData.ADD_ON_BOTTOM_SHEET
            availableBottomSheetData = AvailableBottomSheetData(
                    defaultTo = defaultReceiver,
                    defaultFrom = userName,
                    products = listOf(Product(
                            cartId = orderProduct.cartId,
                            productId = productId,
                            productName = orderProduct.productName,
                            productImageUrl = orderProduct.productImageUrl,
                            productPrice = orderProduct.productPrice,
                            productQuantity = orderProduct.orderQuantity,
                            productParentId = orderProduct.parentId
                    )),
                    isTokoCabang = orderShop.isFulfillment,
                    cartString = orderCart.cartString,
                    warehouseId = orderShop.warehouseId.toString(),
                    shopName = orderShop.shopName,
                    addOnInfoWording = orderCart.addOnWordingData,
                    addOnSavedStates = addOn.addOnsDataItemModelList.map {
                        AddOnData(
                                addOnId = it.addOnId,
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
        }

        return AddOnProductData(
                bottomSheetType = bottomSheetType,
                bottomSheetTitle = addOn.addOnsBottomSheetModel.headerTitle,
                source = AddOnProductData.SOURCE_ONE_CLICK_CHECKOUT,
                availableBottomSheetData = availableBottomSheetData,
                unavailableBottomSheetData = unavailableBottomSheetData
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
                addOnId = addOnData.addOnId,
                addOnQty = addOnData.addOnQty.toLong(),
                addOnMetadata = mapAddOnMetadata(addOnData.addOnMetadata)
        )
    }

    private fun mapAddOnMetadata(addOnMetadata: AddOnMetadata): AddOnMetadataItemModel {
        return AddOnMetadataItemModel(
                addOnNoteItemModel = mapAddOnNoteItem(addOnMetadata.addOnNote)
        )
    }

    private fun mapAddOnNoteItem(addOnNote: AddOnNote): AddOnNoteItemModel {
        return AddOnNoteItemModel(
                isCustomNote = addOnNote.isCustomNote,
                to = addOnNote.to,
                from = addOnNote.from,
                notes = addOnNote.notes
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