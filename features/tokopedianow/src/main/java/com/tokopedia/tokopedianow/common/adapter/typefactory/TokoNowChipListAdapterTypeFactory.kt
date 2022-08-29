package com.tokopedia.tokopedianow.common.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.common.model.TokoNowChipUiModel
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChipViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChipViewHolder.ChipListener

class TokoNowChipListAdapterTypeFactory(
    private val chipListener: ChipListener
) : BaseAdapterTypeFactory(), TokoNowChipTypeFactory {

    override fun type(uiModel: TokoNowChipUiModel): Int = TokoNowChipViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            TokoNowChipViewHolder.LAYOUT -> TokoNowChipViewHolder(parent, chipListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}