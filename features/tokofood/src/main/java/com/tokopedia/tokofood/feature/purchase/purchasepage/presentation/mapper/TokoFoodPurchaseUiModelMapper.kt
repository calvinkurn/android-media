package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.mapper

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tokofood.common.domain.param.UpdateQuantityTokofoodBusinessData
import com.tokopedia.tokofood.common.domain.param.UpdateQuantityTokofoodCart
import com.tokopedia.tokofood.common.domain.param.UpdateQuantityTokofoodParam
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProduct
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProductVariant
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodPromo
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFood
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProductVariantOption
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProductVariantSelectionRules
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShipping
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShop
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShoppingTotal
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodSummaryItemDetail
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodUserAddress
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
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseViewModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.VisitableDataHelper.getUiModelIndex
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.*
import com.tokopedia.utils.currency.CurrencyFormatUtil

object TokoFoodPurchaseUiModelMapper {

    private const val MIN_QUANTITY_STOCK = 1

    private const val INDEX_AFTER_FROM_PROMO = 2

    private const val SOURCE = "checkout_page"

    fun mapShopInfoToUiModel(shop: CheckoutTokoFoodShop): TokoFoodPurchaseFragmentUiModel {
        return TokoFoodPurchaseFragmentUiModel(true, shop.name, shop.distance)
    }

    fun mapCheckoutResponseToUiModels(
        response: CheckoutTokoFood,
        isEnabled: Boolean,
        needPinpoint: Boolean
    ): List<Visitable<*>> {
        val shouldPromoShown = !response.data.promo.hidePromo
        val shouldSummaryShown = !response.data.summaryDetail.hideSummary

        return mutableListOf<Visitable<*>>().apply {
            val topTickerErrorMessage = response.data.errorTickers.top.message.takeIf { it.isNotEmpty() }
            if (topTickerErrorMessage == null) {
                response.data.tickers.top.message.let { topTickerMessage ->
                    if (topTickerMessage.isNotEmpty()) {
                        add(mapGeneralTickerUiModel(topTickerMessage, false))
                    }
                }
            } else {
                add(mapGeneralTickerUiModel(topTickerErrorMessage, true))
            }
            add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel())
            add(mapAddressUiModel(response.data.userAddress))
            val shouldShippingShown =
                response.data.shipping.name.isNotEmpty() || response.data.shipping.eta.isNotEmpty()
            if (shouldShippingShown) {
                add(
                    mapShippingUiModel(
                        shipping = response.data.shipping,
                        needPinpoint = needPinpoint,
                        isEnabled = isEnabled
                    )
                )
            }
            if (response.data.availableSection.products.isNotEmpty()) {
                add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel())
                add(mapProductListHeaderUiModel(isEnabled))
                response.data.errorsUnblocking.takeIf { it.isNotEmpty() }?.let { message ->
                    add(mapTickerErrorShopLevelUiModel(isEnabled, message))
                }
                addAll(response.data.availableSection.products.map {
                    mapProductUiModel(it, isEnabled, true)
                })
            }
            response.data.unavailableSections.firstOrNull()?.let { unavailableSection ->
                unavailableSection.products.takeIf { it.isNotEmpty() }?.let { unavailableProducts ->
                    add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel())
                    response.data.unavailableSectionHeader.takeIf { it.isNotBlank() }?.let { unavailableSectionHeader ->
                        add(mapProductListHeaderUiModel(isEnabled, unavailableSectionHeader))
                    }
                    add(mapProductUnavailableReasonUiModel(isEnabled, unavailableSection.title))
                    addAll(unavailableProducts.map { mapProductUiModel(it, isEnabled, false) })
                    if (unavailableProducts.size > Int.ONE) {
                        add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel())
                        add(mapAccordionUiModel(isEnabled))
                    }
                }
            }
            if (isEnabled) {
                if (shouldPromoShown) {
                    add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel())
                    add(mapPromoUiModel(response.data.promo))
                }
                if (shouldSummaryShown) {
                    add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel())
                    add(mapSummaryTransactionUiModel(response.data.summaryDetail.details))
                }
            }
            val bottomTickerErrorMessage = response.data.errorTickers.bottom.message.takeIf { it.isNotEmpty() }
            if (bottomTickerErrorMessage == null) {
                response.data.tickers.bottom.message.let { topTickerMessage ->
                    if (topTickerMessage.isNotEmpty()) {
                        add(mapGeneralTickerUiModel(topTickerMessage, false))
                    }
                }
            } else {
                add(mapGeneralTickerUiModel(bottomTickerErrorMessage, true))
            }
            add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel())
            add(mapTotalAmountUiModel(isEnabled && shouldSummaryShown, response.data.shoppingSummary.total))
        }
    }

    fun mapResponseToPartialUiModel(
        response: CheckoutTokoFood,
        isEnabled: Boolean,
        needPinpoint: Boolean
    ): PartialTokoFoodUiModel {
        val shouldShippingShown = response.data.shipping.name.isNotEmpty()
        val shouldPromoShown = !response.data.promo.hidePromo && isEnabled
        val shouldSummaryShown = !response.data.summaryDetail.hideSummary && isEnabled
        val shouldTickerShopLevelShown =
            response.data.errorsUnblocking.isNotEmpty() && response.data.availableSection.products.isNotEmpty()

        val isTopTickerError = response.data.errorTickers.top.message.isNotEmpty()
        val topTickerMessage =
            if (isTopTickerError) {
                response.data.errorTickers.top.message
            } else {
                response.data.tickers.top.message
            }

        val isBottomTickerError = response.data.errorTickers.bottom.message.isNotEmpty()
        val bottomTickerMessage =
            if (isBottomTickerError) {
                response.data.errorTickers.bottom.message
            } else {
                response.data.tickers.bottom.message
            }
        return PartialTokoFoodUiModel(
            topTickerUiModel = topTickerMessage.takeIf { it.isNotBlank() }?.let { topMessage ->
                mapGeneralTickerUiModel(topMessage, isTopTickerError)
            },
            shippingUiModel = mapShippingUiModel(
                shipping = response.data.shipping,
                needPinpoint = needPinpoint,
                isEnabled = isEnabled
            ).takeIf { shouldShippingShown },
            promoUiModel = mapPromoUiModel(response.data.promo).takeIf { shouldPromoShown },
            summaryUiModel = mapSummaryTransactionUiModel(
                response.data.summaryDetail.details
            ).takeIf { shouldSummaryShown },
            totalAmountUiModel = mapTotalAmountUiModel(
                isEnabled && shouldSummaryShown,
                response.data.shoppingSummary.total
            ),
            tickerErrorShopLevelUiModel = mapTickerErrorShopLevelUiModel(
                isEnabled,
                response.data.errorsUnblocking
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

    private fun mapAddressUiModel(address: CheckoutTokoFoodUserAddress): TokoFoodPurchaseAddressTokoFoodPurchaseUiModel {
        return TokoFoodPurchaseAddressTokoFoodPurchaseUiModel().apply {
            addressName = address.addressName
            isMainAddress = address.isMainAddress()
            receiverName = address.receiverName
            receiverPhone = address.phone
            addressDetail = address.address
        }
    }

    private fun mapShippingUiModel(shipping: CheckoutTokoFoodShipping,
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

    private fun mapProductUiModel(product: CheckoutTokoFoodProduct,
                                  isEnabled: Boolean,
                                  mIsAvailable: Boolean): TokoFoodPurchaseProductTokoFoodPurchaseUiModel {
        val addOnsAndParamPair = getAddOnsAndParamPairList(product.variants)
        return TokoFoodPurchaseProductTokoFoodPurchaseUiModel(
            isAvailable = mIsAvailable,
            id = product.productId,
            name = product.productName,
            imageUrl = product.imageUrl,
            price = product.price,
            priceFmt = product.priceFmt,
            quantity = product.quantity,
            minQuantity = MIN_QUANTITY_STOCK,
            maxQuantity = TokofoodExt.MAXIMUM_QUANTITY,
            notes = product.notes,
            addOns = addOnsAndParamPair.map { it.first },
            originalPrice = product.originalPrice,
            originalPriceFmt = product.originalPriceFmt,
            discountPercentage = product.discountPercentage,
            cartId = product.cartId,
            variantsParam = addOnsAndParamPair.map { it.second },
            variants = product.variants
        ).apply {
            this.isEnabled = isEnabled
        }
    }

    private fun getAddOnsAndParamPairList(variants: List<CheckoutTokoFoodProductVariant>): List<Pair<String, UpdateProductVariantParam>> {
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

    private fun mapPromoUiModel(promo: CheckoutTokoFoodPromo): TokoFoodPurchasePromoTokoFoodPurchaseUiModel {
        return TokoFoodPurchasePromoTokoFoodPurchaseUiModel(
            title = promo.title,
            description = promo.subtitle
        )
    }

    private fun mapSummaryTransactionUiModel(summaryDetails: List<CheckoutTokoFoodSummaryItemDetail>): TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel {
        val summaryDetailList = summaryDetails.map {
            it.mapToUiModel()
        }
        return TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel(summaryDetailList.toList())
    }

    private fun CheckoutTokoFoodSummaryItemDetail.mapToUiModel(): TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel.Transaction {
        return TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel.Transaction(
            title = title,
            value = priceFmt,
            detailInfo = info
        )
    }

    private fun mapTotalAmountUiModel(isEnabled: Boolean,
                                      total: CheckoutTokoFoodShoppingTotal): TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel {
        return TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel(total.cost.toLong(), false).apply {
            this.isEnabled = isEnabled
        }
    }

    private fun mapAccordionUiModel(isEnabled: Boolean): TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel {
        return TokoFoodPurchaseAccordionTokoFoodPurchaseUiModel().apply {
            isCollapsed = false
            this.isEnabled = isEnabled
        }
    }

    private fun List<CheckoutTokoFoodProductVariant>.mapVariantIntoCustomListItem(notes: String): List<CustomListItem> {
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

    private fun CheckoutTokoFoodProductVariant.mapVariantToAddOnUiModel(): AddOnUiModel {
        return AddOnUiModel(
            id = this.variantId,
            name = this.name,
            isRequired = this.rules.selectionRules.isRequired,
            maxQty = this.rules.selectionRules.maxQuantity,
            minQty = this.rules.selectionRules.minQuantity,
            options = mapOptionDetailsToOptionUiModels(this.rules.selectionRules, this.options)
        )
    }

    private fun mapOptionDetailsToOptionUiModels(selectionRules: CheckoutTokoFoodProductVariantSelectionRules, optionDetails: List<CheckoutTokoFoodProductVariantOption>): List<OptionUiModel> {
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
                        selectionRules.maxQuantity > Int.ONE -> SelectionControlType.MULTIPLE_SELECTION
                        else -> SelectionControlType.SINGLE_SELECTION
                    }
            )
        }
    }

}
