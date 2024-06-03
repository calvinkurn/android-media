package com.tokopedia.search.result.presentation.model

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.analytics.byteio.EntranceForm.SEARCH_PURE_GOODS_CARD
import com.tokopedia.analytics.byteio.EntranceForm.SEARCH_VIDEO_GOODS_CARD
import com.tokopedia.analytics.byteio.SourcePageType.PRODUCT_CARD
import com.tokopedia.analytics.byteio.SourcePageType.VIDEO
import com.tokopedia.analytics.byteio.search.AppLogSearch
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.GOODS
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.VIDEO_GOODS
import com.tokopedia.analytics.byteio.topads.AppLogTopAds
import com.tokopedia.analytics.byteio.topads.models.AdsLogRealtimeClickModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowOverModel
import com.tokopedia.kotlin.extensions.view.ifNullOrBlank
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationAdsLog
import com.tokopedia.search.analytics.SearchTracking
import com.tokopedia.search.result.presentation.model.LabelGroupDataView.Companion.hasFulfillment
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.addtocart.AddToCartConstant.DEFAULT_PARENT_ID
import com.tokopedia.search.result.product.byteio.ByteIORanking
import com.tokopedia.search.result.product.byteio.ByteIORankingImpl
import com.tokopedia.search.result.product.byteio.ByteIOTrackingData
import com.tokopedia.search.result.product.productitem.ProductItemVisitable
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationConstant.DEFAULT_KEYWORD_INTENT
import com.tokopedia.search.result.product.samesessionrecommendation.SameSessionRecommendationConstant.KEYWORD_INTENT_LOW
import com.tokopedia.search.result.product.wishlist.Wishlistable
import com.tokopedia.search.utils.getPositionNameMap
import com.tokopedia.search.utils.orNone
import com.tokopedia.topads.sdk.domain.model.Badge
import com.tokopedia.topads.sdk.domain.model.FreeOngkir
import com.tokopedia.topads.sdk.domain.model.LabelGroup
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.text.currency.StringUtils
import com.tokopedia.topads.sdk.domain.model.Data as TopAdsProductData

class ProductItemDataView:
    ImpressHolder(),
    ProductItemVisitable,
    Wishlistable,
    ByteIORanking by ByteIORankingImpl() {

    var productID: String = ""
    var warehouseID: String = ""
    var productName: String = ""
    var imageUrl: String = ""
    var imageUrl300: String = ""
    var imageUrl700: String = ""
    var ratingString: String = ""
    var price: String = ""
    var priceInt = 0
    var priceRange: String = ""
    var shopID: String = ""
    var shopName: String = ""
    var shopCity: String = ""
    var shopUrl: String = ""
    override var isWishlisted = false
    var isWishlistButtonEnabled = true
    var badgesList: List<BadgeItemDataView>? = null
    var position = 0
    var originalPrice = ""
    var discountPercentage = 0
    var categoryID = 0
    var categoryName: String = ""
    var categoryBreadcrumb: String? = ""
    var isTopAds = false
    var isOrganicAds = false
    var topadsImpressionUrl: String? = null
    var topadsClickUrl: String? = null
    var topadsWishlistUrl: String? = null
    var topadsClickShopUrl: String? = null
    var isNew = false
    var labelGroupList: List<LabelGroupDataView>? = mutableListOf()
    var labelGroupVariantList: List<LabelGroupVariantDataView> = mutableListOf()
    var freeOngkirDataView = FreeOngkirDataView()
    var boosterList = ""
    var sourceEngine = ""
    var minOrder = 1
    var productUrl = ""
    var pageTitle: String? = null
    val isAds: Boolean
        get() = isTopAds || isOrganicAds
    val categoryString: String?
        get() = if (StringUtils.isBlank(categoryName)) categoryBreadcrumb else categoryName
    var dimension90: String = ""
    var topadsTag: Int = 0
    var applink: String = ""
    var customVideoURL: String = ""
    var productListType: String = ""
    var dimension131: String = ""
    var keywordIntention: Int = DEFAULT_KEYWORD_INTENT
    var showButtonAtc: Boolean = false
    var parentId: String = DEFAULT_PARENT_ID
    var isPortrait: Boolean = false
    var isImageBlurred: Boolean = false
    var byteIOTrackingData: ByteIOTrackingData = ByteIOTrackingData()
    val byteIOImpressHolder = ImpressHolder()
    var recommendationAdsLog: RecommendationAdsLog = RecommendationAdsLog()

    override fun setWishlist(productID: String, isWishlisted: Boolean) {
        if (this.productID == productID) {
            this.isWishlisted = isWishlisted
        }
    }

    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    fun getProductAsObjectDataLayer(
        filterSortParams: String,
        componentId: String,
        additionalPositionMap: Map<String, String> = emptyMap(),
    ): Any {
        return DataLayer.mapOf(
                "name", productName,
                "id", productID,
                "price", priceInt,
                "brand", "none / other",
                "category", categoryBreadcrumb,
                "variant", "none / other",
                "list", SearchTracking.getActionFieldString(isOrganicAds, topadsTag, componentId),
                "index", position.toString(),
                "dimension40", SearchTracking.getActionFieldString(isOrganicAds, topadsTag, componentId),
                "dimension56", warehouseID.ifNullOrBlank { "0" },
                "dimension61", filterSortParams.ifEmpty { "none / other" },
                "dimension79", shopID,
                "dimension81", getDimension81(),
                "dimension83", getFreeOngkirDataLayer(),
                "dimension87", "search result",
                "dimension88", "search - product",
                "dimension90", dimension90,
                "dimension96", boosterList,
                "dimension99", System.currentTimeMillis(),
                "dimension100", sourceEngine,
                "dimension115", getDimension115(additionalPositionMap),
                "dimension131", dimension131.orNone(),
                "dimension58", hasLabelGroupFulfillment.toString(),
        )
    }

    fun getAtcObjectDataLayer(
        filterSortParams: String,
        componentId: String,
        cartId: String?,
        additionalPositionMap: Map<String, String> = emptyMap(),
    ): Any {
        return DataLayer.mapOf(
            "name", productName,
            "id", productID,
            "price", priceInt,
            "brand", "none / other",
            "category", categoryBreadcrumb,
            "variant", "none / other",
            "list", SearchTracking.getActionFieldString(isOrganicAds, topadsTag, componentId),
            "index", position.toString(),
            "dimension40", SearchTracking.getActionFieldString(isOrganicAds, topadsTag, componentId),
            "dimension45", cartId,
            "dimension56", warehouseID.ifNullOrBlank { "0" },
            "dimension61", filterSortParams.ifEmpty { "none / other" },
            "dimension87", "search result",
            "dimension88", "search - product",
            "dimension115", getDimension115(additionalPositionMap),
            "dimension131", dimension131.orNone(),
            "quantity", minOrder,
            "shop_id", shopID,
            "shop_name", shopName,
            "shop_type", "none / other",
            "dimension58", hasLabelGroupFulfillment.toString(),
        )
    }

    fun shouldOpenVariantBottomSheet(): Boolean = hasParent()

    private fun hasParent() = parentId != "" && parentId != DEFAULT_PARENT_ID

    private fun getDimension81(): String {
        val shopType = badgesList?.find { it.isShown && it.imageUrl.isNotEmpty() && it.title.isNotEmpty() }
        return shopType?.title ?: "regular merchant"
    }

    private fun getFreeOngkirDataLayer(): String {
        val isFreeOngkirActive = isFreeOngkirActive
        val hasLabelGroupFulfillment = hasLabelGroupFulfillment

        return when {
            isFreeOngkirActive && hasLabelGroupFulfillment -> "bebas ongkir extra"
            isFreeOngkirActive && !hasLabelGroupFulfillment -> "bebas ongkir"
            else -> "none / other"
        }
    }

    private val isFreeOngkirActive: Boolean
        get() = freeOngkirDataView.isActive

    override val hasLabelGroupFulfillment: Boolean
        get() = hasFulfillment(labelGroupList)

    fun getDimension115(positionMap: Map<String, String>): String {
        return labelGroupList.getPositionNameMap().plus(positionMap)
            .map { with(it) { "$key.$value" } }
            .joinToString { it }
    }

    fun createAdditionalLabel(
        positionMap: Map<String, String>,
    ): Map<String, String> {
        return when {
            isImageBlurred -> positionMap.plus(LABEL_POSITION_SHOW_BLUR to LABEL_POSITION_BLUR)
            else -> positionMap
        }
    }

    val isKeywordIntentionLow : Boolean
        get() = keywordIntention == KEYWORD_INTENT_LOW

    private val productIdByteIo: String
        get() = if (parentId.isBlank() || parentId == "0") productID else parentId

    fun asAdsLogShowModel(): AdsLogShowModel {
        return AdsLogShowModel(
            recommendationAdsLog.creativeID.toLongOrZero(),
            recommendationAdsLog.logExtra,
            AdsLogShowModel.AdExtraData(
                productId = productIdByteIo,
                productName = productName
            )
        )
    }

    fun asAdsLogShowOverModel(visiblePercentage: Int): AdsLogShowOverModel {
        return AdsLogShowOverModel(
            recommendationAdsLog.creativeID.toLongOrZero(),
            recommendationAdsLog.logExtra,
            AdsLogShowOverModel.AdExtraData(
                productId = productIdByteIo,
                productName = productName,
                sizePercent = visiblePercentage.toString()
            )
        )
    }

    fun asAdsLogRealtimeClickModel(refer: String): AdsLogRealtimeClickModel {
        return AdsLogRealtimeClickModel(
            refer,
            recommendationAdsLog.creativeID.toLongOrZero(),
            recommendationAdsLog.logExtra,
            AdsLogRealtimeClickModel.AdExtraData(
                productId = productIdByteIo,
                productName = productName
            )
        )
    }

    fun asByteIOSearchResult(aladdinButtonType: String?) =
        AppLogSearch.SearchResult(
            imprId = byteIOTrackingData.imprId,
            searchId = byteIOTrackingData.searchId,
            searchEntrance = byteIOTrackingData.searchEntrance,
            searchResultId = byteIOProductId(),
            listItemId = null,
            itemRank = null,
            listResultType = null,
            productID = byteIOProductId(),
            searchKeyword = byteIOTrackingData.keyword,
            tokenType = if (customVideoURL.isBlank()) GOODS else VIDEO_GOODS,
            rank = getRank(),
            isAd = isAds,
            isFirstPage = byteIOTrackingData.isFirstPage,
            shopId = shopID,
            aladdinButtonType = aladdinButtonType,
        )

    private fun byteIOProductId(): String =
        if (hasParent()) parentId
        else productID

    fun asByteIOProduct() = AppLogSearch.Product(
        entranceForm = if (customVideoURL.isBlank()) SEARCH_PURE_GOODS_CARD else SEARCH_VIDEO_GOODS_CARD,
        isAd = isAds,
        productID = byteIOProductId(),
        searchID = byteIOTrackingData.searchId,
        requestID = byteIOTrackingData.imprId,
        searchResultID = byteIOProductId(),
        listItemId = null,
        itemRank = null,
        listResultType = null,
        searchKeyword = byteIOTrackingData.keyword,
        tokenType = if (customVideoURL.isBlank()) GOODS else VIDEO_GOODS,
        rank = getRank(),
        shopID = shopID,
        searchEntrance = byteIOTrackingData.searchEntrance,
        sourcePageType = if (customVideoURL.isBlank()) PRODUCT_CARD else VIDEO,
    )

    companion object {
        private const val LABEL_POSITION_SHOW_BLUR = "show"
        private const val LABEL_POSITION_BLUR = "blur"
        fun create(
            topAds: TopAdsProductData,
            position: Int,
            dimension90: String,
            productListType: String,
            externalReference: String,
            keywordIntention: Int,
            showButtonAtc: Boolean,
            byteIOTrackingData: ByteIOTrackingData,
        ): ProductItemDataView {
            val item = ProductItemDataView()
            item.productID = topAds.product.id
            item.isTopAds = true
            item.topadsImpressionUrl = topAds.product.image.s_url
            item.topadsClickUrl = topAds.productClickUrl
            item.topadsWishlistUrl = topAds.productWishlistUrl
            item.topadsClickShopUrl = topAds.shopClickUrl
            item.topadsTag = topAds.tag
            item.productName = topAds.product.name
            item.price = topAds.product.priceFormat
            item.priceRange = topAds.product.priceRange
            item.priceInt = CurrencyFormatHelper.convertRupiahToInt(topAds.product.priceFormat)
            item.shopCity = topAds.shop.location
            item.imageUrl = topAds.product.image.s_ecs
            item.imageUrl300 = topAds.product.image.m_ecs
            item.imageUrl700 = topAds.product.image.m_ecs
            item.isWishlisted = topAds.product.isWishlist
            item.ratingString = topAds.product.productRatingFormat
            item.badgesList = mapBadges(topAds.shop.badges)
            item.isNew = topAds.product.isProductNewLabel
            item.shopID = topAds.shop.id
            item.shopName = topAds.shop.name
            item.shopUrl = topAds.shop.uri
            item.originalPrice = topAds.product.campaign.originalPrice
            item.discountPercentage = topAds.product.campaign.discountPercentage
            item.labelGroupList = mapLabelGroupList(topAds.product.labelGroupList)
            item.freeOngkirDataView = mapFreeOngkir(topAds.product.freeOngkir)
            item.position = position
            item.categoryID = topAds.product.category.id
            item.categoryBreadcrumb = topAds.product.categoryBreadcrumb
            item.productUrl = topAds.product.uri
            item.minOrder = topAds.product.productMinimumOrder
            item.dimension90 = dimension90
            item.applink = topAds.applinks
            item.customVideoURL = topAds.product.customVideoUrl
            item.productListType = productListType
            item.dimension131 = externalReference
            item.keywordIntention = keywordIntention
            item.showButtonAtc = showButtonAtc
            item.parentId = topAds.product.parentId
            item.byteIOTrackingData = byteIOTrackingData
            item.recommendationAdsLog = RecommendationAdsLog(topAds.creativeId, topAds.logExtra)
            return item
        }

        private fun mapBadges(badges: List<Badge>): List<BadgeItemDataView> {
            return badges.map { badge ->
                BadgeItemDataView(badge.imageUrl, badge.title, badge.isShow)
            }
        }

        private fun mapLabelGroupList(labelGroupList: List<LabelGroup>): List<LabelGroupDataView> {
            return labelGroupList.map { labelGroup ->
                LabelGroupDataView(
                    labelGroup.position,
                    labelGroup.type,
                    labelGroup.title,
                    labelGroup.imageUrl,
                    labelGroup.styleList.map(StyleDataView::create)
                )
            }
        }

        private fun mapFreeOngkir(freeOngkir: FreeOngkir): FreeOngkirDataView {
            return FreeOngkirDataView(freeOngkir.isActive, freeOngkir.imageUrl)
        }
    }
}
