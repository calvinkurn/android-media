package com.tokopedia.home_component.widget.lego3auto

import android.os.Bundle
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.visitable.HomeComponentVisitable

/**
 * Created by frenzel
 */
data class Lego3AutoModel(
        val channelModel: ChannelModel,
        val isCache: Boolean = false
): HomeComponentVisitable {
    override fun visitableId(): String {
        return channelModel.id
    }

    override fun equalsWith(b: Any?): Boolean {
        return b == this
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return null
    }

    override fun type(typeFactory: HomeComponentTypeFactory): Int {
        return typeFactory.type(this)
    }
}
