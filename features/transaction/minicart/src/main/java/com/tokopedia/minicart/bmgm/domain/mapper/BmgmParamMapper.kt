package com.tokopedia.minicart.bmgm.domain.mapper

import com.tokopedia.cartcommon.domain.model.bmgm.request.BmGmGetGroupProductTickerParams
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.minicart.common.data.response.minicartlist.AvailableGroup
import com.tokopedia.minicart.common.data.response.minicartlist.AvailableSection
import com.tokopedia.minicart.common.data.response.minicartlist.CartDetail
import com.tokopedia.minicart.common.data.response.minicartlist.Shop
import com.tokopedia.minicart.common.data.response.minicartlist.WholesalePrice
import com.tokopedia.purchase_platform.common.utils.removeSingleDecimalSuffix

object BmgmParamMapper {
    private fun mapFinalPrice(
        productQuantity: Int,
        wholesalePrice: List<WholesalePrice>,
        priceBeforeBenefit: Double
    ): Double {
        wholesalePrice.forEach {
            if (productQuantity >= it.qtyMin && productQuantity <= it.qtyMax) {
                return it.prdPrc
            }
        }
        return priceBeforeBenefit
    }

    private fun mapBundleDetail(
        cartDetail: CartDetail
    ) = BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.BundleDetail(
        bundleId = cartDetail.bundleDetail.bundleId.toLongOrZero(),
        bundleGroupId = cartDetail.bundleDetail.bundleGroupId
    )

    private fun mapOffer(
        cartDetail: CartDetail
    ) = BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Offer(
        offerId = cartDetail.cartDetailInfo.bmgmData.offerId,
        offerJsonData = cartDetail.cartDetailInfo.bmgmData.offerJsonData,
        tierProducts = cartDetail.cartDetailInfo.bmgmData.tierProductList.map { tierProduct ->
            BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Offer.TierProducts(
                tierId = tierProduct.tierId,
                productsBenefit = tierProduct.productsBenefit.map { productBenefit ->
                    BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Offer.TierProducts.ProductsBenefit(
                        productId = productBenefit.productId.toLongOrZero(),
                        quantity = productBenefit.quantity
                    )
                }
            )
        }
    )

    private fun mapProducts(
        cartDetail: CartDetail,
        shop: Shop
    ) = cartDetail.products.map { product ->
        BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Product(
            cartId = product.cartId,
            productId = product.productId,
            warehouseId = product.warehouseId,
            qty = product.productQuantity,
            finalPrice = mapFinalPrice(
                productQuantity = product.productQuantity,
                wholesalePrice = product.wholesalePrice.map { wholesalePrice ->
                    WholesalePrice(
                        qtyMin = wholesalePrice.qtyMin,
                        qtyMax = wholesalePrice.qtyMax,
                        prdPrc = wholesalePrice.prdPrc
                    )
                },
                priceBeforeBenefit = product.productOriginalPrice
            ).toBigDecimal().toPlainString().removeSingleDecimalSuffix(),
            shopId = shop.shopId
        )
    }

    private fun mapCartDetails(
        availableGroup: AvailableGroup
    ) = availableGroup.cartDetails.filter { it.isBmGm() }.map { cartDetail ->
        BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails(
            bundleDetail = mapBundleDetail(cartDetail),
            offer = mapOffer(cartDetail),
            products = mapProducts(cartDetail, availableGroup.shop)
        )
    } as ArrayList<BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails>

    fun mapBmGmGroupProductTickerParams(
        availableSection: AvailableSection
    ) = BmGmGetGroupProductTickerParams(
        carts = availableSection.availableGroup
            .filter { availableGroup ->
                availableGroup.cartDetails.any { cartDetail -> cartDetail.isBmGm() }
            }
            .map { availableGroup ->
                BmGmGetGroupProductTickerParams.BmGmCart(
                    cartDetails = mapCartDetails(
                        availableGroup = availableGroup
                    )
                )
             } as ArrayList<BmGmGetGroupProductTickerParams.BmGmCart>
    )
}
