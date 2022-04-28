package com.tokopedia.tokofood.purchase.purchasepage.presentation.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProduct
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodProductVariant
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodPromo
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodResponse
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShipping
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShop
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShoppingCostBreakdown
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShoppingDiscountBreakdown
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShoppingSurge
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShoppingSurgeBottomsheet
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodShoppingTotal
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodTickerInfo
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodUserAddress
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateParam
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateProductParam
import com.tokopedia.tokofood.common.presentation.uimodel.UpdateProductVariantParam
import com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel.*

object TokoFoodPurchaseUiModelMapper {

    fun mapShopInfoToUiModel(shop: CheckoutTokoFoodShop): TokoFoodPurchaseFragmentUiModel {
        return TokoFoodPurchaseFragmentUiModel(true, shop.name, shop.distance)
    }

    fun mapCheckoutResponseToUiModels(
        response: CheckoutTokoFoodResponse,
        isEnabled: Boolean,
        needPinpoint: Boolean
    ): List<Visitable<*>> {
        val shouldPromoShown = !response.data.promo.hidePromo
        val shouldSummaryShown = !response.data.shoppingSummary.hideSummary

        return mutableListOf<Visitable<*>>().apply {
            val tickerErrorMessage = response.data.tickerErrorMessage.takeIf { it.isNotEmpty() }
            if (tickerErrorMessage == null) {
                response.data.tickers.top.let { topTicker ->
                    add(mapGeneralTickerUiModel(topTicker.message, false))
                }
            } else {
                add(mapGeneralTickerUiModel(tickerErrorMessage, true))
            }
            add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel(id = "1"))
            add(mapAddressUiModel(response.data.userAddress))
            val shouldShippingShown = response.data.shipping.name.isNotEmpty()
            if (shouldShippingShown) {
                add(
                    mapShippingUiModel(
                        shipping = response.data.shipping,
                        needPinpoint = needPinpoint,
                        isEnabled = isEnabled
                    )
                )
            }
            add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel(id = "2"))
            if (response.data.availableSection.products.isNotEmpty()) {
                add(mapProductListHeaderUiModel(isEnabled))
                response.data.errorsUnblocking.takeIf { it.isNotEmpty() }?.let { message ->
                    add(mapTickerErrorShopLevelUiModel(isEnabled, message))
                }
                addAll(response.data.availableSection.products.map {
                    mapProductUiModel(it, isEnabled, true)
                })
                add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel(id = "3"))
            }
            response.data.unavailableSection.products.takeIf { it.isNotEmpty() }?.let { unavailableProducts ->
                add(mapProductListHeaderUiModel(isEnabled, response.data.unavailableSectionHeader))
                add(mapProductUnavailableReasonUiModel(isEnabled, response.data.unavailableSection.title))
                addAll(unavailableProducts.map { mapProductUiModel(it, isEnabled, false) })
                add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel(id = "4"))
                add(mapAccordionUiModel(isEnabled))
            }
            if (isEnabled) {
                if (shouldPromoShown) {
                    add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel(id = "5"))
                    add(mapPromoUiModel(response.data.promo))
                }
                if (shouldSummaryShown) {
                    add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel(id = "6"))
                    add(
                        mapSummaryTransactionUiModel(
                            response.data.shoppingSummary.costBreakdown,
                            response.data.shoppingSummary.discountBreakdown,
                            response.data.tickers.bottom.message
                        )
                    )
                }
            }
            add(TokoFoodPurchaseDividerTokoFoodPurchaseUiModel(id = "7"))
            add(mapTotalAmountUiModel(isEnabled && shouldSummaryShown, response.data.shoppingSummary.total))
        }
    }

    fun mapResponseToPartialUiModel(
        response: CheckoutTokoFoodResponse,
        isEnabled: Boolean,
        needPinpoint: Boolean
    ): PartialTokoFoodUiModel {
        val shouldShippingShown = response.data.shipping.name.isNotEmpty()
        val shouldPromoShown = !response.data.promo.hidePromo
        val shouldSummaryShown = !response.data.shoppingSummary.hideSummary
        return PartialTokoFoodUiModel(
            shippingUiModel = mapShippingUiModel(
                shipping = response.data.shipping,
                needPinpoint = needPinpoint,
                isEnabled = isEnabled
            ).takeIf { shouldShippingShown },
            promoUiModel = mapPromoUiModel(response.data.promo).takeIf { shouldPromoShown },
            summaryUiModel = mapSummaryTransactionUiModel(
                response.data.shoppingSummary.costBreakdown,
                response.data.shoppingSummary.discountBreakdown,
                response.data.tickers.bottom.message
            ).takeIf { shouldSummaryShown },
            totalAmountUiModel = mapTotalAmountUiModel(
                isEnabled && shouldSummaryShown,
                response.data.shoppingSummary.total
            )
        )
    }

    fun mapUiModelToUpdateParam(uiModels: List<TokoFoodPurchaseProductTokoFoodPurchaseUiModel>): UpdateParam {
        return UpdateParam(
            productList = uiModels.map { uiModel ->
                UpdateProductParam(
                    productId = uiModel.id,
                    cartId = uiModel.cartId,
                    notes = uiModel.notes,
                    quantity = uiModel.quantity,
                    variants = uiModel.variants
                )
            }
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
            // TODO: Use fmt instead
            shippingPrice = shipping.price.toLong()
            isNeedPinpoint = needPinpoint
            isShippingAvailable = isEnabled
            this.isEnabled = isEnabled
        }
    }

    private fun mapProductListHeaderUiModel(isEnabled: Boolean,
                                            unavailableSectionHeader: String? = null): TokoFoodPurchaseProductListHeaderTokoFoodPurchaseUiModel {
        return if (unavailableSectionHeader == null) {
            // TODO: Static for available products
            TokoFoodPurchaseProductListHeaderTokoFoodPurchaseUiModel().apply {
                title = "Daftar Pesanan"
                action = "Tambah Pesanan"
                isAvailableHeader = true
                this.isEnabled = isEnabled
            }
        } else {
            TokoFoodPurchaseProductListHeaderTokoFoodPurchaseUiModel().apply {
                title = unavailableSectionHeader
                action = "Hapus"
                isAvailableHeader = false
                this.isEnabled = isEnabled
            }
        }
    }

    private fun mapProductUnavailableReasonUiModel(isEnabled: Boolean,
                                                   reason: String): TokoFoodPurchaseProductUnavailableReasonTokoFoodPurchaseUiModel {
        return TokoFoodPurchaseProductUnavailableReasonTokoFoodPurchaseUiModel().apply {
            this.reason = reason
            // TODO: Remove detail
            detail = ""
            this.isEnabled = isEnabled
        }
    }

    private fun mapTickerErrorShopLevelUiModel(isEnabled: Boolean,
                                               tickerMessage: String): TokoFoodPurchaseTickerErrorShopLevelTokoFoodPurchaseUiModel {
        return TokoFoodPurchaseTickerErrorShopLevelTokoFoodPurchaseUiModel().apply {
            // TODO: Hardcoded 'lihat' action
            message = tickerMessage
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
            // TODO: Change price to fmt
            price = product.price.toLong(),
            // TODO: Add quantity in pojo,
            quantity = 1,
            // TODO: Check for min/max quantity
            minQuantity = 1,
            maxQuantity = 10,
            notes = product.notes,
            addOns = addOnsAndParamPair.map { it.first },
            // TODO: Change original price to fmt
            originalPrice = product.originalPrice.toLong(),
            discountPercentage = product.discountPercentage,
            cartId = product.cartId,
            variants = addOnsAndParamPair.map { it.second }
        ).apply {
            this.isEnabled = isEnabled
        }
    }

    private fun getAddOnsAndParamPairList(variants: List<CheckoutTokoFoodProductVariant>): List<Pair<String, UpdateProductVariantParam>> {
        return variants.flatMap { variant ->
            variant.options.map { option ->
                "${variant.name}: ${option.name}" to UpdateProductVariantParam(variant.name, option.optionId)
            }
        }
    }

    private fun mapPromoUiModel(promo: CheckoutTokoFoodPromo): TokoFoodPurchasePromoTokoFoodPurchaseUiModel {
        return TokoFoodPurchasePromoTokoFoodPurchaseUiModel(
            title = promo.title,
            description = promo.subtitle
        )
    }

    private fun mapSummaryTransactionUiModel(costBreakdown: CheckoutTokoFoodShoppingCostBreakdown,
                                             discountBreakdown: CheckoutTokoFoodShoppingDiscountBreakdown,
                                             bottomTickerMessage: String): TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel {
        // TODO: Confirm to PIC about positioning
        val transactionList = costBreakdown.mapToUiModelList().toMutableList()
        transactionList.add(1, discountBreakdown.mapToUiModel())
        return TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel(
            transactionList.toList(),
            bottomTickerMessage
        )
    }

    private fun CheckoutTokoFoodShoppingCostBreakdown.mapToUiModelList(): List<TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel.Transaction> {
        return listOf(
            // TODO: Tidying transaction
            TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel.Transaction(
                title = totalCartPrice.title,
                value = totalCartPrice.amount.toLong(),
                defaultValueForZero = TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel.Transaction.DEFAULT_ZERO),
            TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel.Transaction(
                title = deliveryFee.title,
                value = deliveryFee.amount.toLong(),
                defaultValueForZero = TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel.Transaction.DEFAULT_ZERO,
                surgePriceInfo = getSurgePriceInfo(deliveryFee.surge)),
            TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel.Transaction(
                title = takeAwayFee.title,
                value = takeAwayFee.amount.toLong(),
                defaultValueForZero = TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel.Transaction.DEFAULT_ZERO),
            TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel.Transaction(
                title = convenienceFee.title,
                value = convenienceFee.amount.toLong(),
                defaultValueForZero = TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel.Transaction.DEFAULT_ZERO),
            TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel.Transaction(
                title = parkingFee.title,
                value = parkingFee.amount.toLong(),
                defaultValueForZero = TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel.Transaction.DEFAULT_HIDE)
        )
    }

    private fun getSurgePriceInfo(surge: CheckoutTokoFoodShoppingSurge): CheckoutTokoFoodShoppingSurgeBottomsheet? {
        return if (surge.isSurgePrice) {
            surge.bottomsheet
        } else {
            null
        }
    }

    private fun CheckoutTokoFoodShoppingDiscountBreakdown.mapToUiModel(): TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel.Transaction {
        return TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel.Transaction(
            title = title,
            value = -amount.toLong(),
            defaultValueForZero = TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel.Transaction.DEFAULT_ZERO)
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
            // TODO: Put to res
            showMoreWording = "Tampilkan Lebih Banyak"
            showLessWording = "Tampilkan Lebih Sedikit"
            this.isEnabled = isEnabled
        }
    }

}