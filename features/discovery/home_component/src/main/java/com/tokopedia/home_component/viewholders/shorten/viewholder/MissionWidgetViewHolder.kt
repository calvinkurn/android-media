package com.tokopedia.home_component.viewholders.shorten.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.GlobalComponent2squareMissionWidgetBinding
import com.tokopedia.home_component.util.ShortenUtils.TWO_SQUARE_MAX_PRODUCT_LIMIT
import com.tokopedia.home_component.viewholders.shorten.ContainerMultiTwoSquareListener
import com.tokopedia.home_component.viewholders.shorten.viewholder.item.ItemContentCardAdapter
import com.tokopedia.home_component.visitable.shorten.MissionWidgetUiModel
import com.tokopedia.home_component_header.model.ChannelHeader
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.home_component.visitable.shorten.MultiTwoSquareWidgetUiModel.Type as ItemTwoSquareType

class MissionWidgetViewHolder(
    view: View,
    pool: RecyclerView.RecycledViewPool?,
    private val listener: ContainerMultiTwoSquareListener
) : AbstractViewHolder<MissionWidgetUiModel>(view) {

    private val binding: GlobalComponent2squareMissionWidgetBinding? by viewBinding()
    private var mAdapter: ItemContentCardAdapter? = null

    init {
        if (pool != null) {
            binding?.lstMissionCard?.setRecycledViewPool(pool)
        }

        setupRecyclerView()
    }

    override fun bind(element: MissionWidgetUiModel?) {
        if (element == null) return
        setupWidgetHeader(element.header)
        mAdapter?.submitList(element.data)
    }

    private fun setupWidgetHeader(header: ChannelHeader) {
        binding?.txtHeader?.text = header.name
        binding?.txtHeader?.setOnClickListener {
            listener.missionChannelHeaderClicked(header.applink)
        }
    }

    private fun setupRecyclerView() {
        mAdapter = ItemContentCardAdapter(ItemTwoSquareType.Mission, listener)
        binding?.lstMissionCard?.layoutManager = GridLayoutManager(
            itemView.context,
            TWO_SQUARE_MAX_PRODUCT_LIMIT
        )

        binding?.lstMissionCard?.adapter = mAdapter
        binding?.lstMissionCard?.setHasFixedSize(true)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.global_component_2square_mission_widget
    }
}
