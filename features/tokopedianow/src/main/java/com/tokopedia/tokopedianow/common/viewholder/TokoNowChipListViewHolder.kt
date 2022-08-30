package com.tokopedia.tokopedianow.common.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.adapter.TokoNowChipListAdapter
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowChipListAdapterTypeFactory
import com.tokopedia.tokopedianow.common.decroation.ChipListDecoration
import com.tokopedia.tokopedianow.common.model.TokoNowChipListUiModel
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChipViewHolder.ChipListener
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRecyclerViewBinding
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowChipListViewHolder(
    itemView: View,
    private val listener: ChipListener
) : AbstractViewHolder<TokoNowChipListUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_recycler_view
    }

    private var binding: ItemTokopedianowRecyclerViewBinding? by viewBinding()

    private val layoutManager by lazy {
        ChipsLayoutManager
            .newBuilder(itemView.context)
            .setOrientation(ChipsLayoutManager.HORIZONTAL)
            .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
            .build()
    }

    override fun bind(chip: TokoNowChipListUiModel) {
        val typeFactory = TokoNowChipListAdapterTypeFactory(listener)
        val adapter = TokoNowChipListAdapter(typeFactory)

        binding?.recyclerView?.apply {
            this.adapter = adapter
            this.layoutManager = this@TokoNowChipListViewHolder.layoutManager
            addItemDecoration(ChipListDecoration())
        }

        adapter.submitList(chip.items)
    }
}