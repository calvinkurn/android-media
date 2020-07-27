package com.tokopedia.product.detail.data.model

import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.product.detail.common.data.model.product.WishlistCount
import com.tokopedia.product.detail.data.model.financing.FinancingDataResponse
import com.tokopedia.product.detail.data.model.financing.PDPInstallmentRecommendationResponse
import com.tokopedia.product.detail.data.model.installment.InstallmentBank
import com.tokopedia.product.detail.data.model.purchaseprotection.ProductPurchaseProtectionInfo
import com.tokopedia.product.detail.data.model.shopfeature.ShopFeatureData
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopCommitment

data class ProductInfoP2General(
        var wishlistCount: WishlistCount = WishlistCount(),
        var vouchers: List<MerchantVoucherViewModel> = listOf(),
        var shopBadge: ShopBadge? = null,
        var shopCommitment: ShopCommitment = ShopCommitment(),
        var minInstallment: InstallmentBank.Installment? = null,
        var productPurchaseProtectionInfo: ProductPurchaseProtectionInfo = ProductPurchaseProtectionInfo(),
        var shopFeature: ShopFeatureData = ShopFeatureData(),
        var productFinancingRecommendationData: PDPInstallmentRecommendationResponse = PDPInstallmentRecommendationResponse(),
        var productFinancingCalculationData: FinancingDataResponse = FinancingDataResponse()
)