package com.tokopedia.digital.home.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.digital.home.presentation.adapter.DigitalHomePageTypeFactory

data class RechargeHomepageSections (
        @SerializedName("section")
        @Expose
        val sections: List<Section> = listOf()
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
            val items: List<Items> = listOf(),
            var layout: Int = 0
    ): Visitable<DigitalHomePageTypeFactory> {
            override fun type(typeFactory: DigitalHomePageTypeFactory): Int {
                    return typeFactory.type(this)
            }
    }

    data class Items (
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