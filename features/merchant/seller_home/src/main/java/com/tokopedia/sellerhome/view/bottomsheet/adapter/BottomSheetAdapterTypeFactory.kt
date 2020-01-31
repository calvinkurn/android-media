package com.tokopedia.sellerhome.view.bottomsheet.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.view.bottomsheet.model.BottomSheetContentUiModel
import com.tokopedia.sellerhome.view.bottomsheet.model.BottomSheetListItemUiModel
import com.tokopedia.sellerhome.view.bottomsheet.viewholder.BottomSheetContentViewHolder
import com.tokopedia.sellerhome.view.bottomsheet.viewholder.BottomSheetListItemViewHolder

class BottomSheetAdapterTypeFactory : BaseAdapterTypeFactory() {
    fun type(content: BottomSheetContentUiModel): Int {
        return BottomSheetContentViewHolder.RES_LAYOUT
    }

    fun type(listItem: BottomSheetListItemUiModel): Int {
        return BottomSheetListItemViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            BottomSheetContentViewHolder.RES_LAYOUT -> BottomSheetContentViewHolder(parent)
            BottomSheetListItemViewHolder.RES_LAYOUT -> BottomSheetListItemViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}