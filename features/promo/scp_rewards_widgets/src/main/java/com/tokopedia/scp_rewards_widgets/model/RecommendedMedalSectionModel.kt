package com.tokopedia.scp_rewards_widgets.model

import com.tokopedia.scp_rewards_widgets.medal.MedalItem

data class RecommendedMedalSectionModel(
    val title: String?,
    val medalList: List<MedalItem>?
)
