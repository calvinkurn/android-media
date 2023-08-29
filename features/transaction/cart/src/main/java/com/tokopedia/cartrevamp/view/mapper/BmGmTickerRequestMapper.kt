package com.tokopedia.cartrevamp.view.mapper

import com.tokopedia.cartrevamp.domain.model.bmgm.request.BmGmGetGroupProductTickerParams
import com.tokopedia.cartrevamp.view.uimodel.CartGroupHolderData

object BmGmTickerRequestMapper {
    fun generateGetGroupProductTickerRequestParams(cartGroup: CartGroupHolderData): BmGmGetGroupProductTickerParams {
        val listCart = arrayListOf<BmGmGetGroupProductTickerParams.BmGmCart>()
        val cartDetailsBmGm = arrayListOf<BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails>()
        cartGroup.productUiModelList.forEach { cartItem ->
            val listProductBmGm = arrayListOf<BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Product>()
            cartItem.bmGmCartInfoData.bmGmTierProductList.forEach { tiersApplied ->
                tiersApplied.listProduct.forEach { product ->
                    listProductBmGm.add(
                            BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Product(
                                    cartId = product.cartId,
                                    shopId = product.shopId,
                                    productId = product.productId,
                                    warehouseId = product.warehouseId,
                                    qty = product.qty,
                                    finalPrice = product.finalPrice,
                                    checkboxState = product.checkboxState
                            )
                    )
                }
            }

            cartDetailsBmGm.add(
                    BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails(
                            bundleDetail = BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.BundleDetail(
                                    bundleId = cartItem.bundleId,
                                    bundleGroupId = cartItem.bundleGroupId
                            ),
                            offer = BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Offer(
                                    offerId = cartItem.bmGmCartInfoData.bmGmData.offerId,
                                    offerJsonData = cartItem.bmGmCartInfoData.bmGmData.offerJsonData
                            ),
                            products = listProductBmGm
                    )
            )

            listCart.add(
                    BmGmGetGroupProductTickerParams.BmGmCart(
                            cartStringOrder = cartItem.cartStringOrder,
                            cartDetails = cartDetailsBmGm
                    )
            )
        }

        return BmGmGetGroupProductTickerParams(
            carts = listCart
        )
    }
}
