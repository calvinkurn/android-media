package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.design.image.SquareImageView
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.DynamicLinkHelper
import com.tokopedia.home.beranda.helper.TextViewHelper
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.GridSpacingItemDecoration
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils

/**
 * Created by devarafikry on 12/08/19.
 */

class DynamicChannelSprintViewHolder(sprintView: View,
                                     private val homeCategoryListener: HomeCategoryListener,
                                     countDownListener: CountDownView.CountDownListener) :
        DynamicChannelViewHolder<DynamicChannelSprintViewHolder.SprintViewHolder>(
                sprintView, homeCategoryListener, countDownListener
        ) {

    companion object {
        private const val TYPE_SPRINT_SALE = 0
        private const val TYPE_SPRINT_LEGO = 1
        private const val TYPE_ORGANIC = 2
        private const val TYPE_CURATED = 3

        @LayoutRes
        val LAYOUT_ITEM_SPRINT = R.layout.layout_sprint_product_item_simple
    }

    val context = sprintView.context

    private fun getSprintType(channels: DynamicHomeChannel.Channels): Int {
        when(channels.layout) {
            DynamicHomeChannel.Channels.LAYOUT_SPRINT -> return TYPE_SPRINT_SALE
            DynamicHomeChannel.Channels.LAYOUT_SPRINT_CAROUSEL -> return TYPE_SPRINT_SALE
            DynamicHomeChannel.Channels.LAYOUT_SPRINT_LEGO -> return TYPE_SPRINT_LEGO
            DynamicHomeChannel.Channels.LAYOUT_ORGANIC -> return TYPE_ORGANIC
        }
        return TYPE_CURATED
    }

    override fun getItemAdapter(channel: DynamicHomeChannel.Channels): RecyclerView.Adapter<SprintViewHolder> {
        return SprintAdapter(context, homeCategoryListener, channel, getSprintType(channel), countDownView)
    }

    override fun getRecyclerViewDecorator(): RecyclerView.ItemDecoration {
        return GridSpacingItemDecoration(defaultSpanCount,
                itemView.getContext().getResources().getDimensionPixelSize(R.dimen.dp_10),
                true)
    }

    override fun onSeeAllClickTracker(channel: DynamicHomeChannel.Channels, applink: String) {
        when(getSprintType(channel)) {
            TYPE_SPRINT_SALE -> HomePageTracking.eventClickSeeAllProductSprint(context)
            TYPE_SPRINT_LEGO -> HomePageTracking.eventClickSeeAllLegoProduct(context, channel.header.name)
            else -> HomePageTracking.eventClickSeeAllDynamicChannel(
                    context,
                    DynamicLinkHelper.getActionLink(channel.header))
        }
    }

    override fun getViewHolderClassName(): String {
        return DynamicChannelSprintViewHolder::class.java.simpleName
    }

    class SprintAdapter(private val context: Context,
                             private val listener: HomeCategoryListener,
                             private val channels: DynamicHomeChannel.Channels,
                             private val sprintType: Int,
                             private val countDownView: CountDownView) : RecyclerView.Adapter<SprintViewHolder>() {
        private var grids: Array<DynamicHomeChannel.Grid> = channels.grids

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SprintViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(LAYOUT_ITEM_SPRINT, parent, false)
            return SprintViewHolder(v)
        }

        override fun onBindViewHolder(holder: SprintViewHolder, position: Int) {
            try {
                val grid = grids[position]
                ImageHandler.loadImageThumbs(holder.context, holder.channelImage1, grid.imageUrl)
                TextViewHelper.displayText(holder.channelName, grid.name)
                TextViewHelper.displayText(holder.channelPrice1, grid.price)
                TextViewHelper.displayText(holder.channelDiscount1, grid.discount)
                TextViewHelper.displayText(holder.channelBeforeDiscPrice1, grid.slashedPrice)
                TextViewHelper.displayText(holder.channelCashback, grid.cashback)

                holder.itemContainer1.setOnClickListener {
                    var attr = ""
                    when(sprintType){
                        TYPE_SPRINT_SALE -> {
                            HomePageTracking.eventEnhancedClickSprintSaleProduct(context,
                                    channels.getEnhanceClickSprintSaleHomePage(position, countDownView.currentCountDown))
                            attr = channels.getHomeAttribution(position + 1, channels.grids[position].id)
                        }
                        TYPE_SPRINT_LEGO -> {
                            HomePageTracking.eventEnhancedClickSprintSaleProduct(context,
                                    channels.getEnhanceClickSprintSaleLegoHomePage(position))
                            attr = channels.getHomeAttribution(position + 1, channels.grids[position].id)
                        }
                        TYPE_ORGANIC -> {
                            HomePageTracking.eventEnhancedClickSprintSaleProduct(context,
                                    channels.getEnhanceClickSprintSaleLegoHomePage(position))
                            attr = channels.getHomeAttribution(position + 1, channels.grids[position].id)
                        }
                        TYPE_CURATED -> {
                            HomePageTracking.eventEnhancedClickDynamicChannelHomePage(context,
                                    channels.getEnhanceClickDynamicChannelHomePage(grid, position + 1))
                            attr = channels.getHomeAttribution(position + 1, grid.attribution)
                        }
                    }

                    listener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(grid), attr)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        override fun getItemCount(): Int {
            return grids.size
        }
    }

    class SprintViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val channelImage1: ImageView = view.findViewById(R.id.channel_image_1)
        val channelName: TextView = view.findViewById(R.id.product_name)
        val channelPrice1: TextView = view.findViewById(R.id.channel_price_1)
        val channelDiscount1: TextView = view.findViewById(R.id.channel_discount_1)
        val channelCashback: TextView = view.findViewById(R.id.channel_cashback)
        val channelBeforeDiscPrice1: TextView = view.findViewById(R.id.channel_before_disc_price_1)
        val itemContainer1: RelativeLayout = view.findViewById(R.id.channel_item_container_1)
        val context: Context
            get() = itemView.context
    }
}
