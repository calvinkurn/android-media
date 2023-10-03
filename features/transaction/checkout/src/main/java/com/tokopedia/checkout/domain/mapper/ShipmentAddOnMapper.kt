package com.tokopedia.checkout.domain.mapper

import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.purchase_platform.common.constant.AddOnConstant.SOURCE_NORMAL_CHECKOUT
import com.tokopedia.purchase_platform.common.constant.AddOnConstant.SOURCE_ONE_CLICK_SHIPMENT
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingBottomSheetModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingDataModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingWordingModel
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnData
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnMetadata
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnNote
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnProductData
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnWordingData
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AvailableBottomSheetData
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.Product
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.UnavailableBottomSheetData

object ShipmentAddOnMapper {
    const val QTY = "{{qty}}"

    fun mapAddOnBottomSheetParam(
        addOnsDataModel: AddOnGiftingDataModel,
        availableBottomSheetData: AvailableBottomSheetData,
        unavailableBottomSheetData: UnavailableBottomSheetData,
        isOneClickShipment: Boolean
    ): AddOnProductData {
        return AddOnProductData(
            bottomSheetType = addOnsDataModel.addOnsButtonModel.action,
            bottomSheetTitle = addOnsDataModel.addOnsBottomSheetModel.headerTitle,
            source = if (isOneClickShipment) SOURCE_ONE_CLICK_SHIPMENT else SOURCE_NORMAL_CHECKOUT,
            availableBottomSheetData = availableBottomSheetData,
            unavailableBottomSheetData = unavailableBottomSheetData
        )
    }

    fun mapAvailableBottomSheetOrderLevelData(addOnWordingModel: AddOnGiftingWordingModel, shipmentCartItemModel: ShipmentCartItemModel): AvailableBottomSheetData {
        val addOnsDataModel = shipmentCartItemModel.addOnsOrderLevelModel

        val addOnWordingData = AddOnWordingData()
        addOnWordingData.onlyGreetingCard = addOnWordingModel.onlyGreetingCard
        addOnWordingData.packagingAndGreetingCard = addOnWordingModel.packagingAndGreetingCard
        addOnWordingData.invoiceNotSendToRecipient = addOnWordingModel.invoiceNotSendToRecipient

        val listProduct = arrayListOf<Product>()
        for (cartItemModel in shipmentCartItemModel.cartItemModels) {
            val product = Product()
            product.cartId = cartItemModel.cartId.toString()
            product.productId = cartItemModel.productId.toString()
            product.productName = cartItemModel.name
            product.productPrice = cartItemModel.price.toLong()
            product.productQuantity = cartItemModel.quantity
            product.productImageUrl = cartItemModel.imageUrl
            product.productParentId = cartItemModel.variantParentId
            listProduct.add(product)
        }

        val addOnDataList = arrayListOf<AddOnData>()
        if (addOnsDataModel.addOnsDataItemModelList.isNotEmpty()) {
            for (addOnItemModel in addOnsDataModel.addOnsDataItemModelList) {
                val addOnData = AddOnData()
                addOnData.addOnId = addOnItemModel.addOnId
                addOnData.addOnPrice = addOnItemModel.addOnPrice
                addOnData.addOnQty = addOnItemModel.addOnQty.toInt()
                val addOnNote = AddOnNote()
                val addOnNoteItemModel = addOnItemModel.addOnMetadata.addOnNoteItemModel
                addOnNote.isCustomNote = addOnNoteItemModel.isCustomNote
                addOnNote.notes = addOnNoteItemModel.notes
                addOnNote.from = addOnNoteItemModel.from
                addOnNote.to = addOnNoteItemModel.to
                val addOnMetadata = AddOnMetadata()
                addOnMetadata.addOnNote = addOnNote
                addOnData.addOnMetadata = addOnMetadata
                addOnDataList.add(addOnData)
            }
        }

        return AvailableBottomSheetData(
            addOnInfoWording = addOnWordingData,
            shopName = shipmentCartItemModel.shopName ?: "",
            products = listProduct,
            addOnSavedStates = addOnDataList,
            cartString = shipmentCartItemModel.cartStringGroup ?: "",
            isTokoCabang = shipmentCartItemModel.isFulfillment,
            warehouseId = shipmentCartItemModel.fulfillmentId.toString(),
            defaultFrom = shipmentCartItemModel.addOnDefaultFrom ?: "",
            defaultTo = shipmentCartItemModel.addOnDefaultTo ?: ""
        )
    }

    fun mapUnavailableBottomSheetOrderLevelData(addOnBottomSheetModel: AddOnGiftingBottomSheetModel, shipmentCartItemModel: ShipmentCartItemModel): UnavailableBottomSheetData {
        val listUnavailableProduct: MutableList<Product> = arrayListOf()
        for ((_, productName) in addOnBottomSheetModel.products) {
            for ((cartId, _, _, productId, _, name, price, _, _, _, variantParentId, _, _, _, _, quantity, _, imageUrl) in shipmentCartItemModel.cartItemModels) {
                if (productName.equals(name, ignoreCase = true)) {
                    val product = Product()
                    product.cartId = cartId.toString()
                    product.productId = productId.toString()
                    product.productPrice = price.toLong()
                    product.productQuantity = quantity
                    product.productName = name
                    product.productImageUrl = imageUrl
                    product.productParentId = variantParentId
                    listUnavailableProduct.add(product)
                    break
                }
            }
        }

        return UnavailableBottomSheetData(
            description = addOnBottomSheetModel.description,
            tickerMessage = addOnBottomSheetModel.ticker.text,
            unavailableProducts = listUnavailableProduct
        )
    }

    fun mapAvailableBottomSheetProductLevelData(addOnWordingModel: AddOnGiftingWordingModel, cartItemModel: CartItemModel): AvailableBottomSheetData {
        val addOnWordingData = AddOnWordingData()
        var onlyGreetingCard: String = addOnWordingModel.onlyGreetingCard
        if (onlyGreetingCard.contains(QTY)) {
            onlyGreetingCard = onlyGreetingCard.replace(QTY, cartItemModel.quantity.toString())
        }
        addOnWordingData.onlyGreetingCard = onlyGreetingCard

        var packageAndGreetingCard: String = addOnWordingModel.packagingAndGreetingCard
        if (packageAndGreetingCard.contains(QTY)) {
            packageAndGreetingCard = packageAndGreetingCard.replace(QTY, cartItemModel.quantity.toString())
        }

        addOnWordingData.packagingAndGreetingCard = packageAndGreetingCard
        addOnWordingData.invoiceNotSendToRecipient = addOnWordingModel.invoiceNotSendToRecipient

        val listProduct = arrayListOf<Product>()
        val product = Product()
        product.cartId = cartItemModel.cartId.toString()
        product.productId = cartItemModel.productId.toString()
        product.productName = cartItemModel.name
        product.productPrice = cartItemModel.price.toLong()
        product.productQuantity = cartItemModel.quantity
        product.productImageUrl = cartItemModel.imageUrl
        product.productParentId = cartItemModel.variantParentId
        listProduct.add(product)

        val addOnDataList = arrayListOf<AddOnData>()
        val (_, addOnsDataItemModelList) = cartItemModel.addOnGiftingProductLevelModel
        if (cartItemModel.addOnGiftingProductLevelModel.addOnsDataItemModelList.isNotEmpty()) {
            for (addOnDataItemModel in addOnsDataItemModelList) {
                val addOnData = AddOnData()
                addOnData.addOnId = addOnDataItemModel.addOnId
                addOnData.addOnUniqueId = addOnDataItemModel.addOnUniqueId
                addOnData.addOnPrice = addOnDataItemModel.addOnPrice
                addOnData.addOnQty = addOnDataItemModel.addOnQty.toInt()
                val addOnNote = AddOnNote()
                val addOnNoteItemModel = addOnDataItemModel.addOnMetadata.addOnNoteItemModel
                addOnNote.isCustomNote = addOnNoteItemModel.isCustomNote
                addOnNote.notes = addOnNoteItemModel.notes
                addOnNote.from = addOnNoteItemModel.from
                addOnNote.to = addOnNoteItemModel.to
                val addOnMetadata = AddOnMetadata()
                addOnMetadata.addOnNote = addOnNote
                addOnData.addOnMetadata = addOnMetadata
                addOnDataList.add(addOnData)
            }
        }

        return AvailableBottomSheetData(
            addOnInfoWording = addOnWordingData,
            shopName = cartItemModel.shopName,
            products = listProduct,
            addOnSavedStates = addOnDataList,
            cartString = cartItemModel.cartStringGroup,
            isTokoCabang = cartItemModel.isTokoCabang,
            warehouseId = cartItemModel.warehouseId,
            defaultFrom = cartItemModel.addOnDefaultFrom,
            defaultTo = cartItemModel.addOnDefaultTo
        )
    }

    fun mapUnavailableBottomSheetProductLevelData(addOnBottomSheetModel: AddOnGiftingBottomSheetModel, cartItemModel: CartItemModel): UnavailableBottomSheetData {
        val listUnavailableProduct = arrayListOf<Product>()
        for ((productImageUrl, productName) in addOnBottomSheetModel.products) {
            val product = Product()
            product.cartId = cartItemModel.cartId.toString()
            product.productId = cartItemModel.productId.toString()
            product.productPrice = cartItemModel.price.toLong()
            product.productQuantity = cartItemModel.quantity
            product.productName = productName
            product.productImageUrl = productImageUrl
            product.productParentId = cartItemModel.variantParentId
            listUnavailableProduct.add(product)
        }

        return UnavailableBottomSheetData(
            description = addOnBottomSheetModel.description,
            tickerMessage = addOnBottomSheetModel.ticker.text,
            unavailableProducts = listUnavailableProduct
        )
    }
}
