package com.tokopedia.search.result.domain.model

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.PMAX
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.PMIN
import com.tokopedia.discovery.common.constants.SearchConstant.InspirationCard.LAYOUT_FILTER
import com.tokopedia.filter.common.data.DataValue
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.data.Option.Companion.KEY_PRICE_RANGE
import com.tokopedia.search.result.domain.model.LastFilterModel.LastFilter
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.domain.model.TopAdsModel

data class SearchProductModel(
    @SerializedName("ace_search_product_v4")
    @Expose
    val searchProduct: SearchProduct = SearchProduct(),

    @SerializedName("searchProductV5")
    @Expose
    val searchProductV5: SearchProductV5 = SearchProductV5(),

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
    val searchInspirationWidget: SearchInspirationWidget = SearchInspirationWidget(),

    @SerializedName("fetchLastFilter")
    val lastFilter: LastFilter = LastFilter(),
) {

    private val topAdsImageViewModelList: MutableList<TopAdsImageViewModel> = mutableListOf()

    fun hasProducts(isV5: Boolean): Boolean =
        if (isV5) searchProductV5.data.productList.isNotEmpty()
        else searchProduct.data.productList.isNotEmpty()

    fun isQuerySafe(isV5: Boolean): Boolean =
        if (isV5) searchProductV5.header.isQuerySafe
        else searchProduct.data.isQuerySafe

    fun isPostProcessing(isV5: Boolean): Boolean =
        if (isV5) searchProductV5.header.meta.isPostProcessing
        else searchProduct.header.meta.isPostProcessing

    fun isShowButtonAtc(isV5: Boolean): Boolean =
        if (isV5) searchProductV5.header.meta.showButtonAtc
        else searchProduct.header.meta.showButtonAtc

    fun autocompleteApplink(isV5: Boolean): String =
        if (isV5) searchProductV5.header.autocompleteApplink
        else searchProduct.data.autocompleteApplink

    fun backendFilters(isV5: Boolean): String =
        if (isV5) searchProductV5.backendFilters
        else searchProduct.backendFilters

    fun componentId(isV5: Boolean): String =
        if (isV5) searchProductV5.header.componentID
        else searchProduct.header.componentId

    fun totalData(isV5: Boolean): Long =
        if (isV5) searchProductV5.header.totalData
        else searchProduct.header.totalData.toLong()

    fun additionalParams(isV5: Boolean): String =
        if (isV5) searchProductV5.header.additionalParams
        else searchProduct.header.additionalParams

    fun backendFiltersToggle(isV5: Boolean): String =
        if (isV5) searchProductV5.header.backendFiltersToggle
        else searchProduct.backendFiltersToggle

    fun keywordIntention(isV5: Boolean): Int =
        if (isV5) searchProductV5.header.keywordIntention
        else searchProduct.data.keywordIntention

    val errorMessage: String
        get() = searchProduct.errorMessage

    fun redirectApplink(isV5: Boolean): String =
        if (isV5) searchProductV5.data.redirection.redirectApplink
        else searchProduct.data.redirection.redirectApplink

    fun responseCode(isV5: Boolean) =
        if (isV5) searchProductV5.header.responseCode
        else searchProduct.header.responseCode

    fun keywordProcess(isV5: Boolean) =
        if (isV5) searchProductV5.header.keywordProcess
        else searchProduct.header.keywordProcess

    fun ticker(isV5: Boolean) =
        if (isV5) searchProductV5.data.ticker
        else searchProduct.data.ticker

    fun suggestion(isV5: Boolean) =
        if (isV5) searchProductV5.data.suggestion
        else searchProduct.data.suggestion

    fun relatedKeyword(isV5: Boolean) =
        if (isV5) searchProductV5.relatedKeyword
        else searchProduct.relatedKeyword

    fun violation(isV5: Boolean) =
        if (isV5) searchProductV5.data.violation
        else searchProduct.data.violation

    fun banner(isV5: Boolean) =
        if (isV5) searchProductV5.data.banner
        else searchProduct.data.banner

    fun productListType() = searchProduct.header.meta.productListType

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
    ) {

        val backendFilters: String
            get() = data.backendFilters

        val backendFiltersToggle: String
            get() = data.backendFiltersToggle

        val errorMessage: String
            get() = header.errorMessage

        val relatedKeyword: String
            get() = data.related.relatedKeyword
    }

    data class SearchProductHeader(
            @SerializedName("totalData")
            @Expose
            val totalData: Int = 0,

            @SerializedName("totalDataText")
            @Expose
            val totalDataText: String = "",

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
            val keywordProcess: String = "0",

            @SerializedName("componentId")
            @Expose
            val componentId: String = "",

            @SerializedName("meta")
            @Expose
            val meta: SearchProductHeaderMeta = SearchProductHeaderMeta()
    )

    data class SearchProductHeaderMeta(
        @SerializedName("productListType")
        val productListType: String = "",

        @SerializedName("isPostProcessing", alternate = ["hasPostProcessing"])
        val isPostProcessing: Boolean = false,

        @SerializedName("showButtonAtc", alternate = ["hasButtonATC"])
        val showButtonAtc: Boolean = false,
    )

    data class SearchProductData(
            @SerializedName("isQuerySafe")
            @Expose
            val isQuerySafe: Boolean = true,

            @SerializedName("autocompleteApplink")
            @Expose
            val autocompleteApplink: String = "",

            @SerializedName("backendFilters")
            @Expose
            val backendFilters: String = "",

            @SerializedName("backendFiltersToggle")
            @Expose
            val backendFiltersToggle: String = "",

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
            val productList: List<Product> = listOf(),

            @SerializedName("violation")
            @Expose
            val violation: Violation = Violation(),

            @SerializedName("keywordIntention")
            @Expose
            val keywordIntention: Int = 1,
    )

    data class Redirection(
            @SerializedName("redirectApplink", alternate = ["applink"])
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

            @SuppressLint("Invalid Data Type")
            @SerializedName("typeId", alternate = ["id"])
            @Expose
            val typeId: Int = 0,

            @SerializedName("componentId", alternate = ["componentID"])
            @Expose
            val componentId: String = "",

            @SerializedName("trackingOption")
            @Expose
            val trackingOption: Int = 0,
    )

    data class Related(
            @SerializedName("relatedKeyword")
            @Expose
            val relatedKeyword: String = "",

            @SerializedName("position")
            @Expose
            val position: Int = 0,

            @SerializedName("trackingOption")
            @Expose
            val trackingOption: Int = 0,

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

            @SerializedName("componentId")
            @Expose
            val componentId: String = "",

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
            val ads: ProductAds = ProductAds(),

            @SerializedName("componentId")
            @Expose
            val componentId: String = "",

            @SerializedName("warehouseIdDefault")
            @Expose
            val warehouseIdDefault: String = "",
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

        @SerializedName("title")
        @Expose
        val title: String = "",

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
            val text: String = "",

            @SerializedName("componentId", alternate = ["componentID"])
            @Expose
            val componentId: String = "",

            @SerializedName("trackingOption")
            @Expose
            val trackingOption: Int = 0,
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

            @SerializedName("imageUrl", alternate = ["imageURL"])
            @Expose
            val imageUrl: String = "",

            @SerializedName("componentId", alternate = ["componentID"])
            @Expose
            val componentId: String = "",

            @SerializedName("trackingOption")
            @Expose
            val trackingOption: Int = 0,
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

            @SuppressLint("Invalid Data Type")
            @SerializedName("priceInt")
            @Expose
            val priceInt: Int = 0,

            @SerializedName("priceRange")
            @Expose
            val priceRange: String = "",

            @SerializedName("categoryBreadcrumb")
            @Expose
            val categoryBreadcrumb: String = "",

            @SuppressLint("Invalid Data Type")
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
            val url: String = "",

            @SerializedName("applink")
            @Expose
            val applink: String = "",

            @SerializedName("customVideoURL")
            @Expose
            val customVideoURL: String = "",

            @SerializedName("parentId")
            @Expose
            val parentId: String = "",

            @SerializedName("isPortrait")
            @Expose
            val isPortrait: Boolean = false,
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
            val productViewUrl: String = "",

            @SerializedName("tag")
            @Expose
            val tag: Int = 0,
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

            @SerializedName("component_id")
            @Expose
            val componentId: String = "",

            @SerializedName("tracking_option")
            @Expose
            val trackingOption: String = "0",

            @SerializedName("list")
            @Expose
            val globalNavItems: List<GlobalNavItem> = ArrayList(),

            @SerializedName("info")
            @Expose
            val info: String = "",
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
            val logoUrl: String = "",

            @SerializedName("component_id")
            @Expose
            val componentId: String = "",
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

            @SerializedName("tracking_option")
            @Expose
            val trackingOption: String = "0",

            @SerializedName("options")
            @Expose
            val inspirationCarouselOptions: List<InspirationCarouselOption> = listOf()
    )

    data class InspirationCarouselOption(
            @SerializedName("title")
            @Expose
            val title: String = "",

            @SerializedName("subtitle")
            @Expose
            val subtitle: String = "",

            @SerializedName("icon_subtitle")
            @Expose
            val iconSubtitle: String = "",

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

            @SerializedName("meta")
            @Expose
            val meta: String = "",

            @SerializedName("component_id")
            @Expose
            val componentId: String = "",

            @SerializedName("product")
            @Expose
            val inspirationCarouselProducts: List<InspirationCarouselProduct> = listOf(),

            @SerializedName("card_button")
            @Expose
            val cardButton: InspirationCarouselCardButton = InspirationCarouselCardButton(),

            @SerializedName("bundle")
            val bundle: InspirationCarouselBundle = InspirationCarouselBundle(),
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

            @SerializedName("discount")
            @Expose
            val discount: String = "",

            @SerializedName("badges")
            @Expose
            val badgeList: List<InspirationCarouselProductBadge> = listOf(),

            @SerializedName("shop")
            @Expose
            val shop: InspirationCarouselProductShop = InspirationCarouselProductShop(),

            @SerializedName("freeOngkir")
            @Expose
            val freeOngkir: InspirationCarouselProductFreeOngkir = InspirationCarouselProductFreeOngkir(),

            @SerializedName("ads")
            @Expose
            val ads: ProductAds = ProductAds(),

            @SerializedName("component_id")
            @Expose
            val componentId: String = "",

            @SerializedName("customvideo_url")
            @Expose
            val customVideoURL: String = "",

            @SerializedName("label")
            @Expose
            val label: String = "",

            @SerializedName("bundle_id")
            @Expose
            val bundleId: String = "",

            @SerializedName("parent_id")
            @Expose
            val parentId: String = "",

            @SerializedName("min_order")
            @Expose
            val minOrder: String = "",

            @SerializedName("stockbar")
            @Expose
            val stockBar: InspirationCarouselStockBar = InspirationCarouselStockBar(),

            @SerializedName("warehouse_id_default")
            @Expose
            val warehouseIdDefault: String = "",
    ) {
        fun isOrganicAds(): Boolean = ads.id.isNotEmpty()
    }

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
            @SerializedName("id")
            @Expose
            val id: String = "",
            @SerializedName("name")
            @Expose
            val name: String = "",
            @SerializedName("city")
            @Expose
            val city: String = ""
    )

    data class InspirationCarouselProductFreeOngkir(
        @SerializedName("isActive")
        @Expose
        val isActive: Boolean = false,

        @SerializedName("image_url")
        @Expose
        val imageUrl: String = ""
    )

    data class InspirationCarouselStockBar(
        @SerializedName("stock")
        @Expose
        val stock: Int = 0,

        @SerializedName("original_stock")
        @Expose
        val originalStock: Int = 0,

        @SerializedName("percentage_value")
        @Expose
        val percentageValue: Int = 0,

        @SerializedName("value")
        @Expose
        val value: String = "",

        @SerializedName("color")
        @Expose
        val color: String = "",
    )

    data class InspirationCarouselCardButton(
        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("applink")
        @Expose
        val applink: String = "",
    )

    @SuppressLint("Invalid Data Type")
    data class InspirationCarouselBundle(
        @SerializedName("shop")
        val shop: Shop = Shop(),
        @SerializedName("count_sold")
        val countSold: String = "",
        @SerializedName("price")
        val price: Long = 0,
        @SerializedName("original_price")
        val originalPrice: String = "",
        @SerializedName("discount")
        val discount: String = "",
        @SerializedName("discount_percentage")
        val discountPercentage: Int = 0,
    ) {
        data class Shop(
            @SerializedName("name")
            val name: String = "",
            @SerializedName("url")
            val url: String = "",
        )
    }

    data class SearchInspirationWidget(
        @SerializedName("data")
        @Expose
        val data: List<InspirationWidgetData> = listOf()
    ) {
        fun asFilterList(): List<Filter> =
            filterWidgetList()
                .map { it.asFilter() }

        fun filterWidgetList() =
            data.filter { it.layout == LAYOUT_FILTER }
    }

    data class InspirationWidgetData (
        @SerializedName("title")
        @Expose
        val title: String = "",

        @SerializedName("header_title")
        @Expose
        val headerTitle: String = "",

        @SerializedName("header_subtitle")
        @Expose
        val headerSubtitle: String = "",

        @SerializedName("layout")
        @Expose
        val layout: String = "",

        @SerializedName("type")
        @Expose
        val type: String = "",

        @SerializedName("position")
        @Expose
        val position: Int = 0,

        @SerializedName("options")
        @Expose
        val inspirationWidgetOptions: List<InspirationWidgetOption> = listOf(),

        @SerializedName("tracking_option")
        @Expose
        val trackingOption: String = "0",

        @SerializedName("input_type")
        @Expose
        val inputType: String = "",
    ) {
        fun asFilter(): Filter {
            return if (isPriceRangeWidget()) Filter(options = priceRangeOptions())
            else Filter(options = inspirationWidgetAsFilterOption())
        }

        private fun isPriceRangeWidget() = inspirationWidgetOptions
            .flatMap { it.multiFilters.orEmpty() }
            .any { it.key.contains(KEY_PRICE_RANGE) }

        private fun priceRangeOptions() =
            listOf(Option(key = PMIN), Option(key = PMAX))

        private fun inspirationWidgetAsFilterOption() = inspirationWidgetOptions.flatMap {
            it.asOptionList()
        }

        fun isTypeRadio(): Boolean {
            return Option.INPUT_TYPE_RADIO == inputType
        }
    }

    data class InspirationWidgetOption (
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
        val applink: String = "",

        @SerializedName("multi_filters")
        @Expose
        val multiFilters: List<InspirationWidgetFilter>? = emptyList(),

        @SerializedName("component_id")
        @Expose
        val componentId: String,
    ) {

        private fun asOption(filter: InspirationWidgetFilter): Option {
            return Option(
                key = filter.key,
                value = filter.value,
                name = filter.name,
                valMin = filter.valMin,
                valMax = filter.valMax,
            )
        }

        fun asOptionList() = multiFilters.orEmpty().map { asOption(it) }
    }

    data class InspirationWidgetFilter (
        @SerializedName("key")
        @Expose
        val key: String = "",

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("value")
        @Expose
        val value: String = "",

        @SerializedName("val_min")
        @Expose
        val valMin: String = "",

        @SerializedName("val_max")
        @Expose
        val valMax: String = "",
    )

    data class Violation(
        @SerializedName("headerText")
        @Expose
        val headerText: String = "",
        @SerializedName("descriptionText")
        @Expose
        val descriptionText: String = "",
        @SerializedName("imageURL")
        @Expose
        val imageUrl: String = "",
        @SerializedName("ctaURL")
        @Expose
        val ctaUrl: String = "",
        @SerializedName("buttonText")
        @Expose
        val buttonText: String = "",
        @SerializedName("buttonType")
        @Expose
        val buttonType: String = "",
    ) {
        fun isValid(): Boolean {
            return headerText.isNotEmpty() && descriptionText.isNotEmpty()
        }
    }
}
