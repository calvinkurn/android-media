package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.applink.ApplinkConst
import com.tokopedia.kotlin.model.ImpressHolder

data class MilestoneDataUiModel(
    override var dataKey: String = "",
    override var error: String = "",
    override var isFromCache: Boolean = false,
    override val showWidget: Boolean = true,
    override val lastUpdated: Long = 0,
    val title: String = "",
    val subTitle: String = "",
    val backgroundColor: String = "",
    val backgroundImageUrl: String = "",
    val showNumber: Boolean = false,
    val isError: Boolean = false,
    val isShowMission: Boolean = true,
    val milestoneProgress: MilestoneProgressbarUiModel = MilestoneProgressbarUiModel(),
    val milestoneMissions: List<BaseMilestoneMissionUiModel> = emptyList(),
    val milestoneCta: MilestoneCtaUiModel = MilestoneCtaUiModel()
) : BaseDataUiModel {

    override fun isWidgetEmpty(): Boolean {
        return milestoneMissions.isNullOrEmpty()
    }
}

data class MilestoneProgressbarUiModel(
    val description: String = "",
    val percentage: Double = 0.0,
    val percentageFormatted: String = "",
    val taskCompleted: Int = 0,
    val totalTask: Int = 0
)

interface BaseMilestoneMissionUiModel {

    val itemMissionKey: String
    val imageUrl: String
    val title: String
    val subTitle: String
    val missionCompletionStatus: Boolean
    val missionButton: MissionButtonUiModel
    val impressHolder: ImpressHolder

    companion object {
        const val MISSION_KEY = "mission"
        const val FINISH_MISSION_KEY = "finishMission"
        const val TYPE_URL_REDIRECT = 1
        const val TYPE_URL_SHARING = 3
    }

    enum class UrlType(val urlType: Int) {
        REDIRECT(TYPE_URL_REDIRECT), SHARE(TYPE_URL_SHARING)
    }

    enum class ButtonStatus {
        HIDDEN, ENABLED, DISABLED
    }
}

data class MissionButtonUiModel(
    val title: String = "",
    val urlType: BaseMilestoneMissionUiModel.UrlType = BaseMilestoneMissionUiModel.UrlType.REDIRECT,
    val url: String = "",
    val appLink: String = "",
    val buttonStatus: BaseMilestoneMissionUiModel.ButtonStatus = BaseMilestoneMissionUiModel.ButtonStatus.ENABLED
)

data class MilestoneMissionUiModel(
    override val itemMissionKey: String = BaseMilestoneMissionUiModel.MISSION_KEY,
    override val imageUrl: String = "",
    override val title: String = "",
    override val subTitle: String = "",
    override val missionCompletionStatus: Boolean = false,
    override val missionButton: MissionButtonUiModel = MissionButtonUiModel(),
    override val impressHolder: ImpressHolder = ImpressHolder()
) : BaseMilestoneMissionUiModel

data class MilestoneFinishMissionUiModel(
    override val itemMissionKey: String = BaseMilestoneMissionUiModel.FINISH_MISSION_KEY,
    override val imageUrl: String = "",
    override val title: String = "",
    override val subTitle: String = "",
    override val missionCompletionStatus: Boolean = false,
    override val missionButton: MissionButtonUiModel = MissionButtonUiModel(),
    override val impressHolder: ImpressHolder = ImpressHolder()
) : BaseMilestoneMissionUiModel {

    companion object {
        private const val WEB_VIEW_FORMAT = "%s?url=%s"
    }

    fun getWebViewAppLink(): String {
        return String.format(
            WEB_VIEW_FORMAT,
            ApplinkConst.WEBVIEW,
            missionButton.url
        )
    }
}

data class MilestoneCtaUiModel(
    val text: String = "",
    val appLink: String = ""
)