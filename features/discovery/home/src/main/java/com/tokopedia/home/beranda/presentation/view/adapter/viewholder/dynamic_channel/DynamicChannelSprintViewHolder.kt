package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
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
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.GridSpacingItemDecoration
import com.tokopedia.home.beranda.presentation.view.customview.ThematicCardView
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.tokopoints.view.util.ImageUtil
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by devarafikry on 12/08/19.
 */

class DynamicChannelSprintViewHolder(sprintView: View,
                                     private val homeCategoryListener: HomeCategoryListener,
                                     countDownListener: CountDownView.CountDownListener) :
        DynamicChannelViewHolder(
                sprintView, homeCategoryListener, countDownListener
        ) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_dc_lego_sprint
    }

    val context: Context = sprintView.context
    val defaultSpanCount = 3

    override fun onSeeAllClickTracker(channel: DynamicHomeChannel.Channels, applink: String) {
        HomePageTracking.eventClickSeeAllProductSprint(context, channel.id)
    }

    override fun getViewHolderClassName(): String {
        return DynamicChannelSprintViewHolder::class.java.simpleName
    }

    override fun setupContent(channel: DynamicHomeChannel.Channels) {
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recycleList)
        val backgroundThematic: ImageView = itemView.findViewById(R.id.background_thematic)
        val title: Typography = itemView.findViewById(R.id.heading_3)

        if(channel.header.backImage.isNotBlank()) {
            ImageUtil.loadImage(backgroundThematic, channel.header.backImage)
            title.setTextColor(
                    if(channel.header.textColor.isNotEmpty()) Color.parseColor(channel.header.textColor)
                    else ContextCompat.getColor(title.context, R.color.white)
            )
        }

        if (recyclerView.itemDecorationCount == 0) recyclerView.addItemDecoration(
                GridSpacingItemDecoration(defaultSpanCount,
                itemView.getContext().getResources().getDimensionPixelSize(R.dimen.dp_10),
                true))

        recyclerView.layoutManager = GridLayoutManager(
                itemView.context,
                defaultSpanCount,
                GridLayoutManager.VERTICAL, false)

        recyclerView.adapter = SprintAdapter(context, homeCategoryListener, channel, getLayoutType(channel), countDownView)
    }

    class SprintAdapter(private val context: Context,
                             private val listener: HomeCategoryListener,
                             private val channels: DynamicHomeChannel.Channels,
                             private val sprintType: Int,
                             private val countDownView: CountDownView) : RecyclerView.Adapter<SprintViewHolder>() {
        private var grids: Array<DynamicHomeChannel.Grid> = channels.grids

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SprintViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_sprint_product_item_simple, parent, false)
            return SprintViewHolder(v)
        }

        override fun onBindViewHolder(holder: SprintViewHolder, position: Int) {
            try {
                val grid = grids[position]

                holder.thematicCardView.setImageSrc(grid.imageUrl)
                holder.thematicCardView.setName(grid.name)
                holder.thematicCardView.setPrice(grid.price)
                holder.thematicCardView.setDiscount(grid.discount)
                holder.thematicCardView.setSlashedPrice(grid.slashedPrice)
                holder.thematicCardView.setCashback(grid.cashback)
                holder.thematicCardView.setOnClickListener {
                    HomePageTracking.eventEnhancedClickSprintSaleProduct(context,
                            channels.getEnhanceClickSprintSaleHomePage(position, countDownView.currentCountDown))
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

    class SprintViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thematicCardView: ThematicCardView = view.findViewById(R.id.thematic_card)
        val context: Context
            get() = itemView.context
    }
}
