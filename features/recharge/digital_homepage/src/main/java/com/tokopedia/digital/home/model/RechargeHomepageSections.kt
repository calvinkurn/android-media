package com.tokopedia.digital.home.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.digital.home.presentation.adapter.RechargeHomepageAdapterTypeFactory
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.model.ImpressHolder

data class RechargeHomepageSections(
        @SerializedName("sections")
        @Expose
        val sections: List<Section> = listOf(),
        var requestIDs: List<Int> = listOf()
) {
    data class Response(
            @SerializedName("rechargeGetDynamicPage")
            @Expose
            val response: RechargeHomepageSections = RechargeHomepageSections()
    )

    data class Section(
            @SerializedName("id")
            @Expose
            val id: String = "",
            @SerializedName("object_id")
            @Expose
            val objectId: String = "",
            @SerializedName("title")
            @Expose
            val title: String = "",
            @SerializedName("sub_title")
            @Expose
            val subtitle: String = "",
            @SerializedName("template")
            @Expose
            val template: String = "",
            @SerializedName("tracking")
            @Expose
            val tracking: List<Tracking> = listOf(),
            @SerializedName("app_link")
            @Expose
            val applink: String = "",
            @SerializedName("text_link")
            @Expose
            val textLink: String = "",
            @SerializedName("media_url")
            @Expose
            val mediaUrl: String = "",
            @SerializedName("label_1")
            @Expose
            val label1: String = "#FFFFFF",
            @SerializedName("items")
            @Expose
            val items: List<Item> = listOf()
    ) : ImpressHolder()

    data class Item(
            @SerializedName("id")
            @Expose
            val id: String = "",
            @SerializedName("object_id")
            @Expose
            val objectId: String = "",
            @SerializedName("title")
            @Expose
            val title: String = "",
            @SerializedName("sub_title")
            @Expose
            val subtitle: String = "",
            @SerializedName("tracking")
            @Expose
            val tracking: List<Tracking> = listOf(),
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
            @SerializedName("server_date")
            @Expose
            val serverDate: String = "",
            @SerializedName("due_date")
            @Expose
            val dueDate: String = ""
    )

    data class Tracking(
            @SerializedName("action")
            @Expose
            val action: String = "",
            @SerializedName("data")
            @Expose
            val data: String = ""
    )
}

interface RechargeHomepageSectionModel : Visitable<RechargeHomepageAdapterTypeFactory> {
    fun visitableId(): String
    fun equalsWith(b: Any?): Boolean
}

data class RechargeHomepageBannerModel(val section: RechargeHomepageSections.Section) : RechargeHomepageSectionModel {
    override fun visitableId(): String {
        return section.id
    }

    override fun equalsWith(b: Any?): Boolean {
        return if (b is RechargeHomepageBannerModel) {
            section == b.section
        } else false
    }

    override fun type(typeFactory: RechargeHomepageAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}

data class RechargeHomepageBannerEmptyModel(val section: RechargeHomepageSections.Section) : RechargeHomepageSectionModel {
    override fun type(typeFactory: RechargeHomepageAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun visitableId(): String {
        return section.id
    }

    override fun equalsWith(b: Any?): Boolean {
        return if (b is RechargeHomepageBannerEmptyModel) {
            section == b.section
        } else false
    }

}

data class RechargeHomepageFavoriteModel(val section: RechargeHomepageSections.Section) : RechargeHomepageSectionModel {
    override fun type(typeFactory: RechargeHomepageAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun visitableId(): String {
        return section.id
    }

    override fun equalsWith(b: Any?): Boolean {
        return if (b is RechargeHomepageFavoriteModel) {
            section == b.section
        } else false
    }

}

data class RechargeHomepageCategoryModel(val section: RechargeHomepageSections.Section) : RechargeHomepageSectionModel {
    override fun type(typeFactory: RechargeHomepageAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun visitableId(): String {
        return section.id
    }

    override fun equalsWith(b: Any?): Boolean {
        return if (b is RechargeHomepageCategoryModel) {
            section == b.section
        } else false
    }

}

data class RechargeHomepageTrustMarkModel(val section: RechargeHomepageSections.Section) : RechargeHomepageSectionModel {
    override fun type(typeFactory: RechargeHomepageAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun visitableId(): String {
        return section.id
    }

    override fun equalsWith(b: Any?): Boolean {
        return if (b is RechargeHomepageTrustMarkModel) {
            section == b.section
        } else false
    }

}

data class RechargeHomepageVideoHighlightModel(val section: RechargeHomepageSections.Section) : RechargeHomepageSectionModel {
    override fun type(typeFactory: RechargeHomepageAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun visitableId(): String {
        return section.id
    }

    override fun equalsWith(b: Any?): Boolean {
        return if (b is RechargeHomepageVideoHighlightModel) {
            section == b.section
        } else false
    }

}

data class RechargeHomepageSingleBannerModel(val section: RechargeHomepageSections.Section,
                                             val channelModel: ChannelModel?, val isLoadFromCloud: Boolean = false) : RechargeHomepageSectionModel {
    override fun type(typeFactory: RechargeHomepageAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun visitableId(): String {
        return section.id
    }

    override fun equalsWith(b: Any?): Boolean {
        return if (b is RechargeHomepageSingleBannerModel) {
            section == b.section
        } else false
    }

}

data class RechargeHomepageDualBannersModel(val section: RechargeHomepageSections.Section) : RechargeHomepageSectionModel {
    override fun type(typeFactory: RechargeHomepageAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun visitableId(): String {
        return section.id
    }

    override fun equalsWith(b: Any?): Boolean {
        return if (b is RechargeHomepageDualBannersModel) {
            section == b.section
        } else false
    }

}

data class RechargeHomepageProductCardsModel(val section: RechargeHomepageSections.Section) : RechargeHomepageSectionModel {
    override fun type(typeFactory: RechargeHomepageAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun visitableId(): String {
        return section.id
    }

    override fun equalsWith(b: Any?): Boolean {
        return if (b is RechargeHomepageProductCardsModel) {
            section == b.section
        } else false
    }

}

data class RechargeHomepageProductBannerModel(val section: RechargeHomepageSections.Section,
                                              val channelModel: ChannelModel?, val isLoadFromCloud: Boolean = false) : RechargeHomepageSectionModel {
    override fun type(typeFactory: RechargeHomepageAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun visitableId(): String {
        return section.id
    }

    override fun equalsWith(b: Any?): Boolean {
        return if (b is RechargeHomepageProductBannerModel) {
            section == b.section
        } else false
    }

}

data class RechargeProductCardCustomBannerModel(val section: RechargeHomepageSections.Section) : RechargeHomepageSectionModel {
    override fun type(typeFactory: RechargeHomepageAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun visitableId(): String {
        return section.id
    }

    override fun equalsWith(b: Any?): Boolean {
        return if (b is RechargeHomepageProductBannerModel) {
            section == b.section
        } else false
    }
}

data class RechargeHomepageCarousellModel(val section: RechargeHomepageSections.Section) : RechargeHomepageSectionModel {
    override fun type(typeFactory: RechargeHomepageAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun visitableId(): String {
        return section.id
    }

    override fun equalsWith(b: Any?): Boolean {
        return if (b is RechargeHomepageCarousellModel) {
            section == b.section
        } else false
    }

}