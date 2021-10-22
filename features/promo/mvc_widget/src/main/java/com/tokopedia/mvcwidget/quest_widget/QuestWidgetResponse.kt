package com.tokopedia.mvcwidget.quest_widget

import com.google.gson.annotations.SerializedName

data class QuestWidgetResponse(

	@SerializedName("data")
	val data: Data? = null
)

data class Data(

	@SerializedName("questWidgetList")
	val questWidgetList: QuestWidgetList? = null,

	@SerializedName("isEligible")
	val isEligible: Boolean? = null,

	@SerializedName("pageDetail")
	val pageDetail: PageDetail? = null
)

data class QuestWidgetList(

	@SerializedName("resultStatus")
	val resultStatus: ResultStatus? = null,

	@SerializedName("questWidgetList")
	val questWidgetList: List<QuestWidgetListItem?>? = null
)

data class QuestWidgetListItem(

	@SerializedName("progressInfoText")
	val progressInfoText: String? = null,

	@SerializedName("expiredDate")
	val expiredDate: String? = null,

	@SerializedName("description")
	val description: String? = null,

	@SerializedName("label")
	val label: Label? = null,

	@SerializedName("title")
	val title: String? = null,

	@SerializedName("prize")
	val prize: List<PrizeItem?>? = null,

	@SerializedName("isDisabledIcon")
	val isDisabledIcon: Boolean? = null,

	@SerializedName("questUser")
	val questUser: QuestUser? = null,

	@SerializedName("task")
	val task: List<TaskItem?>? = null,

	@SerializedName("actionButton")
	val actionButton: ActionButton? = null,

	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("category")
	val category: Category? = null,

	@SerializedName("config")
	val config: String? = null
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
	val cta: Cta? = null,

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
	val title: String? = null,

	@SerializedName("type")
	val type: String? = null,

	@SerializedName("textColor")
	val textColor: String? = null
)

data class Cta(

	@SerializedName("applink")
	val applink: String? = null,

	@SerializedName("url")
	val url: String? = null
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
	val id: Int? = null,

	@SerializedName("status")
	val status: String? = null
)

data class Category(

	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("title")
	val title: String? = null
)

data class TaskItem(

	@SerializedName("progress")
	val progress: Progress? = null,

	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("title")
	val title: String? = null
)

data class PageDetail(

	@SerializedName("cta")
	val cta: Cta? = null,

	@SerializedName("isHiddenCta")
	val isHiddenCta: Boolean? = null,

	@SerializedName("text")
	val text: String? = null,

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
	val milestone_text: String? = null

)