package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.gms.analytics.Tracker
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.design.image.SquareImageView
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.analytics.HomePageTrackingV2
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.glide.FPM_DYNAMIC_LEGO_BANNER
import com.tokopedia.home.beranda.helper.glide.loadImageFitCenter
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.GridSpacingItemDecoration
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils
import com.tokopedia.track.TrackApp

/**
 * Created by devarafikry on 12/08/19.
 */

class DynamicLegoBannerViewHolder(legoBannerView: View,
                                  private val homeCategoryListener: HomeCategoryListener,
                                  countDownListener: CountDownView.CountDownListener,
                                  private val parentRecycledViewPool: RecyclerView.RecycledViewPool) :
        DynamicChannelViewHolder(
                legoBannerView, homeCategoryListener, countDownListener
        ) {

    companion object {
        private const val TYPE_SIX_GRID_LEGO = 3
        private const val TYPE_THREE_GRID_LEGO = 4
        private const val TYPE_FOUR_GRID_LEGO = 9

        @LayoutRes
        val LAYOUT = R.layout.home_dc_lego_banner
    }

    val context = legoBannerView.context
    var defaultSpanCount = 3

    override fun onSeeAllClickTracker(channel: DynamicHomeChannel.Channels, applink: String) {
        when(getLayoutType(channel)) {
            TYPE_SIX_GRID_LEGO -> HomePageTracking.eventClickSeeAllLegoBannerChannel(
                    context, applink, channel.id)
            TYPE_THREE_GRID_LEGO -> HomePageTracking.eventClickSeeAllThreeLegoBannerChannel(context, channel.header.name, channel.id)
            TYPE_FOUR_GRID_LEGO -> TrackApp.getInstance().gtm.sendGeneralEvent(HomePageTrackingV2.LegoBanner.getLegoBannerFourImageSeeAllClick(channel))
            else -> HomePageTracking.eventClickSeeAllLegoBannerChannel(
                    context, applink, channel.id)
        }
    }

    override fun getViewHolderClassName(): String {
        return DynamicLegoBannerViewHolder::class.java.simpleName
    }

    override fun setupContent(channel: DynamicHomeChannel.Channels) {
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recycleList)

        defaultSpanCount = when(getLayoutType(channel)){
            TYPE_FOUR_GRID_LEGO -> 2
            else -> 3
        }

        if (recyclerView.itemDecorationCount == 0) recyclerView.addItemDecoration(
                GridSpacingItemDecoration(defaultSpanCount, 0, true))

        recyclerView.setRecycledViewPool(parentRecycledViewPool)
        recyclerView.setHasFixedSize(true)

        recyclerView.layoutManager = GridLayoutManager(
                itemView.context,
                defaultSpanCount,
                GridLayoutManager.VERTICAL, false)

        recyclerView.adapter = LegoItemAdapter(context,
                homeCategoryListener,
                channel,
                getLayoutType(channel),
                adapterPosition)
    }

    class LegoItemAdapter(private val context: Context,
                             private val listener: HomeCategoryListener,
                             private val channels: DynamicHomeChannel.Channels,
                             private val legoBannerType: Int,
                             private val parentPosition: Int) : RecyclerView.Adapter<LegoItemViewHolder>() {
        private var grids: Array<DynamicHomeChannel.Grid> = channels.grids
        companion object{
            private val LEGO_SQUARE = R.layout.layout_lego_item
            private val LEGO_LANDSCAPE = R.layout.layout_lego_landscape
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LegoItemViewHolder {
            val layout = if(legoBannerType == TYPE_FOUR_GRID_LEGO) LEGO_LANDSCAPE else LEGO_SQUARE
            val v = LayoutInflater.from(parent.context).inflate(layout, parent, false)
            return LegoItemViewHolder(v)
        }

        override fun getItemViewType(position: Int): Int {
            return R.layout.layout_lego_item
        }

        override fun onBindViewHolder(holder: LegoItemViewHolder, position: Int) {
            try {
                val grid = grids[position]
                holder.imageView.loadImageFitCenter(grid.imageUrl, FPM_DYNAMIC_LEGO_BANNER)
                holder.imageView.setOnClickListener {
                    when(legoBannerType) {
                        TYPE_SIX_GRID_LEGO -> {
                            HomePageTracking.eventEnhancedClickDynamicChannelHomePage(
                                    HomePageTracking.getEnhanceClickLegoBannerHomePage(
                                            grid,
                                            channels,
                                            position + 1)
                            )
                        }
                        TYPE_THREE_GRID_LEGO -> {
                            HomePageTracking.eventEnhancedClickDynamicChannelHomePage(
                                    HomePageTracking.getEnhanceClickThreeLegoBannerHomePage(
                                            grid,
                                            channels,
                                            position + 1)
                            )
                        }
                        TYPE_FOUR_GRID_LEGO -> {
                            HomePageTracking.eventEnhancedClickDynamicChannelHomePage(
                                HomePageTrackingV2.LegoBanner.getLegoBannerFourImageClick(channels, grid, position + 1)
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
        val imageView: AppCompatImageView = view.findViewById(R.id.image)
        val context: Context
            get() = itemView.context
    }
}
