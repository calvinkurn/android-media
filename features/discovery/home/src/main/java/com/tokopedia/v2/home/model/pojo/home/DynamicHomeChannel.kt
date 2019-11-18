package com.tokopedia.v2.home.model.pojo.home

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DynamicHomeChannel(
    @SerializedName("channels")
    val channels: List<Channel> = listOf()
)

data class Channel(
        @Expose
    @SerializedName("id")
    val id: String?,

        @Expose
    @SerializedName("layout")
    val layout: String?,

        @Expose
    @SerializedName("name")
    val name: String?,

        @Expose
    @SerializedName("grids")
    val grids: Array<Grid>?,

        @Expose
    @SerializedName("hero")
    val hero: Array<Hero>?,

        @Expose
    @SerializedName("type")
    val type: String?,

        @Expose
    @SerializedName("showPromoBadge")
    val showPromoBadge: Boolean?,

        @Expose
    @SerializedName("header")
    val header: Header?,

        @Expose
    @SerializedName("banner")
    val banner: Banner?,

        @SerializedName("promoName")
    val promoName: String?,

        @SerializedName("homeAttribution")
    val homeAttribution: String?
){

    enum class ChannelType{
        @SerializedName("hero_4_image")
        HERO,
        @SerializedName("3_image")
        THREE_IMAGE,
        @SerializedName("sprint_3_image")
        SPRINT,
        @SerializedName("sprint_lego")
        SPRINT_LEGO,
        @SerializedName("organic")
        ORGANIC,
        @SerializedName("6_image")
        SIX_IMAGE,
        @SerializedName("banner_image")
        BANNER_GIF,
        @SerializedName("lego_3_image")
        LEGO_THREE_IMAGE,
        @SerializedName("sprint_carousel")
        SPRINT_CAROUSEL,
        @SerializedName("digital_widget")
        DIGITAL_WIDGET,
        @SerializedName("bu_widget")
        BU_WIDGET,
        @SerializedName("topads")
        TOPADS,
        @SerializedName("spotlight")
        SPOTLIGHT,
        @SerializedName("home_widget")
        HOME_WIDGET,
        @SerializedName("banner_organic")
        BANNER_ORGANIC,
        @SerializedName("banner_carousel")
        BANNER_CAROUSEL;

        override fun toString(): String {
            return when(this){
                HERO -> "hero_4_image"
                THREE_IMAGE -> "3_image"
                SPRINT -> "sprint_3_image"
                SPRINT_LEGO -> "sprint_lego"
                ORGANIC -> "organic"
                SIX_IMAGE -> "6_image"
                BANNER_GIF -> "banner_image"
                LEGO_THREE_IMAGE -> "lego_3_image"
                SPRINT_CAROUSEL -> "sprint_carousel"
                DIGITAL_WIDGET -> "digital_widget"
                BU_WIDGET -> "bu_widget"
                TOPADS -> "topads"
                SPOTLIGHT -> "spotlight"
                HOME_WIDGET -> "home_widget"
                BANNER_ORGANIC -> "banner_organic"
                BANNER_CAROUSEL -> "banner_carousel"
            }
        }
    }

    data class Header(
        @Expose
        @SerializedName("id")
        val id: String?,

        @Expose
        @SerializedName("name")
        val name: String?,

        @Expose
        @SerializedName("expiredTime")
        val expiredTime: String?,

        @Expose
        @SerializedName("serverTime")
        val serverTimeUnix: Long = 0,

        @Expose
        @SerializedName("applink")
        val applink: String?,

        @Expose
        @SerializedName("url")
        val url: String?,

        @Expose
        @SerializedName("backColor")
        val backColor: String?,

        @Expose
        @SerializedName("backImage")
        val backImage: String?,

        @Expose
        @SerializedName("textColor")
        val textColor: String?
    )

    data class Banner(
            @Expose
        @SerializedName("id")
        val id: String?,

            @Expose
        @SerializedName("title")
        val title: String?,

            @Expose
        @SerializedName("description")
        val description: String?,

            @Expose
        @SerializedName("cta")
        val cta: CtaData?,

            @Expose
        @SerializedName("url")
        val url: String?,

            @Expose
        @SerializedName("applink")
        val applink: String?,

            @Expose
        @SerializedName("text_color")
        val textColor: String?,

            @Expose
        @SerializedName("image_url")
        val imageUrl: String?,

            @Expose
        @SerializedName("attribution")
        val attribution: String?
    )

    data class CtaData(
            @Expose
            @SerializedName("type")
            val type: String?,

            @Expose
            @SerializedName("mode")
            val mode: String?,

            @Expose
            @SerializedName("text")
            val text: String?,

            @Expose
            @SerializedName("coupon_code")
            val couponCode: String?
    )

    data class Grid(
        @Expose
        @SerializedName("id")
        val id: String?,

        @Expose
        @SerializedName("price")
        val price: String?,
    
        @Expose
        @SerializedName("imageUrl")
        val imageUrl: String?,
    
        @Expose
        @SerializedName("name")
        val name: String?,
    
        @Expose
        @SerializedName("applink")
        val applink: String?,
    
        @Expose
        @SerializedName("url")
        val url: String?,
    
        @Expose
        @SerializedName("discount")
        val discount: String?,
    
        @Expose
        @SerializedName("slashedPrice")
        val slashedPrice: String?,
    
        @Expose
        @SerializedName("label")
        val label: String?,
    
        @Expose
        @SerializedName("soldPercentage")
        val soldPercentage: Int = 0,
    
        @Expose
        @SerializedName("attribution")
        val attribution: String?,
    
        @Expose
        @SerializedName("impression")
        val impression: String?,
    
        @Expose
        @SerializedName("cashback")
        val cashback: String?,
    
        @Expose
        @SerializedName("productClickUrl")
        val productClickUrl: String?,
    
        @Expose
        @SerializedName("freeOngkir")
        val freeOngkir: FreeOngkir?
    )

    data class FreeOngkir(
        @SerializedName("isActive")
        @Expose
        val isActive: Boolean = false,
        @SerializedName("imageUrl")
        @Expose
        val imageUrl: String = ""
    )

    data class Hero(
        @Expose
        @SerializedName("id")
        val id: String?,

        @Expose
        @SerializedName("imageUrl")
        val imageUrl: String?,

        @Expose
        @SerializedName("name")
        val name: String?,

        @Expose
        @SerializedName("applink")
        val applink: String?,

        @Expose
        @SerializedName("url")
        val url: String?,

        @Expose
        @SerializedName("price")
        val price: String?,

        @Expose
        @SerializedName("attribution")
        val attribution: String?

    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Channel

        if (id != other.id) return false
        if (layout != other.layout) return false
        if (name != other.name) return false
        if (grids != null) {
            if (other.grids == null) return false
            if (!grids.contentEquals(other.grids)) return false
        } else if (other.grids != null) return false
        if (hero != null) {
            if (other.hero == null) return false
            if (!hero.contentEquals(other.hero)) return false
        } else if (other.hero != null) return false
        if (type != other.type) return false
        if (showPromoBadge != other.showPromoBadge) return false
        if (header != other.header) return false
        if (banner != other.banner) return false
        if (promoName != other.promoName) return false
        if (homeAttribution != other.homeAttribution) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (layout?.hashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (grids?.contentHashCode() ?: 0)
        result = 31 * result + (hero?.contentHashCode() ?: 0)
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (showPromoBadge?.hashCode() ?: 0)
        result = 31 * result + header.hashCode()
        result = 31 * result + banner.hashCode()
        result = 31 * result + (promoName?.hashCode() ?: 0)
        result = 31 * result + (homeAttribution?.hashCode() ?: 0)
        return result
    }
}