package com.tokopedia.cartrevamp.view.mapper

import com.tokopedia.cartrevamp.domain.model.bmgm.request.BmGmGetGroupProductTickerParams
import com.tokopedia.cartrevamp.view.uimodel.CartItemHolderData
import com.tokopedia.purchase_platform.common.utils.removeSingleDecimalSuffix

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
            listProductBmGm.add(
                BmGmGetGroupProductTickerParams.BmGmCart.BmGmCartDetails.Product(
                    cartId = product.cartId,
                    shopId = product.shopHolderData.shopId,
                    productId = product.productId,
                    warehouseId = product.warehouseId,
                    qty = product.quantity,
                    finalPrice = if (product.wholesalePrice > 0.0) product.wholesalePrice.toString().removeSingleDecimalSuffix() else product.productPrice.toString().removeSingleDecimalSuffix(),
                    checkboxState = product.isSelected
                )
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
                    offerJsonData = offerJsonData
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
            carts = listCart
        )
    }
}
