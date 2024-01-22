package com.tokopedia.buy_more_get_more.minicart.domain.mapper

import com.tokopedia.bmsm_widget.presentation.model.GiftWidgetState
import com.tokopedia.bmsm_widget.presentation.model.ProductGiftUiModel
import com.tokopedia.buy_more_get_more.common.Const
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartDataUiModel
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.buy_more_get_more.minicart.presentation.model.GwpMiniCartEditorVisitable
import com.tokopedia.cartcommon.data.response.bmgm.BmGmData
import com.tokopedia.cartcommon.domain.model.bmgm.response.BmGmGetGroupProductTickerResponse
import com.tokopedia.minicart.common.data.response.minicartlist.Product
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 16/01/24.
 */

class GroupProductTickerMapper @Inject constructor() {

    fun mapToUiModel(
        data: BmgmMiniCartDataUiModel,
        product: BmgmMiniCartVisitable.ProductUiModel,
        groupProductTicker: BmGmGetGroupProductTickerResponse,
        newQty: Int = -1
    ): BmgmMiniCartDataUiModel {
        val tiers = data.tiers.toMutableList()

        //adjust product list
        val isUpdateQty = newQty >= 0
        if (isUpdateQty) {
            val updatedTiers = getTiersAfterQtyAdjusted(data, product, newQty)
            tiers.clear()
            tiers.addAll(updatedTiers)
        } else {
            tiers.remove(product)
        }

        //adjust gift list
        val bmgmData = getBmgmData(groupProductTicker)
        tiers.removeAll { it is BmgmMiniCartVisitable.GwpGiftWidgetUiModel }
        val giftView = getGiftData(bmgmData)
        if (giftView != null) {
            tiers.add(giftView)
        }

        //calculate final price
        val lastIndexOfProduct = tiers.indexOfLast { it is BmgmMiniCartVisitable.ProductUiModel }
        var finalPrice = 0.0
        val finalTiers = tiers.mapIndexed { i, tier ->
            return@mapIndexed when (tier) {
                is BmgmMiniCartVisitable.ProductUiModel -> {
                    finalPrice = finalPrice.plus(tier.finalPrice.times(tier.quantity))
                    val isLastProduct = i == lastIndexOfProduct
                    tier.copy(ui = product.ui.copy(showDivider = !isLastProduct))
                }

                is GwpMiniCartEditorVisitable.GiftMessageUiModel -> {
                    tier.copy(messages = bmgmData.offerMessage)
                }

                else -> tier
            }
        }.toList()

        return data.copy(
            tiers = finalTiers,
            finalPrice = finalPrice,
            priceBeforeBenefit = finalPrice,
            isTierAchieved = bmgmData.isTierAchieved,
            offerJsonData = bmgmData.offerJsonData,
            offerMessage = bmgmData.offerMessage
        )
    }

    private fun getTiersAfterQtyAdjusted(
        data: BmgmMiniCartDataUiModel, product: BmgmMiniCartVisitable.ProductUiModel, newQty: Int
    ): List<BmgmMiniCartVisitable> {
        val productsMap: Map<String, Product> = data.products.associateBy { it.productId }
        return data.tiers.map { p ->
            return@map if (p is BmgmMiniCartVisitable.ProductUiModel && p.productId == product.productId) {
                val wholeSalePrice = productsMap[p.productId]?.wholesalePrice.orEmpty()
                val finalPrice =
                    BmgmMiniCartDataMapper.getFinalPrice(newQty, wholeSalePrice, p.finalPrice)
                p.copy(quantity = newQty, finalPrice = finalPrice)
            } else {
                p
            }
        }
    }

    private fun getGiftData(bmgmData: BmGmData): BmgmMiniCartVisitable.GwpGiftWidgetUiModel? {
        val giftList = bmgmData.tierProductList.associate { tier ->
            tier.tierId to tier.productsBenefit.map {
                ProductGiftUiModel(
                    id = it.productId,
                    qty = it.quantity,
                    name = it.productName,
                    imageUrl = it.productImage,
                    isUnlocked = bmgmData.isTierAchieved,
                    tierId = tier.tierId
                )
            }
        }.flatMap { it.value }
        if (giftList.isNotEmpty()) {
            val benefitWording = bmgmData.tierProductList.firstNotNullOf { it.benefitWording }
            val benefitCta = bmgmData.tierProductList.firstNotNullOf { it.actionWording }
            return BmgmMiniCartVisitable.GwpGiftWidgetUiModel(
                state = GiftWidgetState.SUCCESS,
                benefitWording = benefitWording,
                benefitCta = benefitCta,
                productList = giftList
            )
        }
        return null
    }

    private fun getBmgmData(groupProductTicker: BmGmGetGroupProductTickerResponse): BmGmData {
        return groupProductTicker.getGroupProductTicker.data.multipleData.firstOrNull { g ->
            g.type.equals(Const.TYPE_BMGM, true)
        }?.bmgmData ?: BmGmData()
    }
}