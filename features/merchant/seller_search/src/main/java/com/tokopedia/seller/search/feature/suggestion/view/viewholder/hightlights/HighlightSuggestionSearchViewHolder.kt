package com.tokopedia.seller.search.feature.suggestion.view.viewholder.hightlights

import android.view.View
import androidx.core.view.ViewCompat
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller.search.R
import com.tokopedia.seller.search.common.util.HighlightItemDecoration
import com.tokopedia.seller.search.feature.initialsearch.view.viewholder.HighlightSuggestionSearchListener
import com.tokopedia.seller.search.feature.suggestion.view.model.sellersearch.hightlights.HighlightSuggestionSearchUiModel
import kotlinx.android.synthetic.main.item_highlight_search_list.view.*

class HighlightSuggestionSearchViewHolder(view: View, private val searchListener: HighlightSuggestionSearchListener):
        AbstractViewHolder<HighlightSuggestionSearchUiModel>(view)  {

    companion object {
        val LAYOUT = R.layout.item_highlight_search_list
    }

    private var highLightInitialListAdapter: ItemHighLightSuggestionChipsAdapter? = null

    override fun bind(element: HighlightSuggestionSearchUiModel?) {
        highLightInitialListAdapter = ItemHighLightSuggestionChipsAdapter(searchListener)
        setupChipsHighlightAdapter(element)
    }

    private fun setupChipsHighlightAdapter(data: HighlightSuggestionSearchUiModel?) {
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
                it.adapter = highLightInitialListAdapter
                data?.highlightSuggestionSearch?.let { highLightList -> highLightInitialListAdapter?.setChipsHighlight(highLightList) }
            }
        }
    }
}