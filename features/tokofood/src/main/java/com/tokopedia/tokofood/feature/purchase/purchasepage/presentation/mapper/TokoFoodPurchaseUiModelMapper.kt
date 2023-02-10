package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.mapper

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tokofood.common.domain.TokoFoodCartUtil
import com.tokopedia.tokofood.common.domain.param.UpdateQuantityTokofoodBusinessData
import com.tokopedia.tokofood.common.domain.param.UpdateQuantityTokofoodCart
import com.tokopedia.tokofood.common.domain.param.UpdateQuantityTokofoodParam
import com.tokopedia.tokofood.common.domain.response.CartGeneralCartListData
import com.tokopedia.tokofood.common.domain.response.CartListBusinessBreakdownAddOns
import com.tokopedia.tokofood.common.domain.response.CartListBusinessData
import com.tokopedia.tokofood.common.domain.response.CartListBusinessDataPromo
import com.tokopedia.tokofood.common.domain.response.CartListBusinessDataShipping
import com.tokopedia.tokofood.common.domain.response.CartListBusinessDataShop
import com.tokopedia.tokofood.common.domain.response.CartListBusinessDataUserAddress
import com.tokopedia.tokofood.common.domain.response.CartListCartGroupCart
import com.tokopedia.tokofood.common.domain.response.CartListCartGroupCartOption
import com.tokopedia.tokofood.common.domain.response.CartListCartGroupCartSelectionRule
import com.tokopedia.tokofood.common.domain.response.CartListCartGroupCartVariant
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProductVariantSelectionRules
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateProductParam
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateProductVariantParam
import com.tokopedia.tokofood.common.util.TokofoodExt
import com.tokopedia.tokofood.feature.merchant.presentation.enums.CustomListItemType
import com.tokopedia.tokofood.feature.merchant.presentation.enums.SelectionControlType
import com.tokopedia.tokofood.feature.merchant.presentation.model.AddOnUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomListItem
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomOrderDetail
import com.tokopedia.tokofood.feature.merchant.presentation.model.OptionUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.VisitableDataHelper.getUiModelIndex
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.*
import com.tokopedia.utils.currency.CurrencyFormatUtil

object TokoFoodPurchaseUiModelMapper {

    private const val MIN_QUANTITY_STOCK = 1

    private const val INDEX_AFTER_FROM_PROMO = 2

    private const val SOURCE = "checkout_page"

    fun mapShopInfoToUiModel(shop: CartListBusinessDataShop): TokoFoodPurchaseFragmentUiModel {
        return TokoFoodPurchaseFragmentUiModel(true, shop.name, shop.distance)
    }

    fun mapCheckoutResponseToUiModels(
        response: CartGeneralCartListData,
        isEnabled: Boolean,
        needPinpoint: Boolean
    ): List<Visitable<*>> {
        val businessData = response.data.getTokofoodBusinessData()
        val customResponse = businessData.customResponse
        val shouldPromoShown = !customResponse.promo.hidePromo
        val businessBreakdown = response.data.shoppingSummary.getTokofoodBusinessBreakdown()
        val shouldSummaryShown = !businessBreakdown.customResponse.hideSummary

        return mutableListOf<Visitable<*>>().apply {
            val topTickerErrorMessage = businessData.ticker.errorTickers.top.message.takeIf { it.isNotEmpty() }
            if (topTickerErrorMessage == null) {
                businessData.ticker.top.message.let { topTickerMessage ->
                    if (topTickerMessage.isNotEmpty()) {
                        add(mapGeneralTickerUiModel(topTickerMessage, false))
                    }
                }
            } else {
                add(mapGeneralTickerUiModel(topTickerErrorMessage, true))
            }

            add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel())
            add(mapAddressUiModel(customResponse.userAddress))
            val shouldShippingShown =
                customResponse.shipping.name.isNotEmpty() || customResponse.shipping.eta.isNotEmpty()
            if (shouldShippingShown) {
                add(
                    mapShippingUiModel(
                        shipping = customResponse.shipping,
                        needPinpoint = needPinpoint,
                        isEnabled = isEnabled
                    )
                )
            }

            val availableProducts = businessData.getAvailableSectionProducts()
            if (availableProducts.isNotEmpty()) {
                add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel())
                add(mapProductListHeaderUiModel(isEnabled))
                customResponse.errorUnblocking.takeIf { it.isNotEmpty() }?.let { message ->
                    add(mapTickerErrorShopLevelUiModel(isEnabled, message))
                }
                addAll(availableProducts.map {
                    mapProductUiModel(it, isEnabled, true)
                })
            }

            val (unavailableSectionHeader, unavailableSectionsMap) = getUnavailableSections(businessData)
            unavailableSectionsMap?.takeIf { it.isNotEmpty() }?.let { unavailableMap ->
                add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel())
                unavailableSectionHeader?.takeIf { it.isNotBlank() }?.let { unavailableSectionHeader ->
                    add(mapProductListHeaderUiModel(isEnabled, unavailableSectionHeader))
                }
                unavailableMap.forEach { (message, carts) ->
                    add(mapProductUnavailableReasonUiModel(isEnabled, message))
                    addAll(carts.map { mapProductUiModel(it, isEnabled, false) })
                }
                if (unavailableMap.values.size > Int.ONE) {
                    add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel())
                    add(mapAccordionUiModel(isEnabled))
                }
            }

            if (isEnabled) {
                if (shouldPromoShown) {
                    add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel())
                    add(mapPromoUiModel(businessData.customResponse.promo))
                }
                if (shouldSummaryShown) {
                    add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel())
                    add(mapSummaryTransactionUiModel(businessBreakdown.addOns))
                }
            }

            val bottomTickerErrorMessage = businessData.ticker.errorTickers.bottom.message.takeIf { it.isNotEmpty() }
            if (bottomTickerErrorMessage == null) {
                businessData.ticker.bottom.message.let { topTickerMessage ->
                    if (topTickerMessage.isNotEmpty()) {
                        add(mapGeneralTickerUiModel(topTickerMessage, false))
                    }
                }
            } else {
                add(mapGeneralTickerUiModel(bottomTickerErrorMessage, true))
            }
            add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel())
            add(mapTotalAmountUiModel(isEnabled && shouldSummaryShown, businessBreakdown.totalBill))
        }
    }

    fun mapResponseToPartialUiModel(
        response: CartGeneralCartListData,
        isEnabled: Boolean,
        needPinpoint: Boolean
    ): PartialTokoFoodUiModel {
        val businessData = response.data.getTokofoodBusinessData()
        val customResponse = businessData.customResponse
        val shouldPromoShown = !customResponse.promo.hidePromo
        val businessBreakdown = response.data.shoppingSummary.getTokofoodBusinessBreakdown()
        val shouldSummaryShown = !businessBreakdown.customResponse.hideSummary
        val shouldShippingShown =
            customResponse.shipping.name.isNotEmpty() || customResponse.shipping.eta.isNotEmpty()

        val availableProducts = businessData.getAvailableSectionProducts()
        val shouldTickerShopLevelShown =
            customResponse.errorUnblocking.isNotEmpty() && availableProducts.isNotEmpty()

        val isTopTickerError = businessData.ticker.top.message.isNotEmpty()
        val topTickerMessage =
            if (isTopTickerError) {
                businessData.ticker.errorTickers.top.message
            } else {
                businessData.ticker.top.message
            }

        val isBottomTickerError = businessData.ticker.errorTickers.bottom.message.isNotEmpty()
        val bottomTickerMessage =
            if (isBottomTickerError) {
                businessData.ticker.errorTickers.bottom.message
            } else {
                businessData.ticker.bottom.message
            }
        return PartialTokoFoodUiModel(
            topTickerUiModel = topTickerMessage.takeIf { it.isNotBlank() }?.let { topMessage ->
                mapGeneralTickerUiModel(topMessage, isTopTickerError)
            },
            shippingUiModel = mapShippingUiModel(
                shipping = businessData.customResponse.shipping,
                needPinpoint = needPinpoint,
                isEnabled = isEnabled
            ).takeIf { shouldShippingShown },
            promoUiModel = mapPromoUiModel(businessData.customResponse.promo).takeIf { shouldPromoShown },
            summaryUiModel = mapSummaryTransactionUiModel(
                businessBreakdown.addOns
            ).takeIf { shouldSummaryShown },
            totalAmountUiModel = mapTotalAmountUiModel(
                isEnabled && shouldSummaryShown,
                businessBreakdown.totalBill
            ),
            tickerErrorShopLevelUiModel = mapTickerErrorShopLevelUiModel(
                isEnabled,
                customResponse.errorUnblocking
            ).takeIf { shouldTickerShopLevelShown },
            bottomTickerUiModel = bottomTickerMessage.takeIf { it.isNotBlank() }?.let { bottomMessage ->
                mapGeneralTickerUiModel(bottomMessage, isBottomTickerError)
            }
        )
    }

    fun MutableList<Visitable<*>>.updateShippingData(shippingUiModel: TokoFoodPurchaseShippingTokoFoodPurchaseUiModel?) {
        getUiModelIndex<TokoFoodPurchaseShippingTokoFoodPurchaseUiModel>().let { shippingIndex ->
            if (shippingIndex >= Int.ZERO) {
                removeAt(shippingIndex)
                shippingUiModel?.let { shippingUiModel ->
                    add(shippingIndex, shippingUiModel)
                }
            } else {
                getUiModelIndex<TokoFoodPurchaseAddressTokoFoodPurchaseUiModel>().let { addressIntex ->
                    shippingUiModel?.let { shippingUiModel ->
                        add(addressIntex + Int.ONE, shippingUiModel)
                    }
                }
            }
        }
    }

    fun MutableList<Visitable<*>>.updatePromoData(promoUiModel: TokoFoodPurchasePromoTokoFoodPurchaseUiModel?) {
        getUiModelIndex<TokoFoodPurchasePromoTokoFoodPurchaseUiModel>().let { promoIndex ->
            if (promoIndex >= Int.ZERO) {
                val affectedIndex = promoIndex - Int.ONE
                removeAt(affectedIndex)
                removeAt(affectedIndex)
                promoUiModel?.let { promoUiModel ->
                    add(affectedIndex, TokoFoodPurchaseDividerTokoFoodPurchaseUiModel())
                    add(affectedIndex + Int.ONE, promoUiModel)
                }
            } else {
                promoUiModel?.let { promoUiModel ->
                    val hasUnavailableProducts = any { visitable ->
                        visitable is TokoFoodPurchaseProductTokoFoodPurchaseUiModel && !visitable.isAvailable
                    }
                    val futurePromoIndex =
                        if (hasUnavailableProducts) {
                            getUiModelIndex<TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel>().let { accordionIndex ->
                                if (accordionIndex == RecyclerView.NO_POSITION) {
                                    indexOfLast { visitable ->
                                        visitable is TokoFoodPurchaseProductTokoFoodPurchaseUiModel
                                    }
                                } else {
                                    accordionIndex + Int.ONE
                                }
                            }
                        } else {
                            indexOfLast { visitable ->
                                visitable is TokoFoodPurchaseProductTokoFoodPurchaseUiModel
                            }
                        }.plus(Int.ONE)
                    add(futurePromoIndex, TokoFoodPurchaseDividerTokoFoodPurchaseUiModel())
                    add(futurePromoIndex + Int.ONE, promoUiModel)
                }
            }
        }
    }

    fun MutableList<Visitable<*>>.updateSummaryData(summaryUiModel: TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel?) {
        getUiModelIndex<TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel>().let { summaryIndex ->
            if (summaryIndex >= Int.ZERO) {
                val affectedIndex = summaryIndex - Int.ONE
                removeAt(affectedIndex)
                removeAt(affectedIndex)
                summaryUiModel?.let { summaryUiModel ->
                    add(affectedIndex, TokoFoodPurchaseDividerTokoFoodPurchaseUiModel())
                    add(affectedIndex + Int.ONE, summaryUiModel)
                }
            } else {
                summaryUiModel?.let { summaryUiModel ->
                    val currentPromoIndex = getUiModelIndex<TokoFoodPurchasePromoTokoFoodPurchaseUiModel>()
                    if (currentPromoIndex == RecyclerView.NO_POSITION) {
                        val hasUnavailableProducts = any { visitable ->
                            visitable is TokoFoodPurchaseProductTokoFoodPurchaseUiModel && !visitable.isAvailable
                        }
                        val futureSummaryIndex =
                            if (hasUnavailableProducts) {
                                getUiModelIndex<TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel>().let { accordionIndex ->
                                    if (accordionIndex == RecyclerView.NO_POSITION) {
                                        indexOfLast { visitable ->
                                            visitable is TokoFoodPurchaseProductTokoFoodPurchaseUiModel
                                        }
                                    } else {
                                        accordionIndex
                                    }
                                }
                            } else {
                                indexOfLast { visitable ->
                                    visitable is TokoFoodPurchaseProductTokoFoodPurchaseUiModel
                                }
                            }.plus(Int.ONE)
                        add(futureSummaryIndex, TokoFoodPurchaseDividerTokoFoodPurchaseUiModel())
                        add(futureSummaryIndex + Int.ONE, summaryUiModel)
                    } else {
                        add(currentPromoIndex + Int.ONE, TokoFoodPurchaseDividerTokoFoodPurchaseUiModel())
                        add(currentPromoIndex + INDEX_AFTER_FROM_PROMO, summaryUiModel)
                    }
                }
            }
        }
    }

    fun MutableList<Visitable<*>>.updateTotalAmountData(totalAmountUiModel: TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel) {
        getUiModelIndex<TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel>().let { totalAmountIndex ->
            if (totalAmountIndex >= Int.ZERO) {
                removeAt(totalAmountIndex)
                add(totalAmountIndex, totalAmountUiModel)
            }
        }
    }

    fun MutableList<Visitable<*>>.updateTickerErrorShopLevelData(tickerErrorShopLevelUiModel: TokoFoodPurchaseTickerErrorShopLevelTokoFoodPurchaseUiModel?) {
        getUiModelIndex<TokoFoodPurchaseTickerErrorShopLevelTokoFoodPurchaseUiModel>().let { tickerErrorIndex ->
            when {
                tickerErrorShopLevelUiModel == null && tickerErrorIndex >= Int.ZERO -> {
                    removeAt(tickerErrorIndex)
                }
                tickerErrorShopLevelUiModel != null && tickerErrorIndex < Int.ZERO -> {
                    getUiModelIndex<TokoFoodPurchaseProductTokoFoodPurchaseUiModel>().let { firstProductIndex ->
                        if (firstProductIndex >= Int.ZERO) {
                            add(firstProductIndex, tickerErrorShopLevelUiModel)
                        }
                    }
                }
                tickerErrorIndex >= Int.ZERO -> {
                    removeAt(tickerErrorIndex)
                    tickerErrorShopLevelUiModel?.let { tickerErrorUiModel ->
                        add(tickerErrorIndex, tickerErrorUiModel)
                    }
                }
                else -> {}
            }
        }
    }

    fun MutableList<Visitable<*>>.updateTickersData(topTickerUiModel: TokoFoodPurchaseGeneralTickerTokoFoodPurchaseUiModel?,
                                                    bottomTickerUiModel: TokoFoodPurchaseGeneralTickerTokoFoodPurchaseUiModel?) {
        getUiModelIndex<TokoFoodPurchaseGeneralTickerTokoFoodPurchaseUiModel>().let { firstTickerIndex ->
            val lastTickerIndex = indexOfLast { ticker ->
                ticker is TokoFoodPurchaseGeneralTickerTokoFoodPurchaseUiModel
            }
            val bottomTickerPosition =
                getUiModelIndex<TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel>() - Int.ONE

            when {
                firstTickerIndex < Int.ZERO -> {
                    // There are no tickers initially
                    var shiftIndex = 0
                    topTickerUiModel?.let { uiModel ->
                        add(Int.ZERO, uiModel)
                        shiftIndex++
                    }
                    bottomTickerUiModel?.let { uiModel ->
                        add(bottomTickerPosition + shiftIndex, uiModel)
                    }
                }
                firstTickerIndex == Int.ZERO && lastTickerIndex > firstTickerIndex -> {
                    // Both of tickers exist
                    removeAt(Int.ZERO)
                    var shiftIndex = -Int.ONE
                    topTickerUiModel?.let { uiModel ->
                        add(Int.ZERO, uiModel)
                        shiftIndex++
                    }

                    removeAt(lastTickerIndex + shiftIndex)
                    bottomTickerUiModel?.let { uiModel ->
                        add(lastTickerIndex + shiftIndex, uiModel)
                    }
                }
                firstTickerIndex > Int.ZERO -> {
                    // Only bottom ticker exist
                    var shiftIndex = 0
                    topTickerUiModel?.let { uiModel ->
                        add(Int.ZERO, uiModel)
                        shiftIndex++
                    }
                    removeAt(lastTickerIndex + shiftIndex)
                    bottomTickerUiModel?.let { uiModel ->
                        add(lastTickerIndex + shiftIndex, uiModel)
                    }
                }
                else -> {
                    // Only top ticker exist
                    removeAt(Int.ZERO)
                    var shiftIndex = -Int.ONE
                    topTickerUiModel?.let { uiModel ->
                        add(Int.ZERO, uiModel)
                        shiftIndex++
                    }
                    bottomTickerUiModel?.let { uiModel ->
                        add(bottomTickerPosition + shiftIndex, uiModel)
                    }
                }
            }
        }
    }

    fun mapUiModelToUpdateParam(uiModels: List<TokoFoodPurchaseProductTokoFoodPurchaseUiModel>,
                                shopId: String): UpdateParam {
        return UpdateParam(
            productList = uiModels.map { uiModel ->
                UpdateProductParam(
                    productId = uiModel.id,
                    cartId = uiModel.cartId,
                    notes = uiModel.notes,
                    quantity = uiModel.quantity,
                    variants = uiModel.variantsParam
                )
            },
            shopId = shopId
        )
    }

    fun mapUiModelToUpdateQuantityParam(uiModels: List<TokoFoodPurchaseProductTokoFoodPurchaseUiModel>): UpdateQuantityTokofoodParam {
        // TODO: Add businessId
        return UpdateQuantityTokofoodParam(
            source = SOURCE,
            businessData = UpdateQuantityTokofoodBusinessData(
                businessId = String.EMPTY,
                carts = uiModels.map {
                    UpdateQuantityTokofoodCart(
                        cartId = it.cartId,
                        quantity = it.quantity
                    )
                }
            )
        )
    }

    fun mapUiModelToCustomizationUiModel(uiModel: TokoFoodPurchaseProductTokoFoodPurchaseUiModel,
                                         customOrderDetails: MutableList<CustomOrderDetail>): ProductUiModel {
        return ProductUiModel(
            id = uiModel.id,
            name = uiModel.name,
            imageURL = uiModel.imageUrl,
            price = uiModel.getBasePrice(),
            priceFmt = uiModel.getBasePriceFmt(),
            subTotal = uiModel.price,
            subTotalFmt = uiModel.priceFmt,
            isOutOfStock = false,
            isShopClosed = false,
            customListItems = uiModel.variants.mapVariantIntoCustomListItem(uiModel.notes),
            cartId = uiModel.cartId,
            orderQty = uiModel.quantity,
            orderNote = uiModel.notes,
            isAtc = true,
            customOrderDetails = customOrderDetails
        )
    }

    private fun mapGeneralTickerUiModel(message: String,
                                        isError: Boolean): TokoFoodPurchaseGeneralTickerTokoFoodPurchaseUiModel {
        return TokoFoodPurchaseGeneralTickerTokoFoodPurchaseUiModel().apply {
            isErrorTicker = isError
            this.message = message
        }
    }

    private fun mapAddressUiModel(address: CartListBusinessDataUserAddress): TokoFoodPurchaseAddressTokoFoodPurchaseUiModel {
        return TokoFoodPurchaseAddressTokoFoodPurchaseUiModel().apply {
            addressName = address.addressName
            isMainAddress = address.isMainAddress()
            receiverName = address.receiverName
            receiverPhone = address.phone
            addressDetail = address.address
        }
    }

    private fun mapShippingUiModel(shipping: CartListBusinessDataShipping,
                                   needPinpoint: Boolean = true,
                                   isEnabled: Boolean): TokoFoodPurchaseShippingTokoFoodPurchaseUiModel {
        return TokoFoodPurchaseShippingTokoFoodPurchaseUiModel().apply {
            shippingCourierName = shipping.name
            shippingEta = shipping.eta
            shippingLogoUrl = shipping.logoUrl
            shippingPriceFmt = shipping.priceFmt.takeIf { it.isNotEmpty() }
                ?: CurrencyFormatUtil.convertPriceValueToIdrFormat(shipping.price, false)
            isNeedPinpoint = needPinpoint
            isShippingAvailable = isEnabled
            this.isEnabled = isEnabled
        }
    }

    private fun mapProductListHeaderUiModel(isEnabled: Boolean,
                                            unavailableSectionHeader: String? = null): TokoFoodPurchaseProductListHeaderTokoFoodPurchaseUiModel {
        return if (unavailableSectionHeader == null) {
            TokoFoodPurchaseProductListHeaderTokoFoodPurchaseUiModel(isAvailableHeader = true)
        } else {
            TokoFoodPurchaseProductListHeaderTokoFoodPurchaseUiModel(
                title = unavailableSectionHeader,
                isAvailableHeader = false
            )
        }.apply {
            this.isEnabled = isEnabled
        }
    }

    private fun mapProductUnavailableReasonUiModel(isEnabled: Boolean,
                                                   reason: String): TokoFoodPurchaseProductUnavailableReasonTokoFoodPurchaseUiModel {
        return TokoFoodPurchaseProductUnavailableReasonTokoFoodPurchaseUiModel(reason = reason).apply {
            this.isEnabled = isEnabled
        }
    }

    private fun mapTickerErrorShopLevelUiModel(isEnabled: Boolean,
                                               tickerMessage: String): TokoFoodPurchaseTickerErrorShopLevelTokoFoodPurchaseUiModel {
        return TokoFoodPurchaseTickerErrorShopLevelTokoFoodPurchaseUiModel(message = tickerMessage).apply {
            this.isEnabled = isEnabled
        }
    }

    private fun mapProductUiModel(product: CartListCartGroupCart,
                                  isEnabled: Boolean,
                                  mIsAvailable: Boolean): TokoFoodPurchaseProductTokoFoodPurchaseUiModel {
        val addOnsAndParamPair = getAddOnsAndParamPairList(product.customResponse.variants)
        return TokoFoodPurchaseProductTokoFoodPurchaseUiModel(
            isAvailable = mIsAvailable,
            id = product.productId,
            name = product.customResponse.name,
            imageUrl = product.customResponse.imageUrl,
            price = product.price,
            priceFmt = product.priceFmt,
            quantity = product.quantity,
            minQuantity = MIN_QUANTITY_STOCK,
            maxQuantity = TokofoodExt.MAXIMUM_QUANTITY,
            notes = product.customResponse.notes,
            addOns = addOnsAndParamPair.map { it.first },
            originalPrice = product.customResponse.originalPrice,
            originalPriceFmt = product.customResponse.originalPriceFmt,
            discountPercentage = product.customResponse.discountPercentage,
            cartId = product.cartId,
            variantsParam = addOnsAndParamPair.map { it.second },
            variants = product.customResponse.variants
        ).apply {
            this.isEnabled = isEnabled
        }
    }

    private fun getAddOnsAndParamPairList(variants: List<CartListCartGroupCartVariant>): List<Pair<String, UpdateProductVariantParam>> {
        val pairList = mutableListOf<Pair<String, UpdateProductVariantParam>>()
        variants.forEach { variant ->
            variant.options.forEach { option ->
                if (option.isSelected) {
                    pairList.add("${variant.name}: ${option.name}" to UpdateProductVariantParam(variant.variantId, option.optionId))
                }
            }
        }
        return pairList
    }

    private fun mapPromoUiModel(promo: CartListBusinessDataPromo): TokoFoodPurchasePromoTokoFoodPurchaseUiModel {
        return TokoFoodPurchasePromoTokoFoodPurchaseUiModel(
            title = promo.title,
            description = promo.subtitle
        )
    }

    private fun mapSummaryTransactionUiModel(summaryDetails: List<CartListBusinessBreakdownAddOns>): TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel {
        val summaryDetailList = summaryDetails.map {
            it.mapToUiModel()
        }
        return TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel(summaryDetailList.toList())
    }

    private fun CartListBusinessBreakdownAddOns.mapToUiModel(): TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel.Transaction {
        return TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel.Transaction(
            title = title,
            value = priceFmt,
            detailInfo = customResponse.info
        )
    }

    private fun mapTotalAmountUiModel(isEnabled: Boolean,
                                      totalBill: Double,
    ): TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel {
        return TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel(totalBill, false).apply {
            this.isEnabled = isEnabled
        }
    }

    private fun mapAccordionUiModel(isEnabled: Boolean): TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel {
        return TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel().apply {
            isCollapsed = false
            this.isEnabled = isEnabled
        }
    }

    private fun List<CartListCartGroupCartVariant>.mapVariantIntoCustomListItem(notes: String): List<CustomListItem> {
        val customListItems = mutableListOf<CustomListItem>()
        // add on selections widget
        this.forEach { variant ->
            customListItems.add(
                CustomListItem(
                    listItemType = CustomListItemType.PRODUCT_ADD_ON,
                    addOnUiModel = variant.mapVariantToAddOnUiModel()
                )
            )
        }
        if (customListItems.isNotEmpty()) {
            // order input widget
            customListItems.add(
                CustomListItem(
                    listItemType = CustomListItemType.ORDER_NOTE_INPUT,
                    addOnUiModel = null,
                    orderNote = notes
                )
            )
        }
        return customListItems.toList()
    }

    private fun CartListCartGroupCartVariant.mapVariantToAddOnUiModel(): AddOnUiModel {
        return AddOnUiModel(
            id = this.variantId,
            name = this.name,
            isRequired = this.rules.selectionRule.isRequired,
            maxQty = this.rules.selectionRule.maxQty,
            minQty = this.rules.selectionRule.minQty,
            options = mapOptionDetailsToOptionUiModels(this.rules.selectionRule, this.options)
        )
    }

    private fun mapOptionDetailsToOptionUiModels(selectionRules: CartListCartGroupCartSelectionRule, optionDetails: List<CartListCartGroupCartOption>): List<OptionUiModel> {
        return optionDetails.map { optionDetail ->
            OptionUiModel(
                    isSelected = optionDetail.isSelected && !optionDetail.isOutOfStock(),
                    id = optionDetail.optionId,
                    status = optionDetail.status,
                    name = optionDetail.name,
                    price = optionDetail.price,
                    priceFmt = optionDetail.priceFmt,
                    selectionControlType = when {
                        selectionRules.type == CheckoutTokoFoodProductVariantSelectionRules.SELECT_MANY -> SelectionControlType.MULTIPLE_SELECTION
                        selectionRules.type == CheckoutTokoFoodProductVariantSelectionRules.SELECT_ONE -> SelectionControlType.SINGLE_SELECTION
                        selectionRules.maxQty > Int.ONE -> SelectionControlType.MULTIPLE_SELECTION
                        else -> SelectionControlType.SINGLE_SELECTION
                    }
            )
        }
    }

    private fun getUnavailableSections(businessData: CartListBusinessData): Pair<String?, Map<String, List<CartListCartGroupCart>>?> {
        var sectionHeader: String? = null
        var unavailableSectionMap: Map<String, List<CartListCartGroupCart>>? = null
        businessData.additionalGrouping.details.find { it.additionalGroupId == TokoFoodCartUtil.UNAVAILABLE_SECTION }?.let { unavailableCartGroup ->
            val isMultipleCartGroup = unavailableCartGroup.additionalGroupChildIds.isNotEmpty()
            if (isMultipleCartGroup) {
                sectionHeader = unavailableCartGroup.message
                unavailableSectionMap = unavailableCartGroup.additionalGroupChildIds.associateWith { groupId ->
                    val groupChildIds = businessData.additionalGrouping.details.find { it.additionalGroupId == groupId }
                    val carts = businessData.cartGroups.firstOrNull()?.carts?.filter { cart ->
                        groupChildIds?.cartIds?.contains(cart.cartId) == true
                    }
                    carts.orEmpty()
                }
            } else {
                unavailableCartGroup.message.let { message ->
                    val unavailableCarts = businessData.cartGroups.firstOrNull()?.carts?.filter {
                        unavailableCartGroup.cartIds.contains(it.cartId)
                    }.orEmpty()
                    unavailableSectionMap = hashMapOf(message to unavailableCarts)
                }
            }
        }
        return sectionHeader to unavailableSectionMap
    }

}
