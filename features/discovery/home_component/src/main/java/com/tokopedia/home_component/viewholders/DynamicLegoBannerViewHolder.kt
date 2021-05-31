package com.tokopedia.home_component.viewholders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.HomeComponentRollenceController
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.customview.ShimmeringImageView
import com.tokopedia.home_component.decoration.GridSpacingItemDecoration
import com.tokopedia.home_component.decoration.clearDecorations
import com.tokopedia.home_component.listener.DynamicLegoBannerListener
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.DynamicChannelLayout
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.util.FPM_DYNAMIC_LEGO_BANNER
import com.tokopedia.home_component.util.convertDpToPixel
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import kotlinx.android.synthetic.main.home_component_lego_banner.view.*
import kotlinx.android.synthetic.main.home_component_lego_banner.view.home_component_divider_footer
import kotlinx.android.synthetic.main.home_component_lego_banner.view.home_component_divider_header
import kotlinx.android.synthetic.main.home_component_lego_banner.view.home_component_header_view

/**
 * Created by Devara on 2020-04-28
 */
class DynamicLegoBannerViewHolder(itemView: View,
                                  val legoListener: DynamicLegoBannerListener?,
                                  val homeComponentListener: HomeComponentListener?,
                                  val parentRecyclerViewPool: RecyclerView.RecycledViewPool? = null):
        AbstractViewHolder<DynamicLegoBannerDataModel>(itemView) {

    private var isCacheData = false
    private var isLego4UsingRollenceVariant = false
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_lego_banner
    }

    override fun bind(element: DynamicLegoBannerDataModel) {
        isCacheData = element.isCache
        isLego4UsingRollenceVariant = HomeComponentRollenceController.isHomeComponentLego4BannerUsingRollenceVariant()
        setHeaderComponent(element)
        setChannelDivider(element)
        setGrids(element)
    }

    override fun bind(element: DynamicLegoBannerDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setChannelDivider(element: DynamicLegoBannerDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = itemView.home_component_divider_header,
            dividerBottom = itemView.home_component_divider_footer
        )
    }

    private fun setGrids(element: DynamicLegoBannerDataModel) {
        if (element.channelModel.channelGrids.isNotEmpty()) {
            val recyclerView: RecyclerView = itemView.findViewById(R.id.recycleList)
            val defaultSpanCount = getRecyclerViewDefaultSpanCount(element)
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
                    isCacheData,
                    isLego4UsingRollenceVariant)
            var marginValue = 0
            recyclerView.clearDecorations()

            //setup for lego 4 banner rollence
            //need to be deleted after rollence duration end
            if (element.channelModel.channelConfig.layout == DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE && isLego4UsingRollenceVariant) {
                if (recyclerView.itemDecorationCount == 0) recyclerView.addItemDecoration(
                        GridSpacingItemDecoration(2, 10, false))
                marginValue = 0
            }
            else if (element.channelModel.channelConfig.layout == DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE
                    || element.channelModel.channelConfig.layout == DynamicChannelLayout.LAYOUT_LEGO_2_IMAGE) {
                if (recyclerView.itemDecorationCount == 0) recyclerView.addItemDecoration(
                        GridSpacingItemDecoration(2, 20, false))
                marginValue = itemView.resources.getDimension(R.dimen.home_component_margin_default).toInt()
            }
            //end setup for lego 4 banner rollence
            val marginLayoutParams = recyclerView.layoutParams as ConstraintLayout.LayoutParams
            marginLayoutParams.leftMargin = marginValue
            marginLayoutParams.rightMargin = marginValue
            marginLayoutParams.topToBottom = R.id.home_component_header_view
            recyclerView.layoutParams = marginLayoutParams

        } else {
            legoListener?.getDynamicLegoBannerData(element.channelModel)
        }
    }

    private fun setViewportImpression(element: DynamicLegoBannerDataModel) {
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
                DynamicChannelLayout.LAYOUT_LEGO_2_IMAGE -> {
                    legoListener?.onChannelImpressionTwoImage(element.channelModel, adapterPosition)
                }
            }
        }
    }

    private fun getRecyclerViewDefaultSpanCount(element: DynamicLegoBannerDataModel): Int {
        return when (element.channelModel.channelConfig.layout) {
            DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE, DynamicChannelLayout.LAYOUT_LEGO_2_IMAGE -> 2
            else -> 3
        }
    }

    class LegoItemAdapter(private val listener: DynamicLegoBannerListener?,
                          private val channel: ChannelModel,
                          private val parentPosition: Int,
                          private val isCacheData: Boolean,
                          private val isLego4UsingRollenceVariant: Boolean = false ) : RecyclerView.Adapter<LegoItemViewHolder>() {
        private var grids: List<ChannelGrid> = channel.channelGrids
        private val layout = channel.channelConfig.layout
        companion object{
            private val LEGO_SQUARE = R.layout.layout_dynamic_lego_item
            private val LEGO_LANDSCAPE = R.layout.layout_dynamic_lego_landscape
            private val LEGO_LANDSCAPE_NON_RADIUS = R.layout.layout_dynamic_lego_landscape_non_radius
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LegoItemViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            return LegoItemViewHolder(v)
        }

        override fun getItemViewType(position: Int): Int {
            return if (layout == DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE && isLego4UsingRollenceVariant)
                LEGO_LANDSCAPE_NON_RADIUS
            else if(layout == DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE
                    || layout ==  DynamicChannelLayout.LAYOUT_LEGO_2_IMAGE)
                LEGO_LANDSCAPE
            else LEGO_SQUARE
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
                        listener?.onImpressionGridSixImage(channel, parentPosition)
                    }
                    DynamicChannelLayout.LAYOUT_LEGO_3_IMAGE -> {
                        listener?.onImpressionGridThreeImage(channel, parentPosition)
                    }
                    DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE -> {
                        listener?.onImpressionGridFourImage(channel, parentPosition)
                    }
                    DynamicChannelLayout.LAYOUT_LEGO_2_IMAGE -> {
                        listener?.onImpressionGridTwoImage(channel, parentPosition)
                    }
                }
            }
        }

        private fun setLegoClickListener(holder: LegoItemViewHolder, grid: ChannelGrid, position: Int) {
            holder.imageView.setOnClickListener {
                when (layout) {
                    DynamicChannelLayout.LAYOUT_6_IMAGE -> {
                        listener?.onClickGridSixImage(channel, grid, position, parentPosition)
                    }
                    DynamicChannelLayout.LAYOUT_LEGO_3_IMAGE -> {
                        listener?.onClickGridThreeImage(channel, grid, position, parentPosition)
                    }
                    DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE -> {
                        listener?.onClickGridFourImage(channel, grid, position, parentPosition)
                    }
                    DynamicChannelLayout.LAYOUT_LEGO_2_IMAGE -> {
                        listener?.onClickGridTwoImage(channel, grid, position, parentPosition)
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
                    DynamicChannelLayout.LAYOUT_6_IMAGE -> legoListener?.onSeeAllSixImage(element.channelModel, adapterPosition)
                    DynamicChannelLayout.LAYOUT_LEGO_3_IMAGE -> legoListener?.onSeeAllThreemage(element.channelModel, adapterPosition)
                    DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE -> legoListener?.onSeeAllFourImage(element.channelModel, adapterPosition)
                    DynamicChannelLayout.LAYOUT_LEGO_2_IMAGE -> legoListener?.onSeeAllTwoImage(element.channelModel, adapterPosition)
                }
            }

            override fun onChannelExpired(channelModel: ChannelModel) {
                homeComponentListener?.onChannelExpired(channelModel, adapterPosition, element)
            }
        })
    }
}