package com.tokopedia.product.detail.data.model

import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.product.detail.common.data.model.ar.ProductArInfo
import com.tokopedia.product.detail.common.data.model.bebasongkir.BebasOngkir
import com.tokopedia.product.detail.common.data.model.bundleinfo.BundleInfo
import com.tokopedia.product.detail.common.data.model.carttype.AlternateCopy
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
import com.tokopedia.product.detail.common.data.model.rates.P2RatesEstimate
import com.tokopedia.product.detail.common.data.model.re.RestrictionInfoResponse
import com.tokopedia.product.detail.common.data.model.warehouse.WarehouseInfo
import com.tokopedia.product.detail.data.model.custom_info_title.CustomInfoTitle
import com.tokopedia.product.detail.data.model.financing.FtInstallmentCalculationDataResponse
import com.tokopedia.product.detail.data.model.financing.PDPInstallmentRecommendationData
import com.tokopedia.product.detail.data.model.generalinfo.ObatKeras
import com.tokopedia.product.detail.data.model.merchantvouchersummary.MerchantVoucherSummary
import com.tokopedia.product.detail.data.model.navbar.NavBar
import com.tokopedia.product.detail.data.model.purchaseprotection.ProductPurchaseProtectionInfo
import com.tokopedia.product.detail.data.model.review.ProductRatingCount
import com.tokopedia.product.detail.data.model.review.Review
import com.tokopedia.product.detail.data.model.review.ReviewImage
import com.tokopedia.product.detail.data.model.review_list.ReviewListData
import com.tokopedia.product.detail.data.model.shop_additional.ProductShopAdditional
import com.tokopedia.product.detail.data.model.social_proof.SocialProofData
import com.tokopedia.product.detail.data.model.ticker.ProductTicker
import com.tokopedia.product.detail.data.model.ticker.TickerDataResponse
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
    var imageReview: ReviewImage = ReviewImage(),
    var helpfulReviews: List<Review>? = null,
    var miniCart: MutableMap<String, MiniCartItem.MiniCartItemProduct>? = null,
    var alternateCopy: List<AlternateCopy> = listOf(),
    var bundleInfoMap: Map<String, BundleInfo> = emptyMap(),
    var rating: ProductRatingCount = ProductRatingCount(),
    var ticker: ProductTicker = ProductTicker(),
    var navBar: NavBar = NavBar(),
    var shopFinishRate: String = "",
    var shopAdditional: ProductShopAdditional = ProductShopAdditional(),
    var arInfo: ProductArInfo = ProductArInfo(),
    var obatKeras: ObatKeras = ObatKeras(),
    var customInfoTitle: List<CustomInfoTitle> = emptyList(),
    var socialProof: List<SocialProofData> = emptyList(),
    var reviewList: ReviewListData = ReviewListData()
) {
    fun getTickerByProductId(productId: String): List<TickerDataResponse>? {
        return ticker.tickerInfo.firstOrNull {
            productId in it.productIDs
        }?.tickerDatumResponses
    }

    fun getTotalStockMiniCartByParentId(parentId: String): Int {
        if (parentId == "0" || parentId.isEmpty()) return 0
        return miniCart?.values?.toList()?.filter {
            it.productParentId == parentId
        }?.sumBy {
            it.quantity
        } ?: 0
    }

    fun getRatesEstimateBoMetadata(productId: String): String {
        return ratesEstimate.firstOrNull { productId in it.listfProductId }?.boMetadata ?: ""
    }

    fun getRatesProductMetadata(productId: String): String {
        return ratesEstimate.firstOrNull { productId in it.listfProductId }?.productMetadata?.firstOrNull { it.productId == productId }?.value ?: ""
    }
}
