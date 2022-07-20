package com.tokopedia.tokopedianow.home.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetQuestListResponse(
    @SerializedName("questList")
    @Expose
    val questWidgetList: QuestListResponse
)

data class QuestListResponse(
    @SerializedName("questList")
    @Expose
    val questWidgetList: List<QuestList>,
    @SerializedName("resultStatus")
    @Expose
    val resultStatus: ResultStatus
)

data class QuestList(
    @SerializedName("id")
    @Expose
    val id: String,
    @SerializedName("title")
    @Expose
    val title: String,
    @SerializedName("description")
    @Expose
    val description: String,
    @SerializedName("config")
    @Expose
    val config: String,
    @SerializedName("questUser")
    @Expose
    val questUser: QuestUser,
    @SerializedName("task")
    @Expose
    val task: List<Task>,
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

data class ResultStatus(
    @SerializedName("code")
    @Expose
    val code: String,
    @SerializedName("reason")
    @Expose
    val reason: String
)


