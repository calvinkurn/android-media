package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel

import android.os.Bundle
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home.beranda.data.model.PlayChannel
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder
import java.util.*

data class PlayCardDataModel(
        val channel: DynamicHomeChannel.Channels,
        val playCardHome: PlayChannel? = null
): HomeVisitable, ImpressHolder() {
    override fun visitableId(): String {
        return channel.type
    }

    override fun equalsWith(b: Any?): Boolean {
        if (b is PlayCardDataModel) {
            return channel.id == b.channel.id
                    && channel.name == b.channel.name
                    && channel.header.name == b.channel.header.name
                    && channel.header.applink == b.channel.header.applink
                    && playCardHome == b.playCardHome
                    && playCardHome?.channelId == b.playCardHome?.channelId
                    && playCardHome?.coverUrl == b.playCardHome?.coverUrl
                    && playCardHome?.videoStream?.config?.streamUrl == b.playCardHome?.videoStream?.config?.streamUrl
                    && playCardHome?.videoStream?.isLive == b.playCardHome?.videoStream?.isLive
                    && playCardHome?.description == b.playCardHome?.description
                    && playCardHome?.moderatorName == b.playCardHome?.moderatorName
                    && playCardHome?.title == b.playCardHome?.title
                    && playCardHome?.totalView == b.playCardHome?.totalView
                    && playCardHome?.isShowTotalView == b.playCardHome?.isShowTotalView
        }
        return false
    }

    private var isCache: Boolean = false
    private var trackingData: Map<String, Any>? = null
    private var isCombined: Boolean = false
    private var trackingDataForCombination: List<Any> = emptyList()

    override fun isCache(): Boolean {
        return isCache
    }

    fun setCache(cache: Boolean) {
        isCache = cache
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
        return Bundle()
    }

    val enhanceClickPlayBanner: Map<String, Any>
        get()  {
            val list: List<Any> = convertPromoEnhancePlayBanner(playCardHome)
            return DataLayer.mapOf(
                    "event", "promoClick",
                    "eventCategory", "homepage-cmp",
                    "channelId", playCardHome?.channelId,
                    "eventAction", "click on play dynamic banner",
                    "eventLabel", "Play-CMP_OTHERS_${playCardHome?.slug} - ${playCardHome?.channelId} - ${getLiveOrVod(playCardHome?.videoStream?.isLive)}",
                    "campaignCode", channel.campaignCode,
                    "ecommerce", DataLayer.mapOf(
                    "promoClick", DataLayer.mapOf(
                    "promotions", DataLayer.listOf(
                    *list.toTypedArray()
            )
            )
            )
            )

    }


    fun getEnhanceImpressionPlayBanner(): Map<String, Any> {
        val list: List<Any> = convertPromoEnhancePlayBanner(playCardHome)
        return DataLayer.mapOf(
                "event", "promoView",
                "eventCategory", "homepage-cmp",
                "eventAction", "impression on play dynamic banner",
                "eventLabel", "Play-CMP_OTHERS_${playCardHome?.slug} - ${playCardHome?.channelId} - ${getLiveOrVod(playCardHome?.videoStream?.isLive)}",
                "ecommerce", DataLayer.mapOf(
                "promoView", DataLayer.mapOf(
                "promotions", DataLayer.listOf(
                *list.toTypedArray()
        )
        )
        )
        )
    }

    fun getEnhanceImpressionIrisPlayBanner(): Map<String, Any> {
        val list: List<Any> = convertPromoEnhancePlayBanner(playCardHome)
        return DataLayer.mapOf(
                "event", "promoViewIris" ,
                "eventCategory", "homepage-cmp",
                "eventAction", "impression on play dynamic banner",
                "channelId", playCardHome?.channelId,
                "eventLabel", "Play-CMP_OTHERS_${playCardHome?.slug} - ${playCardHome?.channelId} - ${getLiveOrVod(playCardHome?.videoStream?.isLive)}",
                "ecommerce", DataLayer.mapOf(
                "promoView", DataLayer.mapOf(
                "promotions", DataLayer.listOf(
                *list.toTypedArray()
        )
        )
        )
        )
    }

    private fun getLiveOrVod(isLive: Boolean?): String {
        return if(isLive == true) "live" else "vod"
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