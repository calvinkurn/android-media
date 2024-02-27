package com.tokopedia.order_management_common.presentation.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel
import com.tokopedia.order_management_common.presentation.viewholder.AddOnViewHolder

class AddOnAdapterFactory(
    private val bmgmAddOnListener: AddOnViewHolder.Listener
): BaseAdapterTypeFactory() {

    fun type(addOnUiModel: AddOnSummaryUiModel.AddonItemUiModel): Int = AddOnViewHolder.RES_LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            AddOnViewHolder.RES_LAYOUT -> AddOnViewHolder(parent, bmgmAddOnListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}
