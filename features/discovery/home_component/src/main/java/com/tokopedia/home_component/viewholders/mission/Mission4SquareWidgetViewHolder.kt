package com.tokopedia.home_component.viewholders.mission

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.databinding.GlobalComponent4squareMissionWidgetBinding
import com.tokopedia.home_component.viewholders.mission.v3.MissionWidgetAdapter
import com.tokopedia.home_component.visitable.MissionWidgetDataModel
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel
import com.tokopedia.home_component_header.model.ChannelHeader
import com.tokopedia.utils.view.binding.viewBinding

class Mission4SquareWidgetViewHolder constructor(
    view: View
) : AbstractViewHolder<MissionWidgetListDataModel>(view) {

    private val binding: GlobalComponent4squareMissionWidgetBinding? by viewBinding()

    private var mAdapter: MissionWidgetAdapter? = null

    init {
        setupRecyclerView()
    }

    override fun bind(element: MissionWidgetListDataModel?) {
        if (element == null) return

        setupHeaderView(element.header)
        setupMissionWidgetList(element.missionWidgetList)
    }

    private fun setupHeaderView(header: ChannelHeader?) {
        if (header == null) return
        binding?.headerView?.bind(header)
    }

    private fun setupMissionWidgetList(data: List<MissionWidgetDataModel>) {
        mAdapter?.submitList(data)
    }

    private fun setupRecyclerView() {
        mAdapter = if (binding?.lstCard?.adapter == null) {
            MissionWidgetAdapter()
        } else {
            binding?.lstCard?.adapter as? MissionWidgetAdapter
        }

        val layoutManager = LinearLayoutManager(
            itemView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        binding?.lstCard?.layoutManager = layoutManager
        binding?.lstCard?.adapter = mAdapter
    }

    companion object {
        @LayoutRes
        val LAYOUT = home_componentR.layout.global_component_4square_mission_widget
    }
}
