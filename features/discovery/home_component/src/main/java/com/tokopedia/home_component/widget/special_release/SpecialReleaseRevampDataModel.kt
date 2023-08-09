package com.tokopedia.home_component.widget.special_release

import android.os.Bundle
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.visitable.HomeComponentVisitable

/**
 * Created by frenzel
 */
data class SpecialReleaseRevampDataModel(
        val channelModel: ChannelModel,
        val cardInteraction: Boolean,
        val isCache: Boolean = false
): HomeComponentVisitable {
    override fun visitableId(): String {
        return channelModel.id
    }

    override fun equalsWith(b: Any?): Boolean {
        return b == this
    }

    override fun getChangePayloadFrom(b: Any?): Bundle {
        return Bundle()
    }

    override fun type(typeFactory: HomeComponentTypeFactory): Int {
        return typeFactory.type(this)
    }
}
