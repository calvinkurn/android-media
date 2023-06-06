package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel

import android.os.Bundle
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.play.widget.ui.PlayWidgetState

/**
 * Created by mzennis on 14/10/20.
 */
data class CarouselPlayWidgetDataModel(
        val homeChannel: ChannelModel,
        val widgetState: PlayWidgetState = PlayWidgetState(isLoading = true)
) : HomeVisitable {

    private val visitableId = "play_carousel"

    override fun setTrackingData(trackingData: MutableMap<String, Any>?) {
    }

    override fun getTrackingData(): MutableMap<String, Any> {
        return mutableMapOf()
    }

    override fun getTrackingDataForCombination(): MutableList<Any> {
        return mutableListOf()
    }

    override fun setTrackingDataForCombination(`object`: MutableList<Any>?) {
    }

    override fun isTrackingCombined(): Boolean {
        return false
    }

    override fun setTrackingCombined(isCombined: Boolean) {
    }

    override fun isCache(): Boolean {
        return false
    }

    override fun visitableId(): String = visitableId

    override fun equalsWith(obj: Any?): Boolean {
        if (obj is CarouselPlayWidgetDataModel) {
            return homeChannel.id == obj.homeChannel.id
                    && homeChannel.name == obj.homeChannel.name
                    && homeChannel.channelHeader.name == obj.homeChannel.channelHeader.name
                    && homeChannel.channelHeader.applink == obj.homeChannel.channelHeader.applink
                    && widgetState == obj.widgetState
        }
        return false
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return null
    }

    override fun type(typeFactory: HomeTypeFactory?): Int {
        return typeFactory?.type(this).orZero()
    }
}
