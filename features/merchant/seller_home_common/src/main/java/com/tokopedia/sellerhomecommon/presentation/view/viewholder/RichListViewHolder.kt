package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcRichListWidgetBinding
import com.tokopedia.sellerhomecommon.presentation.model.BaseRichListItemUiModel
import com.tokopedia.sellerhomecommon.presentation.model.RichListWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.view.adapter.RichListAdapter

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

    private val binding by lazy {
        ShcRichListWidgetBinding.bind(itemView)
    }
    private val richlistAdapter by lazy { RichListAdapter() }
    private val layoutManager by lazy {
        object : LinearLayoutManager(itemView.context) {
            override fun canScrollVertically(): Boolean = false
        }
    }

    override fun bind(element: RichListWidgetUiModel) {
        val data = element.data
        when {
            null == data || element.isLoading -> showLoadingState()
            data.error.isNotBlank() -> {
                showErrorState(element)
                listener.setOnErrorWidget(absoluteAdapterPosition, element, data.error)
            }
            else -> showSuccessState(element)
        }
    }

    private fun showSuccessState(element: RichListWidgetUiModel) {
        setupRichListItem(element.data?.richListData.orEmpty())
    }

    private fun setupRichListItem(items: List<BaseRichListItemUiModel>) {
        with(binding) {
            if (rvShcRichList.layoutManager == null || rvShcRichList.adapter == null) {
                rvShcRichList.layoutManager = layoutManager
                rvShcRichList.adapter = richlistAdapter
            }
            if (richlistAdapter.data != items) {
                richlistAdapter.clearAllElements()
                richlistAdapter.addElement(items)
                richlistAdapter.notifyItemRangeChanged(Int.ZERO, items.size.minus(Int.ONE))
            }
        }
    }

    private fun showErrorState(element: RichListWidgetUiModel) {

    }

    private fun showLoadingState() {

    }

    interface Listener : BaseViewHolderListener
}