package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.decoration.GridSpacingItemDecoration
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.listener.Lego4AutoBannerListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.viewholders.adapter.Lego4AutoBannerAdapter
import com.tokopedia.home_component.visitable.Lego4AutoDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import kotlinx.android.synthetic.main.home_component_lego_banner.view.*

/**
 * @author by yoasfs on 27/07/20
 */
class Lego4AutoBannerViewHolder (itemView: View,
                                 val legoListener: Lego4AutoBannerListener?,
                                 val homeComponentListener: HomeComponentListener?,
                                 val parentRecyclerViewPool: RecyclerView.RecycledViewPool? = null): AbstractViewHolder<Lego4AutoDataModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.global_component_lego_banner_auto
        const val GRID_COUNT = 2
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var adapter: Lego4AutoBannerAdapter

    private var isCacheData = false

    override fun bind(element: Lego4AutoDataModel) {
        isCacheData = element.isCache
        setHeaderComponent(element)
        setChannelDivider(element)
        initView(element)
    }

    override fun bind(element: Lego4AutoDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setChannelDivider(element: Lego4AutoDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = itemView.home_component_divider_header,
            dividerBottom = itemView.home_component_divider_footer
        )
    }

    private fun initView(element: Lego4AutoDataModel) {
        initRV()
        initItems(element)
        if (!isCacheData) {
            itemView.addOnImpressionListener(element.channelModel) {
                legoListener?.onChannelLegoImpressed(element.channelModel, adapterPosition)
            }
        }
    }

    private fun initRV() {
        recyclerView = itemView.findViewById(R.id.recycleList)
        layoutManager = GridLayoutManager(itemView.context, GRID_COUNT)
        parentRecyclerViewPool?.let { recyclerView.setRecycledViewPool(parentRecyclerViewPool) }
        recyclerView.layoutManager = layoutManager
    }

    private fun initItems(element: Lego4AutoDataModel) {
        adapter = Lego4AutoBannerAdapter(legoListener, adapterPosition, isCacheData)
        adapter.addData(element)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        if (recyclerView.itemDecorationCount == 0) recyclerView.addItemDecoration(
                GridSpacingItemDecoration(2, 10, false))
    }

    private fun setHeaderComponent(element: Lego4AutoDataModel) {
        itemView.home_component_header_view.setChannel(element.channelModel, object : HeaderListener {
            override fun onSeeAllClick(link: String) {
                legoListener?.onSeeAllClicked(element.channelModel, adapterPosition)
            }

            override fun onChannelExpired(channelModel: ChannelModel) {
                homeComponentListener?.onChannelExpired(channelModel, adapterPosition, element)
            }
        })
    }
}