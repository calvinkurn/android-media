package com.tokopedia.quest_widget.data

import com.google.gson.annotations.SerializedName

data class QuestWidgetResponse(

    @SerializedName("data")
    val data: WidgetData? = null
)

data class WidgetData(

    @SerializedName("questWidgetList")
    val questWidgetList: QuestWidgetList? = null,

    )

data class QuestWidgetList(

    @SerializedName("resultStatus")
    val resultStatus: ResultStatus? = null,

    @SerializedName("questWidgetList")
    val questWidgetList: List<QuestWidgetListItem>?,

    @SerializedName("isEligible")
    val isEligible: Boolean? = null,

    @SerializedName("widgetPageDetail")
    val pageDetail: PageDetail? = null
)

data class QuestWidgetListItem(

    @SerializedName("id")
    val id: String? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("isDisabledIcon")
    val isDisabledIcon: Boolean? = null,

    @SerializedName("progressInfoText")
    val progressInfoText: String? = null,

    @SerializedName("expiredDate")
    val expiredDate: String? = null,

    @SerializedName("cardBannerBackgroundURL")
    val cardBannerBackgroundURL: String? = null,

    @SerializedName("detailBannerBackgroundURL")
    val detailBannerBackgroundURL: String? = null,

    @SerializedName("widgetPrizeIconURL")
    val widgetPrizeIconURL: String? = null,

    @SerializedName("label")
    val label: Label? = null,

    @SerializedName("config")
    val config: String? = null,

    @SerializedName("questUser")
    val questUser: QuestUser? = null,

    @SerializedName("task")
    val task: List<TaskItem?>? = null,

    @SerializedName("actionButton")
    val actionButton: ActionButton? = null,

    @SerializedName("prize")
    val prize: List<PrizeItem?>? = null,

    @SerializedName("category")
    val category: Category? = null

)

data class ResultStatus(

    @SerializedName("reason")
    val reason: String? = null,

    @SerializedName("code")
    val code: String? = null
)

data class ActionButton(

    @SerializedName("isDisable")
    val isDisable: Boolean? = null,

    @SerializedName("cta")
    val cta: CtaActionButton? = null,

    @SerializedName("backgroundColor")
    val backgroundColor: String? = null,

    @SerializedName("shortText")
    val shortText: String? = null,

    @SerializedName("text")
    val text: String? = null
)

data class Label(

    @SerializedName("backgroundColor")
    val backgroundColor: String? = null,

    @SerializedName("imageURL")
    val imageURL: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("title")
    var title: String? = null,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("textColor")
    val textColor: String? = null
)

data class CtaActionButton(

    @SerializedName("url")
    val url: String? = null,

    @SerializedName("applink")
    val applink: String? = null

)

data class Cta(

    @SerializedName("text")
    val text: String? = null,

    @SerializedName("url")
    val url: String? = null,

    @SerializedName("appLink")
    val applink: String? = null

)

data class PrizeItem(

    @SerializedName("shortText")
    val shortText: String? = null,

    @SerializedName("iconUrl")
    val iconUrl: String? = null,

    @SerializedName("text")
    val text: String? = null,

    @SerializedName("textColor")
    val textColor: String? = null
)

data class QuestUser(

    @SerializedName("id")
    val id: String? = null,

    @SerializedName("status")
    var status: String? = null
)

data class Category(

    @SerializedName("id")
    val id: String? = null,

    @SerializedName("title")
    val title: String? = null
)

data class TaskItem(

    @SerializedName("id")
    val id: String? = null,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("progress")
    val progress: Progress? = null

)

data class PageDetail(

    @SerializedName("cta")
    val cta: Cta? = null,

    @SerializedName("title")
    val title: String? = null
)

data class Progress(

    @SerializedName("current")
    val current: Int? = null,

    @SerializedName("target")
    val target: Int? = null
)

data class Config(
    @SerializedName("banner_icon_url")
    val banner_icon_url: String? = null,

    @SerializedName("banner_title")
    val banner_title: String? = null,

    @SerializedName("banner_description")
    val banner_description: String? = null,

    @SerializedName("banner_background_color")
    val banner_background_color: String? = null,

    @SerializedName("milestone_text")
    val milestone_text: String? = null,

    @SerializedName("pages")
    val pages: ArrayList<String>? = null

)

data class QuestData(
    val config: ArrayList<Config>? = null,
    val widgetData: WidgetData? = null,
)