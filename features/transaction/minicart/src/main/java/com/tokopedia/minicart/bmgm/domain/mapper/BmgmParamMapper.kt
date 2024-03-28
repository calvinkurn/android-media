package com.tokopedia.minicart.bmgm.domain.mapper

import com.tokopedia.cartcommon.domain.model.bmgm.request.BmGmGetGroupProductTickerParams
import com.tokopedia.cartcommon.domain.model.bmgm.request.BmGmGetGroupProductTickerParams.Companion.SOURCE_MINI_CART_BOTTOM_SHEET_NOW
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.minicart.common.data.response.minicartlist.AvailableGroup
import com.tokopedia.minicart.common.data.response.minicartlist.AvailableSection
import com.tokopedia.minicart.common.data.response.minicartlist.CartDetail
import com.tokopedia.minicart.common.data.response.minicartlist.Shop

object BmgmParamMapper {
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
            finalPrice = product.productPrice.toLong(),
            shopId = shop.shopId,
            checkboxState = true
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

    fun mapParamsToUpdateGwp(
        availableSection: AvailableSection
    ) = BmGmGetGroupProductTickerParams(
        carts = availableSection.availableGroup
            .filter { availableGroup ->
                availableGroup.cartDetails.any { cartDetail -> cartDetail.isBmGm() }
            }
            .map { availableGroup ->
                BmGmGetGroupProductTickerParams.BmGmCart(
                    cartStringOrder = availableGroup.cartString,
                    cartDetails = mapCartDetails(
                        availableGroup = availableGroup
                    )
                )
            } as ArrayList<BmGmGetGroupProductTickerParams.BmGmCart>
    )

    fun mapParamsFilteredToUpdateGwp(
        params: BmGmGetGroupProductTickerParams?,
        offerId: Long,
        qty: Int?
    ): BmGmGetGroupProductTickerParams {
        val carts = (
            params?.carts?.filter { cart ->
                cart.cartDetails.any { it.offer.offerId == offerId }
            } as ArrayList<BmGmGetGroupProductTickerParams.BmGmCart>
            )

        if (qty != null) {
            carts.forEachIndexed { index, it ->
                val cartDetails = mutableListOf<BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails>()
                it.cartDetails.forEach {
                    val products = mutableListOf<BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Product>()
                    it.products.forEach { product ->
                        products.add(product.copy(qty = qty))
                    }
                    cartDetails.add(it.copy(products = products))
                }
                carts[index] = carts[index].copy(cartDetails = ArrayList(cartDetails))
            }
        }

        return BmGmGetGroupProductTickerParams(
            carts = carts,
            source = SOURCE_MINI_CART_BOTTOM_SHEET_NOW
        )
    }
}
