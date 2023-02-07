package com.tokopedia.home_component.viewholders

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.DynamicChannelHeaderView
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.customview.ShimmeringImageView
import com.tokopedia.home_component.customview.util.RoundedCornersTransformation.CornerType
import com.tokopedia.home_component.decoration.GridSpacingItemDecoration
import com.tokopedia.home_component.decoration.clearDecorations
import com.tokopedia.home_component.listener.DynamicLegoBannerListener
import com.tokopedia.home_component.listener.HomeComponentListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.DynamicChannelLayout
import com.tokopedia.home_component.util.ChannelStyleUtil
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.util.DynamicChannelTabletConfiguration
import com.tokopedia.home_component.util.FPM_DYNAMIC_LEGO_BANNER
import com.tokopedia.home_component.util.toDpInt
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.toPx
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.DividerUnify

/**
 * Created by Devara on 2020-04-28
 */
class DynamicLegoBannerViewHolder(
    itemView: View,
    val legoListener: DynamicLegoBannerListener?,
    val homeComponentListener: HomeComponentListener?,
    val parentRecyclerViewPool: RecyclerView.RecycledViewPool? = null
) : AbstractViewHolder<DynamicLegoBannerDataModel>(itemView) {
    private var isCacheData = false
    private var isUsingPaddingStyle = false
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_lego_banner
        private const val SPAN_COUNT_2 = 2
        private const val SPAN_COUNT_3 = 3
        private const val SPAN_SPACING_0 = 0
        private const val SPAN_SPACING_ROUNDED = 8f
        private const val SPAN_SPACING_BLEEDING = 10
        private const val ROUNDED_CORNER_RADIUS = 8f
    }

    override fun bind(element: DynamicLegoBannerDataModel) {
        isCacheData = element.isCache
        isUsingPaddingStyle = element.channelModel.channelConfig.borderStyle == ChannelStyleUtil.BORDER_STYLE_PADDING
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
            dividerTop = itemView.findViewById<DividerUnify>(R.id.home_component_divider_header),
            dividerBottom = itemView.findViewById<DividerUnify>(R.id.home_component_divider_footer),
            useBottomPadding = element.channelModel.channelConfig.borderStyle == ChannelStyleUtil.BORDER_STYLE_BLEEDING
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

            recyclerView.layoutManager = GridLayoutManager(
                itemView.context,
                defaultSpanCount,
                GridLayoutManager.VERTICAL,
                false
            )

            recyclerView.clearDecorations()
            // setup for lego rollence
            val layout = element.channelModel.channelConfig.layout
            if (layout == DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE || layout == DynamicChannelLayout.LAYOUT_LEGO_2_IMAGE) {
                val spanCount = if (layout == DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE) {
                    DynamicChannelTabletConfiguration.getSpanCountFor2x2(itemView.context)
                } else {
                    SPAN_COUNT_2
                }
                val spacing = if (isUsingPaddingStyle) SPAN_SPACING_ROUNDED.toDpInt() else SPAN_SPACING_BLEEDING
                if (recyclerView.itemDecorationCount == 0) {
                    recyclerView.addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, false))
                }
            } else {
                if (recyclerView.itemDecorationCount == 0) {
                    recyclerView.addItemDecoration(
                        GridSpacingItemDecoration(defaultSpanCount, SPAN_SPACING_0, true)
                    )
                }
            }
            // end for lego rollence
            val marginValue = if (isUsingPaddingStyle) itemView.resources.getDimension(R.dimen.home_component_margin_default).toInt() else 0
            recyclerView.setPadding(
                marginValue,
                0,
                marginValue,
                marginValue
            )

            recyclerView.adapter = LegoItemAdapter(
                legoListener,
                element.channelModel,
                adapterPosition + 1,
                isCacheData,
                isUsingPaddingStyle,
                element.cardInteraction
            )
        } else {
            legoListener?.getDynamicLegoBannerData(element.channelModel)
        }
    }

    private fun Float.toDpInt(): Int = this.toPx().toInt()

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
            DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE -> DynamicChannelTabletConfiguration.getSpanCountFor2x2(itemView.context)
            DynamicChannelLayout.LAYOUT_LEGO_2_IMAGE -> SPAN_COUNT_2
            else -> SPAN_COUNT_3
        }
    }

    class LegoItemAdapter(
        private val listener: DynamicLegoBannerListener?,
        private val channel: ChannelModel,
        private val parentPosition: Int,
        private val isCacheData: Boolean,
        private val isUsingPaddingStyle: Boolean = false,
        private val cardInteraction: Boolean = false
    ) : RecyclerView.Adapter<LegoItemViewHolder>() {
        private val grids: List<ChannelGrid> = channel.channelGrids
        private val layout = channel.channelConfig.layout

        companion object {
            private val LEGO_SQUARE = R.layout.layout_dynamic_lego_item
            private val LEGO_LANDSCAPE = R.layout.layout_dynamic_lego_landscape
            private val LEGO_LANDSCAPE_NON_RADIUS = R.layout.layout_dynamic_lego_landscape_non_radius
            private const val POSITION_TOP_LEFT = 0
            private const val POSITION_TOP_RIGHT = 2
            private const val POSITION_BOTTOM_LEFT = 3
            private const val POSITION_BOTTOM_RIGHT = 5
            private const val LEGO_36_RATIO_BLEEDING = "1:1"
            private const val LEGO_36_RATIO_PADDING = "120:135"
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LegoItemViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            val viewHolder = LegoItemViewHolder(v)
            if (viewType == LEGO_LANDSCAPE) {
                viewHolder.cardUnify.animateOnPress = if (cardInteraction) CardUnify2.ANIMATE_OVERLAY_BOUNCE else CardUnify2.ANIMATE_OVERLAY
            } else if (viewType == LEGO_SQUARE) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    viewHolder.imageView.findViewById<ImageView>(R.id.imageView).foreground =
                        ColorDrawable(ContextCompat.getColor(parent.context, android.R.color.transparent))
                }
            }
            return viewHolder
        }

        override fun getItemViewType(position: Int): Int {
            return if (layout == DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE && isUsingPaddingStyle) {
                LEGO_LANDSCAPE
            } else if (layout == DynamicChannelLayout.LAYOUT_LEGO_2_IMAGE && isUsingPaddingStyle) {
                LEGO_LANDSCAPE
            } else if (layout == DynamicChannelLayout.LAYOUT_LEGO_4_IMAGE ||
                layout == DynamicChannelLayout.LAYOUT_LEGO_2_IMAGE
            ) {
                LEGO_LANDSCAPE_NON_RADIUS
            } else {
                LEGO_SQUARE
            }
        }

        override fun onBindViewHolder(holder: LegoItemViewHolder, position: Int) {
            try {
                val grid = grids[position]
                setLegoViewData(holder, grid, position)
                setLegoClickListener(holder, grid, position)
                if (!isCacheData) {
                    setLegoImpressionListener(holder)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun setLegoViewData(holder: LegoItemViewHolder, grid: ChannelGrid, position: Int) {
            if (getItemViewType(position) == LEGO_SQUARE && isUsingPaddingStyle) {
                val cornerType = when (position) {
                    POSITION_TOP_LEFT -> if (layout == DynamicChannelLayout.LAYOUT_LEGO_3_IMAGE) CornerType.LEFT else CornerType.TOP_LEFT
                    POSITION_TOP_RIGHT -> if (layout == DynamicChannelLayout.LAYOUT_LEGO_3_IMAGE) CornerType.RIGHT else CornerType.TOP_RIGHT
                    POSITION_BOTTOM_LEFT -> CornerType.BOTTOM_LEFT
                    POSITION_BOTTOM_RIGHT -> CornerType.BOTTOM_RIGHT
                    else -> null
                }
                (holder.imageView.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio = LEGO_36_RATIO_PADDING
                cornerType?.let {
                    holder.imageView.loadImageRounded(
                        grid.imageUrl,
                        ROUNDED_CORNER_RADIUS.toDpInt(),
                        FPM_DYNAMIC_LEGO_BANNER,
                        it
                    )
                } ?: holder.imageView.loadImage(grid.imageUrl, FPM_DYNAMIC_LEGO_BANNER)
            } else {
                if (getItemViewType(position) == LEGO_SQUARE) {
                    (holder.imageView.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio = LEGO_36_RATIO_BLEEDING
                }
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
            val clickListener = View.OnClickListener {
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
            if (getItemViewType(position) == LEGO_LANDSCAPE) {
                holder.itemView.setOnClickListener(clickListener)
            } else {
                holder.imageView.setOnClickListener(clickListener)
            }
        }

        override fun getItemCount(): Int {
            return grids.size
        }
    }

    class LegoItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardUnify: CardUnify2 by lazy { view.findViewById(R.id.item_lego_card) }
        val imageView: ShimmeringImageView = view.findViewById<ShimmeringImageView?>(R.id.image)
        val context: Context
            get() = itemView.context
    }

    private fun setHeaderComponent(element: DynamicLegoBannerDataModel) {
        itemView.findViewById<DynamicChannelHeaderView>(R.id.home_component_header_view).setChannel(
            element.channelModel,
            object : HeaderListener {
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
            }
        )
    }
}
