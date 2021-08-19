package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerhomecommon.domain.model.MilestoneData
import com.tokopedia.sellerhomecommon.domain.model.MissionProgressBar
import com.tokopedia.sellerhomecommon.presentation.model.*
import javax.inject.Inject

class MilestoneMapper @Inject constructor() {

    fun mapMilestoneResponseToUiModel(
        data: List<MilestoneData>,
        isFromCache: Boolean
    ): List<MilestoneDataUiModel> {
        return data.map {
            MilestoneDataUiModel(
                dataKey = it.dataKey.orEmpty(),
                error = it.errorMsg.orEmpty(),
                isFromCache = isFromCache,
                showWidget = it.showWidget.orTrue(),
                title = it.title.orEmpty(),
                subTitle = it.subtitle.orEmpty(),
                backgroundColor = it.backgroundColor.orEmpty(),
                backgroundImageUrl = it.backgroundImageUrl.orEmpty(),
                showNumber = it.showNumber.orFalse(),
                isError = it.error.orFalse(),
                milestoneProgressbar = mapGetMilestoneProgressbar(it.progressBar),
                milestoneMissionUiModel = mapGetMilestoneMission(it.mission),
                milestoneFinishMissionUiModel = mapGetMilestoneFinish(it.finishMission),
                milestoneCtaUiModel = mapGetMilestoneCta(it.cta)
            )
        }
    }

    private fun mapGetMilestoneMission(missionData: List<MilestoneData.Mission>): List<MilestoneMissionUiModel> {
        return missionData.map {
            val buttonMissionData = it.button
            MilestoneMissionUiModel(
                imageUrl = it.imageUrl,
                title = it.title,
                subTitle = it.subtitle,
                missionCompletionStatus = it.missionCompletionStatus,
                buttonMission = MilestoneMissionUiModel.ButtonMission(
                    title = buttonMissionData.title,
                    url = buttonMissionData.url,
                    urlType = buttonMissionData.urlType,
                    appLink = buttonMissionData.applink,
                    buttonStatus = buttonMissionData.buttonStatus
                )
            )
        }
    }

    private fun mapGetMilestoneFinish(finishData: MilestoneData.FinishMission): MilestoneFinishMissionUiModel {
        val buttonFinishData = finishData.button
        return MilestoneFinishMissionUiModel(
            imageUrl = finishData.imageUrl,
            title = finishData.title,
            subTitle = finishData.subtitle,
            buttonFinishMission = MilestoneFinishMissionUiModel.ButtonFinishMission(
                title = buttonFinishData.title,
                url = buttonFinishData.url,
                urlType = buttonFinishData.urlType,
                appLink = buttonFinishData.applink,
                buttonStatus = buttonFinishData.buttonStatus
            )
        )
    }

    private fun mapGetMilestoneCta(ctaData: MilestoneData.Cta): MilestoneCtaUiModel {
        return MilestoneCtaUiModel(
            text = ctaData.text,
            url = ctaData.url,
            appLink = ctaData.applink
        )
    }

    private fun mapGetMilestoneProgressbar(progressbarData: MissionProgressBar): MilestoneProgressbarUiModel {
        return MilestoneProgressbarUiModel(
            description = progressbarData.description,
            percentage = progressbarData.percentage.orZero(),
            percentageFormatted = progressbarData.percentageFormatted,
            taskCompleted = progressbarData.taskCompleted.orZero(),
            totalTask = progressbarData.totalTask.orZero()
        )
    }
}