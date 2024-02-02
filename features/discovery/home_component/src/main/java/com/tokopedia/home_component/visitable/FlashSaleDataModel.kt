package com.tokopedia.home_component.visitable

import android.os.Bundle
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.ChannelModel

/**
 * Created by frenzel
 */
data class FlashSaleDataModel(
        val channelModel: ChannelModel,
        val cardInteraction: Boolean,
        val isCache: Boolean = false
): HomeComponentVisitable, HasChannelModel {

    override val model: ChannelModel
        get() = channelModel

    override fun visitableId(): String? {
        return channelModel.id
    }

    override fun equalsWith(b: Any?): Boolean {
        return b == this
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return Bundle()
    }

    override fun type(typeFactory: HomeComponentTypeFactory): Int {
        return typeFactory.type(this)
    }
}
