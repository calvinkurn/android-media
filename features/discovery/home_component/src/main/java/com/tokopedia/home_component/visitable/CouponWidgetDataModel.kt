package com.tokopedia.home_component.visitable

import android.os.Bundle
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component_header.model.ChannelHeader

data class CouponWidgetDataModel(
    val channelModel: ChannelModel,
    val backgroundGradientColor: ArrayList<String>,
    val coupons: List<CouponWidgetDataItemModel>
) : HomeComponentVisitable {

    override fun visitableId() = channelModel.id

    override fun equalsWith(b: Any?) = b == this

    override fun getChangePayloadFrom(b: Any?) = Bundle()

    override fun type(typeFactory: HomeComponentTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun header(): ChannelHeader {
        return ChannelHeader(
            name = channelModel.channelHeader.name
        )
    }
}
