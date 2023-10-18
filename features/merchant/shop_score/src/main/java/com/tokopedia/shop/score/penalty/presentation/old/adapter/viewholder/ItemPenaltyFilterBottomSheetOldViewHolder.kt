package com.tokopedia.shop.score.penalty.presentation.old.adapter.viewholder

import android.view.View
import androidx.core.view.ViewCompat
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.PenaltyFilterItemDecoration
import com.tokopedia.shop.score.databinding.ItemPenaltyFilterListOldBinding
import com.tokopedia.shop.score.penalty.presentation.adapter.FilterPenaltyBottomSheetListener
import com.tokopedia.shop.score.penalty.presentation.adapter.filter.ItemChipsFilterPenaltyAdapter
import com.tokopedia.shop.score.penalty.presentation.old.adapter.filter.FilterPenaltyAdapterOld.Companion.PAYLOAD_CHIPS_FILTER
import com.tokopedia.shop.score.penalty.presentation.old.model.PenaltyFilterUiModelOld
import com.tokopedia.utils.view.binding.viewBinding


class ItemPenaltyFilterBottomSheetOldViewHolder(
    view: View,
    private val penaltyBottomSheetListener: FilterPenaltyBottomSheetListener
) : AbstractViewHolder<PenaltyFilterUiModelOld>(view) {

    companion object {
        val LAYOUT = R.layout.item_penalty_filter_list_old
    }

    private val binding: ItemPenaltyFilterListOldBinding? by viewBinding()

    private var itemChipsFilterPenaltyAdapter: ItemChipsFilterPenaltyAdapter? = null

    override fun bind(element: PenaltyFilterUiModelOld) {
        itemChipsFilterPenaltyAdapter =
            ItemChipsFilterPenaltyAdapter(penaltyBottomSheetListener, element.title)
        binding?.run {
            tvTitleHeaderPenaltyFilter.text = element.title
            dividerSomFilter.showWithCondition(element.isDividerVisible)
        }
        setupChipsAdapter(element)
    }

    override fun bind(element: PenaltyFilterUiModelOld?, payloads: MutableList<Any>) {
        if (element == null || payloads.isNullOrEmpty()) return

        when (payloads.getOrNull(0) as? Int) {
            PAYLOAD_CHIPS_FILTER -> setupChipsAdapter(element)
        }
    }

    private fun setupChipsAdapter(data: PenaltyFilterUiModelOld) {
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
                itemChipsFilterPenaltyAdapter?.setItemChipsFilterPenaltyList(data.chipsFilterList)
            }
        }
    }
}
