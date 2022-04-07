package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.databinding.GlobalDcCueCategoryBinding
import com.tokopedia.home_component.decoration.CueWidgetCategoryItemDecoration
import com.tokopedia.home_component.listener.CueWidgetCategoryListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.viewholders.adapter.CueWidgetCategoryAdapter
import com.tokopedia.home_component.visitable.CueCategoryDataModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class CueWidgetCategoryViewHolder (
    itemView: View,
    private val cueWidgetCategoryListener: CueWidgetCategoryListener
) : AbstractViewHolder<CueCategoryDataModel>(itemView) {
    private var binding: GlobalDcCueCategoryBinding? by viewBinding()
    private var adapter: CueWidgetCategoryAdapter? = null

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.global_dc_cue_category
        private const val SPAN_COUNT_2x2_MOBILE = 2
        private const val SPAN_COUNT_2x2_TABLET = 4
        private const val SPAN_COUNT_3x2_MOBILE = 3
        private const val SPAN_COUNT_3x2_TABLET = 6
        private const val FIRST_ITEM_DECORATION = 0
        private const val CUE_WIDGET_2x2_MIN_SIZE = 4
        private const val CUE_WIDGET_2x2_MAX_SIZE = 5
        private const val CUE_WIDGET_3x2 = 6
        private const val HIDE_CUE_WIDGET = 0
    }

    override fun bind(element: CueCategoryDataModel) {
        setHeaderComponent(element = element)
        setChannelDivider(element)
        mappingView(element.channelModel)
    }

    private fun getSpanCount(gridSize: Int): Int {
        return if (gridSize in CUE_WIDGET_2x2_MIN_SIZE..CUE_WIDGET_2x2_MAX_SIZE) {
            if (DeviceScreenInfo.isTablet(itemView.context)) SPAN_COUNT_2x2_TABLET else SPAN_COUNT_2x2_MOBILE
        } else if (gridSize >= CUE_WIDGET_3x2) {
            if (DeviceScreenInfo.isTablet(itemView.context)) SPAN_COUNT_3x2_TABLET else SPAN_COUNT_3x2_MOBILE
        } else HIDE_CUE_WIDGET
    }

    private fun mappingView(channel: ChannelModel) {
        binding?.run {
            adapter = CueWidgetCategoryAdapter(channel, cueWidgetCategoryListener, adapterPosition)
            homeComponentCueCategory2x2Rv.adapter = adapter
            val spanCount = getSpanCount(channel.channelGrids.size)
            if (spanCount == HIDE_CUE_WIDGET) {
                root.gone()
            } else {
                val layoutManager = StaggeredGridLayoutManager(
                    getSpanCount(channel.channelGrids.size),
                    LinearLayoutManager.VERTICAL
                )
                homeComponentCueCategory2x2Rv.layoutManager = layoutManager
                if (homeComponentCueCategory2x2Rv.itemDecorationCount == FIRST_ITEM_DECORATION) {
                    homeComponentCueCategory2x2Rv.addItemDecoration(CueWidgetCategoryItemDecoration())
                }
            }
        }
    }

    private fun setChannelDivider(element: CueCategoryDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = binding?.homeComponentDividerHeader,
            dividerBottom = binding?.homeComponentDividerFooter
        )
    }

    private fun setHeaderComponent(element: CueCategoryDataModel) {
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