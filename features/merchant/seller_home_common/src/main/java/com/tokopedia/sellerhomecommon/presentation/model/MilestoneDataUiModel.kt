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
    val isShowMission: Boolean = true,
    val milestoneProgressbar: MilestoneProgressbarUiModel = MilestoneProgressbarUiModel(),
    val milestoneMissionUiModel: List<BaseMilestoneMissionUiModel> = emptyList(),
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

open class BaseMilestoneMissionUiModel {

    open val itemMissionKey: String = ""
    open val imageUrl: String = ""
    open val title: String = ""
    open val subTitle: String = ""
    open val missionCompletionStatus: Boolean = false
    open val buttonMission: BaseButtonMission = BaseButtonMission()

    open class BaseButtonMission {
        open val title: String = ""
        open val urlType: Int = REDIRECT_URL_TYPE
        open val url: String = ""
        open val appLink: String = ""
        open val buttonStatus: Int = ENABLED_BUTTON_STATUS
    }

    companion object {
        const val MISSION_KEY = "mission"
        const val FINISH_MISSION_KEY = "finishMission"

        const val REDIRECT_URL_TYPE = 1
        const val SHARE_URL_TYPE = 2
        const val FINISH_URL_TYPE = 3

        const val HIDDEN_BUTTON_STATUS = 0
        const val ENABLED_BUTTON_STATUS = 1
        const val DISABLED_BUTTON_STATUS = 2
    }
}

data class MilestoneMissionUiModel(
    override val itemMissionKey: String = MISSION_KEY,
    override val imageUrl: String = "",
    override val title: String = "",
    override val subTitle: String = "",
    override val missionCompletionStatus: Boolean = false,
    override val buttonMission: ButtonMission
) : BaseMilestoneMissionUiModel() {

    data class ButtonMission(
        override val title: String = "",
        override val urlType: Int,
        override val url: String = "",
        override val appLink: String = "",
        override val buttonStatus: Int
    ): BaseButtonMission()
}

data class MilestoneFinishMissionUiModel(
    override val itemMissionKey: String = FINISH_MISSION_KEY,
    override val imageUrl: String = "",
    override val title: String = "",
    override val subTitle: String = "",
    override val missionCompletionStatus: Boolean = false,
    override val buttonMission: ButtonFinishMission
): BaseMilestoneMissionUiModel() {

    data class ButtonFinishMission(
        override val title: String = "",
        override val urlType: Int,
        override val url: String = "",
        override val appLink: String = "",
        override val buttonStatus: Int
    ): BaseButtonMission()
}

data class MilestoneCtaUiModel(
    val text: String = "",
    val url: String = "",
    val appLink: String = ""
)