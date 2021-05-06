package com.tokopedia.product.detail.data.model

import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
import com.tokopedia.product.detail.common.data.model.warehouse.WarehouseInfo
import com.tokopedia.product.detail.data.model.financing.FtInstallmentCalculationDataResponse
import com.tokopedia.product.detail.data.model.financing.PDPInstallmentRecommendationData
import com.tokopedia.product.detail.data.model.merchantvouchersummary.MerchantVoucherSummary
import com.tokopedia.product.detail.data.model.purchaseprotection.ProductPurchaseProtectionInfo
import com.tokopedia.product.detail.data.model.ratesestimate.P2RatesEstimate
import com.tokopedia.product.detail.data.model.restrictioninfo.BebasOngkir
import com.tokopedia.product.detail.data.model.restrictioninfo.RestrictionInfoResponse
import com.tokopedia.product.detail.data.model.review.ImageReview
import com.tokopedia.product.detail.data.model.review.Review
import com.tokopedia.product.detail.data.model.tradein.ValidateTradeIn
import com.tokopedia.product.detail.data.model.upcoming.ProductUpcomingData
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopCommitment
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo

/**
 * Created by Yehezkiel on 28/07/20
 */
data class ProductInfoP2UiData(
        var shopInfo: ShopInfo = ShopInfo(),
        var shopSpeed: Long = 0L,
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
        var nearestWarehouseInfo: Map<String, WarehouseInfo> = mapOf(),
        var upcomingCampaigns: Map<String, ProductUpcomingData> = mapOf(),
        var vouchers: List<MerchantVoucherViewModel> = listOf(),
        var productFinancingRecommendationData: PDPInstallmentRecommendationData = PDPInstallmentRecommendationData(),
        var productFinancingCalculationData: FtInstallmentCalculationDataResponse = FtInstallmentCalculationDataResponse(),
        var restrictionInfo: RestrictionInfoResponse = RestrictionInfoResponse(),
        var ratesEstimate: List<P2RatesEstimate> = listOf(),
        var bebasOngkir: BebasOngkir = BebasOngkir(),
        var uspImageUrl: String = "",
        var merchantVoucherSummary: MerchantVoucherSummary = MerchantVoucherSummary(),
        var imageReviews: ImageReview? = null,
        var helpfulReviews: List<Review>? = null,
)