package com.tokopedia.product.detail.data.model

import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.product.detail.common.data.model.product.ProductOther
import com.tokopedia.product.detail.common.data.model.product.Rating
import com.tokopedia.product.detail.common.data.model.product.WishlistCount
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.data.model.installment.InstallmentBank
import com.tokopedia.product.detail.data.model.review.Review
import com.tokopedia.product.detail.data.model.purchaseprotection.ProductPurchaseProtectionInfo
import com.tokopedia.product.detail.data.model.shopfeature.ShopFeatureData
import com.tokopedia.product.detail.data.model.talk.Talk
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopCommitment

data class ProductInfoP2General (
        var rating: Rating = Rating(),
        var wishlistCount: WishlistCount = WishlistCount(),
        var vouchers: List<MerchantVoucherViewModel> = listOf(),
        var shopBadge: ShopBadge? = null,
        var shopCommitment: ShopCommitment = ShopCommitment(),
        var minInstallment: InstallmentBank.Installment? = null,
        var imageReviews: List<ImageReviewItem> = listOf(),
        var helpfulReviews: List<Review> = listOf(),
        var latestTalk: Talk = Talk(),
        var productPurchaseProtectionInfo: ProductPurchaseProtectionInfo = ProductPurchaseProtectionInfo(),
        var variantResp: ProductVariant? = null,
        var shopFeature: ShopFeatureData = ShopFeatureData()
)