package com.tokopedia.discovery2.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery2.Constant.ProductTemplate.GRID

data class Properties(
        @SerializedName("columns")
        val columns: String? = null,

        @SerializedName("registered_message")
        val registeredMessage: String? = null,

        @SerializedName("unregistered_message")
        val unregisteredMessage: String? = null,

        @SerializedName("background")
        val background: String? = null,

        @SerializedName("dynamic")
        var dynamic: Boolean = false,

        @SerializedName("sticky")
        val sticky: Boolean = false,

        @SerializedName("banner_title")
        val bannerTitle: String? = null,

        @SerializedName("cta_app")
        val ctaApp: String? = null,

        @SerializedName("design")
        val design: String? = null,

        @SerializedName("type")
        val type: String? = null,

        @SerializedName("is_disabled_auto_slide")
        val isDisabledAutoSlide: Boolean? = null,

        @SerializedName("comp_type")
        val compType: String? = null,

        @SerializedName("limit_number")
        val limitNumber: String = "20",

        @SerializedName("limit_product")
        val limitProduct: Boolean = false,

        @SerializedName("target_id")
        var targetId: String? = null,

        @SerializedName("template")
        var template: String = GRID,

        @SerializedName("timer_style")
        val timerStyle: String? = null,

        @SerializedName("category_detail")
        val categoryDetail: Boolean = false,

        @SerializedName("filter")
        val filter:Boolean = true,

        @SerializedName("sort")
        val sort:Boolean = true,

        @SerializedName("tokonow_add_to_cart_active")
        val tokonowATCActive : Boolean = false,

        @SerializedName("calendar_layout")
        val calendarLayout: String = "",

        @SerializedName("calendar_type")
        val calendarType: String = "",

        @SerializedName("background_image_url")
        val backgroundImageUrl: String? = null,

        @SerializedName("background_color")
        val backgroundColor: String? = null,

        @SerializedName("shop_info")
        val shopInfo: String? = null,

        @SerializedName("mix_left")
        val mixLeft: MixLeft? = null,

        @SerializedName("index")
        val index: Int = 0,

        @SerializedName("targeted_component_id")
        val targetedComponentId: String = "",

        @SerializedName("catalog_slug")
        val catalogSlug: String? = "",

        @SerializedName("category_slug")
        val categorySlug: String? = "",

        @SerializedName("chip_size")
        val chipSize: String? = "",

        @SerializedName("full_filter_type")
        var fullFilterType: String? = "",

        @SerializedName("supergraphic_image_url")
        val supergraphicImageUrl: String? = "",

)
