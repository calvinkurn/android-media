package com.tokopedia.home_component.viewholders

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.R
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.decoration.GridSpacingItemDecoration
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.listener.Lego6AutoBannerListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.visitable.DynamicLegoBannerSixAutoDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.home_component_lego_banner.view.home_component_header_view
import kotlinx.android.synthetic.main.home_component_lego_banner_six_auto.view.*
import kotlinx.android.synthetic.main.home_component_lego_banner_six_auto.view.recycleList

/**
 * Created by Devara on 2020-04-28
 */
class DynamicLegoBannerSixAutoViewHolder(val view: View,
                                         val legoSixAutoListener: Lego6AutoBannerListener?,
                                         val homeComponentListener: HomeComponentListener?,
                                         val parentRecyclerViewPool: RecyclerView.RecycledViewPool? = null): AbstractViewHolder<DynamicLegoBannerSixAutoDataModel>(view) {

    private var isCacheData = false
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_lego_banner_six_auto
    }

    init {
        parentRecyclerViewPool?.let {
            itemView.recycleList.setRecycledViewPool(parentRecyclerViewPool)
        }
    }

    override fun bind(element: DynamicLegoBannerSixAutoDataModel) {
        isCacheData = element.isCache
        setHeaderComponent(element)
        setChannelDivider(element)
        setGrids(element)
    }

    override fun bind(element: DynamicLegoBannerSixAutoDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setChannelDivider(element: DynamicLegoBannerSixAutoDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = itemView.home_component_divider_header,
            dividerBottom = itemView.home_component_divider_footer
        )
    }

    private fun setGrids(element: DynamicLegoBannerSixAutoDataModel) {
        if (element.channelModel.channelBanner.imageUrl.isNotEmpty()) {
            itemView.item_lego_auto_image.post {
                itemView.item_lego_auto_image.onUrlLoaded = {success ->
                    if (success && element.channelModel.channelBanner.backColor.isNotEmpty()) {
                        itemView.gridHolder.setBackgroundColor(
                                Color.parseColor(element.channelModel.channelBanner.backColor)
                        )
                    }
                }
                itemView.item_lego_auto_image.setImageUrl(url = element.channelModel.channelBanner.imageUrl)
            }
            itemView.item_lego_auto_image.setOnClickListener {
                legoSixAutoListener?.onTallImageClicked(element.channelModel, (adapterPosition+1))
            }
        }
        if (element.channelModel.channelGrids.isNotEmpty()) {
            val recyclerView: RecyclerView = itemView.findViewById(R.id.recycleList)
            val defaultSpanCount = 2
            if (!isCacheData) {
                setViewportImpression(element)
            }
            recyclerView.setHasFixedSize(true)
            if (recyclerView.itemDecorationCount == 0) recyclerView.addItemDecoration(
                    GridSpacingItemDecoration(defaultSpanCount, 0, true))

            if (recyclerView.layoutManager == null) {
                recyclerView.layoutManager = GridLayoutManager(
                        itemView.context,
                        defaultSpanCount,
                        GridLayoutManager.VERTICAL, false)
            }

            recyclerView.itemAnimator = null
            if (recyclerView.adapter == null) {
                val adapter = LegoItemAdapter(legoSixAutoListener)
                recyclerView.adapter = adapter
                adapter.setItems(element.channelModel.channelGrids, isCacheData, adapterPosition, element.channelModel)
            } else {
                val adapter = recyclerView.adapter as? LegoItemAdapter
                adapter?.setItems(element.channelModel.channelGrids, isCacheData, adapterPosition, element.channelModel)
            }
        }
    }

    private fun setViewportImpression(element: DynamicLegoBannerSixAutoDataModel) {
        itemView.addOnImpressionListener(element.channelModel) {
            legoSixAutoListener?.onChannelLegoImpressed(element.channelModel, adapterPosition)
        }
    }

    class LegoItemAdapter(private val listener: Lego6AutoBannerListener?
    ) : RecyclerView.Adapter<LegoItemViewHolder>() {
        var grids: List<ChannelGrid> = listOf()
        var isCacheData: Boolean = true
        var parentPosition: Int = -1
        var channel: ChannelModel? = null
        companion object{
            private val LEGO_SQUARE = R.layout.layout_dynamic_lego_item_with_label
        }

        fun setItems(grids: List<ChannelGrid>, isCacheData: Boolean, parentPosition: Int, channelModel: ChannelModel) {
            this.isCacheData = isCacheData
            this.parentPosition = parentPosition
            this.channel = channelModel
            if (this.grids != grids) {
                this.grids = grids
                notifyDataSetChanged()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LegoItemViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            return LegoItemViewHolder(v)
        }

        override fun getItemViewType(position: Int): Int {
            return LEGO_SQUARE
        }

        override fun onBindViewHolder(holder: LegoItemViewHolder, position: Int) {
            try {
                val grid = grids[position]
                setLegoViewData(holder, grid, position, parentPosition, isCacheData)
                setLegoClickListener(holder, grid, position)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun setLegoViewData(holder: LegoItemViewHolder, grid: ChannelGrid, position: Int, adapterPosition: Int, isCacheData: Boolean) {
            holder.imageView.post {
                holder.imageView.setImageUrl(grid.imageUrl)
            }
            holder.label.text = grid.label
            if (!isCacheData) {
                channel?.let {
                    holder.itemView.addOnImpressionListener(it) {
                        listener?.onLegoItemImpressed(it, grid, position, adapterPosition)
                    }
                }
            }
        }

        private fun setLegoClickListener(holder: LegoItemViewHolder, grid: ChannelGrid, position: Int) {
            holder.imageView.setOnClickListener {
                channel?.let {
                    listener?.onLegoItemClicked(it, grid, position, parentPosition)
                }
            }
        }

        override fun getItemCount(): Int {
            return if (grids.size > 4) 4 else grids.size
        }
    }

    class LegoItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageUnify = view.findViewById(R.id.image)
        val label: Typography = view.findViewById(R.id.label)

        val context: Context
            get() = itemView.context
    }

    private fun setHeaderComponent(element: DynamicLegoBannerSixAutoDataModel) {
        itemView.home_component_header_view.setChannel(element.channelModel, object : HeaderListener {
            override fun onSeeAllClick(link: String) {
                legoSixAutoListener?.onSeeAllClicked(element.channelModel, adapterPosition)
            }

            override fun onChannelExpired(channelModel: ChannelModel) {
                homeComponentListener?.onChannelExpired(channelModel, adapterPosition, element)
            }
        })
    }
}