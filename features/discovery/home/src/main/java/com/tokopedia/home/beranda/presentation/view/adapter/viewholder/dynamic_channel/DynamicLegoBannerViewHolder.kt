package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.design.image.SquareImageView
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.GridSpacingItemDecoration
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils

/**
 * Created by devarafikry on 12/08/19.
 */

class DynamicLegoBannerViewHolder(legoBannerView: View,
                                  private val homeCategoryListener: HomeCategoryListener,
                                  countDownListener: CountDownView.CountDownListener) :
        DynamicChannelViewHolder<DynamicLegoBannerViewHolder.LegoItemViewHolder>(
                legoBannerView, homeCategoryListener, countDownListener
        ) {

    companion object {
        private const val TYPE_SIX_GRID_LEGO = 0
        private const val TYPE_THREE_GRID_LEGO = 1

        @LayoutRes
        val LAYOUT_ITEM_LEGO = R.layout.layout_lego_item
    }

    val context = legoBannerView.context

    private fun getLegoBannerType(channels: DynamicHomeChannel.Channels): Int {
        when(channels.layout) {
            DynamicHomeChannel.Channels.LAYOUT_6_IMAGE -> return TYPE_SIX_GRID_LEGO
            DynamicHomeChannel.Channels.LAYOUT_LEGO_3_IMAGE -> return TYPE_THREE_GRID_LEGO
        }
        return TYPE_SIX_GRID_LEGO
    }

    override fun getItemAdapter(channel: DynamicHomeChannel.Channels): RecyclerView.Adapter<LegoItemViewHolder> {
        return LegoItemAdapter(context, homeCategoryListener, channel, getLegoBannerType(channel), adapterPosition)
    }

    override fun getRecyclerViewDecorator(): RecyclerView.ItemDecoration {
        return GridSpacingItemDecoration(defaultSpanCount, 0, true)
    }

    override fun onSeeAllClickTracker(channel: DynamicHomeChannel.Channels, applink: String) {
        when(getLegoBannerType(channel)) {
            TYPE_SIX_GRID_LEGO -> HomePageTracking.eventClickSeeAllLegoBannerChannel(
                    context, applink)
            TYPE_THREE_GRID_LEGO -> HomePageTracking.eventClickSeeAllThreeLegoBannerChannel(context, channel.header.name)
            else -> HomePageTracking.eventClickSeeAllLegoBannerChannel(
                    context, applink)
        }
    }

    override fun getViewHolderClassName(): String {
        return DynamicLegoBannerViewHolder::class.java.simpleName
    }

    class LegoItemAdapter(private val context: Context,
                             private val listener: HomeCategoryListener,
                             private val channels: DynamicHomeChannel.Channels,
                             private val legoBannerType: Int,
                             private val parentPosition: Int) : RecyclerView.Adapter<LegoItemViewHolder>() {
        private var grids: Array<DynamicHomeChannel.Grid> = channels.grids

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LegoItemViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_lego_item, parent, false)
            return LegoItemViewHolder(v)
        }

        override fun onBindViewHolder(holder: LegoItemViewHolder, position: Int) {
            try {
                val grid = grids[position]
                ImageHandler.loadImageFitCenter(holder.context, holder.imageView, grid.imageUrl)
                holder.imageView.setOnClickListener {
                    when(legoBannerType) {
                        TYPE_SIX_GRID_LEGO -> {
                            HomePageTracking.eventEnhancedClickDynamicChannelHomePage(
                                    holder.context,
                                    channels.getEnhanceClickLegoBannerHomePage(grid, position + 1)
                            )
                        }
                        TYPE_THREE_GRID_LEGO -> {
                            HomePageTracking.eventEnhancedClickDynamicChannelHomePage(
                                    holder.context,
                                    channels.getEnhanceClickThreeLegoBannerHomePage(grid, position + 1)
                            )
                        }
                    }

                    listener.onLegoBannerClicked(
                            if (grid.applink.isEmpty()) grid.applink else grid.url,
                            channels.getHomeAttribution(position + 1, grid.attribution))
                    HomeTrackingUtils.homeDiscoveryWidgetClick(context,
                            parentPosition + 1, grid,
                            if (grid.applink.isEmpty()) grid.applink else grid.url,
                            channels.type)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        override fun getItemCount(): Int {
            return grids.size
        }
    }

    class LegoItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: SquareImageView = view.findViewById(R.id.image)
        val context: Context
            get() = itemView.context
    }
}
