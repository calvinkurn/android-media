package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import android.graphics.Paint
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.DynamicLinkHelper
import com.tokopedia.home.beranda.helper.HomeImageHandler
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.GridSpacingItemDecoration
import com.tokopedia.kotlin.extensions.view.displayTextOrHide

/**
 * Created by devarafikry on 12/08/19.
 */

class ProductOrganicChannelViewHolder(sprintView: View,
                                      private val homeCategoryListener: HomeCategoryListener,
                                      countDownListener: CountDownView.CountDownListener) :
        DynamicChannelViewHolder(
                sprintView, homeCategoryListener, countDownListener
        ) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_dc_lego_product
    }

    val context: Context = sprintView.context
    val defaultSpanCount = 3

    override fun onSeeAllClickTracker(channel: DynamicHomeChannel.Channels, applink: String) {
        when(getLayoutType(channel)) {
            TYPE_SPRINT_LEGO -> HomePageTracking.eventClickSeeAllLegoProduct(context, channel.header.name, channel.id)
            TYPE_ORGANIC -> HomePageTracking.eventClickSeeAllLegoProduct(context, channel.header.name, channel.id)
            else -> HomePageTracking.eventClickSeeAllDynamicChannel(
                    context,
                    DynamicLinkHelper.getActionLink(channel.header),
                    channel.id)
        }
    }

    override fun getViewHolderClassName(): String {
        return ProductOrganicChannelViewHolder::class.java.simpleName
    }

    override fun setupContent(channel: DynamicHomeChannel.Channels) {
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recycleList)

        if (recyclerView.itemDecorationCount == 0) recyclerView.addItemDecoration(
                GridSpacingItemDecoration(defaultSpanCount,
                itemView.getContext().getResources().getDimensionPixelSize(R.dimen.dp_10),
                true))

        recyclerView.layoutManager = GridLayoutManager(
                itemView.context,
                defaultSpanCount,
                GridLayoutManager.VERTICAL, false)

        recyclerView.adapter = OrganicAdapter(context, homeCategoryListener, channel, getLayoutType(channel), countDownView)
    }

    class OrganicAdapter(private val context: Context,
                         private val listener: HomeCategoryListener,
                         private val channels: DynamicHomeChannel.Channels,
                         private val sprintType: Int,
                         private val countDownView: CountDownView) : RecyclerView.Adapter<OrganicViewHolder>() {
        private var grids: Array<DynamicHomeChannel.Grid> = channels.grids

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrganicViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_organic_product_item_simple, parent, false)
            return OrganicViewHolder(v)
        }

        override fun onBindViewHolder(holder: OrganicViewHolder, position: Int) {
            try {
                val grid = grids[position]
//                ImageHandler.loadImageThumbs(holder.context, holder.channelImage1, grid.imageUrl)
                HomeImageHandler.loadImage(holder.context, holder.channelImage1, grid.imageUrl)
                holder.channelName.displayTextOrHide(grid.name)
                holder.channelPrice1.displayTextOrHide(grid.price)
                holder.channelDiscount1.displayTextOrHide(grid.discount)
                holder.channelBeforeDiscPrice1.displayTextOrHide(grid.slashedPrice)
                holder.channelCashback.displayTextOrHide(grid.cashback)
                holder.channelBeforeDiscPrice1.paintFlags = holder.channelBeforeDiscPrice1.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                holder.itemContainer1.setOnClickListener {
                    var attr = ""
                    when(sprintType){
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

                    listener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(grid))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        override fun getItemCount(): Int {
            return grids.size
        }
    }

    class OrganicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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
