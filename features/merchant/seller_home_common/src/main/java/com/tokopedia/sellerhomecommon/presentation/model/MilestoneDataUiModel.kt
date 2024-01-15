package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.sellerhomecommon.presentation.adapter.factory.MilestoneAdapterTypeFactory
import java.util.*

data class MilestoneDataUiModel(
    override var dataKey: String = "",
    override var error: String = "",
    override var isFromCache: Boolean = false,
    override val showWidget: Boolean = true,
    override val lastUpdated: LastUpdatedUiModel = LastUpdatedUiModel(),
    val title: String = "",
    val subTitle: String = "",
    val backgroundColor: String = "",
    val backgroundImageUrl: String = "",
    val showNumber: Boolean = false,
    val isError: Boolean = false,
    val milestoneProgress: MilestoneProgressbarUiModel = MilestoneProgressbarUiModel(),
    val milestoneMissions: List<Visitable<MilestoneAdapterTypeFactory>> = emptyList(),
    val milestoneCta: MilestoneCtaUiModel = MilestoneCtaUiModel(),
    val deadlineMillis: Long = 0L
) : BaseDataUiModel, LastUpdatedDataInterface {

    override fun isWidgetEmpty(): Boolean {
        return milestoneMissions.isNullOrEmpty() || isOverDue()
    }

    fun isOverDue(): Boolean {
        val now = Date().time
        return deadlineMillis < now
    }
}

data class MilestoneProgressbarUiModel(
    val description: String = "",
    val percentage: Double = 0.0,
    val percentageFormatted: String = "",
    val taskCompleted: Int = 0,
    val totalTask: Int = 0
)

abstract class BaseMilestoneMissionUiModel: Visitable<MilestoneAdapterTypeFactory> {

    abstract val itemMissionKey: String
    abstract val imageUrl: String
    abstract val title: String
    abstract val subTitle: String
    abstract val missionCompletionStatus: Boolean
    abstract val showNumber: Boolean
    abstract val missionButton: MissionButtonUiModel
    abstract val impressHolder: ImpressHolder

    companion object {
        const val MISSION_KEY = "mission"
        const val FINISH_MISSION_KEY = "finishMission"
        const val TYPE_URL_REDIRECT = 1
        const val TYPE_URL_SHARING = 3
    }

    override fun type(typeFactory: MilestoneAdapterTypeFactory): Int {
        return typeFactory.type(this)
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
    override val itemMissionKey: String = MISSION_KEY,
    override val imageUrl: String = "",
    override val title: String = "",
    override val subTitle: String = "",
    override val missionCompletionStatus: Boolean = false,
    override val showNumber: Boolean = false,
    override val missionButton: MissionButtonUiModel = MissionButtonUiModel(),
    override val impressHolder: ImpressHolder = ImpressHolder(),
    val missionProgress: MissionProgressUiModel = MissionProgressUiModel()
) : BaseMilestoneMissionUiModel() {

    override fun type(typeFactory: MilestoneAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun isProgressAvailable(): Boolean {
        return missionProgress.target.isNotBlank() && missionProgress.completed.isNotBlank()
    }
}

data class MilestoneFinishMissionUiModel(
    override val itemMissionKey: String = FINISH_MISSION_KEY,
    override val imageUrl: String = "",
    override val title: String = "",
    override val subTitle: String = "",
    override val missionCompletionStatus: Boolean = false,
    override val showNumber: Boolean = false,
    override val missionButton: MissionButtonUiModel = MissionButtonUiModel(),
    override val impressHolder: ImpressHolder = ImpressHolder()
) : BaseMilestoneMissionUiModel() {

    companion object {
        private const val WEB_VIEW_FORMAT = "%s?url=%s"
    }

    override fun type(typeFactory: MilestoneAdapterTypeFactory): Int {
        return typeFactory.type(this)
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
