package com.tokopedia.sellerhomecommon.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetMilestoneDataResponse(
    @Expose
    @SerializedName("fetchMilestoneWidgetData")
    val fetchMilestoneWidgetData: FetchMilestoneWidgetData? = FetchMilestoneWidgetData()
)

data class FetchMilestoneWidgetData(
    @Expose
    @SerializedName("data")
    val `data`: List<MilestoneData> = listOf()
)

data class MilestoneData(
    @Expose
    @SerializedName("backgroundColor")
    val backgroundColor: String? = "",
    @Expose
    @SerializedName("backgroundImageUrl")
    val backgroundImageUrl: String? = "",
    @Expose
    @SerializedName("cta")
    val cta: Cta = Cta(),
    @Expose
    @SerializedName("dataKey")
    val dataKey: String? = "",
    @Expose
    @SerializedName("error")
    val error: Boolean? = false,
    @Expose
    @SerializedName("errorMsg")
    val errorMsg: String? = "",
    @Expose
    @SerializedName("finishMission")
    val finishMission: FinishMission = FinishMission(),
    @Expose
    @SerializedName("mission")
    val mission: List<Mission>? = emptyList(),
    @Expose
    @SerializedName("progressBar")
    val progressBar: MissionProgressBar = MissionProgressBar(),
    @Expose
    @SerializedName("showNumber")
    val showNumber: Boolean? = false,
    @Expose
    @SerializedName("timeDeadline")
    val deadlineMillis: Long? = 0L,
    @Expose
    @SerializedName("showWidget")
    val showWidget: Boolean? = false,
    @Expose
    @SerializedName("subtitle")
    val subtitle: String? = "",
    @Expose
    @SerializedName("title")
    val title: String? = ""
) {
    data class Mission(
        @Expose
        @SerializedName("button")
        val button: Button? = Button(),
        @Expose
        @SerializedName("imageUrl")
        val imageUrl: String? = "",
        @Expose
        @SerializedName("missionCompletionStatus")
        val missionCompletionStatus: Boolean? = false,
        @Expose
        @SerializedName("subtitle")
        val subtitle: String? = "",
        @Expose
        @SerializedName("title")
        val title: String? = "",
        @Expose
        @SerializedName("progress")
        val progress: MissionProgressModel? = MissionProgressModel()
    )

    data class FinishMission(
        @Expose
        @SerializedName("button")
        val button: ButtonFinish? = ButtonFinish(),
        @Expose
        @SerializedName("imageUrl")
        val imageUrl: String? = "",
        @Expose
        @SerializedName("subtitle")
        val subtitle: String? = "",
        @Expose
        @SerializedName("title")
        val title: String? = ""
    )

    data class Cta(
        @Expose
        @SerializedName("applink")
        val applink: String = "",
        @Expose
        @SerializedName("text")
        val text: String = ""
    )
}

data class Button(
    @Expose
    @SerializedName("applink")
    val applink: String = "",
    @Expose
    @SerializedName("buttonStatus")
    val buttonStatus: Int = 0,
    @Expose
    @SerializedName("title")
    val title: String = "",
    @Expose
    @SerializedName("url")
    val url: String = "",
    @Expose
    @SerializedName("urlType")
    val urlType: Int = 0
)

data class MissionProgressBar(
    @Expose
    @SerializedName("description")
    val description: String = "",
    @Expose
    @SerializedName("percentage")
    val percentage: Double = 0.0,
    @Expose
    @SerializedName("percentageFormatted")
    val percentageFormatted: String = "",
    @Expose
    @SerializedName("taskCompleted")
    val taskCompleted: Int = 0,
    @Expose
    @SerializedName("totalTask")
    val totalTask: Int = 0
)

data class ButtonFinish(
    @Expose
    @SerializedName("applink")
    val applink: String = "",
    @Expose
    @SerializedName("buttonStatus")
    val buttonStatus: Int = 0,
    @Expose
    @SerializedName("title")
    val title: String = "",
    @Expose
    @SerializedName("url")
    val url: String = "",
    @Expose
    @SerializedName("urlType")
    val urlType: Int = 0
)

data class MissionProgressModel(
    @Expose
    @SerializedName("appDescription")
    val description: String = "",
    @Expose
    @SerializedName("percentage")
    val percentage: Int = 0,
    @Expose
    @SerializedName("completed")
    val completed: String = "",
    @Expose
    @SerializedName("target")
    val target: String = ""
)