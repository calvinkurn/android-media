package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel

import android.os.Bundle
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.HomeTypeFactory
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel


/**
 * Created by mzennis on 14/10/20.
 */
data class CarouselPlayWidgetDataModel(
        val homeChannel: DynamicHomeChannel.Channels,
        val widgetUiModel: PlayWidgetUiModel = PlayWidgetUiModel.Placeholder
) : HomeVisitable {

    private val visitableId = "play_carousel"

    override fun setTrackingData(trackingData: MutableMap<String, Any>?) {
        TODO("Not yet implemented")
    }

    override fun getTrackingData(): MutableMap<String, Any> {
        TODO("Not yet implemented")
    }

    override fun getTrackingDataForCombination(): MutableList<Any> {
        TODO("Not yet implemented")
    }

    override fun setTrackingDataForCombination(`object`: MutableList<Any>?) {
        TODO("Not yet implemented")
    }

    override fun isTrackingCombined(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setTrackingCombined(isCombined: Boolean) {
        TODO("Not yet implemented")
    }

    override fun isCache(): Boolean {
        return false // todo implement caching
    }

    override fun visitableId(): String = visitableId

    /**
     * Diff util implementation, I guess
     */
    override fun equalsWith(obj: Any?): Boolean {
        if (obj is CarouselPlayWidgetDataModel) {
            return homeChannel.id == obj.homeChannel.id
                    && homeChannel.name == obj.homeChannel.name
                    && homeChannel.header.name == obj.homeChannel.header.name
                    && homeChannel.header.applink == obj.homeChannel.header.applink
                    && widgetUiModel == obj.widgetUiModel
        }
        return false
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return null // todo implement change payload form
    }

    override fun type(typeFactory: HomeTypeFactory?): Int {
        return 0
    }
}