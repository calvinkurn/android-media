package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.view.View
import androidx.core.view.ViewCompat
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.PenaltyFilterItemDecoration
import com.tokopedia.shop.score.databinding.ItemPenaltyFilterListBinding
import com.tokopedia.shop.score.penalty.presentation.adapter.FilterPenaltyBottomSheetListener
import com.tokopedia.shop.score.penalty.presentation.adapter.filter.FilterPenaltyAdapter.Companion.PAYLOAD_CHIPS_FILTER
import com.tokopedia.shop.score.penalty.presentation.adapter.filter.ItemChipsFilterPenaltyAdapter
import com.tokopedia.shop.score.penalty.presentation.model.PenaltyFilterUiModel
import com.tokopedia.utils.view.binding.viewBinding


class ItemPenaltyFilterBottomSheetViewHolder(
    view: View,
    private val penaltyBottomSheetListener: FilterPenaltyBottomSheetListener
) : AbstractViewHolder<PenaltyFilterUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_penalty_filter_list
    }

    private val binding: ItemPenaltyFilterListBinding? by viewBinding()

    private var itemChipsFilterPenaltyAdapter: ItemChipsFilterPenaltyAdapter? = null

    override fun bind(element: PenaltyFilterUiModel) {
        itemChipsFilterPenaltyAdapter =
            ItemChipsFilterPenaltyAdapter(penaltyBottomSheetListener, element.title)
        binding?.run {
            tvTitleHeaderPenaltyFilter.text = element.title
            dividerSomFilter.showWithCondition(element.isDividerVisible)
        }
        setupChipsAdapter(element)
    }

    override fun bind(element: PenaltyFilterUiModel?, payloads: MutableList<Any>) {
        if (element == null || payloads.isNullOrEmpty()) return

        when (payloads.getOrNull(0) as? Int) {
            PAYLOAD_CHIPS_FILTER -> setupChipsAdapter(element)
        }
    }

    private fun setupChipsAdapter(data: PenaltyFilterUiModel) {
        binding?.run {
            val layoutManagerChips = ChipsLayoutManager.newBuilder(root.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()
            rvPenaltyFilter.let {
                if (it.itemDecorationCount.isZero()) {
                    it.addItemDecoration(PenaltyFilterItemDecoration())
                }
                it.layoutManager = layoutManagerChips
                ViewCompat.setLayoutDirection(it, ViewCompat.LAYOUT_DIRECTION_LTR)
                it.adapter = itemChipsFilterPenaltyAdapter
                itemChipsFilterPenaltyAdapter?.setItemChipsFilterPenaltyList(data.chipsFilerList)
            }
        }
    }
}