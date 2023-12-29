package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.databinding.GlobalDcDealsWidgetBinding
import com.tokopedia.home_component.decoration.DealsWidgetSpacingItemDecoration
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.listener.VpsWidgetListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.util.VpsWidgetTabletConfiguration
import com.tokopedia.home_component.viewholders.adapter.DealsWidgetAdapter
import com.tokopedia.home_component.visitable.DealsDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel
 */
class DealsWidgetViewHolder(
    itemView: View,
    val dealsWidgetListener: VpsWidgetListener?,
    val homeComponentListener: HomeComponentListener?
) : AbstractViewHolder<DealsDataModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.global_dc_deals_widget
    }
    private val binding: GlobalDcDealsWidgetBinding? by viewBinding()
    private val recyclerView by lazy { binding?.recycleList }
    private val layoutManager: GridLayoutManager by lazy { GridLayoutManager(itemView.context, VpsWidgetTabletConfiguration.getSpanCount(itemView.context)) }

    private var isCacheData = false

    override fun bind(element: DealsDataModel) {
        isCacheData = element.isCache
        setHeaderComponent(element)
        setChannelDivider(element)
        initView(element)
    }

    override fun bind(element: DealsDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setHeaderComponent(element: DealsDataModel) {
        binding?.homeComponentHeaderView?.setChannel(
            element.channelModel,
            object : HeaderListener {
                override fun onSeeAllClick(link: String) {
                    dealsWidgetListener?.onSeeAllClicked(element.channelModel, link, element.channelModel.verticalPosition)
                }

                override fun onChannelExpired(channelModel: ChannelModel) {
                    homeComponentListener?.onChannelExpired(channelModel, element.channelModel.verticalPosition, element)
                }
            }
        )
    }

    private fun setChannelDivider(element: DealsDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = binding?.homeComponentDividerHeader,
            dividerBottom = binding?.homeComponentDividerFooter
        )
    }

    private fun initView(element: DealsDataModel) {
        initRV()
        initItems(element)
        if (!isCacheData) {
            itemView.addOnImpressionListener(element.channelModel) {
                dealsWidgetListener?.onChannelImpressed(element.channelModel, element.channelModel.verticalPosition)
            }
        }
    }

    private fun initRV() {
        recyclerView?.layoutManager = layoutManager
    }

    private fun initItems(element: DealsDataModel) {
        val adapter = DealsWidgetAdapter(dealsWidgetListener, element.channelModel.verticalPosition, isCacheData)
        adapter.addData(element)
        recyclerView?.adapter = adapter
        if (recyclerView?.itemDecorationCount == 0) {
            recyclerView?.addItemDecoration(DealsWidgetSpacingItemDecoration())
        }
    }
}
