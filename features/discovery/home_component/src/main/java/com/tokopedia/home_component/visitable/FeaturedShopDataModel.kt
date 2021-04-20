package com.tokopedia.home_component.visitable

import android.os.Bundle
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.ChannelModel

/**
 * Created by Lukas on 07/09/20.
 */

data class FeaturedShopDataModel(
        val channelModel: ChannelModel
) : HomeComponentVisitable {
    override fun visitableId(): String = channelModel.id

    override fun equalsWith(b: Any?): Boolean = b is FeaturedShopDataModel && b.channelModel.channelGrids == channelModel.channelGrids

    override fun getChangePayloadFrom(b: Any?): Bundle? = Bundle()

    override fun type(typeFactory: HomeComponentTypeFactory): Int = typeFactory.type(this)

}