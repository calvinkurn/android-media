package com.tokopedia.home_component.visitable

import android.os.Bundle
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.ChannelModel

/**
 * Create by Firman on 5/4/22
 */
data class CategoryWidgetDataModel(
    val channelModel: ChannelModel,
    val isCache: Boolean = false
) : HomeComponentVisitable {

    override fun visitableId(): String? {
        return channelModel?.id
    }

    override fun equalsWith(b: Any?): Boolean {
        return b is CategoryWidgetDataModel && b.channelModel == channelModel
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return Bundle()
    }

    override fun type(typeFactory: HomeComponentTypeFactory): Int {
        return typeFactory.type(this)
    }
}
