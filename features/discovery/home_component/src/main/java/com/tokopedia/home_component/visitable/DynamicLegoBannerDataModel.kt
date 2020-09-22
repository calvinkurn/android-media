package com.tokopedia.home_component.visitable

import android.os.Bundle
import android.util.Log
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.ChannelModel

data class DynamicLegoBannerDataModel(
        val channelModel: ChannelModel
): HomeComponentVisitable {
    override fun visitableId(): String? {
        return channelModel.id
    }

    override fun equalsWith(b: Any?): Boolean {
        return if (b is DynamicLegoBannerDataModel) {
            Log.d("DevaraFikryChannel", "msg"+(channelModel.channelConfig.createdTimeMillis == b.channelModel.channelConfig.createdTimeMillis))
            channelModel.channelConfig.createdTimeMillis == b.channelModel.channelConfig.createdTimeMillis
        } else false
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return Bundle()
    }

    override fun type(typeFactory: HomeComponentTypeFactory): Int {
        return typeFactory.type(this)
    }
}