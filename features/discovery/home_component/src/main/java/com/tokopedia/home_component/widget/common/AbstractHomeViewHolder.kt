package com.tokopedia.home_component.widget.common

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component_header.model.ChannelHeader
import com.tokopedia.home_component_header.view.HomeChannelHeaderListener
import com.tokopedia.home_component_header.view.HomeChannelHeaderView
import com.tokopedia.unifycomponents.DividerUnify

/**
 * Created by frenzel
 */
abstract class AbstractHomeViewHolder<T: Visitable<*>>(itemView: View): AbstractViewHolder<T>(itemView) {

    abstract fun initAdapter()

    abstract fun setData(model: T)

    abstract fun setChannelDivider(channelModel: ChannelModel)

    abstract fun setHeaderComponent(channelModel: ChannelModel)
}
