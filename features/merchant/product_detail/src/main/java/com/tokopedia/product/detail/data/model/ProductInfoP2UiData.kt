package com.tokopedia.product.detail.data.model

import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.product.detail.common.data.model.ar.ProductArInfo
import com.tokopedia.product.detail.common.data.model.bebasongkir.BebasOngkir
import com.tokopedia.product.detail.common.data.model.carttype.AlternateCopy
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
import com.tokopedia.product.detail.common.data.model.rates.P2RatesEstimate
import com.tokopedia.product.detail.common.data.model.re.RestrictionInfoResponse
import com.tokopedia.product.detail.common.data.model.warehouse.WarehouseInfo
import com.tokopedia.product.detail.data.model.bmgm.BMGMData
import com.tokopedia.product.detail.data.model.bottom_sheet_edu.BottomSheetEduUiModel
import com.tokopedia.product.detail.data.model.custom_info_title.CustomInfoTitle
import com.tokopedia.product.detail.data.model.datamodel.review_list.ProductShopReviewUiModel
import com.tokopedia.product.detail.data.model.dynamic_oneliner_variant.DynamicOneLinerVariantResponse
import com.tokopedia.product.detail.data.model.dynamiconeliner.DynamicOneLiner
import com.tokopedia.product.detail.data.model.generalinfo.ObatKeras
import com.tokopedia.product.detail.data.model.gwp.GWPData
import com.tokopedia.product.detail.data.model.merchantvouchersummary.MerchantVoucherSummary
import com.tokopedia.product.detail.data.model.navbar.NavBar
import com.tokopedia.product.detail.data.model.promoprice.PromoPriceStyle
import com.tokopedia.product.detail.data.model.purchaseprotection.ProductPurchaseProtectionInfo
import com.tokopedia.product.detail.data.model.review.Review
import com.tokopedia.product.detail.data.model.review.ReviewImage
import com.tokopedia.product.detail.data.model.sdui.SDUIData
import com.tokopedia.product.detail.data.model.shop_additional.ProductShopAdditional
import com.tokopedia.product.detail.data.model.ticker.ProductTicker
import com.tokopedia.product.detail.data.model.ticker.TickerDataResponse
import com.tokopedia.product.detail.data.model.tradein.ValidateTradeIn
import com.tokopedia.product.detail.data.model.upcoming.ProductUpcomingData
import com.tokopedia.product.detail.view.viewholder.review.ui.ReviewRatingUiModel
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
    var restrictionInfo: RestrictionInfoResponse = RestrictionInfoResponse(),
    var ratesEstimate: List<P2RatesEstimate> = listOf(),
    var bebasOngkir: BebasOngkir = BebasOngkir(),
    var uspImageUrl: String = "",
    var merchantVoucherSummary: MerchantVoucherSummary = MerchantVoucherSummary(),
    var imageReview: ReviewImage = ReviewImage(),
    var helpfulReviews: List<Review>? = null,
    var miniCart: MutableMap<String, MiniCartItem.MiniCartItemProduct>? = null,
    var alternateCopy: List<AlternateCopy> = listOf(),
    var rating: ReviewRatingUiModel = ReviewRatingUiModel(),
    var ticker: ProductTicker = ProductTicker(),
    var navBar: NavBar = NavBar(),
    var shopFinishRate: String = "",
    var shopAdditional: ProductShopAdditional = ProductShopAdditional(),
    var arInfo: ProductArInfo = ProductArInfo(),
    var obatKeras: ObatKeras = ObatKeras(),
    var customInfoTitle: List<CustomInfoTitle> = emptyList(),
    var shopReview: ProductShopReviewUiModel = ProductShopReviewUiModel(),
    var bottomSheetEdu: BottomSheetEduUiModel = BottomSheetEduUiModel(),
    var dynamicOneLiner: List<DynamicOneLiner> = emptyList(),
    var bmgm: BMGMData = BMGMData(),
    var gwp: GWPData = GWPData(),
    var promoPriceStyle: List<PromoPriceStyle> = emptyList(),
    var dynamicOneLinerVariant: List<DynamicOneLinerVariantResponse> = emptyList(),
    var wishlistVariant: Map<String, Boolean> = mapOf(),
    var sdui: List<SDUIData> = emptyList()
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
        return ratesEstimate.firstOrNull { productId in it.listfProductId }?.productMetadata?.firstOrNull { it.productId == productId }?.value
            ?: ""
    }

    fun getOfferIdPriority(pid: String?): String {
        val gwpOfferId = gwp.data.firstOrNull { it.productIDs.contains(pid) }?.offerId.orEmpty()
        val bmgmOfferId = bmgm.data.firstOrNull { it.productIDs.contains(pid) }?.offerId.orEmpty()
        return gwpOfferId.ifBlank { bmgmOfferId }
    }

    fun getWishlistStatusByProductId(productId: String): Boolean =
        wishlistVariant.getOrElse(productId) { false }

    fun updateWishlistStatus(productId: String, isWishlist: Boolean) {
        val editedWishlist = wishlistVariant.toMutableMap()
        editedWishlist[productId] = isWishlist
        wishlistVariant = editedWishlist
    }
}
