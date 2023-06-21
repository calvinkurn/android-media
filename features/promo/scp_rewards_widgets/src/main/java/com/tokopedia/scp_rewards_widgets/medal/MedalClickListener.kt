package com.tokopedia.scp_rewards_widgets.medal

interface MedalClickListener {
    fun onMedalClick(medalItem: MedalItem)

    fun onSeeMoreClick(medalData: MedalData)
}
