package com.tokopedia.home_component.viewholders.mission

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.databinding.GlobalComponent4squareMissionWidgetBinding
import com.tokopedia.home_component.decoration.StaticMissionWidgetItemDecoration
import com.tokopedia.home_component.viewholders.mission.v3.Mission4SquareWidgetListener
import com.tokopedia.home_component.viewholders.mission.v3.MissionWidgetAdapter
import com.tokopedia.home_component.visitable.MissionWidgetDataModel
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel
import com.tokopedia.home_component_header.model.ChannelHeader
import com.tokopedia.utils.view.binding.viewBinding

class Mission4SquareWidgetViewHolder constructor(
    view: View,
    private val listener: Mission4SquareWidgetListener
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
        binding?.txtHeader?.text = header.name
    }

    private fun setupMissionWidgetList(data: List<MissionWidgetDataModel>) {
        mAdapter?.submitList(data.take(4)) //TODO: create mapper for this one!
    }

    private fun setupRecyclerView() {
        mAdapter = if (binding?.lstCard?.adapter == null) {
            MissionWidgetAdapter(listener)
        } else {
            binding?.lstCard?.adapter as? MissionWidgetAdapter
        }

        val layoutManager = GridLayoutManager(itemView.context, 4)

        binding?.lstCard?.addItemDecoration(StaticMissionWidgetItemDecoration())
        binding?.lstCard?.layoutManager = layoutManager
        binding?.lstCard?.adapter = mAdapter
    }

    companion object {
        @LayoutRes
        val LAYOUT = home_componentR.layout.global_component_4square_mission_widget
    }
}
