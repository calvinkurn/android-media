package com.tokopedia.home_component.widget.common

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.model.ChannelModel

/**
 * Created by frenzel
 */
abstract class AbstractHomeViewHolder<T: Visitable<*>>(itemView: View): AbstractViewHolder<T>(itemView) {

    abstract fun initAdapter()

    abstract fun setData(model: T)

    abstract fun setChannelDivider(channelModel: ChannelModel)

    abstract fun setHeaderComponent(channelModel: ChannelModel)
}
