package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel

import com.tokopedia.dynamicbanner.entity.PlayCardHome
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory

class PlayCardViewModel: HomeVisitable<HomeTypeFactory> {
    private var isCache: Boolean = false
    private var trackingData: Map<String, Any>? = null
    private var isCombined: Boolean = false
    private var trackingDataForCombination: List<Any> = emptyList()
    private var playCardHome: PlayCardHome? = null
    private var channel: DynamicHomeChannel.Channels? = null
//    val url = "https://www.w3schools.com/html/mov_bbb.mp4"
    val url = "rtmp://fms.105.net/live/rmc1"
    override fun isCache(): Boolean {
        return isCache
    }

    fun setCache(cache: Boolean) {
        isCache = cache
    }

    fun setPlayCardHome(playCardHome: PlayCardHome) {
        this.playCardHome = playCardHome
        setBannerImageUrl()
    }

    fun setChannel(channel: DynamicHomeChannel.Channels) {
        this.channel = channel
    }

    fun getChannel(): DynamicHomeChannel.Channels? {
        return channel
    }

    fun getPlayCardHome(): PlayCardHome? {
        return playCardHome
    }

    private fun setBannerImageUrl() {
        val bannerUrl = playCardHome?.playGetCardHome?.data?.card?.imageUrl?: ""
        this.channel?.banner?.imageUrl = bannerUrl
    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun setTrackingData(trackingData: MutableMap<String, Any>) {
        this.trackingData = trackingData
    }

    override fun getTrackingData(): Map<String, Any>? = trackingData

    override fun getTrackingDataForCombination(): List<Any> = trackingDataForCombination

    override fun setTrackingDataForCombination(list: List<Any>) {
        this.trackingDataForCombination = list
    }

    override fun isTrackingCombined(): Boolean = isCombined

    override fun setTrackingCombined(isCombined: Boolean) {
        this.isCombined = isCombined
    }
}