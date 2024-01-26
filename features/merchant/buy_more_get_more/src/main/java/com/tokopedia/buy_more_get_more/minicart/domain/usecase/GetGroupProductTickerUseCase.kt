package com.tokopedia.buy_more_get_more.minicart.domain.usecase

import com.tokopedia.buy_more_get_more.common.Const
import com.tokopedia.buy_more_get_more.minicart.domain.mapper.BmgmMiniCartDataMapper
import com.tokopedia.buy_more_get_more.minicart.domain.mapper.GroupProductTickerMapper
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartDataUiModel
import com.tokopedia.buy_more_get_more.minicart.presentation.model.BmgmMiniCartVisitable
import com.tokopedia.cartcommon.domain.model.bmgm.request.BmGmGetGroupProductTickerParams
import com.tokopedia.cartcommon.domain.usecase.BmGmGetGroupProductTickerUseCase
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.asLowerCase
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.minicart.common.data.response.minicartlist.Product
import dagger.Lazy
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 09/01/24.
 */

class GetGroupProductTickerUseCase @Inject constructor(
    private val getGroupProductTickerUseCase: Lazy<BmGmGetGroupProductTickerUseCase>,
    private val mapper: Lazy<GroupProductTickerMapper>
) {

    suspend operator fun invoke(
        data: BmgmMiniCartDataUiModel,
        product: BmgmMiniCartVisitable.ProductUiModel,
        shopIds: List<Long>,
        newQty: Int = -1
    ): BmgmMiniCartDataUiModel {
        try {
            val param = createParameter(product, data, shopIds, newQty)
            val response = getGroupProductTickerUseCase.get().invoke(param)
            val errorMessage = response.getGroupProductTicker.errorMessage.firstOrNull()
            if (errorMessage.isNullOrBlank()) {
                throw RuntimeException(errorMessage)
            } else {
                return mapper.get().mapToUiModel(data, product, response, newQty)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    companion object {

        private fun createParameter(
            product: BmgmMiniCartVisitable.ProductUiModel,
            data: BmgmMiniCartDataUiModel,
            shopIds: List<Long>,
            newQty: Int = -1
        ): BmGmGetGroupProductTickerParams {
            val products = if (newQty > Int.ZERO) {
                getProductListOnQtyAdjusted(product, data, shopIds, newQty)
            } else {
                getProductListAfterDeleted(product, data, shopIds)
            }

            val tierProducts = getTiersGiftList(data.tiers)
            return getCompiledParam(
                data.cartString, data.offerId, data.offerJsonData, tierProducts, products
            )
        }

        private fun getProductListAfterDeleted(
            deletedProduct: BmgmMiniCartVisitable.ProductUiModel,
            data: BmgmMiniCartDataUiModel,
            shopIds: List<Long>
        ): List<BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Product> {
            val products = data.tiers.filterIsInstance<BmgmMiniCartVisitable.ProductUiModel>().map {
                BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Product(
                    cartId = it.cartId,
                    shopId = shopIds.firstOrNull().orZero().toString(),
                    productId = it.productId,
                    warehouseId = it.warehouseId,
                    qty = it.quantity,
                    finalPrice = it.finalPrice.toLong(),
                    checkboxState = true
                )
            }.toMutableList()
            products.removeAll { it.productId == deletedProduct.productId }
            return products.toList()
        }

        private fun getProductListOnQtyAdjusted(
            product: BmgmMiniCartVisitable.ProductUiModel,
            data: BmgmMiniCartDataUiModel,
            shopIds: List<Long>,
            newQty: Int
        ): List<BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Product> {
            val productsMap: Map<String, Product> = data.products.associateBy { it.productId }
            return data.tiers.filterIsInstance<BmgmMiniCartVisitable.ProductUiModel>().map {
                val wholeSalePrice = productsMap[it.productId]?.wholesalePrice.orEmpty()
                val isUpdatedProduct = it.productId == product.productId
                val quantity = if (isUpdatedProduct) {
                    newQty
                } else {
                    it.quantity
                }
                val finalPrice = if (isUpdatedProduct) {
                    BmgmMiniCartDataMapper.getFinalPrice(quantity, wholeSalePrice, it.finalPrice)
                } else {
                    it.finalPrice
                }
                return@map BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Product(
                    cartId = it.cartId,
                    shopId = shopIds.firstOrNull().orZero().toString(),
                    productId = it.productId,
                    warehouseId = it.warehouseId,
                    qty = quantity,
                    finalPrice = finalPrice.toLong(),
                    checkboxState = true
                )
            }
        }

        private fun getTiersGiftList(tiers: List<BmgmMiniCartVisitable>): List<BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Offer.TierProducts> {
            val tiersGift = tiers.filterIsInstance<BmgmMiniCartVisitable.GwpGiftWidgetUiModel>()
                .flatMap { it.productList }.groupBy { it.tierId }
            return tiersGift.map {
                BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Offer.TierProducts(tierId = it.key,
                    productsBenefit = it.value.map { p ->
                        BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Offer.TierProducts.ProductsBenefit(
                            productId = p.id.toLongOrZero(), quantity = p.qty
                        )
                    })
            }
        }

        private fun getCompiledParam(
            cartString: String,
            offerId: Long,
            offerJsonData: String,
            tierProducts: List<BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Offer.TierProducts>,
            products: List<BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Product>
        ): BmGmGetGroupProductTickerParams {
            return BmGmGetGroupProductTickerParams(
                carts = arrayListOf(
                    BmGmGetGroupProductTickerParams.BmGmCart(
                        cartStringOrder = cartString, cartDetails = arrayListOf(
                            BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails(
                                offer = BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Offer(
                                    offerId = offerId,
                                    offerJsonData = offerJsonData,
                                    tierProducts = tierProducts
                                ), products = products
                            )
                        )
                    )
                ),
                source = BmGmGetGroupProductTickerParams.SOURCE_OFFER_PAGE,
                type = Const.TYPE_BMGM.asLowerCase()
            )
        }
    }
}