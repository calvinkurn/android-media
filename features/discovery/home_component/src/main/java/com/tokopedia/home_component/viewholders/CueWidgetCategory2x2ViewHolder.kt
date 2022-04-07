package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.databinding.GlobalDcCueCategory2x2Binding
import com.tokopedia.home_component.decoration.CueWidgetCategoryItemDecoration
import com.tokopedia.home_component.listener.CueWidgetCategoryListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.util.DynamicChannelTabletConfiguration
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
        private const val SPAN_COUNT_2x2_MOBILE = 2
        private const val SPAN_COUNT_2x2_TABLET = 4
        private const val SPAN_COUNT_3x2_MOBILE = 3
        private const val SPAN_COUNT_3x2_TABLET = 6
        private const val FIRST_ITEM_DECORATION = 0
    }

    override fun bind(element: CueCategory2x2DataModel) {
        setHeaderComponent(element = element)
        setChannelDivider(element)
        mappingView(element.channelModel)
    }

    private fun getSpanCount(gridSize: Int): Int {
        val cueWidget2x2MinSize = 4
        val cueWidget2x2MaxSize = 5
        val gridCount = if (gridSize in cueWidget2x2MinSize .. cueWidget2x2MaxSize) {
            if (DeviceScreenInfo.isTablet(itemView.context)) SPAN_COUNT_2x2_TABLET else SPAN_COUNT_2x2_MOBILE
        } else {
            if (DeviceScreenInfo.isTablet(itemView.context)) SPAN_COUNT_3x2_TABLET else SPAN_COUNT_3x2_MOBILE
        }
        return gridCount
    }

    private fun mappingView(channel: ChannelModel) {
        binding?.run {
            adapter = CueWidgetCategoryAdapter(channel, cueWidgetCategoryListener)
            homeComponentCueCategory2x2Rv.adapter = adapter
            val layoutManager = StaggeredGridLayoutManager(getSpanCount(channel.channelGrids.size), LinearLayoutManager.VERTICAL)
            homeComponentCueCategory2x2Rv.layoutManager = layoutManager
            if (homeComponentCueCategory2x2Rv.itemDecorationCount == FIRST_ITEM_DECORATION) {
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