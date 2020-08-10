package com.tokopedia.product.detail.data.model

import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
import com.tokopedia.product.detail.data.model.financing.FtInstallmentCalculationDataResponse
import com.tokopedia.product.detail.data.model.financing.PDPInstallmentRecommendationData
import com.tokopedia.product.detail.data.model.purchaseprotection.ProductPurchaseProtectionInfo
import com.tokopedia.product.detail.data.model.tradein.ValidateTradeIn
import com.tokopedia.product.detail.data.model.upcoming.ProductUpcomingData
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopCommitment
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.variant_common.model.WarehouseInfo

/**
 * Created by Yehezkiel on 28/07/20
 */
data class ProductInfoP2UiData(
        var shopInfo: ShopInfo = ShopInfo(),
        var shopSpeed: Int = 0,
        var shopChatSpeed: String = "",
        var shopRating: Float = 0F,
        var productView: String = "",
        var wishlistCount: String = "",
        var isGoApotik: Boolean = false,
        var shopBadge: String = "",
        var shopCommitment: ShopCommitment = ShopCommitment(),
        var productPurchaseProtectionInfo: ProductPurchaseProtectionInfo = ProductPurchaseProtectionInfo(),
        var validateTradeIn: ValidateTradeIn = ValidateTradeIn(),
        var cartRedirection: Map<String, CartTypeData> = mapOf(),
        var nearestWarehouseInfo: Map<String,WarehouseInfo> = mapOf(),
        var upcomingCampaigns: Map<String, ProductUpcomingData> = mapOf(),
        var vouchers: List<MerchantVoucherViewModel> = listOf(),
        var productFinancingRecommendationData: PDPInstallmentRecommendationData = PDPInstallmentRecommendationData(),
        var productFinancingCalculationData: FtInstallmentCalculationDataResponse = FtInstallmentCalculationDataResponse())