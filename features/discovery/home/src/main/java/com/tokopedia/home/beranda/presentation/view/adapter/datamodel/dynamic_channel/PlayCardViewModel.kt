package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel

import com.tokopedia.home.beranda.data.model.PlayChannel
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory

class PlayCardViewModel: HomeVisitable<HomeTypeFactory> {
    private var isCache: Boolean = false
    private var trackingData: Map<String, Any>? = null
    private var isCombined: Boolean = false
    private var trackingDataForCombination: List<Any> = emptyList()
    private var playCardHome: PlayChannel? = null
    private var channel: DynamicHomeChannel.Channels? = null

    override fun isCache(): Boolean {
        return isCache
    }

    fun setCache(cache: Boolean) {
        isCache = cache
    }

    fun setPlayCardHome(playCardHome: PlayChannel) {
        this.playCardHome = playCardHome
        setBannerImageUrl()
    }

    fun setChannel(channel: DynamicHomeChannel.Channels) {
        this.channel = channel
    }

    fun getChannel(): DynamicHomeChannel.Channels? {
        return channel
    }

    fun getPlayCardHome(): PlayChannel? {
        return playCardHome
    }

    private fun setBannerImageUrl() {
        val bannerUrl = playCardHome?.coverUrl ?: ""
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