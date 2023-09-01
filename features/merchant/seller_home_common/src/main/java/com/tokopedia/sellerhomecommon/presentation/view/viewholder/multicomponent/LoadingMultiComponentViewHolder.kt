package com.tokopedia.sellerhomecommon.presentation.view.viewholder.multicomponent

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.multicomponent.LoadingMultiComponentUiModel

class LoadingMultiComponentViewHolder(itemView: View): AbstractViewHolder<LoadingMultiComponentUiModel>(itemView) {

    override fun bind(element: LoadingMultiComponentUiModel?) {}

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_multi_component_item_loading
    }

}
