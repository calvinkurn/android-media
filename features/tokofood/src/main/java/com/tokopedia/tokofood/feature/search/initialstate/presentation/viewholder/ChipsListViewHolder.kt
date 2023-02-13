package com.tokopedia.tokofood.feature.search.initialstate.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.presentation.listener.TokofoodScrollChangedListener
import com.tokopedia.tokofood.common.presentation.viewholder.CustomPayloadViewHolder
import com.tokopedia.tokofood.databinding.ChipsListInitialStatePopularSearchBinding
import com.tokopedia.tokofood.feature.search.initialstate.presentation.adapter.ChipsItemAdapter
import com.tokopedia.tokofood.feature.search.initialstate.presentation.itemdecoration.ChipsItemDecoration
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.ChipsListUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.ChipsPopularSearch

class ChipsListViewHolder(
    view: View,
    private val chipsItemListener: ChipsItemAdapter.ChipsItemListener,
    private val tokofoodScrollChangedListener: TokofoodScrollChangedListener
) : CustomPayloadViewHolder<ChipsListUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.chips_list_initial_state_popular_search
    }

    private val binding = ChipsListInitialStatePopularSearchBinding.bind(itemView)

    private var chipsItemAdapter: ChipsItemAdapter? = null

    override fun bind(element: ChipsListUiModel) {
        chipsItemAdapter = ChipsItemAdapter(chipsItemListener, tokofoodScrollChangedListener)
        setupChipsList(element)
    }

    override fun bindPayload(payloads: Pair<*, *>?) {
        super.bindPayload(payloads)
        payloads?.let { (oldItem, newItem) ->
            if (oldItem is ChipsListUiModel && newItem is ChipsListUiModel) {
                if (oldItem.chipsPopularSearchList != newItem.chipsPopularSearchList) {
                    setChipsList(newItem.chipsPopularSearchList)
                }
            }
        }
    }

    private fun setupChipsList(element: ChipsListUiModel) {
        with(binding.rvChipsPopularSearch) {
            if (itemDecorationCount.isZero()) {
                addItemDecoration(ChipsItemDecoration())
            }
            layoutManager = getLayoutManagerChips()
            ViewCompat.setLayoutDirection(this, ViewCompat.LAYOUT_DIRECTION_LTR)
            adapter = chipsItemAdapter
        }
        setChipsList(element.chipsPopularSearchList)
    }

    private fun setChipsList(chipsPopularSearch: List<ChipsPopularSearch>) {
        chipsItemAdapter?.setChipsPopularSearch(chipsPopularSearch)
    }

    private fun getLayoutManagerChips(): ChipsLayoutManager {
        return ChipsLayoutManager.newBuilder(itemView.context)
            .setOrientation(ChipsLayoutManager.HORIZONTAL)
            .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
            .build()
    }
}
