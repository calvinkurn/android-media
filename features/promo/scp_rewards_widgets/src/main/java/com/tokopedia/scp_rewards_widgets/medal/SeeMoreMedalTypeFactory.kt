package com.tokopedia.scp_rewards_widgets.medal

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.scp_rewards_widgets.common.model.LoadingModel
import com.tokopedia.scp_rewards_widgets.common.model.LoadingMoreModel
import com.tokopedia.scp_rewards_widgets.common.viewholder.LoadingMoreViewHolder
import com.tokopedia.scp_rewards_widgets.common.viewholder.LoadingViewHolder

class SeeMoreMedalTypeFactory(listener: MedalCallbackListener) : MedalViewTypeFactory(listener) {

    fun type(model: LoadingModel) = LoadingViewHolder.LAYOUT

    fun type(model: LoadingMoreModel) = LoadingMoreViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            LoadingViewHolder.LAYOUT -> LoadingViewHolder(parent)
            LoadingMoreViewHolder.LAYOUT -> LoadingMoreViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}
