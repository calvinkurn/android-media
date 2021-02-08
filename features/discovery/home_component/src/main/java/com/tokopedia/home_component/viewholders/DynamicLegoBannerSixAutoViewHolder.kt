package com.tokopedia.home_component.viewholders

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
import com.tokopedia.home_component.visitable.DynamicLegoBannerSixAutoDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.home_component_lego_banner.view.*
import kotlinx.android.synthetic.main.home_component_lego_banner.view.home_component_header_view
import kotlinx.android.synthetic.main.home_component_lego_banner_six_auto.view.*
import kotlinx.android.synthetic.main.layout_dynamic_lego_item_with_label.view.*

/**
 * Created by Devara on 2020-04-28
 */
class DynamicLegoBannerSixAutoViewHolder(val view: View,
                                         val legoListener: DynamicLegoBannerListener?,
                                         val homeComponentListener: HomeComponentListener?,
                                         val parentRecyclerViewPool: RecyclerView.RecycledViewPool? = null): AbstractViewHolder<DynamicLegoBannerSixAutoDataModel>(view) {

    private var isCacheData = false
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_lego_banner_six_auto
    }

    override fun bind(element: DynamicLegoBannerSixAutoDataModel) {
        isCacheData = element.isCache
        setHeaderComponent(element)
        setGrids(element)
    }

    override fun bind(element: DynamicLegoBannerSixAutoDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setGrids(element: DynamicLegoBannerSixAutoDataModel) {
        if (element.channelModel.channelBanner.imageUrl.isNotEmpty()) {
            itemView.item_lego_auto_image.setImageUrl(url = element.channelModel.channelBanner.imageUrl)
        }

        if (element.channelModel.channelBanner.backColor.isNotEmpty()) {
            itemView.gridHolder.setBackgroundColor(
                    Color.parseColor(element.channelModel.channelBanner.backColor)
            )
        }
        if (element.channelModel.channelGrids.isNotEmpty()) {
            val recyclerView: RecyclerView = itemView.findViewById(R.id.recycleList)
            val defaultSpanCount = 2
            if (!isCacheData) {
                setViewportImpression(element)
            }
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
                    adapterPosition + 1,
                    isCacheData)
        } else {
            legoListener?.getDynamicLegoBannerData(element.channelModel)
        }
    }

    private fun setViewportImpression(element: DynamicLegoBannerSixAutoDataModel) {
        itemView.addOnImpressionListener(element.channelModel) {
            when (element.channelModel.channelConfig.layout) {
                DynamicChannelLayout.LAYOUT_6_IMAGE -> {
                    legoListener?.onChannelImpressionSixImage(element.channelModel, adapterPosition)
                }
                DynamicChannelLayout.LAYOUT_LEGO_3_IMAGE -> {
                    legoListener?.onChannelImpressionThreeImage(element.channelModel, adapterPosition)
                }
                DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE -> {
                    legoListener?.onChannelImpressionFourImage(element.channelModel, adapterPosition)
                }
            }
        }
    }

    class LegoItemAdapter(private val listener: DynamicLegoBannerListener?,
                          private val channel: ChannelModel,
                          private val parentPosition: Int,
                          private val isCacheData: Boolean) : RecyclerView.Adapter<LegoItemViewHolder>() {
        private var grids: List<ChannelGrid> = channel.channelGrids
        private val layout = channel.channelConfig.layout
        companion object{
            private val LEGO_SQUARE = R.layout.layout_dynamic_lego_item_with_label
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
                setLegoViewData(holder, grid)
                setLegoClickListener(holder, grid, position)
                if (!isCacheData) {
                    setLegoImpressionListener(holder)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        private fun setLegoViewData(holder: LegoItemViewHolder, grid: ChannelGrid) {

            holder.imageView.setImageUrl(grid.imageUrl)
            holder.label.text = grid.label
        }

        private fun setLegoImpressionListener(holder: LegoItemViewHolder) {
            holder.itemView.addOnImpressionListener(channel) {

            }
        }

        private fun setLegoClickListener(holder: LegoItemViewHolder, grid: ChannelGrid, position: Int) {
            holder.imageView.setOnClickListener {

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

            }

            override fun onChannelExpired(channelModel: ChannelModel) {
                homeComponentListener?.onChannelExpired(channelModel, adapterPosition, element)
            }
        })
    }
}