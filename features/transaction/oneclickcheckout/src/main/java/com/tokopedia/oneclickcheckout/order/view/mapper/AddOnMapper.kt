package com.tokopedia.oneclickcheckout.order.view.mapper

import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.oneclickcheckout.order.view.model.OrderCart
import com.tokopedia.oneclickcheckout.order.view.model.OrderProduct
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfileAddress
import com.tokopedia.oneclickcheckout.order.view.model.OrderShop
import com.tokopedia.purchase_platform.common.constant.AddOnConstant.SOURCE_ONE_CLICK_CHECKOUT
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingBottomSheetModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingButtonModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingDataItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingDataModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingMetadataItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingNoteItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingProductItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingTickerModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.response.AddOnGiftingResponse
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnBottomSheetResult
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnButtonResult
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnData
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnMetadata
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnNote
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnProductData
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnResult
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AvailableBottomSheetData
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.Product
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.ProductResult
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.TickerResult
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.UnavailableBottomSheetData
import kotlin.math.roundToLong

object AddOnMapper {

    fun mapAddOnBottomSheetParam(
        addOnBottomSheetType: Int,
        addOn: AddOnGiftingDataModel,
        orderProduct: OrderProduct,
        orderShop: OrderShop,
        orderCart: OrderCart,
        orderProfileAddress: OrderProfileAddress,
        userName: String
    ): AddOnProductData {
        val productId = if (orderProduct.parentId.isNotEmpty() && orderProduct.parentId.toLongOrZero() > 0) {
            orderProduct.parentId
        } else {
            orderProduct.productId
        }

        var defaultReceiver = ""
        if (orderProfileAddress.isAddressActive) {
            defaultReceiver = orderProfileAddress.receiverName
        }

        val bottomSheetType: Int
        var availableBottomSheetData = AvailableBottomSheetData()
        var unavailableBottomSheetData = UnavailableBottomSheetData()

        if (addOnBottomSheetType == AddOnGiftingResponse.STATUS_SHOW_DISABLED_ADD_ON_BUTTON) {
            bottomSheetType = addOn.addOnsButtonModel.action
            val products = addOn.addOnsBottomSheetModel.products.map {
                Product(
                    productName = it.productName,
                    productImageUrl = it.productImageUrl
                )
            }
            unavailableBottomSheetData = UnavailableBottomSheetData(
                unavailableProducts = products,
                description = addOn.addOnsBottomSheetModel.description,
                tickerMessage = addOn.addOnsBottomSheetModel.ticker.text
            )
        } else {
            bottomSheetType = addOn.addOnsButtonModel.action
            availableBottomSheetData = AvailableBottomSheetData(
                defaultTo = defaultReceiver,
                defaultFrom = userName,
                products = listOf(
                    Product(
                        cartId = orderProduct.cartId,
                        productId = productId,
                        productName = orderProduct.productName,
                        productImageUrl = orderProduct.productImageUrl,
                        productPrice = orderProduct.productPrice.roundToLong(),
                        productQuantity = orderProduct.orderQuantity,
                        productParentId = orderProduct.parentId
                    )
                ),
                isTokoCabang = orderShop.isFulfillment,
                cartString = orderCart.cartString,
                warehouseId = orderShop.warehouseId,
                shopName = orderShop.shopName,
                addOnInfoWording = orderCart.addOnWordingData,
                addOnSavedStates = addOn.addOnsDataItemModelList.map {
                    AddOnData(
                        addOnId = it.addOnId,
                        addOnUniqueId = it.addOnUniqueId,
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
            source = SOURCE_ONE_CLICK_CHECKOUT,
            availableBottomSheetData = availableBottomSheetData,
            unavailableBottomSheetData = unavailableBottomSheetData
        )
    }

    fun mapAddOnBottomSheetResult(addOnResult: AddOnResult): AddOnGiftingDataModel {
        return AddOnGiftingDataModel(
            status = addOnResult.status,
            addOnsDataItemModelList = addOnResult.addOnData.map { mapAddOnDataItem(it) },
            addOnsButtonModel = mapAddOnButton(addOnResult.addOnButton),
            addOnsBottomSheetModel = mapAddOnBottomSheet(addOnResult.addOnBottomSheet)
        )
    }

    private fun mapAddOnDataItem(addOnData: AddOnData): AddOnGiftingDataItemModel {
        return AddOnGiftingDataItemModel(
            addOnPrice = addOnData.addOnPrice,
            addOnId = addOnData.addOnId,
            addOnUniqueId = addOnData.addOnUniqueId,
            addOnQty = addOnData.addOnQty.toLong(),
            addOnMetadata = mapAddOnMetadata(addOnData.addOnMetadata)
        )
    }

    private fun mapAddOnMetadata(addOnMetadata: AddOnMetadata): AddOnGiftingMetadataItemModel {
        return AddOnGiftingMetadataItemModel(
            addOnNoteItemModel = mapAddOnNoteItem(addOnMetadata.addOnNote)
        )
    }

    private fun mapAddOnNoteItem(addOnNote: AddOnNote): AddOnGiftingNoteItemModel {
        return AddOnGiftingNoteItemModel(
            isCustomNote = addOnNote.isCustomNote,
            to = addOnNote.to,
            from = addOnNote.from,
            notes = addOnNote.notes
        )
    }

    private fun mapAddOnButton(addOnButtonResult: AddOnButtonResult): AddOnGiftingButtonModel {
        return AddOnGiftingButtonModel(
            leftIconUrl = addOnButtonResult.leftIconUrl,
            rightIconUrl = addOnButtonResult.rightIconUrl,
            description = addOnButtonResult.description,
            action = addOnButtonResult.action,
            title = addOnButtonResult.title
        )
    }

    private fun mapAddOnBottomSheet(addOnBottomSheetResult: AddOnBottomSheetResult): AddOnGiftingBottomSheetModel {
        return AddOnGiftingBottomSheetModel(
            headerTitle = addOnBottomSheetResult.headerTitle,
            description = addOnBottomSheetResult.description,
            ticker = mapAddOnTicker(addOnBottomSheetResult.ticker),
            products = addOnBottomSheetResult.products.map { mapAddOnProduct(it) }
        )
    }

    private fun mapAddOnTicker(tickerResult: TickerResult): AddOnGiftingTickerModel {
        return AddOnGiftingTickerModel(
            text = tickerResult.text
        )
    }

    private fun mapAddOnProduct(productResult: ProductResult): AddOnGiftingProductItemModel {
        return AddOnGiftingProductItemModel(
            productName = productResult.productName,
            productImageUrl = productResult.productImageUrl
        )
    }
}
