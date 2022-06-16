package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProduct
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProductVariant
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodPromo
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFood
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProductVariantOption
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShipping
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShop
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShoppingTotal
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodSummaryItemDetail
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodUserAddress
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateProductParam
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateProductVariantParam
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodCatalogVariantOptionDetail
import com.tokopedia.tokofood.feature.merchant.presentation.enums.CustomListItemType
import com.tokopedia.tokofood.feature.merchant.presentation.enums.SelectionControlType
import com.tokopedia.tokofood.feature.merchant.presentation.model.AddOnUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.model.CustomListItem
import com.tokopedia.tokofood.feature.merchant.presentation.model.OptionUiModel
import com.tokopedia.tokofood.feature.merchant.presentation.model.ProductUiModel
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.*
import com.tokopedia.utils.currency.CurrencyFormatUtil

object TokoFoodPurchaseUiModelMapper {

    private const val MIN_QUANTITY_STOCK = 1
    private const val MAX_QUANTITY_STOCK = 99999

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
            val tickerErrorMessage = response.data.errorTickers.top.message.takeIf { it.isNotEmpty() }
            if (tickerErrorMessage == null) {
                response.data.tickers.top.message.let { topTickerMessage ->
                    if (topTickerMessage.isNotEmpty()) {
                        add(mapGeneralTickerUiModel(topTickerMessage, false))
                    }
                }
            } else {
                add(mapGeneralTickerUiModel(tickerErrorMessage, true))
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
            response.data.unavailableSection.products.takeIf { it.isNotEmpty() }?.let { unavailableProducts ->
                add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel())
                add(mapProductListHeaderUiModel(isEnabled, response.data.unavailableSectionHeader))
                add(mapProductUnavailableReasonUiModel(isEnabled, response.data.unavailableSection.title))
                addAll(unavailableProducts.map { mapProductUiModel(it, isEnabled, false) })
                add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel())
                add(mapAccordionUiModel(isEnabled))
            }
            if (isEnabled) {
                if (shouldPromoShown) {
                    add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel())
                    add(mapPromoUiModel(response.data.promo))
                }
                if (shouldSummaryShown) {
                    add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel())
                    val isBottomTickerError = response.data.errorTickers.bottom.message.isNotEmpty()
                    val bottomTickerMessage =
                        if (isBottomTickerError) {
                            response.data.errorTickers.bottom.message
                        } else {
                            response.data.tickers.bottom.message
                        }
                    add(
                        mapSummaryTransactionUiModel(
                            response.data.summaryDetail.details,
                            isBottomTickerError to bottomTickerMessage
                        )
                    )
                }
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
        val shouldPromoShown = !response.data.promo.hidePromo
        val shouldSummaryShown = !response.data.summaryDetail.hideSummary
        val shouldTickerShopLevelShown =
            response.data.errorsUnblocking.isNotEmpty() && response.data.availableSection.products.isNotEmpty()
        val isBottomTickerError = response.data.errorTickers.bottom.message.isNotEmpty()
        val bottomTickerMessage =
            if (isBottomTickerError) {
                response.data.errorTickers.bottom.message
            } else {
                response.data.tickers.bottom.message
            }
        return PartialTokoFoodUiModel(
            shippingUiModel = mapShippingUiModel(
                shipping = response.data.shipping,
                needPinpoint = needPinpoint,
                isEnabled = isEnabled
            ).takeIf { shouldShippingShown },
            promoUiModel = mapPromoUiModel(response.data.promo).takeIf { shouldPromoShown },
            summaryUiModel = mapSummaryTransactionUiModel(
                response.data.summaryDetail.details,
                isBottomTickerError to bottomTickerMessage
            ).takeIf { shouldSummaryShown },
            totalAmountUiModel = mapTotalAmountUiModel(
                isEnabled && shouldSummaryShown,
                response.data.shoppingSummary.total
            ),
            tickerErrorShopLevelUiModel = mapTickerErrorShopLevelUiModel(
                isEnabled,
                response.data.errorsUnblocking
            ).takeIf { shouldTickerShopLevelShown }
        )
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

    fun mapUiModelToCustomizationUiModel(uiModel: TokoFoodPurchaseProductTokoFoodPurchaseUiModel): ProductUiModel {
        return ProductUiModel(
            id = uiModel.id,
            name = uiModel.name,
            imageURL = uiModel.imageUrl,
            price = uiModel.price,
            priceFmt = uiModel.priceFmt,
            isOutOfStock = false,
            isShopClosed = false,
            customListItems = uiModel.variants.mapVariantIntoCustomListItem(),
            cartId = uiModel.cartId,
            orderQty = uiModel.quantity,
            orderNote = uiModel.notes,
            isAtc = false
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
            maxQuantity = MAX_QUANTITY_STOCK,
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
                    pairList.add("${variant.name}: ${option.name}" to UpdateProductVariantParam(variant.name, option.optionId))
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

    private fun mapSummaryTransactionUiModel(summaryDetails: List<CheckoutTokoFoodSummaryItemDetail>,
                                             bottomTicker: Pair<Boolean, String>): TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel {
        val summaryDetailList = summaryDetails.map {
            it.mapToUiModel()
        }
        return TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel(
            summaryDetailList.toList(),
            bottomTicker
        )
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

    private fun List<CheckoutTokoFoodProductVariant>.mapVariantIntoCustomListItem(): List<CustomListItem> {
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
                    addOnUiModel = null
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
            options = mapOptionDetailsToOptionUiModels(this.rules.selectionRules.maxQuantity, this.options)
        )
    }

    private fun mapOptionDetailsToOptionUiModels(maxQty: Int, optionDetails: List<CheckoutTokoFoodProductVariantOption>): List<OptionUiModel> {
        return optionDetails.map { optionDetail ->
            OptionUiModel(
                    isSelected = optionDetail.isSelected,
                    id = optionDetail.optionId,
                    status = optionDetail.status,
                    name = optionDetail.name,
                    price = optionDetail.price,
                    priceFmt = optionDetail.priceFmt,
                    selectionControlType = if (maxQty > Int.ONE) SelectionControlType.MULTIPLE_SELECTION else SelectionControlType.SINGLE_SELECTION
            )
        }
    }
}