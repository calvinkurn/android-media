package com.tokopedia.digital.home.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageTypeFactory

data class RechargeHomepageSections (
        @SerializedName("sections")
        @Expose
        var sections: List<Section> = listOf()
) {
    data class Response (
            @SerializedName("rechargeGetDynamicPage")
            @Expose
            val response: RechargeHomepageSections = RechargeHomepageSections()
    )

    data class Section (
            @SerializedName("title")
            @Expose
            val title: String = "",
            @SerializedName("sub_title")
            @Expose
            val subTitle: String = "",
            @SerializedName("template")
            @Expose
            val template: String = "",
            @SerializedName("items")
            @Expose
            val items: List<Item> = listOf()
    ): Visitable<DigitalHomePageTypeFactory> {
            override fun type(typeFactory: DigitalHomePageTypeFactory): Int {
                    return typeFactory.type(this)
            }
    }

    data class Item (
            @SerializedName("title")
            @Expose
            val title: String = "",
            @SerializedName("sub_title")
            @Expose
            val subTitle: String = "",
            @SerializedName("content")
            @Expose
            val content: String = "",
            @SerializedName("app_link")
            @Expose
            val applink: String = "",
            @SerializedName("web_link")
            @Expose
            val weblink: String = "",
            @SerializedName("text_link")
            @Expose
            val textlink: String = "",
            @SerializedName("media_url")
            @Expose
            val mediaUrl: String = "",
            @SerializedName("template")
            @Expose
            val template: String = "",
            @SerializedName("button_type")
            @Expose
            val buttonType: String = "",
            @SerializedName("label_1")
            @Expose
            val label1: String = "",
            @SerializedName("label_2")
            @Expose
            val label2: String = "",
            @SerializedName("label_3")
            @Expose
            val label3: String = "",
            @SerializedName("server_data")
            @Expose
            val serverData: String = "",
            @SerializedName("due_date")
            @Expose
            val dueDate: String = ""
    )

    companion object {
            const val SECTION_TOP_BANNER = "TOP_BANNER"
            const val SECTION_TOP_BANNER_EMPTY = "TOP_BANNER_EMPTY"
            const val SECTION_TOP_ICONS = "TOP_ICONS"
            const val SECTION_DYNAMIC_ICONS = "DYNAMIC_ICONS"
            const val SECTION_DUAL_ICONS = "DUAL_ICONS"
            const val SECTION_URGENCY_WIDGET = "URGENCY_WIDGET"
            const val SECTION_VIDEO_HIGHLIGHT = "VIDEO_HIGHLIGHT"
            const val SECTION_VIDEO_HIGHLIGHTS = "VIDEO_HIGHLIGHTS"
            const val SECTION_SINGLE_BANNER = "SINGLE_BANNER"
            const val SECTION_COUNTDOWN_SINGLE_BANNER = "COUNTDOWN_SINGLE_BANNER"
            const val SECTION_DUAL_BANNERS = "DUAL_BANNERS"
            const val SECTION_LEGO_BANNERS = "LEGO_BANNERS"
            const val SECTION_PRODUCT_CARD_ROW = "PRODUCT_CARD_ROW"
            const val SECTION_COUNTDOWN_PRODUCT_BANNER = "COUNTDOWN_PRODUCT_BANNER"
    }
}