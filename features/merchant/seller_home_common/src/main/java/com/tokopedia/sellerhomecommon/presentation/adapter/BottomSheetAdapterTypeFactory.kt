package com.tokopedia.sellerhomecommon.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.presentation.model.BottomSheetContentUiModel
import com.tokopedia.sellerhomecommon.presentation.model.BottomSheetListItemUiModel
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.BottomSheetContentViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.BottomSheetListItemViewHolder

/**
 * Created By @ilhamsuaib on 27/05/20
 */

class BottomSheetAdapterTypeFactory : BaseAdapterTypeFactory() {

    fun type(model: BottomSheetContentUiModel): Int {
        return BottomSheetContentViewHolder.RES_LAYOUT
    }

    fun type(model: BottomSheetListItemUiModel): Int {
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