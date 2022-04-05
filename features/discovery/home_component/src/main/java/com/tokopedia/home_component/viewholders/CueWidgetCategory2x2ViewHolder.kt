package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.databinding.GlobalDcCueCategory2x2Binding
import com.tokopedia.home_component.decoration.CueWidgetCategoryItemDecoration
import com.tokopedia.home_component.listener.CueWidgetCategoryListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.viewholders.adapter.CueWidgetCategoryAdapter
import com.tokopedia.home_component.visitable.CueCategory2x2DataModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class CueWidgetCategory2x2ViewHolder (
    itemView: View,
    private val cueWidgetCategoryListener: CueWidgetCategoryListener
) : AbstractViewHolder<CueCategory2x2DataModel>(itemView) {
    private var binding: GlobalDcCueCategory2x2Binding? by viewBinding()
    private var adapter: CueWidgetCategoryAdapter? = null

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.global_dc_cue_category_2x2
        private const val SPAN_COUNT = 2
    }

    override fun bind(element: CueCategory2x2DataModel) {
        setHeaderComponent(element = element)
        setChannelDivider(element)
        mappingView(element.channelModel)
    }

    private fun mappingView(channel: ChannelModel) {
        binding?.run {
            adapter = CueWidgetCategoryAdapter(channel, cueWidgetCategoryListener)
            homeComponentCueCategory2x2Rv.adapter = adapter
            val layoutManager = StaggeredGridLayoutManager(SPAN_COUNT, LinearLayoutManager.VERTICAL)
            homeComponentCueCategory2x2Rv.layoutManager = layoutManager
            if (homeComponentCueCategory2x2Rv.itemDecorationCount == 0) {
                homeComponentCueCategory2x2Rv.addItemDecoration(CueWidgetCategoryItemDecoration())
            }
        }
    }

    private fun setChannelDivider(element: CueCategory2x2DataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = binding?.homeComponentDividerHeader,
            dividerBottom = binding?.homeComponentDividerFooter
        )
    }

    private fun setHeaderComponent(element: CueCategory2x2DataModel) {
        binding?.homeComponentHeaderView?.setChannel(element.channelModel, object : HeaderListener {
            override fun onSeeAllClick(link: String) {
                //no op
            }
            override fun onChannelExpired(channelModel: ChannelModel) {
                //no op
            }
        })
    }
}