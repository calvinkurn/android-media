package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcRichListWidgetBinding
import com.tokopedia.sellerhomecommon.presentation.model.BaseRichListItem
import com.tokopedia.sellerhomecommon.presentation.model.RichListWidgetUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TooltipUiModel
import com.tokopedia.sellerhomecommon.presentation.view.adapter.RichListAdapter

/**
 * Created by @ilhamsuaib on 12/04/23.
 */

class RichListViewHolder(
    itemView: View, private val listener: Listener
) : AbstractViewHolder<RichListWidgetUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.shc_rich_list_widget
    }

    private var element: RichListWidgetUiModel? = null
    private val binding by lazy {
        ShcRichListWidgetBinding.bind(itemView)
    }
    private val richlistAdapter by lazy { RichListAdapter(getAdapterListener()) }
    private val layoutManager by lazy {
        object : LinearLayoutManager(itemView.context) {
            override fun canScrollVertically(): Boolean = false
        }
    }

    override fun bind(element: RichListWidgetUiModel) {
        this.element = element
        setupRecyclerView()
        val data = element.data
        when {
            null == data || element.showLoadingState -> showLoadingState()
            data.error.isNotBlank() -> {
                showErrorState()
                listener.setOnErrorWidget(absoluteAdapterPosition, element, data.error)
            }
            else -> showSuccessState(element)
        }
    }

    private fun setupRecyclerView() {
        with(binding) {
            if (rvShcRichList.layoutManager == null || rvShcRichList.adapter == null) {
                rvShcRichList.layoutManager = layoutManager
                rvShcRichList.adapter = richlistAdapter
            }
        }
    }

    private fun showSuccessState(element: RichListWidgetUiModel) {
        val items = element.data?.richListData.orEmpty()
        showItems(items)
        setupLastUpdated(element.data?.lastUpdated.orEmpty())
    }

    private fun setupLastUpdated(lastUpdated: String) {
        with(binding) {
            if (lastUpdated.isBlank()) {
                tvShcRichListUpdated.gone()
            } else {
                tvShcRichListUpdated.visible()
                tvShcRichListUpdated.text = lastUpdated
            }
        }
    }

    private fun showErrorState() {
        binding.tvShcRichListUpdated.gone()
        showItems(listOf(BaseRichListItem.ErrorStateUiModel))
    }

    private fun showLoadingState() {
        binding.tvShcRichListUpdated.gone()
        showItems(listOf(BaseRichListItem.LoadingStateUiModel))
    }

    private fun showItems(items: List<BaseRichListItem>) {
        if (richlistAdapter.data != items) {
            richlistAdapter.clearAllElements()
            richlistAdapter.addElement(items)
            richlistAdapter.notifyItemRangeChanged(Int.ZERO, items.size.minus(Int.ONE))
        }
    }

    private fun getAdapterListener(): RichListAdapter.Listener {
        return object : RichListAdapter.Listener {
            override fun setOnCtaClicked(appLink: String) {
                RouteManager.route(itemView.context, appLink)
            }

            override fun setOnTooltipClicked(tooltip: TooltipUiModel) {
                listener.onTooltipClicked(tooltip)
            }

            override fun setOnReloadClicked() {
                element?.let {
                    listener.onReloadWidget(it)
                }
            }
        }
    }

    interface Listener : BaseViewHolderListener
}