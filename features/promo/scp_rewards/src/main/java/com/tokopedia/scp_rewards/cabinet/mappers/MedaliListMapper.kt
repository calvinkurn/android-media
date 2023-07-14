package com.tokopedia.scp_rewards.cabinet.mappers

import com.tokopedia.scp_rewards.cabinet.domain.model.ScpRewardsGetMedaliSectionResponse
import com.tokopedia.scp_rewards.cabinet.domain.model.ScpRewardsGetUserMedalisResponse
import com.tokopedia.scp_rewards_widgets.cabinetHeader.CabinetHeader
import com.tokopedia.scp_rewards_widgets.medal.BannerData
import com.tokopedia.scp_rewards_widgets.medal.Cta
import com.tokopedia.scp_rewards_widgets.medal.MedalData
import com.tokopedia.scp_rewards_widgets.medal.MedalItem

object MedaliListMapper {

    fun getMedalList(
        data: ScpRewardsGetUserMedalisResponse?,
        badgeType: String,
        showConfetti: Boolean = true
    ): List<MedalItem> {
        val medalList = mutableListOf<MedalItem>()
        data?.scpRewardsGetUserMedalisByType?.medaliList?.forEach {
            medalList.add(
                MedalItem(
                    id = it.id,
                    name = it.name,
                    provider = it.provider,
                    extraInfo = it.extraInfo,
                    imageUrl = it.logoImageURL,
                    celebrationUrl = it.celebrationImageURL,
                    showConfetti = showConfetti && it.isNewMedali,
                    isDisabled = it.isDisabled,
                    progression = it.progressionCompletement,
                    cta = Cta(
                        appLink = it.cta?.appLink,
                        deepLink = it.cta?.url
                    ),
                    medalType = badgeType,
                    isPlaceHolder = false
                )
            )
        }

        return medalList
    }

    fun mapMedalTypeResponseToMedalData(
        sectionResponse: ScpRewardsGetMedaliSectionResponse,
        medalResponse: ScpRewardsGetUserMedalisResponse?,
        badgeType: String,
        sectionId: Int
    ): MedalData {
        val medalSection =
            sectionResponse.scpRewardsGetMedaliSectionLayout?.medaliSectionLayoutList?.find { it.id == sectionId }
        return MedalData(
            id = sectionId,
            title = medalSection?.medaliSectionTitle?.content,
            description = medalSection?.medaliSectionTitle?.description,
            textColor = medalSection?.medaliSectionTitle?.color,
            medalType = badgeType,
            medalList = getMedalList(medalResponse, badgeType),
            bannerData = BannerData(
                imageUrl = medalResponse?.scpRewardsGetUserMedalisByType?.medaliBanner?.imageList?.first()?.imageURL,
                appLink = medalResponse?.scpRewardsGetUserMedalisByType?.medaliBanner?.imageList?.first()?.redirectAppLink,
                webLink = medalResponse?.scpRewardsGetUserMedalisByType?.medaliBanner?.imageList?.first()?.redirectURL,
                creativeName = medalResponse?.scpRewardsGetUserMedalisByType?.medaliBanner?.imageList?.first()?.creativeName
            ),
            cta = Cta(
                text = medalResponse?.scpRewardsGetUserMedalisByType?.paging?.cta?.text,
                isShown = medalResponse?.scpRewardsGetUserMedalisByType?.paging?.cta?.isShown,
                appLink = medalResponse?.scpRewardsGetUserMedalisByType?.paging?.cta?.appLink,
                deepLink = medalResponse?.scpRewardsGetUserMedalisByType?.paging?.cta?.url
            )
        )
    }

    fun mapSectionResponseToHeaderData(
        data: ScpRewardsGetMedaliSectionResponse,
        sectionId: Int
    ): CabinetHeader {
        val headerSection =
            data.scpRewardsGetMedaliSectionLayout?.medaliSectionLayoutList?.find { it.id == sectionId }
        return CabinetHeader(
            title = headerSection?.medaliSectionTitle?.content,
            subTitle = headerSection?.medaliSectionTitle?.description,
            background = headerSection?.backgroundImageURL,
            backgroundColor = headerSection?.backgroundColor,
            textColor = headerSection?.medaliSectionTitle?.color
        )
    }
}
