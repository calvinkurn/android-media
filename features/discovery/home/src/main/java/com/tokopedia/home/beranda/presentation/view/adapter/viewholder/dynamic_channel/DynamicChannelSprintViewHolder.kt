package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.DynamicLinkHelper
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.GridSpacingItemDecoration
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils
import com.tokopedia.home.beranda.presentation.view.customview.ThematicCardView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.UnifyButton
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
    private val defaultSpanCount = 3

    override fun onSeeAllClickTracker(channel: DynamicHomeChannel.Channels, applink: String) {
        HomePageTracking.eventClickSeeAllProductSprint(context, channel.id)
    }

    override fun getViewHolderClassName(): String {
        return DynamicChannelSprintViewHolder::class.java.simpleName
    }

    override fun setupContent(channel: DynamicHomeChannel.Channels) {
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recycleList)
        val backgroundThematic: ImageView = itemView.findViewById(R.id.background_thematic)
        val seeAllButton: UnifyButton = itemView.findViewById(R.id.see_all_button_unify)
        val seeAllButtonText: TextView = itemView.findViewById(R.id.see_all_button)

        if(channel.header.backImage.isNotBlank()) {
            val channelTitle: Typography = itemView.findViewById(R.id.channel_title)
            channelTitle.setTextColor(ContextCompat.getColor(channelTitle.context, R.color.white))
            backgroundThematic.show()
            seeAllButton.show()
            seeAllButtonText.hide()
            seeAllButton.setOnClickListener {
                homeCategoryListener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(channel.header))
                HomeTrackingUtils.homeDiscoveryWidgetViewAll(context,
                        DynamicLinkHelper.getActionLink(channel.header))
                onSeeAllClickTracker(channel, DynamicLinkHelper.getActionLink(channel.header))

            }
            Glide.with(context)
                    .load(channel.header.backImage)
                    .override(200, 200)
                    .into(backgroundThematic);
        }else {
            seeAllButton.hide()
            seeAllButtonText.show()
            backgroundThematic.hide()
        }

        if (recyclerView.itemDecorationCount == 0) recyclerView.addItemDecoration(
                GridSpacingItemDecoration(defaultSpanCount,
                itemView.getContext().getResources().getDimensionPixelSize(R.dimen.dp_8),
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
                holder.thematicCardView.apply {
                    setImageProductVisible(true)
                    setPriceVisible(true)
                    setSlashedPriceVisible(true)
                    setLabelDiscountVisible(true)
                    setLabelPromoVisible(grid.cashback.isNotEmpty())
                    setImageProductUrl(grid.imageUrl)
                    setProductNameText(grid.name)
                    setPriceText(grid.price)
                    setLabelDiscountText(grid.discount)
                    setSlashedPriceText(grid.slashedPrice)
                    setLabelPromoText(grid.cashback)
                    setLabelPromoType(ThematicCardView.LIGHT_RED)
                    setOnClickListener {
                        HomePageTracking.eventEnhancedClickSprintSaleProduct(context,
                                channels.getEnhanceClickSprintSaleHomePage(position, countDownView.currentCountDown))
                        listener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(grid))
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

    class SprintViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thematicCardView: ThematicCardView = view.findViewById(R.id.thematic_card)
        val context: Context
            get() = itemView.context
    }
}
