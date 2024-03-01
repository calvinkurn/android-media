package com.tokopedia.home_component.visitable

import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component_header.model.ChannelHeader
import com.tokopedia.kotlin.model.ImpressHolder

data class CouponWidgetDataModel(
    val channelModel: ChannelModel,
    val backgroundImageUrl: String,
    val backgroundGradientColor: ArrayList<String>,
    val coupons: List<CouponWidgetDataItemModel>
) : HomeComponentVisitable, ImpressHolder() {

    override fun visitableId() = channelModel.id

    override fun equalsWith(b: Any?) = b == this

    override fun getChangePayloadFrom(b: Any?) = null

    override fun type(typeFactory: HomeComponentTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun header(): ChannelHeader {
        return ChannelHeader(
            name = channelModel.channelHeader.name
        )
    }
}
