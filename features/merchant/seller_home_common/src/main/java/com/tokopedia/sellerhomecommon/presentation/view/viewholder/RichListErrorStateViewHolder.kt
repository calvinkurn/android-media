package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcRichListWidgetErrorBinding
import com.tokopedia.sellerhomecommon.presentation.model.BaseRichListItem

/**
 * Created by @ilhamsuaib on 06/05/23.
 */

class RichListErrorStateViewHolder(
    itemView: View,
    private val onReloadClicked: () -> Unit = {}
) : AbstractViewHolder<BaseRichListItem.ErrorStateUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_rich_list_widget_error
    }

    private val binding by lazy {
        ShcRichListWidgetErrorBinding.bind(itemView)
    }

    override fun bind(element: BaseRichListItem.ErrorStateUiModel) {
        binding.shcRichListErrorStateView.setOnReloadClicked(onReloadClicked)
    }
}