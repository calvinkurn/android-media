package com.tokopedia.buy_more_get_more.minicart.domain.usecase

import com.tokopedia.bmsm_widget.presentation.model.GiftWidgetState
import com.tokopedia.bmsm_widget.presentation.model.ProductGiftUiModel
import com.tokopedia.buy_more_get_more.common.Const
import com.tokopedia.buy_more_get_more.minicart.domain.mapper.BmgmMiniCartDataMapper
import com.tokopedia.buy_more_get_more.minicart.domain.model.MiniCartParam
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartDataUiModel
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.buy_more_get_more.minicart.presentation.model.GwpMiniCartEditorVisitable
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.minicart.domain.GetMiniCartParam
import com.tokopedia.minicart.domain.GetMiniCartWidgetUseCase
import dagger.Lazy
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 04/12/23.
 */

class GetMiniCartEditorDataUseCase @Inject constructor(
    private val getMiniCartWidgetUseCase: Lazy<GetMiniCartWidgetUseCase>,
    private val mapper: Lazy<BmgmMiniCartDataMapper>
) {

    suspend operator fun invoke(param: MiniCartParam): BmgmMiniCartDataUiModel {
        val response = getMiniCartWidgetUseCase.get().invoke(createMiniCartParams(param))
        val miniCartData = mapper.get().mapToUiModel(response)
        return getMiniCartEditorData(miniCartData)
    }

    private fun getMiniCartEditorData(data: BmgmMiniCartDataUiModel): BmgmMiniCartDataUiModel {
        return data.copy(tiers = getItems(data), offerMessage = data.offerMessage)
    }

    private fun getItems(miniCartData: BmgmMiniCartDataUiModel): List<BmgmMiniCartVisitable> {
        val items = mutableListOf<BmgmMiniCartVisitable>()
        val tiers = miniCartData.tiers.filterIsInstance<BmgmMiniCartVisitable.TierUiModel>()

        val products = tiers.flatMap { it.products }

        if (products.isEmpty()) {
            throw RuntimeException("No product found")
        }

        //add message
        val offerMessage = listOf(tiers.firstOrNull()?.tierMessage.orEmpty())
        val message = GwpMiniCartEditorVisitable.GiftMessageUiModel(offerMessage)
        items.add(message)

        //add products
        val distinctProducts = products.distinctBy { it.productId }
        val productQtyMap = getProductQtyMap(products)
        distinctProducts.forEachIndexed { index, product ->
            val showDivider = index != distinctProducts.size.minus(Int.ONE)
            val quantity = productQtyMap[product.productId] ?: product.quantity
            val newProduct = product.copy(
                quantity = quantity,
                ui = product.ui.copy(
                    showDivider = showDivider
                )
            )

            items.add(newProduct)
        }

        //add gifts
        val giftList = tiers.flatMap { it.productsBenefit }
        if (giftList.isNotEmpty()) {
            val benefitWording = tiers.firstNotNullOf { it.benefitWording }
            val benefitCta = tiers.firstNotNullOf { it.benefitCta }
            val giftWidgetModel = BmgmMiniCartVisitable.GwpGiftWidgetUiModel(
                state = GiftWidgetState.SUCCESS,
                benefitWording = benefitWording,
                benefitCta = benefitCta,
                productList = giftList.map {
                    ProductGiftUiModel(
                        id = it.productId,
                        qty = it.quantity,
                        name = it.productName,
                        imageUrl = it.productImage,
                        isUnlocked = miniCartData.isTierAchieved,
                        tierId = it.tierId
                    )
                }
            )
            items.add(giftWidgetModel)
        }
        return items.toList()
    }

    private fun getProductQtyMap(products: List<BmgmMiniCartVisitable.ProductUiModel>): Map<String, Int> {
        val qtyMap = mutableMapOf<String, Int>()
        products.forEach {
            val qty = qtyMap[it.productId].orZero()
            qtyMap[it.productId] = qty.plus(it.quantity)
        }
        return qtyMap
    }

    private fun createMiniCartParams(param: MiniCartParam): GetMiniCartParam {
        return GetMiniCartParam(
            shopIds = param.shopIds.map { it.toString() },
            source = Const.MINI_CART_SOURCE,
            bmgm = GetMiniCartParam.GetMiniCartBmgmParam(
                offerIds = param.offerIds,
                warehouseIds = param.warehouseIds,
                offerJsonData = param.offerJsonData
            )
        )
    }
}