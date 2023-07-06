package com.tokopedia.scp_rewards_widgets.medal

interface MedalCallbackListener {
    fun onMedalClick(medalItem: MedalItem)
    fun onSeeMoreClick(medalData: MedalData)
    fun onMedalLoad(medalItem: MedalItem)
    fun onMedalFailed(medalItem: MedalItem)
    fun onSeeMoreLoad(medalData: MedalData)
    fun onBannerClick(bannerData: BannerData?, position: Int?)
}
