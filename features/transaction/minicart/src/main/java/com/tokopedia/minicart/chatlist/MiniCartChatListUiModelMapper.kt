package com.tokopedia.minicart.chatlist

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.minicart.cartlist.subpage.summarytransaction.MiniCartSummaryTransactionUiModel
import com.tokopedia.minicart.cartlist.uimodel.*
import com.tokopedia.minicart.chatlist.uimodel.MiniCartChatProductUiModel
import com.tokopedia.minicart.chatlist.uimodel.MiniCartChatSeparatorUiModel
import com.tokopedia.minicart.chatlist.uimodel.MiniCartChatUnavailableReasonUiModel
import com.tokopedia.minicart.common.data.response.minicartlist.*
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import javax.inject.Inject
import kotlin.math.min

class MiniCartChatListUiModelMapper @Inject constructor() {

    companion object {
        const val SEPARATOR_HEIGHT = 8
    }

    fun mapUiModel(miniCartData: MiniCartData): MiniCartListUiModel {
        val totalProductAvailable = getTotalProductAvailable(miniCartData)
        val totalProductUnavailable = getTotalProductUnavailable(miniCartData)

        return MiniCartListUiModel().apply {
            title = miniCartData.data.headerTitle
            miniCartWidgetUiModel = mapMiniCartWidgetData(miniCartData, totalProductAvailable, totalProductUnavailable)
            miniCartSummaryTransactionUiModel = mapMiniCartSummaryTransactionUiModel(miniCartData, totalProductAvailable)
            chatVisitables = mapVisitables(miniCartData, totalProductAvailable, totalProductUnavailable)
            if (miniCartData.data.availableSection.availableGroup.isNotEmpty()) {
                maximumShippingWeight = miniCartData.data.availableSection.availableGroup[0].shop.maximumShippingWeight
                maximumShippingWeightErrorMessage = miniCartData.data.availableSection.availableGroup[0].shop.maximumWeightWording
            }
        }
    }


    private fun getTotalProductAvailable(miniCartData: MiniCartData): Int {
        return miniCartData.data.availableSection.availableGroup.sumBy { availableGroup -> availableGroup.cartDetails.size }
    }

    private fun getTotalProductUnavailable(miniCartData: MiniCartData): Int {
        var count = 0
        miniCartData.data.unavailableSection.forEach { unavailableSection ->
            unavailableSection.unavailableGroup.forEach { unavailableGroup ->
                count += unavailableGroup.cartDetails.size
            }
        }
        return count
    }

    private fun mapMiniCartWidgetData(miniCartData: MiniCartData, totalProductAvailable: Int, totalProductUnavailable: Int): MiniCartWidgetData {
        return MiniCartWidgetData().apply {
            totalProductCount = totalProductAvailable
            totalProductPrice = miniCartData.data.totalProductPrice
            totalProductError = totalProductUnavailable
        }
    }

    private fun mapMiniCartSummaryTransactionUiModel(miniCartData: MiniCartData, totalProductAvailable: Int): MiniCartSummaryTransactionUiModel {
        return MiniCartSummaryTransactionUiModel().apply {
            qty = totalProductAvailable
            totalWording = miniCartData.data.shoppingSummary.totalWording
            totalValue = miniCartData.data.shoppingSummary.totalValue
            discountTotalWording = miniCartData.data.shoppingSummary.discountTotalWording
            discountValue = miniCartData.data.shoppingSummary.discountValue
            paymentTotalWording = miniCartData.data.shoppingSummary.paymentTotalWording
            paymentTotal = miniCartData.data.shoppingSummary.paymentTotalValue
            sellerCashbackWording = miniCartData.data.shoppingSummary.sellerCashbackWording
            sellerCashbackValue = miniCartData.data.shoppingSummary.sellerCashbackValue
        }
    }

    private fun mapVisitables(miniCartData: MiniCartData, totalProductAvailable: Int, totalProductUnavailable: Int): MutableList<Visitable<*>> {
        val miniCartAvailableSectionUiModels: MutableList<MiniCartChatProductUiModel> = mutableListOf()
        val miniCartUnavailableSectionUiModels: MutableList<Visitable<*>> = mutableListOf()

        var weightTotal = 0
        miniCartData.data.availableSection.let { availableSection ->
            availableSection.availableGroup.forEach { availableGroup ->

                // Add available product
                val miniCartChatProductUiModels = mutableListOf<MiniCartChatProductUiModel>()
                availableGroup.cartDetails.forEach { cartDetail ->
                    weightTotal += cartDetail.product.productWeight * cartDetail.product.productQuantity
                    val miniCartChatProductUiModel = mapChatProductUiModel(
                        cartDetail = cartDetail,
                        shop = availableGroup.shop,
                        shipmentInformation = availableGroup.shipmentInformation,
                        action = availableSection.action,
                        notesLength = miniCartData.data.maxCharNote)
                    miniCartChatProductUiModels.add(miniCartChatProductUiModel)
                }
                miniCartAvailableSectionUiModels.addAll(miniCartChatProductUiModels)
            }
        }

        // Add unavailable separator
        if (totalProductUnavailable > 0 && totalProductAvailable > 0) {
            val miniCartSeparatorUiModel = MiniCartChatSeparatorUiModel(height = SEPARATOR_HEIGHT)
            miniCartUnavailableSectionUiModels.add(miniCartSeparatorUiModel)
        }

        miniCartData.data.unavailableSection.forEach { unavailableSection ->
            // Add unavailable reason
            val unavailableReasonUiModel = MiniCartChatUnavailableReasonUiModel("Stok kosong")
            miniCartUnavailableSectionUiModels.add(unavailableReasonUiModel)
            unavailableSection.unavailableGroup.forEach { unavailableGroup ->
                // Add unavailable product
                val miniCartProductUiModels = mutableListOf<MiniCartChatProductUiModel>()
                unavailableGroup.cartDetails.forEach { cartDetail ->
                    val miniCartProductUiModel = mapChatProductUiModel(
                        cartDetail = cartDetail,
                        shop = unavailableGroup.shop,
                        shipmentInformation = unavailableGroup.shipmentInformation,
                        action = unavailableSection.action,
                        isDisabled = true,
                        unavailableActionId = unavailableSection.selectedUnavailableActionId,
                        unavailableReason = unavailableSection.title)
                    miniCartProductUiModels.add(miniCartProductUiModel)
                }
                miniCartUnavailableSectionUiModels.addAll(miniCartProductUiModels)
            }
        }

        return constructChatVisitableOrder(
            miniCartAvailableSectionUiModels,
            miniCartUnavailableSectionUiModels
        )
    }

    private fun constructChatVisitableOrder(miniCartAvailableSectionUiModels: MutableList<MiniCartChatProductUiModel>, miniCartUnavailableSectionUiModels: MutableList<Visitable<*>>): MutableList<Visitable<*>> {
        val visitables = mutableListOf<Visitable<*>>()
        visitables.addAll(miniCartAvailableSectionUiModels)
        visitables.addAll(miniCartUnavailableSectionUiModels)
        return visitables
    }

    private fun mapChatProductUiModel(cartDetail: CartDetail,
                                      shop: Shop,
                                      shipmentInformation: ShipmentInformation,
                                      action: List<Action>,
                                      isDisabled: Boolean = false,
                                      unavailableActionId: String = "",
                                      unavailableReason: String = "",
                                      notesLength: Int = 0): MiniCartChatProductUiModel {
        return MiniCartChatProductUiModel().apply {
            cartId = cartDetail.cartId
            productId = cartDetail.product.productId
            parentId = cartDetail.product.parentId
            productImageUrl = cartDetail.product.productImage.imageSrc100Square
            productName = cartDetail.product.productName
            productVariantName = cartDetail.product.variantDescriptionDetail.variantName.joinToString(", ")
            productSlashPriceLabel = cartDetail.product.slashPriceLabel
            productOriginalPrice = cartDetail.product.productOriginalPrice
            productWholeSalePrice = 0
            productInitialPriceBeforeDrop = cartDetail.product.initialPrice
            productPrice = cartDetail.product.productPrice
            productInformation = cartDetail.product.productInformation
            productNotes = cartDetail.product.productNotes
            productQty = cartDetail.product.productQuantity
            productWeight = cartDetail.product.productWeight
            productMinOrder = cartDetail.product.productMinOrder
            productMaxOrder = if (cartDetail.product.productSwitchInvenage == 0) {
                cartDetail.product.productMaxOrder
            } else {
                min(cartDetail.product.productMaxOrder, cartDetail.product.productInvenageValue)
            }
            productActions = action
            wholesalePriceGroup = cartDetail.product.wholesalePrice.asReversed()
            isProductDisabled = isDisabled
            maxNotesLength = notesLength
            campaignId = cartDetail.product.campaignId
            attribution = cartDetail.product.productTrackerData.attribution
            warehouseId = cartDetail.product.warehouseId
            categoryId = cartDetail.product.categoryId
            category = cartDetail.product.category
            shopId = shop.shopId
            shopName = shop.shopName
            shopType = shop.shopTypeInfo.titleFmt
            freeShippingType =
                if (shipmentInformation.freeShippingExtra.eligible) "bebas ongkir extra"
                else if (shipmentInformation.freeShipping.eligible) "bebas ongkir"
                else ""
            errorType = unavailableReason
            if (isDisabled) {
                selectedUnavailableActionId = unavailableActionId
                selectedUnavailableActionLink = cartDetail.selectedUnavailableActionLink
            } else {
                productQtyLeft = cartDetail.product.productWarningMessage
            }
            productCashbackPercentage = cartDetail.product.productCashback
                .replace(" ", "")
                .replace("%", "")
                .toIntOrZero()
        }
    }
}