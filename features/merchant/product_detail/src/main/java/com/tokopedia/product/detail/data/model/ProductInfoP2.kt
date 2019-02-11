package com.tokopedia.product.detail.data.model

import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.product.detail.data.model.product.Rating
import com.tokopedia.product.detail.data.model.product.WishlistCount
import com.tokopedia.product.detail.data.model.shop.ShopInfo

data class ProductInfoP2(
        var shopInfo: ShopInfo? = null,
        var rating: Rating = Rating(),
        var wishlistCount: WishlistCount = WishlistCount(),
        var vouchers: List<MerchantVoucherViewModel> = listOf()
)