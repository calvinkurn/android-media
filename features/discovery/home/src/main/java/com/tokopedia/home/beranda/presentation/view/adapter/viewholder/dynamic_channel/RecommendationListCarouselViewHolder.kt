package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTrackingV2
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.helper.glide.FPM_RECOMMENDATION_LIST_CAROUSEL
import com.tokopedia.home.beranda.helper.glide.loadImage
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.track.TrackApp
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

class RecommendationListCarouselViewHolder(itemView: View,
                                           private val listener: HomeCategoryListener,
                                           countDownViewListener: CountDownView.CountDownListener,
                                           private val parentRecycledViewPool: RecyclerView.RecycledViewPool) : DynamicChannelViewHolder(
        itemView, listener, countDownViewListener
) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_dc_lego_product
    }

    override fun setupContent(channel: DynamicHomeChannel.Channels) {
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recycleList)
        recyclerView.setRecycledViewPool(parentRecycledViewPool)

        if (channel.grids.size > 1) {
            recyclerView.layoutManager = LinearLayoutManager(
                    itemView.context, LinearLayoutManager.HORIZONTAL, false)
        } else {
            recyclerView.layoutManager = GridLayoutManager(itemView.context, 1)
        }

        recyclerView.adapter = RecommendationListAdapter(channel, listener)
    }

    override fun getViewHolderClassName(): String {
        return RecommendationListCarouselViewHolder::class.java.simpleName
    }

    override fun onSeeAllClickTracker(channel: DynamicHomeChannel.Channels, applink: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
                HomePageTrackingV2.RecommendationList.getRecommendationListSeeAllClick(channel)
        )
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
            val view = inflater.inflate(R.layout.home_recommendation_list_card, parent, false)
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
                    homeCategoryListener.onSectionItemClicked(grid.applink)
                }
            }
        }
    }
}