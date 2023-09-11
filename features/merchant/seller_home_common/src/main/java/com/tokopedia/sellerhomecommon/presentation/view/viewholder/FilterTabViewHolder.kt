package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcFilterTabWidgetBinding
import com.tokopedia.sellerhomecommon.domain.model.TabModel
import com.tokopedia.sellerhomecommon.presentation.model.FilterTabWidgetUiModel

class FilterTabViewHolder(
    view: View?,
    private val listener: Listener
): AbstractViewHolder<FilterTabWidgetUiModel>(view) {

    private val binding by lazy { ShcFilterTabWidgetBinding.bind(itemView) }

    override fun bind(element: FilterTabWidgetUiModel) {
        with(binding) {
            val selectedTab = element.filterTabs?.firstOrNull { it.page == element.selectedFilterPage }
            selectedTab?.let { tab ->
                tvFilterTabTitle.text = tab.tabTitle
                tvFilterTabDesc.text = tab.tabName

                root.setOnClickListener {
                    listener.onFilterClicked(
                        element.filterTabs,
                        tab.page
                    )
                }
            }
        }
    }

    interface Listener: BaseViewHolderListener {
        fun onFilterClicked(
            tabs: List<TabModel>?,
            selectedPage: String?
        )
    }

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.shc_filter_tab_widget
    }

}
