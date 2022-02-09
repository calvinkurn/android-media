package com.tokopedia.digital.home.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.digital.home.presentation.adapter.RechargeHomepageAdapterTypeFactory
import com.tokopedia.digital.home.presentation.adapter.RechargeHomepageCustomLastItemAdapterTypeFactory
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalCardActionModel
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalCardCampaignModel
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalCardInfoModel
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalCardPriceModel
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalCardRatingModel
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalCardSoldPercentageModel
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalUnifyConst
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalUnifyModel
import com.tokopedia.unifycomponents.Label

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
        @SerializedName("label_2")
        @Expose
        val label2: String = "#FFFFFF",
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
        val dueDate: String = "",
        @SerializedName("attributes")
        @Expose
        val attributes: Attributes = Attributes()
    )

    data class Attributes(
        @SerializedName("title_color")
        @Expose
        val titleColor: String = "",
        @SerializedName("sub_title_color")
        @Expose
        val subtitleColor: String = "",
        @SerializedName("media_url_title")
        @Expose
        val mediaUrlTitle: String = "",
        @SerializedName("media_url_type")
        @Expose
        val mediaUrlType: String = "",
        @SerializedName("icon_url")
        @Expose
        val iconUrl: String = "",
        @SerializedName("sold_value")
        @Expose
        val soldValue: String = "",
        @SerializedName("sold_percentage_value")
        @Expose
        val soldPercentageValue: Int = 0,
        @SerializedName("sold_percentage_label")
        @Expose
        val soldPercentageLabel: String = "",
        @SerializedName("sold_percentage_label_color")
        @Expose
        val soldPercentageLabelColor: String = "",
        @SerializedName("show_sold_percentage")
        @Expose
        val showSoldPercentage: Boolean = false,
        @SerializedName("campaign_label_text")
        @Expose
        val campaignLabelText: String = "",
        @SerializedName("campaign_label_text_color")
        @Expose
        val campaignLabelTextColor: String = "",
        @SerializedName("campaign_label_background_url")
        @Expose
        val campaignLabelBackgroundUrl: String = "",
        @SerializedName("rating_type")
        @Expose
        val ratingType: String = "",
        @SerializedName("rating")
        @Expose
        val rating: Double = 0.0,
        @SerializedName("review")
        @Expose
        val review: String = "",
        @SerializedName("special_info_text")
        @Expose
        val specialInfoText: String = "",
        @SerializedName("special_info_color")
        @Expose
        val specialInfoColor: String = "",
        @SerializedName("special_discount")
        @Expose
        val specialDiscount: String = "",
        @SerializedName("cashback")
        @Expose
        val cashback: String = "",
        @SerializedName("price_prefix")
        @Expose
        val pricePrefix: String = "",
        @SerializedName("price_suffix")
        @Expose
        val priceSuffix: String = ""
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

data class RechargeHomepageBannerModel(val section: RechargeHomepageSections.Section) :
    RechargeHomepageSectionModel {
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

data class RechargeHomepageBannerEmptyModel(val section: RechargeHomepageSections.Section) :
    RechargeHomepageSectionModel {
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

data class RechargeHomepageFavoriteModel(val section: RechargeHomepageSections.Section) :
    RechargeHomepageSectionModel {
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

data class RechargeHomepageCategoryModel(val section: RechargeHomepageSections.Section) :
    RechargeHomepageSectionModel {
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

data class RechargeHomepageTrustMarkModel(val section: RechargeHomepageSections.Section) :
    RechargeHomepageSectionModel {
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

data class RechargeHomepageVideoHighlightModel(val section: RechargeHomepageSections.Section) :
    RechargeHomepageSectionModel {
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

data class RechargeHomepageSingleBannerModel(
    val section: RechargeHomepageSections.Section,
    val channelModel: ChannelModel?, val isLoadFromCloud: Boolean = false
) : RechargeHomepageSectionModel {
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

data class RechargeHomepageDualBannersModel(val section: RechargeHomepageSections.Section) :
    RechargeHomepageSectionModel {
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

data class RechargeHomepageProductCardsModel(val section: RechargeHomepageSections.Section) :
    RechargeHomepageSectionModel {
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

data class RechargeHomepageProductBannerModel(
    val section: RechargeHomepageSections.Section,
    val channelModel: ChannelModel?, val isLoadFromCloud: Boolean = false
) : RechargeHomepageSectionModel {
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

data class RechargeProductCardCustomBannerModel(val section: RechargeHomepageSections.Section) :
    RechargeHomepageSectionModel {
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

data class RechargeHomepageCarousellModel(val section: RechargeHomepageSections.Section) :
    RechargeHomepageSectionModel {
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

data class RechargeHomepageSwipeBannerModel(val section: RechargeHomepageSections.Section) :
    RechargeHomepageSectionModel {
    override fun type(typeFactory: RechargeHomepageAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun visitableId(): String {
        return section.id
    }

    override fun equalsWith(b: Any?): Boolean {
        return if (b is RechargeHomepageSwipeBannerModel) {
            section == b.section
        } else false
    }

}

open class RechargeProductCardUnifyModel(val section: RechargeHomepageSections.Section) :
    RechargeHomepageSectionModel {

    // use 1 media type for all items
    var mediaImageType: String = ""

    val digitalUnifyItems: List<DigitalUnifyModel>
        get() =
            section.items.map {
                // use the first media type only
                if (mediaImageType.isEmpty()) {
                    mediaImageType = it.attributes.mediaUrlType
                }

                val discountLabel: String = when {
                    it.attributes.cashback.isNotEmpty() -> it.attributes.cashback
                    it.attributes.specialDiscount.isNotEmpty() -> it.attributes.specialDiscount
                    else -> it.label3
                }

                val discountType: String = when {
                    it.attributes.cashback.isNotEmpty() -> DigitalUnifyConst.DISCOUNT_CASHBACK
                    it.attributes.specialDiscount.isNotEmpty() -> DigitalUnifyConst.DISCOUNT_SPECIAL
                    it.label3.isNotEmpty() -> DigitalUnifyConst.DISCOUNT_SLASH
                    else -> ""
                }

                val discountLabelType: Int = when (discountType) {
                    DigitalUnifyConst.DISCOUNT_CASHBACK -> Label.HIGHLIGHT_LIGHT_GREEN
                    DigitalUnifyConst.DISCOUNT_SPECIAL, DigitalUnifyConst.DISCOUNT_SLASH -> Label.HIGHLIGHT_LIGHT_RED
                    else -> 0
                }

                DigitalUnifyModel(
                    id = it.id,
                    mediaUrl = it.mediaUrl,
                    mediaType = mediaImageType,
                    mediaTitle = it.attributes.mediaUrlTitle,
                    iconUrl = it.attributes.iconUrl,
                    iconBackgroundColor = "",
                    campaign = DigitalCardCampaignModel(
                        text = it.attributes.campaignLabelText,
                        textColor = it.attributes.campaignLabelTextColor,
                        backgroundUrl = it.attributes.campaignLabelBackgroundUrl
                    ),
                    productInfoLeft = DigitalCardInfoModel(
                        text = it.subtitle,
                        textColor = it.attributes.subtitleColor
                    ),
                    productInfoRight = DigitalCardInfoModel(
                        text = it.title,
                        textColor = it.attributes.titleColor
                    ),
                    title = it.content,
                    rating = DigitalCardRatingModel(
                        ratingType = it.attributes.ratingType,
                        rating = it.attributes.rating,
                        review = it.attributes.review
                    ),
                    specialInfo = DigitalCardInfoModel(
                        text = it.attributes.specialInfoText,
                        textColor = it.attributes.specialInfoColor
                    ),
                    priceData = DigitalCardPriceModel(
                        price = it.label2,
                        discountLabel = discountLabel,
                        discountType = discountType,
                        discountLabelType = discountLabelType,
                        slashedPrice = it.label1,
                        pricePrefix = it.attributes.pricePrefix,
                        priceSuffix = it.attributes.priceSuffix
                    ),
                    cashback = it.attributes.cashback,
                    subtitle = it.attributes.soldValue,
                    soldPercentage = DigitalCardSoldPercentageModel(
                        showPercentage = it.attributes.showSoldPercentage,
                        value = it.attributes.soldPercentageValue,
                        label = it.attributes.soldPercentageLabel,
                        labelColor = it.attributes.soldPercentageLabelColor
                    ),
                    actionButton = DigitalCardActionModel(
                        text = it.textlink,
                        buttonType = it.buttonType,
                        applink = it.applink
                    )
                )
            }.toList()

    override fun type(typeFactory: RechargeHomepageAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun visitableId(): String {
        return section.id
    }

    override fun equalsWith(b: Any?): Boolean {
        return if (b is RechargeProductCardUnifyModel) {
            section == b.section
        } else false
    }

}

class RechargeHomepageProductCardCustomBannerV2Model(section: RechargeHomepageSections.Section) :
    RechargeProductCardUnifyModel(section) {

    override fun equalsWith(b: Any?): Boolean {
        return if (b is RechargeHomepageProductCardCustomBannerV2Model) {
            section == b.section
        } else false
    }

    override fun type(typeFactory: RechargeHomepageAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}

data class RechargeHomepageThreeIconsModel(val section: RechargeHomepageSections.Section) :
    RechargeHomepageSectionModel {
    override fun visitableId(): String {
        return section.id
    }

    override fun equalsWith(b: Any?): Boolean {
        return if (b is RechargeHomepageThreeIconsModel) {
            section == b.section
        } else false
    }

    override fun type(typeFactory: RechargeHomepageAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}

class RechargeHomepageRecommendationBannerModel(section: RechargeHomepageSections.Section) :
    RechargeProductCardUnifyModel(section) {
    override fun visitableId(): String {
        return section.id
    }

    override fun equalsWith(b: Any?): Boolean {
        return if (b is RechargeHomepageThreeIconsModel) {
            section == b.section
        } else false
    }

    override fun type(typeFactory: RechargeHomepageAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}

class RechargeHomepageProductCardCustomLastItemModel(section: RechargeHomepageSections.Section) :
    RechargeProductCardUnifyModel(section) {

    override fun equalsWith(b: Any?): Boolean {
        return if (b is RechargeHomepageProductCardCustomBannerV2Model) {
            section == b.section
        } else false
    }

    override fun type(typeFactory: RechargeHomepageAdapterTypeFactory): Int =
        typeFactory.type(this)

    class LastItem(val section: RechargeHomepageSections.Section) :
        Visitable<RechargeHomepageCustomLastItemAdapterTypeFactory> {
        override fun type(typeFactory: RechargeHomepageCustomLastItemAdapterTypeFactory): Int =
            typeFactory.type(this)
    }
}
