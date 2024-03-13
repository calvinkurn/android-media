package com.tokopedia.home_component.viewholders.mission

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.databinding.GlobalComponent4squareMissionWidgetBinding
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel
import com.tokopedia.home_component_header.model.ChannelHeader
import com.tokopedia.utils.view.binding.viewBinding

class Mission4SquareWidgetViewHolder constructor(
    view: View
) : AbstractViewHolder<MissionWidgetListDataModel>(view) {

    private val binding: GlobalComponent4squareMissionWidgetBinding? by viewBinding()

    override fun bind(element: MissionWidgetListDataModel?) {
        setupHeaderView(element?.header)
    }

    private fun setupHeaderView(header: ChannelHeader?) {
        if (header == null) return
        binding?.headerView?.bind(header)
    }

    companion object {
        val LAYOUT = home_componentR.layout.global_component_4square_mission_widget
    }
}
