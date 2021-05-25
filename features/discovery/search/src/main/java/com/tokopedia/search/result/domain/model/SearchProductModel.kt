package com.tokopedia.search.result.domain.model

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.domain.model.TopAdsModel
import java.util.*

data class SearchProductModel(
        @SerializedName("ace_search_product_v4")
        @Expose
        val searchProduct: SearchProduct = SearchProduct(),

        @SerializedName("quick_filter")
        @Expose
        var quickFilterModel: DataValue = DataValue(),

        @SerializedName("productAds")
        @Expose
        val topAdsModel: TopAdsModel = TopAdsModel(),

        @SerializedName("headlineAds")
        @Expose
        val cpmModel: CpmModel = CpmModel(),

        @SerializedName("global_search_navigation")
        @Expose
        val globalSearchNavigation: GlobalSearchNavigation = GlobalSearchNavigation(),

        @SerializedName("searchInspirationCarouselV2")
        @Expose
        val searchInspirationCarousel: SearchInspirationCarousel = SearchInspirationCarousel(),

        @SerializedName("searchInspirationWidget")
        @Expose
        val searchInspirationWidget: SearchInspirationWidget = SearchInspirationWidget()
) {

    private val topAdsImageViewModelList: MutableList<TopAdsImageViewModel> = mutableListOf()

    fun setTopAdsImageViewModelList(topAdsImageViewModelList: List<TopAdsImageViewModel>) {
        this.topAdsImageViewModelList.clear()
        this.topAdsImageViewModelList.addAll(topAdsImageViewModelList)
    }

    fun getTopAdsImageViewModelList(): List<TopAdsImageViewModel> = topAdsImageViewModelList

    data class SearchProduct (
            @SerializedName("header")
            @Expose
            val header: SearchProductHeader = SearchProductHeader(),

            @SerializedName("data")
            @Expose
            val data: SearchProductData = SearchProductData()
    )

    data class SearchProductHeader(
            @SerializedName("totalData")
            @Expose
            val totalData: Int = 0,

            @SerializedName("totalDataText")
            @Expose
            val totalDataText: String = "",

            @SerializedName("defaultView")
            @Expose
            val defaultView: Int = 0,

            @SerializedName("responseCode")
            @Expose
            val responseCode: String = "0",

            @SerializedName("errorMessage")
            @Expose
            val errorMessage: String = "",

            @SerializedName("additionalParams")
            @Expose
            val additionalParams: String = "",

            @SerializedName("keywordProcess")
            @Expose
            val keywordProcess: String = "0"
    )

    data class SearchProductData(
            @SerializedName("isQuerySafe")
            @Expose
            val isQuerySafe: Boolean = true,

            @SerializedName("autocompleteApplink")
            @Expose
            val autocompleteApplink: String = "",

            @SerializedName("redirection")
            @Expose
            val redirection: Redirection = Redirection(),

            @SerializedName("ticker")
            @Expose
            val ticker: Ticker = Ticker(),

            @SerializedName("related")
            @Expose
            val related: Related = Related(),

            @SerializedName("suggestion")
            @Expose
            val suggestion: Suggestion = Suggestion(),

            @SerializedName("banner")
            @Expose
            val banner: Banner = Banner(),

            @SerializedName("products")
            @Expose
            val productList: List<Product> = listOf()
    )

    data class Redirection(
            @SerializedName("redirectApplink")
            @Expose
            val redirectApplink: String = ""
    )

    data class Ticker(
            @SerializedName("text")
            @Expose
            val text: String = "",

            @SerializedName("query")
            @Expose
            val query: String = "",

            @SerializedName("typeId")
            @Expose
            val typeId: Int = 0
    )

    data class Related(
            @SerializedName("relatedKeyword")
            @Expose
            val relatedKeyword: String = "",

            @SerializedName("position")
            @Expose
            val position: Int = 0,

            @SerializedName("otherRelated")
            @Expose
            val otherRelatedList: List<OtherRelated> = listOf()
    )

    data class OtherRelated(
            @SerializedName("keyword")
            @Expose
            val keyword: String = "",

            @SerializedName("url")
            @Expose
            val url: String = "",

            @SerializedName("applink")
            @Expose
            val applink: String = "",

            @SerializedName("product")
            @Expose
            val productList: List<OtherRelatedProduct> = listOf()
    )

    data class OtherRelatedProduct(
            @SerializedName("id")
            @Expose
            val id: String = "",

            @SerializedName("name")
            @Expose
            val name: String = "",

            @SuppressLint("Invalid Data Type")
            @SerializedName("price")
            @Expose
            val price: Int = 0,

            @SerializedName("imageUrl")
            @Expose
            val imageUrl: String = "",

            @SerializedName("url")
            @Expose
            val url: String = "",

            @SerializedName("applink")
            @Expose
            val applink: String = "",

            @SerializedName("priceStr")
            @Expose
            val priceString: String = "",

            @SerializedName("wishlist")
            @Expose
            val isWishlisted: Boolean = false,

            @SerializedName("shop")
            @Expose
            val shop: OtherRelatedProductShop = OtherRelatedProductShop(),

            @SerializedName("badges")
            @Expose
            val badgeList: List<OtherRelatedProductBadge> = listOf(),

            @SerializedName("ratingAverage")
            @Expose
            val ratingAverage: String = "",

            @SerializedName("labelGroups")
            @Expose
            val labelGroupList: List<ProductLabelGroup> = listOf(),

            @SerializedName("freeOngkir")
            @Expose
            val freeOngkir: OtherRelatedProductFreeOngkir = OtherRelatedProductFreeOngkir(),

            @SerializedName("ads")
            @Expose
            val ads: ProductAds = ProductAds()
    ) {
            fun isOrganicAds(): Boolean = ads.id.isNotEmpty()
    }

    data class OtherRelatedProductShop(
            @SerializedName("city")
            @Expose
            val city: String = ""
    )

    data class OtherRelatedProductBadge(
        @SerializedName("imageUrl")
        @Expose
        val imageUrl: String = "",

        @SerializedName("show")
        @Expose
        val isShown: Boolean = false
    )

    data class OtherRelatedProductFreeOngkir(
        @SerializedName("isActive")
        @Expose
        val isActive: Boolean = false,

        @SerializedName("imgUrl")
        @Expose
        val imageUrl: String = ""
    )

    data class Suggestion(
            @SerializedName("suggestion")
            @Expose
            val suggestion: String = "",

            @SerializedName("query")
            @Expose
            val query: String = "",

            @SerializedName("text")
            @Expose
            val text: String = ""
    )

    data class Banner(
            @SerializedName("position")
            @Expose
            val position: Int = -1,

            @SerializedName("text")
            @Expose
            val text: String = "",

            @SerializedName("applink")
            @Expose
            val applink: String = "",

            @SerializedName("imageUrl")
            @Expose
            val imageUrl: String = "",
    )

    data class Product(
            @SerializedName("id")
            @Expose
            val id: String = "",

            @SerializedName("name")
            @Expose
            val name: String = "",

            @SerializedName("ads")
            @Expose
            val ads: ProductAds = ProductAds(),

            @SerializedName("shop")
            @Expose
            val shop: ProductShop = ProductShop(),

            @SerializedName("freeOngkir")
            @Expose
            val freeOngkir: ProductFreeOngkir = ProductFreeOngkir(),

            @SerializedName("imageUrl")
            @Expose
            val imageUrl: String = "",

            @SerializedName("imageUrl300")
            @Expose
            val imageUrl300: String = "",

            @SerializedName("imageUrl700")
            @Expose
            val imageUrl700: String = "",

            @SuppressLint("Invalid Data Type")
            @SerializedName("price")
            @Expose
            val price: String = "",

            @SerializedName("priceInt")
            @Expose
            val priceInt: Int = 0,

            @SerializedName("priceRange")
            @Expose
            val priceRange: String = "",

            @SerializedName("categoryBreadcrumb")
            @Expose
            val categoryBreadcrumb: String = "",

            @SerializedName("categoryId")
            @Expose
            val categoryId: Int = 0,

            @SerializedName("categoryName")
            @Expose
            val categoryName: String = "",

            @SerializedName("ratingAverage")
            @Expose
            val ratingAverage: String = "",

            @SerializedName("originalPrice")
            @Expose
            val originalPrice: String = "",

            @SerializedName("discountPercentage")
            @Expose
            val discountPercentage: Int = 0,

            @SerializedName("warehouseIdDefault")
            @Expose
            val warehouseIdDefault: String = "",

            @SerializedName("boosterList")
            @Expose
            val boosterList: String = "",

            @SerializedName("source_engine")
            @Expose
            val sourceEngine: String = "",

            @SerializedName("labelGroups")
            @Expose
            val labelGroupList: List<ProductLabelGroup> = listOf(),

            @SerializedName("labelGroupVariant")
            @Expose
            val labelGroupVariantList: List<ProductLabelGroupVariant> = listOf(),

            @SerializedName("badges")
            @Expose
            val badgeList: List<ProductBadge> = listOf(),

            @SerializedName("wishlist")
            @Expose
            val isWishlist: Boolean = false,

            @SerializedName("minOrder")
            @Expose
            val minOrder: Int = 1,

            @SerializedName("url")
            @Expose
            val url: String = ""
    ) {

        fun isOrganicAds(): Boolean = ads.id.isNotEmpty()
    }

    data class ProductAds(
            @SerializedName("id")
            @Expose
            val id: String = "",

            @SerializedName("productClickUrl")
            @Expose
            val productClickUrl: String = "",

            @SerializedName("productWishlistUrl")
            @Expose
            val productWishlistUrl: String = "",

            @SerializedName("productViewUrl")
            @Expose
            val productViewUrl: String = ""
    )

    data class ProductShop(
            @SerializedName("id")
            @Expose
            val id: String = "",

            @SerializedName("name")
            @Expose
            val name: String = "",

            @SerializedName("city")
            @Expose
            val city: String = "",

            @SerializedName("rating_average")
            @Expose
            val ratingAverage: String = "",

            @SerializedName("isOfficial")
            @Expose
            val isOfficial: Boolean = false,

            @SerializedName("isPowerBadge")
            @Expose
            val isPowerBadge: Boolean = false,

            @SerializedName("url")
            @Expose
            val url: String = ""
    )

    data class ProductFreeOngkir(
            @SerializedName("isActive")
            @Expose
            val isActive: Boolean = false,

            @SerializedName("imgUrl")
            @Expose
            val imageUrl: String = ""
    )

    data class ProductLabelGroup(
            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("position")
            @Expose
            val position: String = "",

            @SerializedName("type")
            @Expose
            val type: String = "",

            @SerializedName("url")
            @Expose
            val url: String = ""
    )

    data class ProductLabelGroupVariant(
           @SerializedName("title")
           @Expose
           val title: String = "",

           @SerializedName("type")
           @Expose
           val type: String = "",

           @SerializedName("type_variant")
           @Expose
           val typeVariant: String = "",

           @SerializedName("hex_color")
           @Expose
           val hexColor: String = ""
    )

    data class ProductBadge(
            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("imageUrl")
            @Expose
            val imageUrl: String = "",

            @SerializedName("show")
            @Expose
            val isShown: Boolean = false
    )

    data class GlobalSearchNavigation(
            @SerializedName("data")
            @Expose
            val data: GlobalNavData = GlobalNavData()
    )

    data class GlobalNavData(
            @SerializedName("source")
            @Expose
            val source: String = "",

            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("keyword")
            @Expose
            val keyword: String = "",

            @SerializedName("nav_template")
            @Expose
            val navTemplate: String = "",

            @SerializedName("background")
            @Expose
            val background: String = "",

            @SerializedName("see_all_applink")
            @Expose
            val seeAllApplink: String = "",

            @SerializedName("see_all_url")
            @Expose
            val seeAllUrl: String = "",

            @SerializedName("show_topads")
            @Expose
            val isShowTopAds: Boolean = false,

            @SerializedName("list")
            @Expose
            val globalNavItems: List<GlobalNavItem> = ArrayList()
    )

    data class GlobalNavItem(
            @SerializedName("category_name")
            @Expose
            val categoryName: String = "",

            @SerializedName("name")
            @Expose
            val name: String = "",

            @SerializedName("info")
            @Expose
            val info: String = "",

            @SerializedName("image_url")
            @Expose
            val imageUrl: String = "",

            @SerializedName("applink")
            @Expose
            val applink: String = "",

            @SerializedName("url")
            @Expose
            val url: String = "",

            @SerializedName("subtitle")
            @Expose
            val subtitle: String = "",

            @SerializedName("strikethrough")
            @Expose
            val strikethrough: String = "",

            @SerializedName("background_url")
            @Expose
            val backgroundUrl: String = "",

            @SerializedName("logo_url")
            @Expose
            val logoUrl: String = ""
    )

    data class SearchInspirationCarousel(
            @SerializedName("data")
            @Expose
            val data: List<InspirationCarouselData> = listOf()
    )

    data class InspirationCarouselData(
            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("type")
            @Expose
            val type: String = "",

            @SerializedName("position")
            @Expose
            val position: Int = 0,

            @SerializedName("layout")
            @Expose
            val layout: String = "",

            @SerializedName("options")
            @Expose
            val inspirationCarouselOptions: List<InspirationCarouselOption> = listOf()
    )

    data class InspirationCarouselOption(
            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("url")
            @Expose
            val url: String = "",

            @SerializedName("applink")
            @Expose
            val applink: String = "",

            @SerializedName("banner_image_url")
            @Expose
            val bannerImageUrl: String = "",

            @SerializedName("banner_link_url")
            @Expose
            val bannerLinkUrl: String = "",

            @SerializedName("banner_applink_url")
            @Expose
            val bannerApplinkUrl: String = "",

            @SerializedName("identifier")
            @Expose
            val identifier: String = "",

            @SerializedName("product")
            @Expose
            val inspirationCarouselProducts: List<InspirationCarouselProduct> = listOf()
    )

    data class InspirationCarouselProduct (
            @SerializedName("id")
            @Expose
            val id: String = "",

            @SerializedName("name")
            @Expose
            val name: String = "",

            @SuppressLint("Invalid Data Type")
            @SerializedName("price")
            @Expose
            val price: Int = 0,

            @SerializedName("image_url")
            @Expose
            val imgUrl: String = "",

            @SerializedName("price_str")
            @Expose
            val priceStr: String = "",

            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("rating")
            @Expose
            val rating: Int = 0,

            @SerializedName("count_review")
            @Expose
            val countReview: Int = 0,

            @SerializedName("url")
            @Expose
            val url: String = "",

            @SerializedName("applink")
            @Expose
            val applink: String = "",

            @SerializedName("description")
            @Expose
            val description: List<String> = listOf(),

            @SerializedName("rating_average")
            @Expose
            val ratingAverage: String = "",

            @SerializedName("label_groups")
            @Expose
            val labelGroupList: List<ProductLabelGroup> = listOf(),

            @SerializedName("original_price")
            @Expose
            val originalPrice: String = "",

            @SerializedName("discount_percentage")
            @Expose
            val discountPercentage: Int = 0,

            @SerializedName("badges")
            @Expose
            val badgeList: List<InspirationCarouselProductBadge> = listOf(),

            @SerializedName("shop")
            @Expose
            val shop: InspirationCarouselProductShop = InspirationCarouselProductShop(),
    )

    data class InspirationCarouselProductBadge(
            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("image_url")
            @Expose
            val imageUrl: String = "",

            @SerializedName("show")
            @Expose
            val isShown: Boolean = false
    )

    data class InspirationCarouselProductShop(
            @SerializedName("city")
            @Expose
            val city: String = ""
    )

    data class SearchInspirationWidget(
            @SerializedName("data")
            @Expose
            val data: List<InspirationCardData> = listOf()
    )

    data class InspirationCardData (
            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("type")
            @Expose
            val type: String = "",

            @SerializedName("position")
            @Expose
            val position: Int = 0,

            @SerializedName("options")
            @Expose
            val inspiratioWidgetOptions: List<InspirationCardOption> = listOf()
    )

    data class InspirationCardOption (
            @SerializedName("text")
            @Expose
            val text: String = "",

            @SerializedName("img")
            @Expose
            val img: String = "",

            @SerializedName("url")
            @Expose
            val url: String = "",

            @SerializedName("color")
            @Expose
            val color: String = "",

            @SerializedName("applink")
            @Expose
            val applink: String = ""
    )
}