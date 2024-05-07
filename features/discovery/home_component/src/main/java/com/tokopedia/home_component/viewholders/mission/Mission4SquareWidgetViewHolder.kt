package com.tokopedia.home_component.viewholders.mission

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.databinding.GlobalComponent4squareMissionWidgetBinding
import com.tokopedia.home_component.decoration.StaticMissionWidgetItemDecoration
import com.tokopedia.home_component.viewholders.mission.v3.Mission4SquareWidgetListener
import com.tokopedia.home_component.viewholders.mission.v3.MissionWidgetAdapter
import com.tokopedia.home_component.R as home_componentR
import com.tokopedia.home_component.visitable.MissionWidgetListDataModel
import com.tokopedia.home_component_header.model.ChannelHeader
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.view.binding.viewBinding

class Mission4SquareWidgetViewHolder(
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
        setupMissionWidgetList(element)
    }

    override fun bind(element: MissionWidgetListDataModel, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty() && (payloads[0] as? Bundle)?.getBoolean(MissionWidgetListDataModel.PAYLOAD_IS_REFRESH, false) == true) return
        bind(element)
    }

    private fun setupHeaderView(header: ChannelHeader?) {
        if (header == null) return
        binding?.txtHeader?.text = header.name
    }

    private fun setupMissionWidgetList(element: MissionWidgetListDataModel) {
        if (element.isShowMissionWidget()) {
            when (element.status) {
                MissionWidgetListDataModel.STATUS_LOADING -> shouldShowShimmerAndHideDataList()
                MissionWidgetListDataModel.STATUS_ERROR -> shouldHideSimmerAndShowDataList()
                else -> {
                    shouldHideSimmerAndShowDataList()
                    mAdapter?.submitList(element.mission4SquareWidgetList)
                }
            }
        } else {
            binding?.root?.gone()
            itemView.gone()
        }
    }

    private fun setupRecyclerView() {
        mAdapter = if (binding?.lstCard?.adapter == null) {
            MissionWidgetAdapter(listener)
        } else {
            binding?.lstCard?.adapter as? MissionWidgetAdapter
        }

        val layoutManager = GridLayoutManager(itemView.context, 4)

        binding?.lstCard?.addItemDecoration(StaticMissionWidgetItemDecoration.span4())
        binding?.lstCard?.layoutManager = layoutManager
        binding?.lstCard?.adapter = mAdapter
    }

    private fun shouldHideSimmerAndShowDataList() {
        binding?.shimmering?.root?.gone()
        binding?.lstCard?.show()
    }

    private fun shouldShowShimmerAndHideDataList() {
        binding?.shimmering?.root?.show()
        binding?.lstCard?.gone()
    }

    companion object {
        @LayoutRes
        val LAYOUT = home_componentR.layout.global_component_4square_mission_widget
    }
}
