package com.tokopedia.product.detail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.data.model.bmgm.BMGMData
import com.tokopedia.product.detail.common.data.model.ar.ProductArInfo
import com.tokopedia.product.detail.common.data.model.bebasongkir.BebasOngkir
import com.tokopedia.product.detail.common.data.model.bundleinfo.BundleInfo
import com.tokopedia.product.detail.common.data.model.carttype.CartRedirection
import com.tokopedia.product.detail.common.data.model.rates.P2RatesEstimate
import com.tokopedia.product.detail.common.data.model.re.RestrictionInfoResponse
import com.tokopedia.product.detail.common.data.model.usp.UniqueSellingPointTokoCabang
import com.tokopedia.product.detail.common.data.model.warehouse.NearestWarehouseResponse
import com.tokopedia.product.detail.data.model.bottom_sheet_edu.BottomSheetEduData
import com.tokopedia.product.detail.data.model.bottom_sheet_edu.asUiModel
import com.tokopedia.product.detail.data.model.custom_info_title.CustomInfoTitle
import com.tokopedia.product.detail.data.model.financing.FtInstallmentCalculationDataResponse
import com.tokopedia.product.detail.data.model.financing.PDPInstallmentRecommendationData
import com.tokopedia.product.detail.data.model.generalinfo.ObatKeras
import com.tokopedia.product.detail.data.model.merchantvouchersummary.MerchantVoucherSummary
import com.tokopedia.product.detail.data.model.navbar.NavBar
import com.tokopedia.product.detail.data.model.purchaseprotection.ProductPurchaseProtectionInfo
import com.tokopedia.product.detail.data.model.review.MostHelpfulReviewData
import com.tokopedia.product.detail.data.model.review.ProductRatingCount
import com.tokopedia.product.detail.data.model.review.ProductReviewImageListQuery
import com.tokopedia.product.detail.data.model.shop.ProductShopBadge
import com.tokopedia.product.detail.data.model.shopFinishRate.ShopFinishRate
import com.tokopedia.product.detail.data.model.shop_additional.ProductShopAdditional
import com.tokopedia.product.detail.data.model.shop_review.ShopReviewData
import com.tokopedia.product.detail.data.model.shop_review.asUiModel
import com.tokopedia.product.detail.data.model.social_proof.SocialProofData
import com.tokopedia.product.detail.data.model.social_proof.asUiModel
import com.tokopedia.product.detail.data.model.ticker.ProductTicker
import com.tokopedia.product.detail.data.model.tradein.ValidateTradeIn
import com.tokopedia.product.detail.data.model.upcoming.ProductUpcomingData
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopCommitment
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.data.shopscore.ProductShopRatingQuery
import com.tokopedia.shop.common.graphql.data.shopspeed.ProductShopChatSpeedQuery
import com.tokopedia.shop.common.graphql.data.shopspeed.ProductShopPackSpeedQuery

/**
 * Created by Yehezkiel on 20/07/20
 */
data class ProductInfoP2Data(
    @SerializedName("error")
    @Expose
    var error: P2Error = P2Error(),

    @SerializedName("productView")
    @Expose
    var productView: String = "",

    @SerializedName("wishlistCount")
    @Expose
    var wishlistCount: String = "",

    @SerializedName("shopCommitment")
    @Expose
    var shopCommitment: ShopCommitment.Result = ShopCommitment.Result(),

    @SerializedName("ppGetItemDetailPage")
    @Expose
    var productPurchaseProtectionInfo: ProductPurchaseProtectionInfo = ProductPurchaseProtectionInfo(),

    @SerializedName("shopTopChatSpeed")
    @Expose
    var shopChatSpeed: ProductShopChatSpeedQuery = ProductShopChatSpeedQuery(),

    @SerializedName("shopInfo")
    @Expose
    var shopInfo: ShopInfo = ShopInfo(),

    @SerializedName("shopRatingsQuery")
    @Expose
    var shopRating: ProductShopRatingQuery = ProductShopRatingQuery(),

    @SerializedName("shopPackSpeed")
    @Expose
    var shopSpeed: ProductShopPackSpeedQuery = ProductShopPackSpeedQuery(),

    @SerializedName("reputationShop")
    @Expose
    var shopBadge: ProductShopBadge = ProductShopBadge(),

    @SerializedName("validateTradeIn")
    @Expose
    var validateTradeIn: ValidateTradeIn = ValidateTradeIn(),

    @SerializedName("cartRedirection")
    @Expose
    var cartRedirection: CartRedirection = CartRedirection(),

    @SerializedName("nearestWarehouse")
    @Expose
    var nearestWarehouseInfo: List<NearestWarehouseResponse> = listOf(),

    @SerializedName("upcomingCampaigns")
    @Expose
    var upcomingCampaigns: List<ProductUpcomingData> = listOf(),

    @SerializedName("installmentRecommendation")
    @Expose
    var productFinancingRecommendationData: PDPInstallmentRecommendationData = PDPInstallmentRecommendationData(),

    @SerializedName("installmentCalculation")
    @Expose
    var productFinancingCalculationData: FtInstallmentCalculationDataResponse = FtInstallmentCalculationDataResponse(),

    @SerializedName("restrictionInfo")
    @Expose
    var restrictionInfo: RestrictionInfoResponse = RestrictionInfoResponse(),

    @SerializedName("merchantVoucherSummary")
    @Expose
    var merchantVoucherSummary: MerchantVoucherSummary = MerchantVoucherSummary(),

    @SerializedName("ratesEstimate")
    @Expose
    var ratesEstimate: List<P2RatesEstimate> = listOf(),

    @SerializedName("bebasOngkir")
    @Expose
    var bebasOngkir: BebasOngkir = BebasOngkir(),

    @SerializedName("uniqueSellingPoint")
    @Expose
    var uspTokoCabangData: UniqueSellingPointTokoCabang = UniqueSellingPointTokoCabang(),

    @SerializedName("mostHelpFulReviewData")
    @Expose
    var mostHelpFulReviewData: MostHelpfulReviewData = MostHelpfulReviewData(),

    @SerializedName("reviewImage")
    @Expose
    var reviewImage: ProductReviewImageListQuery = ProductReviewImageListQuery(),

    @SerializedName("bundleInfo")
    @Expose
    var bundleInfoList: List<BundleInfo> = emptyList(),

    @SerializedName("rating")
    @Expose
    var rating: ProductRatingCount = ProductRatingCount(),

    @SerializedName("ticker")
    @Expose
    var ticker: ProductTicker = ProductTicker(),

    @SerializedName("navBar")
    @Expose
    var navBar: NavBar = NavBar(),

    @SerializedName("shopFinishRate")
    @Expose
    var shopFinishRate: ShopFinishRate = ShopFinishRate(),

    @SerializedName("shopAdditional")
    @Expose
    var shopAdditional: ProductShopAdditional = ProductShopAdditional(),

    @SerializedName("arInfo")
    @Expose
    var arInfo: ProductArInfo = ProductArInfo(),

    @SerializedName("obatKeras")
    @Expose
    var obatKeras: ObatKeras = ObatKeras(),

    @SerializedName("customInfoTitle")
    @Expose
    val customInfoTitle: List<CustomInfoTitle> = emptyList(),

    @SerializedName("socialProofComponent")
    @Expose
    val socialProof: List<SocialProofData> = emptyList(),

    @SerializedName("reviewList")
    @Expose
    val shopReview: ShopReviewData = ShopReviewData(),

    @SerializedName("bottomSheetEdu")
    @Expose
    val bottomSheetEdu: BottomSheetEduData = BottomSheetEduData(),

    @SerializedName("bmgm")
    @Expose
    val bmgm: BMGMData = BMGMData()
) {
    data class Response(
        @SerializedName("pdpGetData")
        @Expose
        var response: ProductInfoP2Data = ProductInfoP2Data()
    )
}

fun ProductInfoP2Data.asUiModel() = ProductInfoP2UiData(
    shopInfo = shopInfo,
    shopSpeed = shopSpeed.hour,
    shopChatSpeed = shopChatSpeed.messageResponseTime,
    shopRating = shopRating.ratingScore,
    productView = productView,
    wishlistCount = wishlistCount,
    shopBadge = shopBadge.badge,
    shopCommitment = shopCommitment.shopCommitment,
    productPurchaseProtectionInfo = productPurchaseProtectionInfo,
    validateTradeIn = validateTradeIn,
    cartRedirection = cartRedirection.data.associateBy({ it.productId }, { it }),
    nearestWarehouseInfo = nearestWarehouseInfo.associateBy({ it.productId }, { it.warehouseInfo }),
    upcomingCampaigns = upcomingCampaigns.associateBy { it.productId ?: "" },
    productFinancingRecommendationData = productFinancingRecommendationData,
    productFinancingCalculationData = productFinancingCalculationData,
    ratesEstimate = ratesEstimate,
    restrictionInfo = restrictionInfo,
    bebasOngkir = bebasOngkir,
    uspImageUrl = uspTokoCabangData.uspBoe.uspIcon,
    merchantVoucherSummary = merchantVoucherSummary,
    helpfulReviews = mostHelpFulReviewData.list,
    imageReview = DynamicProductDetailMapper.generateImageReview(reviewImage),
    alternateCopy = cartRedirection.alternateCopy,
    bundleInfoMap = bundleInfoList.associateBy { it.productId },
    rating = rating,
    ticker = ticker,
    navBar = navBar,
    shopFinishRate = shopFinishRate.finishRate,
    shopAdditional = shopAdditional,
    arInfo = arInfo,
    obatKeras = obatKeras,
    customInfoTitle = customInfoTitle,
    socialProof = socialProof.asUiModel(),
    shopReview = shopReview.asUiModel(),
    bottomSheetEdu = bottomSheetEdu.asUiModel(),
    bmgm = bmgm
)
