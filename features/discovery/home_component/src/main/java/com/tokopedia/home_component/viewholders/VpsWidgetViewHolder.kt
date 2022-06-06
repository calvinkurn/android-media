package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.databinding.GlobalDcVpsWidgetBinding
import com.tokopedia.home_component.decoration.GridSpacingItemDecoration
import com.tokopedia.home_component.decoration.VpsWidgetSpacingItemDecoration
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.listener.VpsWidgetListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.util.VpsWidgetTabletConfiguration
import com.tokopedia.home_component.util.toDpInt
import com.tokopedia.home_component.viewholders.adapter.VpsWidgetAdapter
import com.tokopedia.home_component.visitable.VpsDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel
 */
class VpsWidgetViewHolder (itemView: View,
                           val vpsWidgetListener: VpsWidgetListener?,
                           val homeComponentListener: HomeComponentListener?,
                           val parentRecyclerViewPool: RecyclerView.RecycledViewPool? = null): AbstractViewHolder<VpsDataModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.global_dc_vps_widget
        private const val SPAN_SPACING = 8f
    }
    private val binding: GlobalDcVpsWidgetBinding? by viewBinding()
    private val layoutManager: GridLayoutManager by lazy { GridLayoutManager(itemView.context, VpsWidgetTabletConfiguration.getSpanCount(itemView.context)) }
    private val adapter: VpsWidgetAdapter by lazy { VpsWidgetAdapter(vpsWidgetListener, adapterPosition, isCacheData) }

    private var isCacheData = false

    override fun bind(element: VpsDataModel) {
        isCacheData = element.isCache
        setHeaderComponent(element)
        setChannelDivider(element)
        initView(element)
    }

    override fun bind(element: VpsDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setHeaderComponent(element: VpsDataModel) {
        binding?.homeComponentHeaderView?.setChannel(element.channelModel, object : HeaderListener {
            override fun onSeeAllClick(link: String) {
                vpsWidgetListener?.onSeeAllClicked(element.channelModel, adapterPosition)
            }

            override fun onChannelExpired(channelModel: ChannelModel) {
                homeComponentListener?.onChannelExpired(channelModel, adapterPosition, element)
            }
        })
    }

    private fun setChannelDivider(element: VpsDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = binding?.homeComponentDividerHeader,
            dividerBottom = binding?.homeComponentDividerFooter
        )
    }

    private fun initView(element: VpsDataModel) {
        initRV()
        initItems(element)
        if (!isCacheData) {
            itemView.addOnImpressionListener(element.channelModel) {
                vpsWidgetListener?.onChannelImpressed(element.channelModel, adapterPosition)
            }
        }
    }

    private fun initRV() {
        parentRecyclerViewPool?.let { binding?.homeComponentVpsRv?.setRecycledViewPool(parentRecyclerViewPool) }
        binding?.homeComponentVpsRv?.layoutManager = layoutManager
    }

    private fun initItems(element: VpsDataModel) {
        adapter.addData(element)
        binding?.homeComponentVpsRv?.adapter = adapter
        adapter.notifyDataSetChanged()
        if (binding?.homeComponentVpsRv?.itemDecorationCount == 0) binding?.homeComponentVpsRv?.addItemDecoration(
            VpsWidgetSpacingItemDecoration(SPAN_SPACING.toDpInt())
        )
    }
}