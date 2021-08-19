package com.tokopedia.sellerhomecommon.presentation.model

data class MilestoneDataUiModel(
    override var dataKey: String = "",
    override var error: String = "",
    override var isFromCache: Boolean = false,
    override val showWidget: Boolean = true,
    val title: String = "",
    val subTitle: String = "",
    val backgroundColor: String = "",
    val backgroundImageUrl: String = "",
    val showNumber: Boolean = false,
    val isError: Boolean = false,
    val milestoneProgressbar: MilestoneProgressbarUiModel = MilestoneProgressbarUiModel(),
    val milestoneMissionUiModel: List<MilestoneMissionUiModel> = emptyList(),
    val milestoneFinishMissionUiModel: MilestoneFinishMissionUiModel =
        MilestoneFinishMissionUiModel(),
    val milestoneCtaUiModel: MilestoneCtaUiModel = MilestoneCtaUiModel()
) : BaseDataUiModel {

    override fun shouldRemove(): Boolean {
        return !isFromCache && !showWidget
    }
}

data class MilestoneProgressbarUiModel(
    val description: String = "",
    val percentage: Double = 0.0,
    val percentageFormatted: String = "",
    val taskCompleted: Int = 0,
    val totalTask: Int = 0
)

data class MilestoneMissionUiModel(
    val imageUrl: String = "",
    val title: String = "",
    val subTitle: String = "",
    val missionCompletionStatus: Boolean = false,
    val buttonMission: ButtonMission = ButtonMission()
) {
    data class ButtonMission(
        val title: String = "",
        val urlType: Int = 0,
        val url: String = "",
        val appLink: String = "",
        val buttonStatus: Int = 0
    )
}

data class MilestoneFinishMissionUiModel(
    val imageUrl: String = "",
    val title: String = "",
    val subTitle: String = "",
    val buttonFinishMission: ButtonFinishMission = ButtonFinishMission()
) {
    data class ButtonFinishMission(
        val title: String = "",
        val urlType: Int = 0,
        val url: String = "",
        val appLink: String = "",
        val buttonStatus: Int = 0
    )
}

data class MilestoneCtaUiModel(
    val text: String = "",
    val url: String = "",
    val appLink: String = ""
)