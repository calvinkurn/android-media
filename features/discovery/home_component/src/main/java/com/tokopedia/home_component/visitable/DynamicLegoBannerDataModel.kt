package com.tokopedia.home_component.visitable

import android.os.Bundle
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.model.ImpressHolder

data class DynamicLegoBannerDataModel(
    val channelModel: ChannelModel,
    val isCache: Boolean = false,
    val cardInteraction: Boolean = false
) : HomeComponentVisitable, ImpressHolder() {
    override fun visitableId(): String? {
        return channelModel.id
    }

    override fun equalsWith(b: Any?): Boolean {
        return if (b is DynamicLegoBannerDataModel) {
            channelModel.channelConfig.createdTimeMillis == b.channelModel.channelConfig.createdTimeMillis &&
                channelModel.channelConfig.borderStyle == b.channelModel.channelConfig.borderStyle &&
                isCache == b.isCache
        } else {
            false
        }
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return Bundle()
    }

    override fun type(typeFactory: HomeComponentTypeFactory): Int {
        return typeFactory.type(this)
    }
}
