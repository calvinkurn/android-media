package com.tokopedia.scp_rewards_widgets.cabinetHeader

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

class CabinetHeaderViewTypeFactory : BaseAdapterTypeFactory() {

    fun type(model: CabinetHeader) = CabinetHeaderViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            CabinetHeaderViewHolder.LAYOUT -> CabinetHeaderViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}
