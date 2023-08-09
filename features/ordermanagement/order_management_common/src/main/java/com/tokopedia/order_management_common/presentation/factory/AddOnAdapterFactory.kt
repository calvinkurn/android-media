package com.tokopedia.order_management_common.presentation.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.order_management_common.presentation.uimodel.AddOnSummaryUiModel
import com.tokopedia.order_management_common.presentation.viewholder.BmgmAddOnViewHolder

class AddOnAdapterFactory(
    private val somDetailAddOnListener: BmgmAddOnViewHolder.Listener
): BaseAdapterTypeFactory() {

    fun type(addOnUiModel: AddOnSummaryUiModel.AddonItemUiModel): Int = BmgmAddOnViewHolder.RES_LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            BmgmAddOnViewHolder.RES_LAYOUT -> BmgmAddOnViewHolder(parent, somDetailAddOnListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}
