package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerhomecommon.data.WidgetLastUpdatedSharedPrefInterface
import com.tokopedia.sellerhomecommon.domain.model.GetMilestoneDataResponse
import com.tokopedia.sellerhomecommon.domain.model.GetRewardDetailByIdResponse
import com.tokopedia.sellerhomecommon.domain.model.MilestoneData
import com.tokopedia.sellerhomecommon.domain.model.MissionProgressBar
import com.tokopedia.sellerhomecommon.domain.usecase.GetMilestoneDataUseCase
import com.tokopedia.sellerhomecommon.presentation.adapter.factory.MilestoneAdapterTypeFactory
import com.tokopedia.sellerhomecommon.presentation.model.BaseMilestoneMissionUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneCtaUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneFinishMissionUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneItemRewardUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneMissionUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneProgressbarUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MissionButtonUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MissionProgressUiModel
import com.tokopedia.sellerhomecommon.presentation.model.RewardDetailBenefit
import com.tokopedia.sellerhomecommon.presentation.model.RewardDetailUiModel
import javax.inject.Inject

class MilestoneMapper @Inject constructor(
    lastUpdatedSharedPref: WidgetLastUpdatedSharedPrefInterface,
    lastUpdatedEnabled: Boolean
) : BaseWidgetMapper(lastUpdatedSharedPref, lastUpdatedEnabled),
    BaseResponseMapper<GetMilestoneDataResponse, List<MilestoneDataUiModel>> {

    companion object {
        private const val HIDDEN_BUTTON_STATUS = 0
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
            mapToUiModel(it, isFromCache)
        }
    }

    override fun mapRemoteDataToUiData(
        response: GetMilestoneDataResponse,
        isFromCache: Boolean,
        extra: Map<String, Any?>
    ): List<MilestoneDataUiModel> {
        val isContainReward = extra[GetMilestoneDataUseCase.REWARD_KEY] != null
        return if (isContainReward) {
            val rewardData = extra[GetMilestoneDataUseCase.REWARD_KEY]
            val data = response.fetchMilestoneWidgetData?.data.orEmpty()
            data.map {
                mapToUiModel(it, isFromCache, rewardData as? GetRewardDetailByIdResponse)
            }
        } else {
            super.mapRemoteDataToUiData(response, isFromCache, extra)
        }
    }

    fun mapToUiModel(
        it: MilestoneData,
        isFromCache: Boolean,
        rewardDetailResponse: GetRewardDetailByIdResponse? = null
    ): MilestoneDataUiModel {
        val missions: List<Visitable<MilestoneAdapterTypeFactory>> = mapGetMilestoneMission(it.showNumber.orFalse(), it.mission.orEmpty())
        val finishCard: List<Visitable<MilestoneAdapterTypeFactory>> = mapGetMilestoneFinish(it.showNumber.orFalse(), it.finishMission)
        val areAllMissionsCompleted =
            missions.all { m -> (m as? BaseMilestoneMissionUiModel)?.missionCompletionStatus == true }
        val allMissions = if (areAllMissionsCompleted) {
            finishCard.plus(missions)
        } else {
            missions.plus(finishCard)
        }.toMutableList()

        if (finishCard.isEmpty() && it.reward.isHaveReward) {
            val rewardCard = mapGetMilestoneReward(
                it,
                mapGetRewardDetail(rewardDetailResponse)
            )
            allMissions.add(Int.ZERO, rewardCard)
        }

        return MilestoneDataUiModel(
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
            milestoneMissions = allMissions,
            milestoneCta = mapGetMilestoneCta(it.cta),
            deadlineMillis = convertSecondToMillisecond(it.deadlineMillis.orZero()),
            lastUpdated = getLastUpdatedMillis(it.dataKey.orEmpty(), isFromCache)
        )
    }

    private fun convertSecondToMillisecond(seconds: Long): Long {
        if (seconds == ZERO_MS) {
            return seconds
        }
        return seconds.times(ONE_SECOND_MILLIS)
    }

    private fun mapGetMilestoneMission(
        showNumber: Boolean,
        missionData: List<MilestoneData.Mission>
    ): List<MilestoneMissionUiModel> {
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
                showNumber = showNumber,
                missionProgress = MissionProgressUiModel(
                    description = it.progress?.description.orEmpty(),
                    percentage = it.progress?.percentage.orZero(),
                    completed = it.progress?.completed.orEmpty(),
                    target = it.progress?.target.orEmpty()
                )
            )
        }
    }

    private fun mapGetMilestoneFinish(
        showNumber: Boolean,
        finishData: MilestoneData.FinishMission
    ): List<MilestoneFinishMissionUiModel> {
        val buttonFinishData = finishData.button ?: return emptyList()
        if (buttonFinishData.title.isBlank()) {
            return emptyList()
        }

        val finishedMission = MilestoneFinishMissionUiModel(
            itemMissionKey = BaseMilestoneMissionUiModel.FINISH_MISSION_KEY,
            imageUrl = finishData.imageUrl.orEmpty(),
            title = finishData.title.orEmpty(),
            subTitle = finishData.subtitle.orEmpty(),
            showNumber = showNumber,
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

    private fun mapGetMilestoneReward(
        data: MilestoneData,
        rewardDetailUiModel: RewardDetailUiModel?
    ): MilestoneItemRewardUiModel {
        val rewardData = data.reward
        return MilestoneItemRewardUiModel(
            title = rewardData.rewardTitle,
            subtitle = rewardData.rewardSubtitle,
            buttonText = rewardData.button.title,
            buttonStatus = rewardData.button.buttonStatus,
            buttonVariant = rewardData.button.buttonStyleType,
            buttonApplink = rewardData.button.applink,
            questStatus = data.questStatus,
            animationUrl = getRewardAnimationUrl(rewardData.rewardImage, data.questStatus),
            rewardDetailUiModel = rewardDetailUiModel
        )
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

    private fun mapGetRewardDetail(
        rewardDetailResponse: GetRewardDetailByIdResponse?
    ): RewardDetailUiModel? {
        return rewardDetailResponse?.data?.result?.let {
            RewardDetailUiModel(
                rewardId = it.rewardId,
                rewardTitle = it.rewardTitle,
                rewardSubtitle = it.rewardSubtitle,
                benefitList = it.benefitList.map { benefit ->
                    RewardDetailBenefit(benefit.benefitName, benefit.benefitValue)
                },
                rewardImage = it.rewardImage
            )
        }
    }

    private fun getRewardAnimationUrl(rewardImage: String, questStatus: Int): String {
        return rewardImage.takeIf { it.isNotBlank() } ?: getRewardAnimationGif(questStatus)
    }

    private fun getRewardAnimationGif(questStatus: Int): String {
        val isInitialState = questStatus == MilestoneItemRewardUiModel.QuestStatus.NOT_STARTED_OR_ONGOING
        return if (isInitialState) {
            TokopediaImageUrl.SELLER_HOME_REWARD_INITIAL_GIF
        } else {
            TokopediaImageUrl.SELLER_HOME_REWARD_ENDING_GIF
        }
    }
}
