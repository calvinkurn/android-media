package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTrackingV2
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.DynamicLinkHelper
import com.tokopedia.home.beranda.helper.glide.FPM_RECOMMENDATION_LIST_CAROUSEL
import com.tokopedia.home.beranda.helper.glide.loadImage
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelViewModel
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.GridSpacingItemDecoration
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.LinearHorizontalSpacingDecoration
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.SimpleHorizontalLinearLayoutDecoration
import com.tokopedia.home.beranda.presentation.view.analytics.HomeTrackingUtils
import com.tokopedia.track.TrackApp
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.home_dc_list_carousel.view.*
import kotlinx.android.synthetic.main.home_master_dynamic_channel.view.*

class RecommendationListCarouselViewHolder(itemView: View,
                                           private val listener: HomeCategoryListener,
                                           private val countDownViewListener: CountDownView.CountDownListener,
                                           private val parentRecycledViewPool: RecyclerView.RecycledViewPool): DynamicChannelViewHolder(
        itemView, listener, countDownViewListener) {
    override fun setupContent(channel: DynamicHomeChannel.Channels) {
        val listCarouselTitle = itemView.findViewById<Typography>(R.id.list_carousel_title)
        val listCarouselDescription = itemView.findViewById<Typography>(R.id.list_carousel_description)
        val listCarouselView = itemView.findViewById<View>(R.id.list_carousel_view)
        val listCarouselRecyclerView = itemView.findViewById<RecyclerView>(R.id.recycleList)
        val listCarouselBannerHeader = itemView.findViewById<View>(R.id.list_carousel_banner_header)

        val banner = channel.banner
        banner.let {
            val textColor = if (banner.textColor.isEmpty())
                ContextCompat.getColor(itemView.context, R.color.Neutral_N50) else Color.parseColor(banner.textColor)

            listCarouselTitle.apply {
                text = banner.title
                setTextColor(textColor)
            }
            listCarouselDescription.apply {
                text = banner.description
                setTextColor(textColor)
            }
            if (banner.backColor.isNotEmpty()) {
                val backColor = Color.parseColor(banner.backColor)
                listCarouselView.setBackgroundColor(backColor)
                listCarouselView.visibility = View.VISIBLE
            } else listCarouselView.visibility = View.GONE

            if(banner.title.isEmpty()) listCarouselBannerHeader.visibility = View.GONE
            else listCarouselBannerHeader.visibility = View.VISIBLE
        }

        channel.let {channel->
            listCarouselRecyclerView.apply {
                layoutManager = if (channel.grids.size > 1) {
                    LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                } else {
                    GridLayoutManager(itemView.context, 1)
                }
                adapter = RecommendationListAdapter(channel, listener)
                setRecycledViewPool(parentRecycledViewPool)
                clearItemRecyclerViewDecoration(this)
                if (channel.grids.size > 1) {
                    addItemDecoration(SimpleHorizontalLinearLayoutDecoration())
                }
            }
        }
    }

    override fun getViewHolderClassName(): String {
        return RecommendationListCarouselViewHolder::class.java.name
    }

    override fun onSeeAllClickTracker(channel: DynamicHomeChannel.Channels, applink: String) {
        HomePageTrackingV2.RecommendationList.sendRecommendationListSeeAllClick(channel)
    }

    private fun clearItemRecyclerViewDecoration(itemRecyclerView: RecyclerView) {
        while (itemRecyclerView.itemDecorationCount > 0) {
            itemRecyclerView.removeItemDecorationAt(0)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_dc_list_carousel
    }

    class HomeRecommendationListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val recommendationImage = itemView.findViewById<ImageView>(R.id.recommendation_list_product_image)
        val recommendationTitle = itemView.findViewById<Typography>(R.id.recommendation_list_title)
        val recommendationDiscountLabel = itemView.findViewById<Label>(R.id.recommendation_list_label_discount)
        val recommendationSlashedPrice = itemView.findViewById<Typography>(R.id.recommendation_list_slashed_price)
        val recommendationPrice = itemView.findViewById<Typography>(R.id.recommendation_list_product_price)
    }

    class RecommendationListAdapter(val channel: DynamicHomeChannel.Channels,
                                    val homeCategoryListener: HomeCategoryListener): RecyclerView.Adapter<HomeRecommendationListViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRecommendationListViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = if (channel.grids.size > 1) {
                inflater.inflate(R.layout.home_recommendation_list_card_carousel, parent, false)
            } else inflater.inflate(R.layout.home_recommendation_list_card, parent, false)
            return HomeRecommendationListViewHolder(view)
        }

        override fun getItemCount(): Int {
            return channel.grids.size
        }

        override fun onBindViewHolder(holder: HomeRecommendationListViewHolder, position: Int) {
            if (channel.grids.size > position) {
                val grid = channel.grids[position]
                holder.recommendationImage.loadImage(grid.imageUrl, FPM_RECOMMENDATION_LIST_CAROUSEL)
                holder.recommendationTitle.text = grid.name
                holder.recommendationDiscountLabel.text = grid.discount
                holder.recommendationSlashedPrice.text = grid.slashedPrice
                holder.recommendationPrice.text = grid.price
                holder.itemView.setOnClickListener {
                    HomePageTrackingV2.RecommendationList.sendRecommendationListClick(channel, grid, position)
                    homeCategoryListener.onSectionItemClicked(grid.applink)
                }
            }
        }
    }
}