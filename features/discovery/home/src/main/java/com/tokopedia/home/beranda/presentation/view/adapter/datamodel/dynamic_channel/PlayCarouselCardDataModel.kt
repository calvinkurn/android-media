package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel

import android.os.Bundle
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play_common.widget.playBannerCarousel.model.PlayBannerCarouselDataModel

data class PlayCarouselCardDataModel(
        val channel: DynamicHomeChannel.Channels? = null,
        val playBannerCarouselDataModel: PlayBannerCarouselDataModel = PlayBannerCarouselDataModel()
): HomeVisitable, ImpressHolder() {
    override fun visitableId(): String {
        return channel?.type ?: ""
    }

    override fun equalsWith(b: Any?): Boolean {
        if (b is PlayCarouselCardDataModel) {
            return channel?.id == b.channel?.id
                    && channel?.name == b.channel?.name
                    && channel?.header?.name == b.channel?.header?.name
                    && channel?.header?.applink == b.channel?.header?.applink
                    && playBannerCarouselDataModel == b.playBannerCarouselDataModel
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

}