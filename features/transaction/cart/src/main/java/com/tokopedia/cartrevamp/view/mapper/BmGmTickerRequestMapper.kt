package com.tokopedia.cartrevamp.view.mapper

import com.tokopedia.cartrevamp.domain.model.bmgm.request.BmGmGetGroupProductTickerParams
import com.tokopedia.cartrevamp.view.uimodel.CartGroupHolderData
import com.tokopedia.purchase_platform.common.utils.removeSingleDecimalSuffix

object BmGmTickerRequestMapper {
    fun generateGetGroupProductTickerRequestParams(cartGroup: CartGroupHolderData, offerId: Long): BmGmGetGroupProductTickerParams {
        val listCart = arrayListOf<BmGmGetGroupProductTickerParams.BmGmCart>()
        val cartDetailsBmGm = arrayListOf<BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails>()
        val listProductBmGm = arrayListOf<BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Product>()
        if (cartGroup.cartGroupBmGmHolderData.offerId == offerId) {
            cartGroup.productUiModelList.forEach { cartItemProduct ->
                listProductBmGm.add(
                    BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Product(
                        cartId = cartItemProduct.cartId,
                        shopId = cartItemProduct.shopHolderData.shopId,
                        productId = cartItemProduct.productId,
                        warehouseId = cartItemProduct.warehouseId,
                        qty = cartItemProduct.quantity,
                        finalPrice = cartItemProduct.productPrice.toString().removeSingleDecimalSuffix(),
                        checkboxState = cartItemProduct.isSelected
                    )
                )
            }
        }

        cartDetailsBmGm.add(
            BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails(
                bundleDetail = BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.BundleDetail(
                    bundleId = cartGroup.cartGroupBmGmHolderData.bundleId,
                    bundleGroupId = cartGroup.cartGroupBmGmHolderData.bundleGroupId
                ),
                offer = BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Offer(
                    offerId = offerId,
                    offerJsonData = cartGroup.cartGroupBmGmHolderData.offerJsonData
                ),
                products = listProductBmGm
            )
        )

        listCart.add(
            BmGmGetGroupProductTickerParams.BmGmCart(
                cartStringOrder = cartGroup.cartString,
                cartDetails = cartDetailsBmGm
            )
        )

        return BmGmGetGroupProductTickerParams(
            carts = listCart
        )
    }
}
