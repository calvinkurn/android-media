package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel

import android.os.Bundle
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory

/**
 * Created by henrypriyono on 31/01/18.
 */

class DynamicChannelViewModel : HomeVisitable {

    var channel: DynamicHomeChannel.Channels? = null

    var serverTimeOffset: Long = 0
    private var isCache: Boolean = false
    private var trackingData: Map<String, Any>? = null
    private var trackingDataForCombination: List<Any>? = null
    private var isCombined: Boolean = false

    companion object {
        val HOME_RV_BANNER_IMAGE_URL = "home_rv_banner_image_url"
        val HOME_RV_SPRINT_BG_IMAGE_URL = "home_rv_sprint_bg_image_url"
    }

    override fun equalsWith(b: Any?): Boolean {
        if (b is DynamicChannelViewModel) {
            if (channel?.grids?.size != b.channel?.grids?.size?:0) return false
            channel?.grids?.let {
                it.forEachIndexed() {position, grid->
                    b.channel?.grids?.let {newGrid->
                        if (grid.imageUrl != newGrid[position].imageUrl) return false
                    }
                }
                return true
            }
        }
        return false
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        val bundle = Bundle()
        if (b is DynamicChannelViewModel) {
            if (channel?.banner?.imageUrl != b.channel?.banner?.imageUrl?:"") {
                bundle.putString(HOME_RV_BANNER_IMAGE_URL, b.channel?.banner?.imageUrl)
            }

            if (channel?.header?.backImage != b.channel?.header?.backImage?:"") {
                bundle.putString(HOME_RV_SPRINT_BG_IMAGE_URL, b.channel?.header?.backImage)
            }
        }
        return bundle
    }

    override fun isCache(): Boolean {
        return isCache
    }

    override fun visitableId(): String {
        return "dcSection"
    }

    fun setCache(cache: Boolean) {
        isCache = cache
    }

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun setTrackingData(trackingData: Map<String, Any>?) {
        this.trackingData = trackingData
    }

    override fun getTrackingData(): Map<String, Any>? {
        return trackingData
    }

    override fun getTrackingDataForCombination(): List<Any>? {
        return trackingDataForCombination
    }

    override fun setTrackingDataForCombination(trackingDataForCombination: List<Any>?) {
        this.trackingDataForCombination = trackingDataForCombination
    }

    override fun isTrackingCombined(): Boolean {
        return isCombined
    }

    override fun setTrackingCombined(isCombined: Boolean) {
        this.isCombined = isCombined
    }
}
