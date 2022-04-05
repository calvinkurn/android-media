package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.databinding.GlobalDcCueCategory3x2Binding
import com.tokopedia.home_component.decoration.CueWidgetCategoryItemDecoration
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.viewholders.adapter.CueWidgetCategoryAdapter
import com.tokopedia.home_component.visitable.CueCategory3x2DataModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class CueWidgetCategory3x2ViewHolder (
    itemView: View
) : AbstractViewHolder<CueCategory3x2DataModel>(itemView) {
    private var binding: GlobalDcCueCategory3x2Binding? by viewBinding()
    private var adapter: CueWidgetCategoryAdapter? = null

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.global_dc_cue_category_3x2
        private const val SPAN_COUNT = 3
    }

    override fun bind(element: CueCategory3x2DataModel) {
        setHeaderComponent(element = element)
        setChannelDivider(element)
        mappingView(element.channelModel)
    }

    private fun mappingView(channel: ChannelModel) {
        mappingItem(channel)
    }

    private fun mappingItem(channel: ChannelModel) {
        binding?.run {
            adapter = CueWidgetCategoryAdapter(channel)
            homeComponentCueCategory3x2Rv.adapter = adapter
            val layoutManager = StaggeredGridLayoutManager(SPAN_COUNT, LinearLayoutManager.VERTICAL)
            homeComponentCueCategory3x2Rv.layoutManager = layoutManager
            if (homeComponentCueCategory3x2Rv.itemDecorationCount == 0) {
                homeComponentCueCategory3x2Rv.addItemDecoration(CueWidgetCategoryItemDecoration())
            }
        }
    }

    private fun setChannelDivider(element: CueCategory3x2DataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = binding?.homeComponentDividerHeader,
            dividerBottom = binding?.homeComponentDividerFooter
        )
    }

    private fun setHeaderComponent(element: CueCategory3x2DataModel) {
        binding?.homeComponentHeaderView?.setChannel(element.channelModel, object : HeaderListener {
            override fun onSeeAllClick(link: String) {

            }
            override fun onChannelExpired(channelModel: ChannelModel) {}
        })
    }
}