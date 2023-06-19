package com.tokopedia.scp_rewards_widgets.medal

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

class MedalViewTypeFactory(private val listener: MedalClickListener) : BaseAdapterTypeFactory() {

    fun type(model: MedalData) = MedalSectionViewHolder.LAYOUT

    fun type(model: MedalItem) = MedalViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            MedalSectionViewHolder.LAYOUT -> MedalSectionViewHolder(parent, listener)
            MedalViewHolder.LAYOUT -> MedalViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }
}
