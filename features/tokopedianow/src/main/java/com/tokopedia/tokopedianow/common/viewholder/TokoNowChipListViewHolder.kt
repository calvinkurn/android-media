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

    private val adapter by lazy {
        TokoNowChipListAdapter(TokoNowChipListAdapterTypeFactory(listener))
    }

    init {
        val layoutManager = ChipsLayoutManager
            .newBuilder(itemView.context)
            .setOrientation(ChipsLayoutManager.HORIZONTAL)
            .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
            .build()

        binding?.recyclerView?.apply {
            addItemDecoration(ChipListDecoration())
            this.layoutManager = layoutManager
            this.itemAnimator = null
            setHasFixedSize(true)
        }
    }

    override fun bind(chip: TokoNowChipListUiModel) {
        binding?.recyclerView?.adapter = adapter
        adapter.submitList(chip.items)
    }
}