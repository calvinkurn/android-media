package com.tokopedia.catalogcommon.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

internal data class CatalogGetDetailModularResponse(
    @SerializedName("data")
    val `data`: Data = Data()
) {

    data class Data(
        @SerializedName("catalogGetDetailModular")
        val catalogGetDetailModular: CatalogGetDetailModular = CatalogGetDetailModular()
    ) {

        data class CatalogGetDetailModular(
            @SerializedName("basicInfo")
            val basicInfo: BasicInfo = BasicInfo(),
            @SerializedName("comparisonInfo")
            val comparisonInfo: ComparisonInfo = ComparisonInfo(),
            @SerializedName("components")
            val components: List<Component> = listOf(),
            @SerializedName("globalStyle")
            val globalStyle: GlobalStyle = GlobalStyle(),
            @SerializedName("header")
            val header: Header = Header(),
            @SerializedName("layout")
            val layout: List<Layout> = listOf()
        ) {

            data class BasicInfo(
                @SerializedName("brand")
                val brand: String = "",
                @SerializedName("catalogImage")
                val catalogImage: List<CatalogImage> = listOf(),
                @SerializedName("departmentId")
                val departmentId: String = "",
                @SerializedName("description")
                val description: String = "",
                @SerializedName("id")
                val id: String = "",
                @SerializedName("longDescription")
                val longDescription: List<Any> = listOf(),
                @SerializedName("marketPrice")
                val marketPrice: List<MarketPrice> = listOf(),
                @SerializedName("mobileUrl")
                val mobileUrl: String = "",
                @SerializedName("name")
                val name: String = "",
                @SerializedName("productSortingStatus")
                val productSortingStatus: Int = 0,
                @SerializedName("shortDescription")
                val shortDescription: String = "",
                @SerializedName("tag")
                val tag: String = "",
                @SerializedName("url")
                val url: String = ""
            )

            data class ComparisonInfo(
                @SerializedName("brand")
                val brand: String = "",
                @SerializedName("catalogImage")
                val catalogImage: List<Any> = listOf(),
                @SerializedName("fullSpec")
                val fullSpec: List<Any> = listOf(),
                @SerializedName("id")
                val id: String = "",
                @SerializedName("marketPrice")
                val marketPrice: List<Any> = listOf(),
                @SerializedName("name")
                val name: String = "",
                @SerializedName("topSpec")
                val topSpec: List<Any> = listOf(),
                @SerializedName("url")
                val url: String = ""
            )

            data class Component(
                @SerializedName("data")
                val `data`: List<ComponentData> = listOf(),
                @SuppressLint("Invalid Data Type")
                @SerializedName("id")
                val id: Int = 0,
                @SerializedName("name")
                val name: String = "",
                @SerializedName("sticky")
                val sticky: Boolean = false,
                @SerializedName("type")
                val type: String = ""
            )

            data class GlobalStyle(
                @SerializedName("bgColor")
                val bgColor: String = "",
                @SerializedName("darkMode")
                val darkMode: Boolean = false,
                @SerializedName("presetKey")
                val presetKey: String = "",
                @SerializedName("primaryColor")
                val primaryColor: String = "",
                @SerializedName("secondaryColor")
                val secondaryColor: String = ""
            )

            data class Header(
                @SerializedName("code")
                val code: Int = 0,
                @SerializedName("message")
                val message: String = "",
                @SerializedName("__typename")
                val typename: String = ""
            )

            data class Layout(
                @SerializedName("data")
                val `data`: LayoutData = LayoutData(),
                @SerializedName("name")
                val name: String = "",
                @SerializedName("position")
                val position: List<Any> = listOf(),
                @SerializedName("type")
                val type: String = ""
            )

            data class CatalogImage(
                @SerializedName("imageUrl")
                val imageUrl: String = "",
                @SerializedName("isPrimary")
                val isPrimary: Boolean = false
            )

            data class MarketPrice(
                @SerializedName("date")
                val date: String = "",
                @SerializedName("max")
                val max: Int = 0,
                @SerializedName("maxFmt")
                val maxFmt: String = "",
                @SerializedName("min")
                val min: Int = 0,
                @SerializedName("minFmt")
                val minFmt: String = "",
                @SerializedName("name")
                val name: String = ""
            )

            data class ComponentData(
                @SerializedName("author")
                val author: String = "",
                @SerializedName("brand")
                val brand: String = "",
                @SerializedName("catalog_count")
                val catalogCount: String = "",
                @SerializedName("catalogImage")
                val catalogImage: List<CatalogImage> = listOf(),
                @SerializedName("catalogs")
                val catalogs: List<Catalog> = listOf(),
                @SerializedName("category_identifier")
                val categoryIdentifier: String = "",
                @SerializedName("category_name")
                val categoryName: String = "",
                @SerializedName("compared_data")
                val comparedData: ComparedData = ComparedData(),
                @SerializedName("fullSpec")
                val fullSpec: List<FullSpec> = listOf(),
                @SerializedName("id")
                val id: String = "",
//                @SerializedName("marketPrice")
//                val marketPrice: List<MarketPriceXXX> = listOf(),
                @SerializedName("name")
                val name: String = "",
                @SerializedName("spec_list")
                val specList: List<Any> = listOf(),
                @SerializedName("thumbnail")
                val thumbnail: String = "",
                @SerializedName("title")
                val title: String = "",
                @SerializedName("topSpec")
                val topSpec: List<TopSpec> = listOf(),
                @SerializedName("type")
                val type: String = "",
                @SerializedName("url")
                val url: String = "",
                @SerializedName("videoId")
                val videoId: String = ""
            )

            data class LayoutData(
                @SerializedName("accordion")
                val accordion: List<Accordion> = listOf(),
                @SerializedName("characteristic")
                val characteristic: List<Characteristic> = listOf(),
                @SerializedName("doubleBanner")
                val doubleBanner: List<DoubleBanner> = listOf(),
                @SerializedName("expertReview")
                val expertReview: List<ExpertReview> = listOf(),
                @SerializedName("hero")
                val hero: Hero = Hero(),
                @SerializedName("imagePanel")
                val imagePanel: List<Any> = listOf(),
                @SerializedName("imageSlider")
                val imageSlider: List<ImageSlider> = listOf(),
                @SerializedName("navInfo")
                val navInfo: NavInfo = NavInfo(),
                @SerializedName("section")
                val section: Section = Section(),
                @SerializedName("singleBanner")
                val singleBanner: SingleBanner = SingleBanner(),
                @SerializedName("style")
                val style: Style = Style(),
                @SerializedName("supportFeature")
                val supportFeature: List<SupportFeature> = listOf(),
                @SerializedName("text")
                val text: Text = Text(),
                @SerializedName("topFeature")
                val topFeature: List<TopFeature> = listOf(),
                @SerializedName("trustmaker")
                val trustmaker: List<Trustmaker> = listOf()
            )

            data class DoubleBanner(
                @SerializedName("imageUrl")
                val imageUrl: String = ""
            )

            data class ComparedData(
                @SerializedName("brand")
                val brand: String = "",
                @SerializedName("catalogImage")
                val catalogImage: List<CatalogImage> = listOf(),
                @SerializedName("id")
                val id: String = "",
//                @SerializedName("marketPrice")
//                val marketPrice: List<MarketPriceXX> = listOf(),
                @SerializedName("name")
                val name: String = "",
                @SerializedName("url")
                val url: String = ""
            )

            data class Characteristic(
                @SerializedName("desc")
                val desc: String = "",
                @SerializedName("iconUrl")
                val iconUrl: String = ""
            )

            data class Catalog(
                @SerializedName("applink")
                val applink: String = "",
                @SerializedName("brand")
                val brand: String = "",
                @SerializedName("brand_id")
                val brandId: String = "",
                @SerializedName("categoryID")
                val categoryID: String = "",
                @SerializedName("id")
                val id: String = "",
                @SerializedName("imageUrl")
                val imageUrl: String = "",
                @SerializedName("marketPrice")
                val marketPrice: MarketPriceX = MarketPriceX(),
                @SerializedName("mobileUrl")
                val mobileUrl: String = "",
                @SerializedName("name")
                val name: String = "",
                @SerializedName("url")
                val url: String = ""
            )

            data class Accordion(
                @SerializedName("desc")
                val desc: String = "",
                @SerializedName("title")
                val title: String = ""
            )

            data class ExpertReview(
                @SerializedName("imageUrl")
                val imageUrl: String = "",
                @SerializedName("name")
                val name: String = "",
                @SerializedName("review")
                val review: String = "",
                @SerializedName("title")
                val title: String = "",
                @SerializedName("videoUrl")
                val videoUrl: String = ""
            )

            data class FullSpec(
                @SerializedName("icon")
                val icon: String = "",
                @SerializedName("name")
                val name: String = "",
                @SerializedName("row")
                val row: List<Row> = listOf()
            )

            data class Hero(
                @SerializedName("brandLogoUrl")
                val brandLogoUrl: String = "",
                @SerializedName("heroSlide")
                val heroSlide: List<HeroSlide> = listOf(),
                @SerializedName("name")
                val name: String = ""
            )

            data class HeroSlide(
                @SerializedName("imageUrl")
                val imageUrl: String = "",
                @SerializedName("subtitle")
                val subtitle: String = "",
                @SerializedName("videoUrl")
                val videoUrl: String = ""
            )

            data class ImageSlider(
                @SerializedName("desc")
                val desc: String = "",
                @SerializedName("imageUrl")
                val imageUrl: String = "",
                @SerializedName("subtitle")
                val subtitle: String = "",
                @SerializedName("title")
                val title: String = ""
            )

            data class MarketPriceX(
                @SerializedName("max")
                val max: Int = 0,
                @SerializedName("maxFmt")
                val maxFmt: String = "",
                @SerializedName("min")
                val min: Int = 0,
                @SerializedName("minFmt")
                val minFmt: String = ""
            )

            data class NavInfo(
                @SerializedName("title")
                val title: String = ""
            )

            data class Row(
                @SerializedName("key")
                val key: String = "",
                @SerializedName("value")
                val value: String = ""
            )

            data class Section(
                @SerializedName("title")
                val title: String = ""
            )

            data class Trustmaker(
                @SerializedName("imageUrl")
                val imageUrl: String = "",
                @SerializedName("subtitle")
                val subtitle: String = "",
                @SerializedName("title")
                val title: String = ""
            )

            data class TopSpec(
                @SerializedName("icon")
                val icon: String = "",
                @SerializedName("key")
                val key: String = "",
                @SerializedName("value")
                val value: String = ""
            )

            data class TopFeature(
                @SerializedName("desc")
                val desc: String = "",
                @SerializedName("iconUrl")
                val iconUrl: String = ""
            )

            data class Text(
                @SerializedName("desc")
                val desc: String = "",
                @SerializedName("subtitle")
                val subtitle: String = "",
                @SerializedName("title")
                val title: String = ""
            )

            data class SupportFeature(
                @SerializedName("desc")
                val desc: String = "",
                @SerializedName("iconUrl")
                val iconUrl: String = "",
                @SerializedName("title")
                val title: String = ""
            )

            data class Style(
                @SerializedName("bannerRatio")
                val bannerRatio: String = "",
                @SerializedName("isHidden")
                val isHidden: Boolean = false,
                @SerializedName("isPremium")
                val isPremium: Boolean = false
            )

            data class SingleBanner(
                @SerializedName("imageUrl")
                val imageUrl: String = ""
            )
        }
    }
}
