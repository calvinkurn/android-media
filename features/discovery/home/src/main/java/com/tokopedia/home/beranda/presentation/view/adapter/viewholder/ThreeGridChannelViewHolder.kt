package com.tokopedia.home.beranda.presentation.view.adapter.viewholder

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

class ThreeGridChannelViewHolder(threeGridView: View,
                                 private val homeCategoryListener: HomeCategoryListener,
                                 countDownListener: CountDownView.CountDownListener) : DynamicChannelViewHolder<ThreeGridChannelViewHolder.ThreeGridItemViewHolder>(
        threeGridView, homeCategoryListener, countDownListener
) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_master_dynamic_channel
        private val TAG = DynamicChannelViewHolder::class.java.simpleName

        private fun getAvailableLink(applink: String, url: String): String {
            return if (!TextUtils.isEmpty(applink)) {
                applink
            } else {
                url
            }
        }
    }

    val context = itemView.context

    override fun getItemAdapter(channel: DynamicHomeChannel.Channels): RecyclerView.Adapter<ThreeGridItemViewHolder> {
        return ThreeGridItemAdapter(context, homeCategoryListener, channel, adapterPosition)
    }

    override fun getRecyclerViewDecorator(): RecyclerView.ItemDecoration {
        return GridSpacingItemDecoration(defaultSpanCount, 0, true)
    }

    override fun onSeeAllClickTracker(headerName: String, applink: String) {
        HomePageTracking.eventClickSeeAllThreeLegoBannerChannel(context, headerName)
    }

    override fun getViewHolderClassName(): String {
        return ThreeGridChannelViewHolder::class.java.simpleName
    }

    private class ThreeGridItemAdapter(
            private val context: Context,
            private val listener: HomeCategoryListener,
            private val channel: DynamicHomeChannel.Channels,
            private val parentPosition: Int) : RecyclerView.Adapter<ThreeGridItemViewHolder>() {

        private var grids: Array<DynamicHomeChannel.Grid?> = channel.grids

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThreeGridItemViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_lego_item, parent, false)
            return ThreeGridItemViewHolder(v)
        }

        override fun onBindViewHolder(holder: ThreeGridItemViewHolder, position: Int) {
            try {
                val grid = grids[position]
                if (grid != null) {
                    ImageHandler.loadImageFitCenter(holder.context, holder.imageView, grid.imageUrl)
                    holder.imageView.setOnClickListener {
                        HomePageTracking.eventEnhancedClickDynamicChannelHomePage(
                                holder.context,
                                channel.getEnhanceClickThreeLegoBannerHomePage(grid, position + 1)
                        )
                        listener.onThreeGridItemClicked(getAvailableLink(grid.applink, grid.url),
                                channel.getHomeAttribution(position + 1, grid.attribution))
                        HomeTrackingUtils.homeDiscoveryWidgetClick(context,
                                parentPosition + 1, grid,
                                getAvailableLink(grid.applink,
                                        grid.url),
                                channel.type)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        override fun getItemCount(): Int {
            return grids.size
        }
    }

    class ThreeGridItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: SquareImageView = view.findViewById(R.id.image)
        val context: Context
            get() = itemView.context
    }
}
