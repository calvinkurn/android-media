package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel

import android.os.Bundle
import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.home.beranda.data.model.PlayChannel
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder
import java.util.*

class PlayCardViewModel: HomeVisitable, ImpressHolder() {
    override fun visitableId(): String {
        return channel.id
    }

    override fun equalsWith(b: Any?): Boolean {
        if (b is DynamicChannelViewModel) {
            return channel == b.channel
        }
        return false
    }

    private var isCache: Boolean = false
    private var trackingData: Map<String, Any>? = null
    private var isCombined: Boolean = false
    private var trackingDataForCombination: List<Any> = emptyList()
    private var playCardHome: PlayChannel? = null
    private var channel: DynamicHomeChannel.Channels = DynamicHomeChannel.Channels()

    override fun isCache(): Boolean {
        return isCache
    }

    fun setCache(cache: Boolean) {
        isCache = cache
    }

    fun setPlayCardHome(playCardHome: PlayChannel) {
        this.playCardHome = playCardHome
    }

    fun setChannel(channel: DynamicHomeChannel.Channels) {
        this.channel = channel
    }

    fun getChannel(): DynamicHomeChannel.Channels {
        return channel
    }

    fun getPlayCardHome(): PlayChannel? {
        return playCardHome
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

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return null
    }

    val enhanceClickPlayBanner: Map<String, Any>
        get()  {
            val list: List<Any> = convertPromoEnhancePlayBanner(playCardHome)
            return DataLayer.mapOf(
                    "event", "promoClick",
                    "eventCategory", "homepage-cmp",
                    "eventAction", "click on play dynamic banner",
                    "eventLabel", "Play-CMP_OTHERS_${playCardHome?.slug} - ${playCardHome?.channelId}",
                    "ecommerce", DataLayer.mapOf(
                    "promoClick", DataLayer.mapOf(
                    "promotions", DataLayer.listOf(
                    *list.toTypedArray()
            )
            )
            )
            )

    }

    val enhanceImpressionPlayBanner: Map<String, Any>
        get() {
            val list: List<Any> = convertPromoEnhancePlayBanner(playCardHome)
            return DataLayer.mapOf(
                    "event", "promoView",
                    "eventCategory", "homepage-cmp",
                    "eventAction", "impression on play dynamic banner",
                    "eventLabel", "Play-CMP_OTHERS_${playCardHome?.slug} - ${playCardHome?.channelId}",
                    "ecommerce", DataLayer.mapOf(
                    "promoView", DataLayer.mapOf(
                    "promotions", DataLayer.listOf(
                    *list.toTypedArray()
            )
            )
            )
            )
    }

    private fun convertPromoEnhancePlayBanner(playChannel: PlayChannel?): List<Any> {
        val list: MutableList<Any> = ArrayList()
        /**
         * Banner always in position 1 because only 1 banner shown
         */
        list.add(
                DataLayer.mapOf(
                        "id", playChannel?.channelId,
                        "name", "/ - p1 - play dynamic banner - ${playChannel?.title}",
                        "creative", "Play-CMP_OTHERS_${playChannel?.slug}",
                        "creative_url", playChannel?.coverUrl,
                        "position", 1.toString())
        )
        return list
    }
}