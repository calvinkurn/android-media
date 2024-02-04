package com.tokopedia.home_component.visitable

import android.os.Bundle
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.ChannelModel

data class CouponWidgetDataModel(
    val channelModel: ChannelModel,
    val coupons: List<CouponWidgetDataItemModel>
) : HomeComponentVisitable {

    override fun visitableId() = channelModel.id

    override fun equalsWith(b: Any?) = b == this

    override fun getChangePayloadFrom(b: Any?) = Bundle()

    override fun type(typeFactory: HomeComponentTypeFactory): Int {
        return typeFactory.type(this)
    }
}
