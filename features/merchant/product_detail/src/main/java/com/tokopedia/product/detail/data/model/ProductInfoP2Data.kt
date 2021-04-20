package com.tokopedia.product.detail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.gallery.networkmodel.ImageReviewGqlResponse
import com.tokopedia.merchantvoucher.common.gql.data.MerchantVoucherQueryResult
import com.tokopedia.product.detail.common.data.model.carttype.CartRedirection
import com.tokopedia.product.detail.data.model.financing.FtInstallmentCalculationDataResponse
import com.tokopedia.product.detail.data.model.financing.PDPInstallmentRecommendationData
import com.tokopedia.product.detail.data.model.merchantvouchersummary.MerchantVoucherSummary
import com.tokopedia.product.detail.data.model.nearestwarehouse.NearestWarehouseResponse
import com.tokopedia.product.detail.data.model.purchaseprotection.ProductPurchaseProtectionInfo
import com.tokopedia.product.detail.data.model.ratesestimate.P2RatesEstimate
import com.tokopedia.product.detail.data.model.restrictioninfo.BebasOngkir
import com.tokopedia.product.detail.data.model.restrictioninfo.RestrictionInfoResponse
import com.tokopedia.product.detail.data.model.review.MostHelpfulReviewData
import com.tokopedia.product.detail.data.model.restrictioninfo.UniqueSellingPointTokoCabang
import com.tokopedia.product.detail.data.model.shop.ProductShopBadge
import com.tokopedia.product.detail.data.model.shopfeature.ShopFeatureP2
import com.tokopedia.product.detail.data.model.tradein.ValidateTradeIn
import com.tokopedia.product.detail.data.model.upcoming.ProductUpcomingData
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

        @SerializedName("shopFeature")
        @Expose
        var shopFeature: ShopFeatureP2 = ShopFeatureP2(),

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

        @SerializedName("merchantVoucher")
        @Expose
        var merchantVoucher: MerchantVoucherQueryResult = MerchantVoucherQueryResult(),

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
        var reviewImage: ImageReviewGqlResponse.ProductReviewImageListQuery = ImageReviewGqlResponse.ProductReviewImageListQuery()
) {
    data class Response(
            @SerializedName("pdpGetData")
            @Expose
            var response: ProductInfoP2Data = ProductInfoP2Data()
    )
}
