package com.tokopedia.home_component.visitable

import android.os.Bundle
import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.home_component.model.ChannelModel

/**
 * Created by Lukas on 07/09/20.
 */

data class FeaturedShopDataModel(
        val channelModel: ChannelModel,
        val state: Int = STATE_LOADING
) : HomeComponentVisitable {
    override fun visitableId(): String = String.format("%s%s", state.hashCode(),channelModel.hashCode())

    override fun equalsWith(b: Any?): Boolean = isEqualWith(b)

    override fun getChangePayloadFrom(b: Any?): Bundle? = Bundle()

    override fun type(typeFactory: HomeComponentTypeFactory): Int = typeFactory.type(this)


    private fun isEqualWith(newData: Any?): Boolean {
        if (newData !is FeaturedShopDataModel)
            return false
        if (state == newData.state) {
            if (areGridsSame(newData.channelModel)) return true
        } else
            return false
        return false
    }

    private fun areGridsSame(newModel: ChannelModel): Boolean {
        return newModel.channelGrids.size == channelModel.channelGrids.size && newModel.channelGrids == channelModel.channelGrids
    }

    companion object {
        const val STATE_LOADING = 0
        const val STATE_READY = 1
        const val STATE_FAILED = 2
    }
}