package com.tokopedia.tokopedianow.home.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetQuestWidgetListResponse(
    @SerializedName("questWidgetList")
    @Expose
    val questWidgetList: QuestWidgetListResponse
)

data class QuestWidgetListResponse(
    @SerializedName("questWidgetList")
    @Expose
    val questWidgetList: List<QuestWidgetList>,
    @SerializedName("widgetPageDetail")
    @Expose
    val widgetPageDetail: WidgetPageDetail,
    @SerializedName("resultStatus")
    @Expose
    val resultStatus: ResultStatus,
    @SerializedName("isEligible")
    @Expose
    val isEligible: Boolean,
)

data class QuestWidgetList(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("title")
    @Expose
    val title: String,
    @SerializedName("description")
    @Expose
    val description: String,
    @SerializedName("widgetPrizeIconURL")
    @Expose
    val widgetPrizeIconURL: String,
    @SerializedName("config")
    @Expose
    val config: String,
    @SerializedName("questUser")
    @Expose
    val questUser: QuestUser,
    @SerializedName("task")
    @Expose
    val task: List<Task>,
    @SerializedName("actionButton")
    @Expose
    val actionButton: ActionButton,
)

data class QuestUser(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("status")
    @Expose
    val status: String
)

data class Task(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("title")
    @Expose
    val title: String,
    @SerializedName("progress")
    @Expose
    val progress: Progress,
)

data class Progress(
    @SerializedName("current")
    @Expose
    val current: Float,
    @SerializedName("target")
    @Expose
    val target: Float,
)

data class ActionButton(
    @SerializedName("shortText")
    @Expose
    val shortText: String,
    @SerializedName("cta")
    @Expose
    val cta: CtaActionButton
)

data class CtaActionButton(
    @SerializedName("applink")
    @Expose
    val appLink: String,
)

data class WidgetPageDetail(
    @SerializedName("title")
    @Expose
    val title: String,
    @SerializedName("cta")
    @Expose
    val cta: CtaWidgetPageDetail
)

data class CtaWidgetPageDetail(
    @SerializedName("appLink")
    @Expose
    val appLink: String,
    @SerializedName("text")
    @Expose
    val text: String
)

data class ResultStatus(
    @SerializedName("code")
    @Expose
    val code: String,
    @SerializedName("reason")
    @Expose
    val reason: String
)


