package com.tokopedia.minicart.chatlist

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.cartlist.subpage.summarytransaction.MiniCartSummaryTransactionUiModel
import com.tokopedia.minicart.cartlist.uimodel.*
import com.tokopedia.minicart.chatlist.uimodel.MiniCartChatProductUiModel
import com.tokopedia.minicart.chatlist.uimodel.MiniCartChatSeparatorUiModel
import com.tokopedia.minicart.chatlist.uimodel.MiniCartChatUnavailableReasonUiModel
import com.tokopedia.minicart.common.data.response.minicartlist.*
import com.tokopedia.minicart.common.domain.data.MiniCartWidgetData
import javax.inject.Inject

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
                    cartDetail.products.forEach { product ->
                        weightTotal += product.productWeight * product.productQuantity
                        val miniCartChatProductUiModel = mapChatProductUiModel(product)
                        miniCartChatProductUiModels.add(miniCartChatProductUiModel)
                    }
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
                    cartDetail.products.forEach { product ->
                        val miniCartProductUiModel = mapChatProductUiModel(product, true)
                        miniCartProductUiModels.add(miniCartProductUiModel)
                    }
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

    private fun mapChatProductUiModel(product: Product, isDisabled: Boolean = false): MiniCartChatProductUiModel {
        return MiniCartChatProductUiModel().apply {
            productId = product.productId
            productImageUrl = product.productImage.imageSrc100Square
            productName = product.productName
            productSlashPriceLabel = product.slashPriceLabel
            productOriginalPrice = product.productOriginalPrice
            productPrice = product.productPrice
            productInformation = product.productInformation
            isProductDisabled = isDisabled
        }
    }
}
