package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerhomecommon.data.WidgetLastUpdatedSharedPrefInterface
import com.tokopedia.sellerhomecommon.domain.model.GetMilestoneDataResponse
import com.tokopedia.sellerhomecommon.domain.model.MilestoneData
import com.tokopedia.sellerhomecommon.domain.model.MissionProgressBar
import com.tokopedia.sellerhomecommon.presentation.model.*
import javax.inject.Inject

class MilestoneMapper @Inject constructor(
    lastUpdatedSharedPref: WidgetLastUpdatedSharedPrefInterface
) : BaseWidgetMapper(lastUpdatedSharedPref),
    BaseResponseMapper<GetMilestoneDataResponse, List<MilestoneDataUiModel>> {

    companion object {
        private const val HIDDEN_BUTTON_STATUS = 0
        private const val ENABLED_BUTTON_STATUS = 1
        private const val DISABLED_BUTTON_STATUS = 2
        private const val ZERO_MS = 0L
        private const val ONE_SECOND_MILLIS = 1000L
    }

    override fun mapRemoteDataToUiData(
        response: GetMilestoneDataResponse,
        isFromCache: Boolean
    ): List<MilestoneDataUiModel> {
        val data = response.fetchMilestoneWidgetData?.data.orEmpty()
        return data.map {
            val missionMilestone = mapGetMilestoneMission(it.mission.orEmpty())
                .plus(mapGetMilestoneFinish(it.finishMission))
            return@map MilestoneDataUiModel(
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
                milestoneProgress = mapGetMilestoneProgressbar(it.progressBar),
                milestoneMissions = missionMilestone,
                milestoneCta = mapGetMilestoneCta(it.cta),
                deadlineMillis = convertSecondToMillisecond(it.deadlineMillis.orZero()),
                lastUpdated = getLastUpdatedMillis(it.dataKey.orEmpty(), isFromCache)
            )
        }
    }

    private fun convertSecondToMillisecond(seconds: Long): Long {
        if (seconds == ZERO_MS) {
            return seconds
        }
        return seconds.times(ONE_SECOND_MILLIS)
    }

    private fun mapGetMilestoneMission(missionData: List<MilestoneData.Mission>): List<MilestoneMissionUiModel> {
        return missionData.map {
            val buttonMissionData = it.button
            return@map MilestoneMissionUiModel(
                itemMissionKey = BaseMilestoneMissionUiModel.MISSION_KEY,
                imageUrl = it.imageUrl.orEmpty(),
                title = it.title.orEmpty(),
                subTitle = it.subtitle.orEmpty(),
                missionCompletionStatus = it.missionCompletionStatus.orFalse(),
                missionButton = MissionButtonUiModel(
                    title = buttonMissionData?.title.orEmpty(),
                    url = buttonMissionData?.url.orEmpty(),
                    urlType = getUrlType(buttonMissionData?.urlType),
                    appLink = buttonMissionData?.applink.orEmpty(),
                    buttonStatus = getButtonStatus(buttonMissionData?.buttonStatus)
                ),
                missionProgress = MissionProgressUiModel(
                    description = it.progress?.description.orEmpty(),
                    percentage = it.progress?.percentage.orZero(),
                    completed = it.progress?.completed.orEmpty(),
                    target = it.progress?.target.orEmpty()
                )
            )
        }
    }

    private fun mapGetMilestoneFinish(finishData: MilestoneData.FinishMission): List<MilestoneFinishMissionUiModel> {
        val buttonFinishData = finishData.button ?: return emptyList()
        if (buttonFinishData.title.isBlank()) {
            return emptyList()
        }

        val finishedMission = MilestoneFinishMissionUiModel(
            itemMissionKey = BaseMilestoneMissionUiModel.FINISH_MISSION_KEY,
            imageUrl = finishData.imageUrl.orEmpty(),
            title = finishData.title.orEmpty(),
            subTitle = finishData.subtitle.orEmpty(),
            missionButton = MissionButtonUiModel(
                title = buttonFinishData.title,
                url = buttonFinishData.url,
                urlType = getUrlType(buttonFinishData.urlType),
                appLink = buttonFinishData.applink,
                buttonStatus = getButtonStatus(buttonFinishData.buttonStatus)
            )
        )
        return listOf(finishedMission)
    }

    private fun getUrlType(urlType: Int?): BaseMilestoneMissionUiModel.UrlType {
        return if (urlType == BaseMilestoneMissionUiModel.TYPE_URL_SHARING) {
            BaseMilestoneMissionUiModel.UrlType.SHARE
        } else {
            BaseMilestoneMissionUiModel.UrlType.REDIRECT
        }
    }

    private fun getButtonStatus(buttonStatus: Int?): BaseMilestoneMissionUiModel.ButtonStatus {
        return when (buttonStatus) {
            HIDDEN_BUTTON_STATUS -> BaseMilestoneMissionUiModel.ButtonStatus.HIDDEN
            DISABLED_BUTTON_STATUS -> BaseMilestoneMissionUiModel.ButtonStatus.DISABLED
            else -> BaseMilestoneMissionUiModel.ButtonStatus.ENABLED
        }
    }

    private fun mapGetMilestoneCta(ctaData: MilestoneData.Cta): MilestoneCtaUiModel {
        return MilestoneCtaUiModel(
            text = ctaData.text,
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