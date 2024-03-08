package com.tokopedia.cart.view.mapper

import com.tokopedia.cart.view.helper.CartDataHelper
import com.tokopedia.cart.view.processor.CartCalculator
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cartcommon.domain.model.bmgm.request.BmGmGetGroupProductTickerParams
import com.tokopedia.kotlin.extensions.view.toLongOrZero

object BmGmTickerRequestMapper {

    fun generateGetGroupProductTickerRequestParams(
        listProduct: List<CartItemHolderData>,
        bundleId: Long,
        bundleGroupId: String,
        offerId: Long,
        offerJsonData: String,
        cartStringOrder: String
    ): BmGmGetGroupProductTickerParams {
        val listCart = arrayListOf<BmGmGetGroupProductTickerParams.BmGmCart>()
        val cartDetailsBmGm = arrayListOf<BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails>()
        val listProductBmGm = arrayListOf<BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Product>()
        listProduct.forEach { product ->
            CartCalculator.calculatePriceWholesaleProduct(product, product.quantity)
            listProductBmGm.add(
                BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Product(
                    cartId = product.cartId,
                    shopId = product.shopHolderData.shopId,
                    productId = product.productId,
                    warehouseId = product.warehouseId,
                    qty = product.quantity,
                    finalPrice = if (product.wholesalePrice > 0.0) {
                        product.wholesalePrice.toLong()
                    } else {
                        product.productPrice.toLong()
                    },
                    checkboxState = product.isSelected
                )
            )
        }

        val tierProducts = listProduct.last().cartBmGmTickerData.bmGmCartInfoData.bmGmTierProductList.map { tierData ->
            BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Offer.TierProducts(
                tierId = tierData.tierId,
                productsBenefit = tierData.purchaseBenefitData.purchaseBenefitProducts.map { productBenefit ->
                    BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Offer.TierProducts.ProductsBenefit(
                        productId = productBenefit.id.toLongOrZero(),
                        quantity = productBenefit.qty
                    )
                }
            )
        }

        cartDetailsBmGm.add(
            BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails(
                bundleDetail = BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.BundleDetail(
                    bundleId = bundleId,
                    bundleGroupId = bundleGroupId
                ),
                offer = BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Offer(
                    offerId = offerId,
                    offerJsonData = offerJsonData,
                    tierProducts = tierProducts
                ),
                products = listProductBmGm
            )
        )

        listCart.add(
            BmGmGetGroupProductTickerParams.BmGmCart(
                cartStringOrder = cartStringOrder,
                cartDetails = cartDetailsBmGm
            )
        )

        return BmGmGetGroupProductTickerParams(
            source = BmGmGetGroupProductTickerParams.SOURCE_CART,
            carts = listCart
        )
    }

    fun generateGetGroupProductTickerRequestParams(
        cartDataList: ArrayList<Any>,
        cartItemHolderData: CartItemHolderData
    ): BmGmGetGroupProductTickerParams {
        val listCart = arrayListOf<BmGmGetGroupProductTickerParams.BmGmCart>()
        val cartDetailsBmGm = arrayListOf<BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails>()
        val listProductBmGm = arrayListOf<BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Product>()

        val listProducts =
            CartDataHelper.getListProductByOfferIdAndCartStringOrder(
                cartDataList,
                cartItemHolderData.cartBmGmTickerData.bmGmCartInfoData.bmGmData.offerId,
                cartItemHolderData.cartStringOrder
            )
        listProducts.forEach { product ->
            listProductBmGm.add(
                BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Product(
                    cartId = product.cartId,
                    shopId = product.shopHolderData.shopId,
                    productId = product.productId,
                    warehouseId = product.warehouseId,
                    qty = product.quantity,
                    finalPrice = if (product.wholesalePrice > 0.0) {
                        product.wholesalePrice.toLong()
                    } else {
                        product.productPrice.toLong()
                    },
                    checkboxState = product.isSelected
                )
            )
        }

        val tierProducts = listProducts.last().cartBmGmTickerData.bmGmCartInfoData.bmGmTierProductList.map { tierData ->
            BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Offer.TierProducts(
                tierId = tierData.tierId,
                productsBenefit = tierData.purchaseBenefitData.purchaseBenefitProducts.map { productBenefit ->
                    BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Offer.TierProducts.ProductsBenefit(
                        productId = productBenefit.id.toLongOrZero(),
                        quantity = productBenefit.qty
                    )
                }
            )
        }

        cartDetailsBmGm.add(
            BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails(
                bundleDetail = BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.BundleDetail(
                    bundleId = cartItemHolderData.bundleId.toLongOrZero(),
                    bundleGroupId = cartItemHolderData.bundleGroupId
                ),
                offer = BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Offer(
                    offerId = cartItemHolderData.cartBmGmTickerData.bmGmCartInfoData.bmGmData.offerId,
                    offerJsonData = cartItemHolderData.cartBmGmTickerData.bmGmCartInfoData.bmGmData.offerJsonData,
                    tierProducts = tierProducts
                ),
                products = listProductBmGm
            )
        )

        listCart.add(
            BmGmGetGroupProductTickerParams.BmGmCart(
                cartStringOrder = cartItemHolderData.cartStringOrder,
                cartDetails = cartDetailsBmGm
            )
        )

        return BmGmGetGroupProductTickerParams(
            source = BmGmGetGroupProductTickerParams.SOURCE_CART,
            carts = listCart
        )
    }
}
