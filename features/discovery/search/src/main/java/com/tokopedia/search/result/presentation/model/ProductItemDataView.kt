package com.tokopedia.search.result.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant.ProductCardLabel
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.search.analytics.SearchTracking
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.utils.getFormattedPositionName
import com.tokopedia.topads.sdk.domain.model.Badge
import com.tokopedia.topads.sdk.domain.model.FreeOngkir
import com.tokopedia.topads.sdk.domain.model.LabelGroup
import com.tokopedia.utils.text.currency.StringUtils
import com.tokopedia.topads.sdk.domain.model.Data as TopAdsProductData

class ProductItemDataView : ImpressHolder(), Visitable<ProductListTypeFactory> {
    var productID: String = ""
    var warehouseID: String = ""
    var productName: String = ""
    var imageUrl: String = ""
    var imageUrl300: String = ""
    var imageUrl700: String = ""
    var ratingString: String = ""
    var price: String = ""
    var priceInt = 0
    var priceRange: String? = null
    var shopID: String = ""
    var shopName: String = ""
    var shopCity: String = ""
    var shopUrl: String = ""
    var isWishlisted = false
    var isWishlistButtonEnabled = true
    var badgesList: List<BadgeItemDataView>? = null
    var position = 0
    var originalPrice = ""
    var discountPercentage = 0
    var categoryID = 0
    var categoryName: String? = ""
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
    var isShopOfficialStore = false
    var isShopPowerMerchant = false
    var productUrl = ""
    var pageTitle: String? = null
    val isAds: Boolean
        get() = isTopAds || isOrganicAds
    val pageNumber: Int
        get() = (position - 1) / SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_ROWS.toInt() + 1
    val categoryString: String?
        get() = if (StringUtils.isBlank(categoryName)) categoryBreadcrumb else categoryName
    var dimension90: String = ""
    var topadsTag: Int = 0
    var applink: String = ""
    var customVideoURL: String = ""

    override fun type(typeFactory: ProductListTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }

    fun getProductAsObjectDataLayer(filterSortParams: String, componentId: String): Any {
        return DataLayer.mapOf(
                "name", productName,
                "id", productID,
                "price", priceInt,
                "brand", "none / other",
                "category", categoryBreadcrumb,
                "variant", "none / other",
                "list", SearchTracking.getActionFieldString(isOrganicAds, topadsTag, componentId),
                "position", position.toString(),
                "dimension61", if (filterSortParams.isEmpty()) "none / other" else filterSortParams,
                "dimension79", shopID,
                "dimension81", getDimension81(),
                "dimension83", getFreeOngkirDataLayer(),
                "dimension87", "search result",
                "dimension88", "search - product",
                "dimension90", dimension90,
                "dimension96", boosterList,
                "dimension99", System.currentTimeMillis(),
                "dimension100", sourceEngine,
                "dimension115", dimension115,
        )
    }

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

    val hasLabelGroupFulfillment: Boolean
        get() = labelGroupList?.any { it.position == ProductCardLabel.LABEL_FULFILLMENT } == true

    val dimension115: String
        get() = labelGroupList.getFormattedPositionName()

    companion object {
        fun create(
            topAds: TopAdsProductData,
            position: Int,
            dimension90: String,
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
            item.isShopOfficialStore = topAds.shop.isShop_is_official
            item.isShopPowerMerchant = topAds.shop.isGoldShop
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
                    labelGroup.position, labelGroup.type, labelGroup.title, labelGroup.imageUrl
                )
            }
        }

        private fun mapFreeOngkir(freeOngkir: FreeOngkir): FreeOngkirDataView {
            return FreeOngkirDataView(freeOngkir.isActive, freeOngkir.imageUrl)
        }
    }
}