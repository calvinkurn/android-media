package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTrackingV2
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.SimpleHorizontalLinearLayoutDecoration
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardViewList
import com.tokopedia.unifyprinciples.Typography

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

            if (banner.title.isNotEmpty()) {
                listCarouselTitle.apply {
                    text = banner.title
                    setTextColor(textColor)
                    visibility = View.VISIBLE
                }
            } else listCarouselTitle.visibility = View.GONE

            if (banner.description.isNotEmpty()) {
                listCarouselDescription.apply {
                    text = banner.description
                    setTextColor(textColor)
                    visibility = View.VISIBLE
                }
            } else listCarouselDescription.visibility = View.GONE

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
                adapter = RecommendationListAdapter(channel.grids.map {
                    HomeRecommendationListData(
                            it.imageUrl,
                            it.name,
                            it.discount,
                            it.slashedPrice,
                            it.price,
                            it.applink,
                            channel.grids.size > 1,
                            channel,
                            it
                    )
                }, listener)
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
        val recommendationCard = itemView.findViewById<ProductCardViewList>(R.id.productCardView)
    }

    data class HomeRecommendationListData(
            val recommendationImageUrl: String,
            val recommendationTitle: String,
            val recommendationDiscountLabel: String,
            val recommendationSlashedPrice: String,
            val recommendationPrice: String,
            val recommendationApplink: String,
            val isCarousel: Boolean,
            val channel: DynamicHomeChannel.Channels,
            val grid: DynamicHomeChannel.Grid
    )

    class RecommendationListAdapter(val recommendationList: List<HomeRecommendationListData>,
                                    val homeCategoryListener: HomeCategoryListener): RecyclerView.Adapter<HomeRecommendationListViewHolder>() {
        val LAYOUT_TYPE_CAROUSEL = 87
        val LAYOUT_TYPE_NON_CAROUSEL = 90
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRecommendationListViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = if (viewType == LAYOUT_TYPE_CAROUSEL) {
                inflater.inflate(R.layout.home_recommendation_list_card_carousel, parent, false)
            } else inflater.inflate(R.layout.home_recommendation_list_card, parent, false)
            return HomeRecommendationListViewHolder(view)
        }

        override fun getItemCount(): Int {
            return recommendationList.size
        }

        override fun getItemViewType(position: Int): Int {
            return if (recommendationList[position].isCarousel) LAYOUT_TYPE_CAROUSEL else LAYOUT_TYPE_NON_CAROUSEL
        }

        override fun onBindViewHolder(holder: HomeRecommendationListViewHolder, position: Int) {
            if (recommendationList.size > position) {
                val recommendation = recommendationList[position]
                holder.recommendationCard.setProductModel(
                        ProductCardModel(
                                productImageUrl = recommendation.recommendationImageUrl,
                                productName = recommendation.recommendationTitle,
                                discountPercentage = recommendation.recommendationDiscountLabel,
                                slashedPrice = recommendation.recommendationSlashedPrice,
                                formattedPrice = recommendation.recommendationPrice
                        )
                )
                holder.itemView.setOnClickListener {
                    HomePageTrackingV2.RecommendationList.sendRecommendationListClick(recommendation.channel, recommendation.grid, position)
                    homeCategoryListener.onSectionItemClicked(recommendation.recommendationApplink)
                }
            }
        }
    }
}