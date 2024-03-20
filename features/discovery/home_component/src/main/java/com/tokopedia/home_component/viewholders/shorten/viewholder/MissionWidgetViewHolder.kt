package com.tokopedia.home_component.viewholders.shorten.viewholder

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.GlobalComponent2squareMissionWidgetBinding
import com.tokopedia.home_component.viewholders.shorten.viewholder.item.PartialItemWidgetAdapter
import com.tokopedia.home_component.visitable.shorten.DealsAndMissionWidgetUiModel
import com.tokopedia.home_component.visitable.shorten.MissionWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class MissionWidgetViewHolder(
    view: View,
    pool: RecyclerView.RecycledViewPool?
) : AbstractViewHolder<MissionWidgetUiModel>(view) {

    private val binding: GlobalComponent2squareMissionWidgetBinding? by viewBinding()

    private var mAdapter: PartialItemWidgetAdapter? = null

    init {
        if (pool != null) {
            binding?.lstCard?.setRecycledViewPool(pool)
        }

        setupRecyclerView()
    }

    override fun bind(element: MissionWidgetUiModel?) {
        if (element == null) return

        mAdapter?.submitList(element.data)
    }

    private fun setupRecyclerView() {
        mAdapter = PartialItemWidgetAdapter(DealsAndMissionWidgetUiModel.Type.Deals)
        binding?.lstCard?.layoutManager = GridLayoutManager(itemView.context, 2)
        binding?.lstCard?.adapter = mAdapter
    }

    companion object {
        val LAYOUT = R.layout.global_component_2square_mission_widget
    }
}
