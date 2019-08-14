package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
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

class SixGridChannelViewHolder(sixGridView: View,
                               private val homeCategoryListener: HomeCategoryListener,
                               countDownListener: CountDownView.CountDownListener) :
        DynamicChannelViewHolder<SixGridChannelViewHolder.SixGridItemViewHolder>(
                sixGridView, homeCategoryListener, countDownListener
        ) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_master_dynamic_channel

        private fun getAvailableLink(applink: String, url: String): String {
            return if (!TextUtils.isEmpty(applink)) {
                applink
            } else {
                url
            }
        }
    }

    val context = sixGridView.context

    override fun getItemAdapter(channel: DynamicHomeChannel.Channels): RecyclerView.Adapter<SixGridItemViewHolder> {
        return SixGridItemAdapter(context, homeCategoryListener, channel, adapterPosition)
    }

    override fun getRecyclerViewDecorator(): RecyclerView.ItemDecoration {
        return GridSpacingItemDecoration(defaultSpanCount, 0, true)
    }

    override fun onSeeAllClickTracker(channel: DynamicHomeChannel.Channels, applink: String) {
        HomePageTracking.eventClickSeeAllLegoBannerChannel(
                context, applink)
    }

    override fun getViewHolderClassName(): String {
        return SixGridChannelViewHolder::class.java.simpleName
    }


    class SixGridItemAdapter(private val context: Context,
                             private val listener: HomeCategoryListener,
                             private val channels: DynamicHomeChannel.Channels,
                             private val parentPosition: Int) : RecyclerView.Adapter<SixGridItemViewHolder>() {
        private var grids: Array<DynamicHomeChannel.Grid> = channels.grids

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SixGridItemViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_lego_item, parent, false)
            return SixGridItemViewHolder(v)
        }

        override fun onBindViewHolder(holder: SixGridItemViewHolder, position: Int) {
            try {
                val grid = grids[position]
                ImageHandler.loadImageFitCenter(holder.context, holder.imageView, grid.imageUrl)
                holder.imageView.setOnClickListener {
                    HomePageTracking.eventEnhancedClickDynamicChannelHomePage(
                            holder.context,
                            channels.getEnhanceClickLegoBannerHomePage(grid, position + 1)
                    )
                    listener.onSixGridItemClicked(getAvailableLink(grid.applink, grid.url),
                            channels.getHomeAttribution(position + 1, grid.attribution))
                    HomeTrackingUtils.homeDiscoveryWidgetClick(context,
                            parentPosition + 1, grid,
                            getAvailableLink(grid.applink,
                                    grid.url),
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

    class SixGridItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: SquareImageView = view.findViewById(R.id.image)
        val context: Context
            get() = itemView.context
    }
}
