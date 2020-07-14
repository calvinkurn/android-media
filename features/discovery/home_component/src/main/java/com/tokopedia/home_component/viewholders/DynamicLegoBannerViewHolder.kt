package com.tokopedia.home_component.viewholders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_component.R
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.customview.ShimmeringImageView
import com.tokopedia.home_component.decoration.GridSpacingItemDecoration
import com.tokopedia.home_component.listener.DynamicLegoBannerListener
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.DynamicChannelLayout
import com.tokopedia.home_component.util.FPM_DYNAMIC_LEGO_BANNER
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import kotlinx.android.synthetic.main.home_component_lego_banner.view.*

/**
 * Created by Devara on 2020-04-28
 */
class DynamicLegoBannerViewHolder(itemView: View,
                                  val legoListener: DynamicLegoBannerListener,
                                  val homeComponentListener: HomeComponentListener,
                                  val parentRecyclerViewPool: RecyclerView.RecycledViewPool? = null): AbstractViewHolder<DynamicLegoBannerDataModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_lego_banner
    }

    override fun bind(element: DynamicLegoBannerDataModel) {
        setHeaderComponent(element)
        setGrids(element)
    }

    override fun bind(element: DynamicLegoBannerDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setGrids(element: DynamicLegoBannerDataModel) {
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recycleList)
        val defaultSpanCount = getRecyclerViewDefaultSpanCount(element)
        setViewportImpression(element)

        parentRecyclerViewPool?.let { recyclerView.setRecycledViewPool(parentRecyclerViewPool) }
        recyclerView.setHasFixedSize(true)
        if (recyclerView.itemDecorationCount == 0) recyclerView.addItemDecoration(
                GridSpacingItemDecoration(defaultSpanCount, 0, true))

        recyclerView.layoutManager = GridLayoutManager(
                itemView.context,
                defaultSpanCount,
                GridLayoutManager.VERTICAL, false)

        recyclerView.adapter = LegoItemAdapter(
                legoListener,
                element.channelModel,
                adapterPosition+1)
    }

    private fun setViewportImpression(element: DynamicLegoBannerDataModel) {
        itemView.addOnImpressionListener(element.channelModel) {
            when (element.channelModel.channelConfig.layout) {
                DynamicChannelLayout.LAYOUT_6_IMAGE -> {
                    legoListener.onChannelImpressionSixImage(element.channelModel, adapterPosition)
                }
                DynamicChannelLayout.LAYOUT_LEGO_3_IMAGE -> {
                    legoListener.onChannelImpressionThreeImage(element.channelModel, adapterPosition)
                }
                DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE -> {
                    legoListener.onChannelImpressionFourImage(element.channelModel, adapterPosition)
                }
            }
        }
    }

    private fun getRecyclerViewDefaultSpanCount(element: DynamicLegoBannerDataModel): Int {
        return when (element.channelModel.channelConfig.layout) {
            DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE -> 2
            else -> 3
        }
    }

    class LegoItemAdapter(private val listener: DynamicLegoBannerListener,
                          private val channel: ChannelModel,
                          private val parentPosition: Int) : RecyclerView.Adapter<LegoItemViewHolder>() {
        private var grids: List<ChannelGrid> = channel.channelGrids
        private val layout = channel.channelConfig.layout
        companion object{
            private val LEGO_SQUARE = R.layout.layout_dynamic_lego_item
            private val LEGO_LANDSCAPE = R.layout.layout_dynamic_lego_landscape
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LegoItemViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            return LegoItemViewHolder(v)
        }

        override fun getItemViewType(position: Int): Int {
            return if(layout == DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE) LEGO_LANDSCAPE else LEGO_SQUARE
        }

        override fun onBindViewHolder(holder: LegoItemViewHolder, position: Int) {
            try {
                val grid = grids[position]
                setLegoViewData(holder, grid)
                setLegoClickListener(holder, grid, position)
                setLegoImpressionListener(holder)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        private fun setLegoViewData(holder: LegoItemViewHolder, grid: ChannelGrid) {
            if (layout == DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE) {
                holder.imageView.loadImage(grid.imageUrl, FPM_DYNAMIC_LEGO_BANNER)
            } else {
                holder.imageView.loadImage(grid.imageUrl, FPM_DYNAMIC_LEGO_BANNER)
            }
        }

        private fun setLegoImpressionListener(holder: LegoItemViewHolder) {
            holder.itemView.addOnImpressionListener(channel) {
                when (layout) {
                    DynamicChannelLayout.LAYOUT_6_IMAGE -> {
                        listener.onImpressionGridSixImage(channel, parentPosition)
                    }
                    DynamicChannelLayout.LAYOUT_LEGO_3_IMAGE -> {
                        listener.onImpressionGridThreeImage(channel, parentPosition)
                    }
                    DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE -> {
                        listener.onImpressionGridFourImage(channel, parentPosition)
                    }
                }
            }
        }

        private fun setLegoClickListener(holder: LegoItemViewHolder, grid: ChannelGrid, position: Int) {
            holder.imageView.setOnClickListener {
                when (layout) {
                    DynamicChannelLayout.LAYOUT_6_IMAGE -> {
                        listener.onClickGridSixImage(channel, grid, position, parentPosition)
                    }
                    DynamicChannelLayout.LAYOUT_LEGO_3_IMAGE -> {
                        listener.onClickGridThreeImage(channel, grid, position, parentPosition)
                    }
                    DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE -> {
                        listener.onClickGridFourImage(channel, grid, position, parentPosition)
                    }
                }
            }
        }

        override fun getItemCount(): Int {
            return grids.size
        }
    }

    class LegoItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ShimmeringImageView = view.findViewById(R.id.image)
        val context: Context
            get() = itemView.context
    }

    private fun setHeaderComponent(element: DynamicLegoBannerDataModel) {
        itemView.home_component_header_view.setChannel(element.channelModel, object : HeaderListener {
            override fun onSeeAllClick(link: String) {
                when (element.channelModel.channelConfig.layout) {
                    DynamicChannelLayout.LAYOUT_6_IMAGE -> legoListener.onSeeAllSixImage(element.channelModel, adapterPosition)
                    DynamicChannelLayout.LAYOUT_LEGO_3_IMAGE -> legoListener.onSeeAllThreemage(element.channelModel, adapterPosition)
                    DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE -> legoListener.onSeeAllFourImage(element.channelModel, adapterPosition)
                }
            }

            override fun onChannelExpired(channelModel: ChannelModel) {
                homeComponentListener.onChannelExpired(channelModel, adapterPosition, element)
            }
        })
    }
}