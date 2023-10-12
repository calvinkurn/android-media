package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.BaseRichListItem

/**
 * Created by @ilhamsuaib on 06/05/23.
 */

class RichListLoadingStateViewHolder(
    itemView: View
) : AbstractViewHolder<BaseRichListItem.LoadingStateUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_rich_list_widget_loading
    }

    override fun bind(element: BaseRichListItem.LoadingStateUiModel) {}
}