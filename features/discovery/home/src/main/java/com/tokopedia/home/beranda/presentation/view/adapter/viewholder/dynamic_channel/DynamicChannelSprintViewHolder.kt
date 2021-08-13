package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTrackingV2
import com.tokopedia.home.beranda.data.mapper.factory.toProductCardModel
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.DynamicLinkHelper
import com.tokopedia.home.beranda.helper.toStringFormat
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.GridSpacingItemDecoration
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by devarafikry on 12/08/19.
 */

class DynamicChannelSprintViewHolder(sprintView: View,
                                     private val homeCategoryListener: HomeCategoryListener,
                                     private val parentRecycledViewPool: RecyclerView.RecycledViewPool) :
        DynamicChannelViewHolder(
                sprintView, homeCategoryListener
        ) {

    private var adapter: SprintAdapter? = null
    val recyclerView: RecyclerView = itemView.findViewById(R.id.recycleList)
    private val backgroundThematic: ImageView = itemView.findViewById(R.id.background_thematic)

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_dc_lego_sprint
    }

    val context: Context = sprintView.context
    private val defaultSpanCount = 3

    override fun onSeeAllClickTracker(channel: DynamicHomeChannel.Channels, applink: String) {
        HomePageTrackingV2.SprintSale.sendSprintSaleSeeAllClick(channel)
    }

    override fun getViewHolderClassName(): String {
        return DynamicChannelSprintViewHolder::class.java.simpleName
    }

    override fun setupContent(channel: DynamicHomeChannel.Channels) {
        recyclerView.setRecycledViewPool(parentRecycledViewPool)
        recyclerView.setHasFixedSize(true)
        if (recyclerView.itemDecorationCount == 0) recyclerView.addItemDecoration(
                GridSpacingItemDecoration(defaultSpanCount,
                        itemView.context.resources.getDimensionPixelSize(R.dimen.dp_4),
                        false))
        recyclerView.layoutManager = GridLayoutManager(
                itemView.context,
                defaultSpanCount,
                GridLayoutManager.VERTICAL, false)
        backgroundThematic.loadImageWithoutPlaceholder(channel.header.backImage)
        mappingHeader(channel)
        mappingGrid(channel)
    }

    override fun setupContent(channel: DynamicHomeChannel.Channels, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty()) {
            payloads.forEach { payload->
                if (payload == DynamicChannelDataModel.HOME_RV_SPRINT_BG_IMAGE_URL) {
                    channel.let {
                        backgroundThematic.loadImageWithoutPlaceholder(channel.header.backImage)
                    }
                }
            }
        }

        channel.let {
            mappingHeader(it)
            mappingGrid(it)
        }
    }

    private fun mappingHeader(channel: DynamicHomeChannel.Channels) {
        if (channel.header.backImage.isNotBlank()) {
            val channelTitle: Typography = itemView.findViewById(R.id.channel_title)
            channelTitle.setTextColor(ContextCompat.getColor(channelTitle.context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
            backgroundThematic.show()
            seeAllButtonUnify?.setOnClickListener {
                homeCategoryListener.onDynamicChannelClicked(DynamicLinkHelper.getActionLink(channel.header))
                HomeTrackingUtils.homeDiscoveryWidgetViewAll(context,
                        DynamicLinkHelper.getActionLink(channel.header))
                onSeeAllClickTracker(channel, DynamicLinkHelper.getActionLink(channel.header))

            }
        }
    }

    private fun mappingGrid(channel: DynamicHomeChannel.Channels) {
        if (adapter == null) {
            adapter = SprintAdapter(homeCategoryListener,
                    channel,
                    countDownView)
            recyclerView.adapter = adapter
        } else {
            adapter?.setItems(channel)
        }
    }

    class SprintAdapter(private val listener: HomeCategoryListener,
                        private val channels: DynamicHomeChannel.Channels,
                        private val countDownView: TimerUnifySingle?) : RecyclerView.Adapter<SprintViewHolder>() {
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
                    applyCarousel()
                    setProductModel(grid.toProductCardModel())
                    setOnClickListener {
                        HomePageTrackingV2.SprintSale.sendSprintSaleClick(channels, countDownView?.targetDate?.toStringFormat() ?: "", grid, position)
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
        val thematicCardView: ProductCardGridView = view.findViewById(R.id.thematic_card)
        val context: Context
            get() = itemView.context
    }
}
