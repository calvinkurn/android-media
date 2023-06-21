package com.tokopedia.scp_rewards.cabinet.mappers

import com.tokopedia.scp_rewards.cabinet.domain.model.ScpRewardsGetUserMedalisResponse
import com.tokopedia.scp_rewards_widgets.medal.Cta
import com.tokopedia.scp_rewards_widgets.medal.MedalItem

object MedaliListMapper {

    fun getMedalList(data: ScpRewardsGetUserMedalisResponse, badgeType: String): List<MedalItem> {
        val medalList = mutableListOf<MedalItem>()
        data.scpRewardsGetUserMedalisByType.medaliList.forEach {
            medalList.add(
                MedalItem(
                    id = it.id,
                    name = it.name,
                    provider = it.provider,
                    extraInfo = it.extraInfo,
                    imageUrl = it.logoImageURL,
                    celebrationUrl = it.celebrationImageURL,
                    isNewMedal = it.isNewMedali,
                    isDisabled = it.isDisabled,
                    progression = it.progressionCompletement,
                    cta = Cta(
                        appLink = it.cta.appLink,
                        deepLink = it.cta.url
                    ),
                    medalType = badgeType,
                    isPlaceHolder = false
                )
            )
        }

        return medalList
    }
}
