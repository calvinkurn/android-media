package com.tokopedia.minicart.cartlist

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.cartlist.uimodel.*
import com.tokopedia.minicart.common.data.response.minicartlist.*
import com.tokopedia.minicart.common.data.response.minicartlist.Action.Companion.ACTION_SHOWLESS
import com.tokopedia.minicart.common.data.response.minicartlist.Action.Companion.ACTION_SHOWMORE
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import javax.inject.Inject

class MiniCartListViewHolderMapper @Inject constructor() {

    fun mapUiModel(miniCartData: MiniCartData): MiniCartUiModel {
        return MiniCartUiModel().apply {
            title = miniCartData.data.headerTitle
            miniCartWidgetData = mapMiniCartWidgetData(miniCartData)
            visitables = mapVisitables(miniCartData)
        }
    }

    private fun mapMiniCartWidgetData(miniCartData: MiniCartData): MiniCartWidgetData {
        return MiniCartWidgetData().apply {
            totalProductCount = miniCartData.data.totalProductCount
            totalProductPrice = miniCartData.data.totalProductPrice
        }
    }

    private fun mapVisitables(miniCartData: MiniCartData): MutableList<Visitable<*>> {
        var miniCartTickerErrorUiModel: MiniCartTickerErrorUiModel? = null
        var miniCartTickerWarningUiModel: MiniCartTickerWarningUiModel? = null
        var miniCartShopUiModel: MiniCartShopUiModel? = null
        val miniCartAvailableSectionUiModels: MutableList<MiniCartProductUiModel> = mutableListOf()
        val miniCartUnavailableSectionUiModels: MutableList<Visitable<*>> = mutableListOf()

        // Add error ticker
        if (miniCartData.data.totalProductError > 0) {
            miniCartTickerErrorUiModel = mapTickerErrorUiModel(miniCartData.data.totalProductError)
        }

        var weightTotal = 0
        miniCartData.data.availableSection.let { availableSection ->
            availableSection.availableGroup.forEach { availableGroup ->
                // Add shop
                miniCartShopUiModel = mapShopUiModel(availableGroup.shop, availableGroup.shipmentInformation)

                // Add available product
                val miniCartProductUiModels = mutableListOf<MiniCartProductUiModel>()
                availableGroup.cartDetails.forEach { cartDetail ->
                    weightTotal += cartDetail.product.productWeight * cartDetail.product.productQuantity
                    val miniCartProductUiModel = mapProductUiModel(cartDetail, availableSection.action)
                    miniCartProductUiModels.add(miniCartProductUiModel)
                }
                miniCartAvailableSectionUiModels.addAll(miniCartProductUiModels)
            }
        }

        // Add warning ticker
        if (weightTotal > 0 && weightTotal > miniCartData.data.availableSection.availableGroup[0].shop.maximumShippingWeight) {
            val shop = miniCartData.data.availableSection.availableGroup[0].shop
            val overWeight = (weightTotal - shop.maximumShippingWeight) / 1000.0F
            miniCartTickerWarningUiModel = mapTickerWarningUiModel(overWeight, shop.maximumWeightWording)
        }

        // Add unavailable separator
        if (miniCartData.data.totalProductError > 0) {
            val miniCartSeparatorUiModel = mapSeparatorUiModel(8)
            miniCartUnavailableSectionUiModels.add(miniCartSeparatorUiModel)
        }

        // Add unavailable header
        if (miniCartData.data.totalProductError > 0) {
            val miniCartUnavailableHeaderUiModel = mapUnavailableHeaderUiModel(miniCartData.data.totalProductError)
            miniCartUnavailableSectionUiModels.add(miniCartUnavailableHeaderUiModel)
        }

        miniCartData.data.unavailableSection.forEach { unavailableSection ->
            // Add unavailable reason
            val unavailableReasonUiModel = mapUnavailableReasonUiModel(unavailableSection.title)
            miniCartUnavailableSectionUiModels.add(unavailableReasonUiModel)
            unavailableSection.unavailableGroup.forEach { unavailableGroup ->
                // Add unavailable product
                val miniCartProductUiModels = mutableListOf<MiniCartProductUiModel>()
                unavailableGroup.cartDetails.forEach { cartDetail ->
                    val miniCartProductUiModel = mapProductUiModel(cartDetail, unavailableSection.action, true, unavailableSection.selectedUnavailableActionId)
                    miniCartProductUiModels.add(miniCartProductUiModel)
                }
                miniCartUnavailableSectionUiModels.addAll(miniCartProductUiModels)
            }
        }

        // Add unavailable accordion
        if (miniCartData.data.totalProductError > 1) {
            // Add unavailable accordion separator
            val miniCartSeparatorUiModel = mapSeparatorUiModel(8)
            miniCartUnavailableSectionUiModels.add(miniCartSeparatorUiModel)

            val showLessUnavailableDataWording = miniCartData.data.unavailableSectionAction.find {
                return@find it.id == ACTION_SHOWLESS
            }?.message ?: ""
            val showMoreUnavailableDataWording = miniCartData.data.unavailableSectionAction.find {
                return@find it.id == ACTION_SHOWMORE
            }?.message ?: ""

            val miniCartAccordionUiModel = mapAccordionUiModel(showLessUnavailableDataWording, showMoreUnavailableDataWording)
            miniCartUnavailableSectionUiModels.add(miniCartAccordionUiModel)
        }

        return constructVisitableOrder(
                miniCartTickerErrorUiModel,
                miniCartTickerWarningUiModel,
                miniCartShopUiModel,
                miniCartAvailableSectionUiModels,
                miniCartUnavailableSectionUiModels
        )
    }

    private fun constructVisitableOrder(miniCartTickerErrorUiModel: MiniCartTickerErrorUiModel?, miniCartTickerWarningUiModel: MiniCartTickerWarningUiModel?, miniCartShopUiModel: MiniCartShopUiModel?, miniCartAvailableSectionUiModels: MutableList<MiniCartProductUiModel>, miniCartUnavailableSectionUiModels: MutableList<Visitable<*>>): MutableList<Visitable<*>> {
        val visitables = mutableListOf<Visitable<*>>()

        miniCartTickerErrorUiModel?.let {
            visitables.add(miniCartTickerErrorUiModel)
        }
        miniCartTickerWarningUiModel?.let {
            visitables.add(miniCartTickerWarningUiModel)
        }
        miniCartShopUiModel?.let {
            visitables.add(it)
        }
        visitables.addAll(miniCartAvailableSectionUiModels)
        visitables.addAll(miniCartUnavailableSectionUiModels)

        return visitables
    }

    private fun mapAccordionUiModel(wordingShowLess: String, wordingShowMore: String): MiniCartAccordionUiModel {
        return MiniCartAccordionUiModel().apply {
            isCollapsed = true
            showLessWording = wordingShowLess
            showMoreWording = wordingShowMore
        }
    }

    private fun mapProductUiModel(cartDetail: CartDetail, action: List<Action>, isDisabled: Boolean = false, unavailableActionId: String = ""): MiniCartProductUiModel {
        return MiniCartProductUiModel().apply {
            cartId = cartDetail.cartId
            productId = cartDetail.product.productId
            productImageUrl = cartDetail.product.productImage.imageSrc100Square
            productName = cartDetail.product.productName
            productVariantName = cartDetail.product.variantDescriptionDetail.variantName.joinToString(", ")
            productQtyLeft = cartDetail.product.productWarningMessage
            productSlashPriceLabel = cartDetail.product.slashPriceLabel
            productOriginalPrice = cartDetail.product.productOriginalPrice
            productWholeSalePrice = 0
            productInitialPriceBeforeDrop = cartDetail.product.initialPrice
            productPrice = cartDetail.product.productPrice
            productInformation = cartDetail.product.productInformation
            productNotes = cartDetail.product.productNotes
            productQty = cartDetail.product.productQuantity
            productMinOrder = cartDetail.product.productMinOrder
            productMaxOrder = cartDetail.product.productMaxOrder
            productActions = action
            isProductDisabled = isDisabled
            if (isDisabled) {
                selectedUnavailableActionId = unavailableActionId
                selectedUnavailableActionLink = cartDetail.selectedUnavailableActionLink
            }
        }
    }

    private fun mapSeparatorUiModel(separatorHeight: Int): MiniCartSeparatorUiModel {
        return MiniCartSeparatorUiModel().apply {
            height = separatorHeight
        }
    }

    private fun mapShopUiModel(shop: Shop, shipmentInformation: ShipmentInformation): MiniCartShopUiModel {
        return MiniCartShopUiModel().apply {
            shopId = shop.shopId
            shopBadgeUrl = shop.shopTypeInfo.badge
            shopLocation = shipmentInformation.shopLocation
            estimatedTimeArrival = shipmentInformation.estimation
        }
    }

    private fun mapTickerErrorUiModel(totalProductError: Int): MiniCartTickerErrorUiModel {
        return MiniCartTickerErrorUiModel().apply {
            unavailableItemCount = totalProductError
            isShowErrorActionLabel = true
        }
    }

    private fun mapTickerWarningUiModel(overWeight: Float, warningWording: String): MiniCartTickerWarningUiModel {
        return MiniCartTickerWarningUiModel().apply {
            warningMessage = warningWording.replace("{{weight}}", overWeight.toString())
        }
    }

    private fun mapUnavailableHeaderUiModel(totalProductError: Int): MiniCartUnavailableHeaderUiModel {
        return MiniCartUnavailableHeaderUiModel().apply {
            unavailableItemCount = totalProductError
        }
    }

    private fun mapUnavailableReasonUiModel(title: String): MiniCartUnavailableReasonUiModel {
        return MiniCartUnavailableReasonUiModel().apply {
            reason = title
        }
    }

}