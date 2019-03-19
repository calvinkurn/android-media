package com.tokopedia.product.detail.data.model

import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.product.detail.common.data.model.product.ProductOther
import com.tokopedia.product.detail.common.data.model.product.Rating
import com.tokopedia.product.detail.common.data.model.product.WishlistCount
import com.tokopedia.product.detail.data.model.installment.InstallmentBank
import com.tokopedia.product.detail.data.model.review.Review
import com.tokopedia.product.detail.data.model.shop.ShopBadge
import com.tokopedia.product.detail.data.model.shop.ShopCommitment
import com.tokopedia.product.detail.data.model.shop.ShopInfo
import com.tokopedia.product.detail.data.model.talk.Talk
import com.tokopedia.product.detail.common.data.model.warehouse.MultiOriginWarehouse

data class ProductInfoP2(
        var shopInfo: ShopInfo? = null,
        var rating: Rating = Rating(),
        var wishlistCount: WishlistCount = WishlistCount(),
        var vouchers: List<MerchantVoucherViewModel> = listOf(),
        var shopBadge: ShopBadge? = null,
        var shopCommitment: ShopCommitment = ShopCommitment(),
        var minInstallment: InstallmentBank.Installment? = null,
        var imageReviews: List<ImageReviewItem> = listOf(),
        var helpfulReviews: List<Review> = listOf(),
        var latestTalk: Talk = Talk(),
        var productOthers: List<ProductOther> = listOf(),
        var shopCod: Boolean = false,
        var nearestWarehouse: MultiOriginWarehouse = MultiOriginWarehouse()
        )