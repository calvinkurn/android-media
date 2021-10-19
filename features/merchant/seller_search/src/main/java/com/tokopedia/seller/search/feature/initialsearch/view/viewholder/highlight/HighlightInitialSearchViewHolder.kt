package com.tokopedia.seller.search.feature.initialsearch.view.viewholder.highlight

import android.view.View
import androidx.core.view.ViewCompat
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.util.HighlightItemDecoration
import com.tokopedia.seller.search.databinding.ItemHighlightSearchListBinding
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.HighlightInitialSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.HistorySearchListener
import com.tokopedia.utils.view.binding.viewBinding

class HighlightInitialSearchViewHolder(
    view: View,
    private val searchListener: HistorySearchListener
) : AbstractViewHolder<HighlightInitialSearchUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_highlight_search_list
    }

    private val binding: ItemHighlightSearchListBinding? by viewBinding()

    private var highLightInitialListAdapter: ItemHighLightInitialChipsAdapter? = null

    override fun bind(element: HighlightInitialSearchUiModel?) {
        highLightInitialListAdapter = ItemHighLightInitialChipsAdapter(searchListener)
        setupChipsHighlightAdapter(element)
    }

    private fun setupChipsHighlightAdapter(data: HighlightInitialSearchUiModel?) {
        binding?.run {
            val layoutManagerChips = ChipsLayoutManager.newBuilder(root.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()
            rvChipsHighlight.let {
                if (it.itemDecorationCount.isZero()) {
                    it.addItemDecoration(HighlightItemDecoration())
                }
                it.layoutManager = layoutManagerChips
                ViewCompat.setLayoutDirection(it, ViewCompat.LAYOUT_DIRECTION_LTR)
                it.adapter = highLightInitialListAdapter
                data?.highlightInitialList?.let { highLightList ->
                    highLightInitialListAdapter?.setChipsHighlight(
                        highLightList
                    )
                }
            }
        }
    }
}