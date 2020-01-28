package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.DynamicLinkHelper
import com.tokopedia.home.beranda.helper.glide.CrossFadeFactory
import com.tokopedia.home.beranda.helper.glide.loadImageCenterCrop
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.GridSpacingItemDecoration
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils
import com.tokopedia.home.beranda.presentation.view.customview.ThematicCardView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by devarafikry on 12/08/19.
 */

class DynamicChannelSprintViewHolder(sprintView: View,
                                     private val homeCategoryListener: HomeCategoryListener,
                                     countDownListener: CountDownView.CountDownListener,
                                     private val parentRecycledViewPool: RecyclerView.RecycledViewPool) :
        DynamicChannelViewHolder(
                sprintView, homeCategoryListener, countDownListener
        ) {

    private var adapter: SprintAdapter? = null
    val recyclerView: RecyclerView = itemView.findViewById(R.id.recycleList)
    val backgroundThematic: ImageView = itemView.findViewById(R.id.background_thematic)
    val seeAllButton: UnifyButton = itemView.findViewById(R.id.see_all_button_unify)
    val seeAllButtonText: TextView = itemView.findViewById(R.id.see_all_button)

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
        recyclerView.setRecycledViewPool(parentRecycledViewPool)
        recyclerView.setHasFixedSize(true)
        if (recyclerView.itemDecorationCount == 0) recyclerView.addItemDecoration(
                GridSpacingItemDecoration(defaultSpanCount,
                        itemView.getContext().getResources().getDimensionPixelSize(R.dimen.dp_8),
                        true))
        recyclerView.layoutManager = GridLayoutManager(
                itemView.context,
                defaultSpanCount,
                GridLayoutManager.VERTICAL, false)
        backgroundThematic.loadImageCenterCrop(channel.header.backImage)
        mappingHeader(channel)
        mappingGrid(channel)
    }

    override fun bind(element: DynamicChannelViewModel?, payloads: MutableList<Any>) {
        val channel = element?.channel

        if (payloads.isNotEmpty()) {
            payloads.forEach { payload->
                if (payload == DynamicChannelViewModel.HOME_RV_SPRINT_BG_IMAGE_URL) {
                    channel?.let {
                        backgroundThematic.loadImageCenterCrop(channel.header.backImage)
                    }
                }
            }
        }

        channel?.let {
            mappingHeader(it)
            mappingGrid(it)
        }
    }

    private fun mappingHeader(channel: DynamicHomeChannel.Channels) {
        if (channel.header.backImage.isNotBlank()) {
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
        } else {
            seeAllButton.hide()
            seeAllButtonText.show()
            backgroundThematic.hide()
        }
    }

    private fun mappingGrid(channel: DynamicHomeChannel.Channels) {
        if (adapter == null) {
            adapter = SprintAdapter(context,
                    homeCategoryListener,
                    channel,
                    getLayoutType(channel),
                    countDownView,
                    channel.showPromoBadge ?: false,
                    computeBlankSpaceConfig(channel))
            recyclerView.adapter = adapter
        } else {
            adapter?.setItems(channel)
        }
    }

    private fun computeBlankSpaceConfig(channel: DynamicHomeChannel.Channels?): BlankSpaceConfig {
        val blankSpaceConfig = BlankSpaceConfig(
                twoLinesProductName = true
        )
        channel?.grids?.forEach {
            if (it.freeOngkir.isActive) blankSpaceConfig.freeOngkir = true
            if (it.slashedPrice.isNotEmpty()) blankSpaceConfig.slashedPrice = true
            if (it.price.isNotEmpty()) blankSpaceConfig.price = true
            if (it.discount.isNotEmpty()) blankSpaceConfig.discountPercentage = true
            if (it.name.isNotEmpty()) blankSpaceConfig.productName = true
        }
        return blankSpaceConfig
    }

    class SprintAdapter(private val context: Context,
                             private val listener: HomeCategoryListener,
                             private val channels: DynamicHomeChannel.Channels,
                             private val sprintType: Int,
                             private val countDownView: CountDownView,
                             private val isFreeOngkir: Boolean,
                             private val blankSpaceConfig: BlankSpaceConfig) : RecyclerView.Adapter<SprintViewHolder>() {
        private var grids: Array<DynamicHomeChannel.Grid> = channels.grids

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SprintViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_sprint_product_item_simple, parent, false)
            return SprintViewHolder(v)
        }

        override fun getItemViewType(position: Int): Int {
            return R.layout.layout_sprint_product_item_simple
        }

        override fun onBindViewHolder(holder: SprintViewHolder, position: Int) {
            try {
                val grid = grids[position]
                holder.thematicCardView.run {
                    setItemWithWrapBlankSpaceConfig(grid, blankSpaceConfig)
                    setOnClickListener {
                        HomePageTracking.eventEnhancedClickSprintSaleProduct(context,
                                channels.getEnhanceClickSprintSaleHomePage(position, countDownView.currentCountDown, grid.freeOngkir.isActive))
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

        fun setItems(channel: DynamicHomeChannel.Channels) {
            grids = channel.grids
            notifyDataSetChanged()
        }
    }

    class SprintViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thematicCardView: ThematicCardView = view.findViewById(R.id.thematic_card)
        val context: Context
            get() = itemView.context
    }
}
