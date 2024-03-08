package com.tokopedia.home_component.visitable

import android.os.Bundle
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.ChannelModel

data class OrigamiSDUIDataModel(
    val origamiData: String,
    val visitableID: String,
    val channel: CampaignWidgetDataModel? = null,
    val channelModel : ChannelModel? = null
) : HomeComponentVisitable {
    override fun visitableId(): String? {
        return visitableID
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
