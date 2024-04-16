package com.tokopedia.home_component.viewholders.shorten.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.GlobalComponent2squareDealsWidgetBinding
import com.tokopedia.home_component.decoration.StaticMissionWidgetItemDecoration
import com.tokopedia.home_component.viewholders.shorten.internal.MAX_LIMIT_CARD
import com.tokopedia.home_component.viewholders.shorten.viewholder.item.PartialItemWidgetAdapter
import com.tokopedia.home_component.visitable.shorten.DealsAndMissionWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.DealsWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class DealsWidgetViewHolder(
    view: View,
    pool: RecyclerView.RecycledViewPool?
) : AbstractViewHolder<DealsWidgetUiModel>(view) {

    private val binding: GlobalComponent2squareDealsWidgetBinding? by viewBinding()

    private var mAdapter: PartialItemWidgetAdapter? = null

    init {
        if (pool != null) {
            binding?.lstCard?.setRecycledViewPool(pool)
        }

        setupRecyclerView()
    }

    override fun bind(element: DealsWidgetUiModel?) {
        if (element == null) return

        binding?.txtHeader?.text = element.header.name
        mAdapter?.submitList(element.data.take(MAX_LIMIT_CARD))
    }

    private fun setupRecyclerView() {
        mAdapter = PartialItemWidgetAdapter(DealsAndMissionWidgetUiModel.Type.Deals)
        binding?.lstCard?.layoutManager = GridLayoutManager(itemView.context, MAX_LIMIT_CARD)
        binding?.lstCard?.addItemDecoration(StaticMissionWidgetItemDecoration.span2())
        binding?.lstCard?.adapter = mAdapter
    }

    companion object {
        val LAYOUT = R.layout.global_component_2square_deals_widget
    }
}
