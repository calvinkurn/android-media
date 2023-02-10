package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.SerializedName

data class GetMilestoneDataResponse(
    @SerializedName("fetchMilestoneWidgetData")
    val fetchMilestoneWidgetData: FetchMilestoneWidgetData? = FetchMilestoneWidgetData()
)

data class FetchMilestoneWidgetData(
    @SerializedName("data")
    val `data`: List<MilestoneData> = listOf()
)

data class MilestoneData(
    @SerializedName("backgroundColor")
    val backgroundColor: String? = "",
    @SerializedName("backgroundImageUrl")
    val backgroundImageUrl: String? = "",
    @SerializedName("cta")
    val cta: Cta = Cta(),
    @SerializedName("dataKey")
    val dataKey: String? = "",
    @SerializedName("error")
    val error: Boolean? = false,
    @SerializedName("errorMsg")
    val errorMsg: String? = "",
    @SerializedName("finishMission")
    val finishMission: FinishMission = FinishMission(),
    @SerializedName("mission")
    val mission: List<Mission>? = emptyList(),
    @SerializedName("progressBar")
    val progressBar: MissionProgressBar = MissionProgressBar(),
    @SerializedName("showNumber")
    val showNumber: Boolean? = false,
    @SerializedName("timeDeadline")
    val deadlineMillis: Long? = 0L,
    @SerializedName("showWidget")
    val showWidget: Boolean? = false,
    @SerializedName("subtitle")
    val subtitle: String? = "",
    @SerializedName("title")
    val title: String? = ""
) {
    data class Mission(
        @SerializedName("button")
        val button: Button? = Button(),
        @SerializedName("imageUrl")
        val imageUrl: String? = "",
        @SerializedName("missionCompletionStatus")
        val missionCompletionStatus: Boolean? = false,
        @SerializedName("subtitle")
        val subtitle: String? = "",
        @SerializedName("title")
        val title: String? = "",
        @SerializedName("progress")
        val progress: MissionProgressModel? = MissionProgressModel()
    )

    data class FinishMission(
        @SerializedName("button")
        val button: ButtonFinish? = ButtonFinish(),
        @SerializedName("imageUrl")
        val imageUrl: String? = "",
        @SerializedName("subtitle")
        val subtitle: String? = "",
        @SerializedName("title")
        val title: String? = ""
    )

    data class Cta(
        @SerializedName("applink")
        val applink: String = "",
        @SerializedName("text")
        val text: String = ""
    )
}

data class Button(
    @SerializedName("applink")
    val applink: String = "",
    @SerializedName("buttonStatus")
    val buttonStatus: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("url")
    val url: String = "",
    @SerializedName("urlType")
    val urlType: Int = 0
)

data class MissionProgressBar(
    @SerializedName("description")
    val description: String = "",
    @SerializedName("percentage")
    val percentage: Double = 0.0,
    @SerializedName("percentageFormatted")
    val percentageFormatted: String = "",
    @SerializedName("taskCompleted")
    val taskCompleted: Int = 0,
    @SerializedName("totalTask")
    val totalTask: Int = 0
)

data class ButtonFinish(
    @SerializedName("applink")
    val applink: String = "",
    @SerializedName("buttonStatus")
    val buttonStatus: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("url")
    val url: String = "",
    @SerializedName("urlType")
    val urlType: Int = 0
)

data class MissionProgressModel(
    @SerializedName("appDescription")
    val description: String = "",
    @SerializedName("percentage")
    val percentage: Int = 0,
    @SerializedName("completed")
    val completed: String = "",
    @SerializedName("target")
    val target: String = ""
)
