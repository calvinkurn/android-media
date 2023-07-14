package com.tokopedia.home_component.visitable

import android.os.Bundle
import com.tokopedia.analytics.performance.perf.BlocksLoadableComponent
import com.tokopedia.analytics.performance.perf.LoadableComponent
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.ChannelModel

data class MixLeftDataModel(
        val channelModel: ChannelModel,
        val isCache: Boolean = false
): HomeComponentVisitable,  LoadableComponent by BlocksLoadableComponent(
    { channelModel.channelGrids.size > 3 },
    "HomeMixLeft"
) {
    override fun visitableId(): String? {
        return channelModel.id
    }

    override fun equalsWith(b: Any?): Boolean {
        return if (b is MixLeftDataModel) {
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
