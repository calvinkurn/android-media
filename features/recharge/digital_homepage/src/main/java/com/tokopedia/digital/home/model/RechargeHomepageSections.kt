package com.tokopedia.digital.home.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder

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
            @SerializedName("id")
            @Expose
            val id: Int = 0,
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
    ): ImpressHolder()

    data class Item (
            @SerializedName("id")
            @Expose
            val id: Int = 0,
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
}

abstract class RechargeHomepageAbstractSectionModel: Visitable<DigitalHomePageTypeFactory> {
        abstract override fun type(typeFactory: DigitalHomePageTypeFactory): Int
}

abstract class RechargeHomepageSectionModel(val section: RechargeHomepageSections.Section): RechargeHomepageAbstractSectionModel()

class RechargeHomepageBannerModel(section: RechargeHomepageSections.Section): RechargeHomepageSectionModel(section) {
        override fun type(typeFactory: DigitalHomePageTypeFactory): Int {
                return typeFactory.type(this)
        }
}

class RechargeHomepageBannerEmptyModel(section: RechargeHomepageSections.Section): RechargeHomepageSectionModel(section) {
        override fun type(typeFactory: DigitalHomePageTypeFactory): Int {
                return typeFactory.type(this)
        }
}

class RechargeHomepageFavoriteModel(section: RechargeHomepageSections.Section): RechargeHomepageSectionModel(section) {
        override fun type(typeFactory: DigitalHomePageTypeFactory): Int {
                return typeFactory.type(this)
        }
}

class RechargeHomepageCategoryModel(section: RechargeHomepageSections.Section): RechargeHomepageSectionModel(section) {
        override fun type(typeFactory: DigitalHomePageTypeFactory): Int {
                return typeFactory.type(this)
        }
}

class RechargeHomepageTrustMarkModel(section: RechargeHomepageSections.Section): RechargeHomepageSectionModel(section) {
        override fun type(typeFactory: DigitalHomePageTypeFactory): Int {
                return typeFactory.type(this)
        }
}

class RechargeHomepageVideoHighlightModel(section: RechargeHomepageSections.Section): RechargeHomepageSectionModel(section) {
        override fun type(typeFactory: DigitalHomePageTypeFactory): Int {
                return typeFactory.type(this)
        }
}