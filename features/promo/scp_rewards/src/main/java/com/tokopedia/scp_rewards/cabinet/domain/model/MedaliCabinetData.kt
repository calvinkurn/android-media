package com.tokopedia.scp_rewards.cabinet.domain.model

data class MedaliCabinetData(
    val sectionData:ScpRewardsGetMedaliSectionResponse? = null,
    val earnedMedaliData:ScpRewardsGetUserMedalisResponse? = null,
    val progressMedaliData:ScpRewardsGetUserMedalisResponse? = null
)
