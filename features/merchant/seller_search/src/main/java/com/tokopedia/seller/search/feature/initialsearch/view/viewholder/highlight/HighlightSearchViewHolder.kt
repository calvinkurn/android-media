package com.tokopedia.seller.search.feature.initialsearch.view.viewholder.highlight

import android.view.View
import androidx.core.view.ViewCompat
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.util.HighlightItemDecoration
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.HighlightSearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.HistorySearchListener
import kotlinx.android.synthetic.main.item_highlight_search_list.view.*

class HighlightSearchViewHolder(view: View, private val searchListener: HistorySearchListener):
        AbstractViewHolder<HighlightSearchUiModel>(view)  {

    companion object {
        val LAYOUT = R.layout.item_highlight_search_list
    }

    private var highLightListAdapter: ItemHighLightChipsAdapter? = null

    override fun bind(element: HighlightSearchUiModel?) {
        highLightListAdapter = ItemHighLightChipsAdapter(searchListener)
        setupChipsHighlightAdapter(element)
    }

    private fun setupChipsHighlightAdapter(data: HighlightSearchUiModel?) {
        with(itemView) {
            val layoutManagerChips = ChipsLayoutManager.newBuilder(context)
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                    .build()
            rvChipsHighlight?.also {
                if (it.itemDecorationCount == 0) {
                    it.addItemDecoration(HighlightItemDecoration())
                }
                it.layoutManager = layoutManagerChips
                ViewCompat.setLayoutDirection(it, ViewCompat.LAYOUT_DIRECTION_LTR)
                it.adapter = highLightListAdapter
                data?.highlightList?.let { highLightList -> highLightListAdapter?.setChipsHighlight(highLightList) }
            }
        }
    }
}