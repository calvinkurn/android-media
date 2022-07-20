package com.tokopedia.sellerorder.detail.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerorder.detail.presentation.adapter.viewholder.SomDetailAddOnViewHolder
import com.tokopedia.sellerorder.detail.presentation.model.AddOnUiModel

class AddOnAdapterFactory(
    private val somDetailAddOnListener: SomDetailAddOnViewHolder.Listener
): BaseAdapterTypeFactory() {
    fun type(addOnUiModel: AddOnUiModel): Int = SomDetailAddOnViewHolder.RES_LAYOUT

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            SomDetailAddOnViewHolder.RES_LAYOUT -> SomDetailAddOnViewHolder(parent, somDetailAddOnListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}