package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.RichListWidgetUiModel

/**
 * Created by @ilhamsuaib on 12/04/23.
 */

class RichListViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<RichListWidgetUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.shc_rich_list_widget
    }

    override fun bind(element: RichListWidgetUiModel) {

    }

    interface Listener : BaseViewHolderListener
}