package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
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
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.thematic_card_view.view.*

/**
 * Created by devarafikry on 12/08/19.
 */

class DynamicChannelSprintViewHolder(sprintView: View,
                                     private val homeCategoryListener: HomeCategoryListener,
                                     countDownListener: CountDownView.CountDownListener) :
        DynamicChannelViewHolder(
                sprintView, homeCategoryListener, countDownListener
        ) {

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
                    .into(backgroundThematic)
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

        measureParentView(homeCategoryListener.getWindowWidth(), recyclerView)
        recyclerView.adapter = SprintAdapter(context,
                homeCategoryListener,
                channel,
                getLayoutType(channel),
                countDownView,
                channel.showPromoBadge?:false,
                getMaxProductCardContentHeight(getLayoutType(channel), channel.grids))
    }

    class SprintAdapter(private val context: Context,
                             private val listener: HomeCategoryListener,
                             private val channels: DynamicHomeChannel.Channels,
                             private val sprintType: Int,
                             private val countDownView: CountDownView,
                             private val isFreeOngkir: Boolean,
                        private val maxHeight: Int) : RecyclerView.Adapter<SprintViewHolder>() {
        private var grids: Array<DynamicHomeChannel.Grid> = channels.grids

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SprintViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_sprint_product_item_simple, parent, false)
            val cardThematic = v.findViewById<ThematicCardView>(R.id.thematic_card)
            val cardProduct = cardThematic.cardViewProductCard
            val layoutParams = cardProduct.layoutParams
            layoutParams.height = maxHeight
            cardProduct.layoutParams = layoutParams
            return SprintViewHolder(v)
        }

        override fun onBindViewHolder(holder: SprintViewHolder, position: Int) {
            try {
                val grid = grids[position]
                holder.thematicCardView.run {
                    initProductImage(grid.imageUrl)
                    initProductName(grid.name)
                    initProductPrice(grid.price)
                    initLabelDiscount(grid.discount)
                    initSlashedPrice(grid.slashedPrice)
                    initLabelPromo(grid.cashback, ThematicCardView.LIGHT_RED)
                    setBlankSpaceConfig(BlankSpaceConfig(freeOngkir = isFreeOngkir))
                    initFreeOngkir(grid.freeOngkir.isActive, grid.freeOngkir.imageUrl)
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
    }

    class SprintViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thematicCardView: ThematicCardView = view.findViewById(R.id.thematic_card)
        val context: Context
            get() = itemView.context
    }

    private fun measureParentView(deviceWidth: Int, parentView: View) {
        parentView.measure(
                View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.UNSPECIFIED
        )
    }

    private fun getMaxProductCardContentHeight(channelType: Int, grids: Array<DynamicHomeChannel.Grid>) : Int {
        var contentHeight = 0
        recyclerView?.let {
            var layoutViewHolder = R.layout.layout_sprint_product_item_simple
            /**
             * Inflate product card for measuring purpose
             */
            val productCardViewHolder = LayoutInflater
                    .from(it.context)
                    .inflate(layoutViewHolder, it.parent as ViewGroup, false)
            val targetLayoutManager = it.layoutManager
            val eachSpanSize = if(channelType == TYPE_BANNER)
                getSizeForEachSpan(recyclerView.measuredWidth, (targetLayoutManager as GridLayoutManager).spanCount) else
                it.context.resources.getDimensionPixelOffset(R.dimen.home_banner_mix_carousel_width)

            /**
             * Compare product card in list
             */

            /**
             * Map first to product visitables
             */
            grids.forEach {
                val sampleProductCard = productCardViewHolder.findViewById<ThematicCardView>(R.id.thematic_card)
                try {
                    val grid = it
                    sampleProductCard.run {
                        initProductImage(grid.imageUrl)
                        initProductName(grid.name)
                        initProductPrice(grid.price)
                        initLabelDiscount(grid.discount)
                        initSlashedPrice(grid.slashedPrice)
                        initLabelPromo(grid.cashback, ThematicCardView.LIGHT_RED)
                        initFreeOngkir(grid.freeOngkir.isActive, grid.freeOngkir.imageUrl)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                /**
                 * Measure product card after setProductModel
                 * to ensure product card have final height
                 *
                 * MeasureSpec.EXACTLY is used to ensure this product
                 * card have width EXACTLY as given eachSpanSize
                 */
                productCardViewHolder.measure(
                        View.MeasureSpec.makeMeasureSpec(eachSpanSize, View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.UNSPECIFIED)
                val measuredContentHeight = sampleProductCard?.measuredHeight?:0
                if (contentHeight < measuredContentHeight) {
                    contentHeight = measuredContentHeight
                }

            }
        }
        return contentHeight
    }

    private fun getSizeForEachSpan(maxWidth: Int, spanCount: Int) : Int {
        return maxWidth/spanCount
    }
}
