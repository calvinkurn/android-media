package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel

import android.os.Bundle
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory

/**
 * Created by henrypriyono on 31/01/18.
 */

class DynamicChannelDataModel : HomeVisitable {

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
        if (b is DynamicChannelDataModel) {
            if (isExpiredTimeChanged(b)) return false
            if (isGridSizeChanged(b)) return false
            if (isLayoutChanged(b)) return false
            channel?.grids?.let {
                it.forEachIndexed {position, grid->
                    b.channel?.grids?.let {newGrid->
                        if (grid.imageUrl != newGrid[position].imageUrl) return false
                    }
                }
                return  channel?.layout == b.channel?.layout
                        &&channel?.header == b.channel?.header
                        && channel?.banner == b.channel?.banner
            }
        }
        return false
    }

    private fun isLayoutChanged(b: DynamicChannelDataModel) =
            channel?.layout ?: "" != b.channel?.layout ?: ""

    private fun isGridSizeChanged(b: DynamicChannelDataModel) =
            channel?.grids?.size != b.channel?.grids?.size ?: 0

    private fun isExpiredTimeChanged(b: DynamicChannelDataModel): Boolean {
        if (channel?.header?.expiredTime?.isNotEmpty() == true && channel?.header?.expiredTime != b.channel?.header?.expiredTime)
            return true
        return false
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        val bundle = Bundle()
        if (b is DynamicChannelDataModel) {
            if (isDcMixType(b) && isBannerImageSame(b)) {
                bundle.putString(HOME_RV_BANNER_IMAGE_URL, b.channel?.banner?.imageUrl)
            }

            if (isSprintType(b) && isSprintBackImageSame(b)) {
                bundle.putString(HOME_RV_SPRINT_BG_IMAGE_URL, b.channel?.header?.backImage)
            }
        }
        return bundle
    }

    private fun isSprintType(b: DynamicChannelDataModel): Boolean {
        return b.channel?.layout == DynamicHomeChannel.Channels.LAYOUT_SPRINT ||
                b.channel?.layout == DynamicHomeChannel.Channels.LAYOUT_SPRINT_LEGO
    }

    private fun isSprintBackImageSame(b: DynamicChannelDataModel) =
            channel?.header?.backImage != b.channel?.header?.backImage ?: ""

    private fun isBannerImageSame(b: DynamicChannelDataModel) =
            channel?.banner?.imageUrl != b.channel?.banner?.imageUrl ?: ""

    private fun isDcMixType(dynamicChannelDataModel: DynamicChannelDataModel): Boolean {
        return dynamicChannelDataModel.channel?.layout == DynamicHomeChannel.Channels.LAYOUT_BANNER_CAROUSEL ||
                dynamicChannelDataModel.channel?.layout == DynamicHomeChannel.Channels.LAYOUT_BANNER_ORGANIC
    }

    override fun isCache(): Boolean {
        return isCache
    }

    override fun visitableId(): String {
        return channel?.id?:""
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
