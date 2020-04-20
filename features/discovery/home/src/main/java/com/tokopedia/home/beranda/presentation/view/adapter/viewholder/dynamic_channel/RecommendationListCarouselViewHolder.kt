package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.R
import com.tokopedia.home.analytics.HomePageTrackingV2
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.itemdecoration.SimpleHorizontalLinearLayoutDecoration
import com.tokopedia.home.util.setGradientBackground
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class RecommendationListCarouselViewHolder(itemView: View,
                                           private val listener: HomeCategoryListener,
                                           private val parentRecycledViewPool: RecyclerView.RecycledViewPool): DynamicChannelViewHolder(
        itemView, listener) {
    override fun setupContent(channel: DynamicHomeChannel.Channels) {
        val listCarouselTitle = itemView.findViewById<Typography>(R.id.list_carousel_title)
        val listCarouselDescription = itemView.findViewById<Typography>(R.id.list_carousel_description)
        val listCarouselView = itemView.findViewById<View>(R.id.list_carousel_view)
        val listCarouselRecyclerView = itemView.findViewById<RecyclerView>(R.id.recycleList)
        val listCarouselBannerHeader = itemView.findViewById<View>(R.id.list_carousel_banner_header)
        val listCarouselCloseButton = itemView.findViewById<AppCompatImageView>(R.id.buy_again_close_image_view)

        val banner = channel.banner
        banner.let {
            val textColor = if (banner.textColor.isEmpty())
                ContextCompat.getColor(itemView.context, R.color.Neutral_N50) else Color.parseColor(banner.textColor)
            if(channel.hasCloseButton){
                listCarouselCloseButton.show()
                listCarouselCloseButton.setOnClickListener {
                    listener.onBuyAgainCloseChannelClick(channel, adapterPosition)
                }
            }else {
                listCarouselCloseButton.hide()
            }

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

            if(banner.gradientColor.isNotEmpty()){
                listCarouselView.visibility = View.VISIBLE
                listCarouselView.setGradientBackground(banner.gradientColor)
            } else {
                if (banner.backColor.isNotEmpty()) {
                    val backColor = Color.parseColor(banner.backColor)
                    listCarouselView.setBackgroundColor(backColor)
                    listCarouselView.visibility = View.VISIBLE
                } else listCarouselView.visibility = View.GONE
            }

            if(banner.title.isEmpty()) listCarouselBannerHeader.visibility = View.GONE
            else listCarouselBannerHeader.visibility = View.VISIBLE
        }

        channel.let { channel->
            listCarouselRecyclerView.apply {
                layoutManager = if (channel.grids.size > 1) {
                    LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                } else {
                    GridLayoutManager(itemView.context, 1)
                }
                val newList : MutableList<HomeRecommendationListCarousel> = channel.grids.map {
                    HomeRecommendationListData(
                            it.imageUrl,
                            it.name,
                            it.discount,
                            it.slashedPrice,
                            it.price,
                            it.applink,
                            channel.grids.size > 1,
                            channel,
                            it,
                            listener
                    )
                }.toMutableList()
                if(channel.grids.size > 1 && channel.header.applink.isNotEmpty()) newList.add(HomeRecommendationListSeeMoreData(channel, listener))
                adapter = RecommendationListAdapter(newList)
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

    abstract class RecommendationListCarouselItem(itemView: View): RecyclerView.ViewHolder(itemView){
        abstract fun bind(homeRecommendationListData: HomeRecommendationListCarousel)
    }

    class HomeRecommendationListViewHolder(
            itemView: View
    ): RecommendationListCarouselItem(itemView) {
        private val recommendationCard = itemView.findViewById<ProductCardListView>(R.id.productCardView)

        override fun bind(recommendation: HomeRecommendationListCarousel) {
            if(recommendation is HomeRecommendationListData) {
                recommendationCard.setProductModel(
                        ProductCardModel(
                                productImageUrl = recommendation.recommendationImageUrl,
                                productName = recommendation.recommendationTitle,
                                discountPercentage = recommendation.recommendationDiscountLabel,
                                slashedPrice = recommendation.recommendationSlashedPrice,
                                formattedPrice = recommendation.recommendationPrice,
                                hasAddToCartButton = recommendation.channel.hasCloseButton
                        )
                )
                val addToCartButton = recommendationCard.findViewById<UnifyButton>(R.id.buttonAddToCart)
                addToCartButton.text = itemView.context.getString(R.string.home_buy_again)
                recommendationCard.setAddToCartOnClickListener {
                    recommendation.listener.onBuyAgainOneClickCheckOutClick(recommendation.grid, recommendation.channel, position)
                }
                itemView.setOnClickListener {
                    HomePageTrackingV2.RecommendationList.sendRecommendationListClick(recommendation.channel, recommendation.grid, position, recommendation.listener.userId)
                    recommendation.listener.onSectionItemClicked(recommendation.recommendationApplink)
                }
            }
        }
    }

    class HomeRecommendationSeeMoreViewHolder(
            itemView: View
    ): RecommendationListCarouselItem(itemView){

        private val container: View by lazy { itemView.findViewById<View>(R.id.container_banner_mix_more) }
        override fun bind(homeRecommendationListData: HomeRecommendationListCarousel) {
            if(homeRecommendationListData is HomeRecommendationListSeeMoreData) {
                container.setOnClickListener {
                    HomePageTrackingV2.RecommendationList.sendRecommendationListSeeAllClick(homeRecommendationListData.channel)
                    homeRecommendationListData.listener.onDynamicChannelClicked(applink = homeRecommendationListData.channel.header.applink)
                }
            }
        }

    }

    interface HomeRecommendationListCarousel
    data class HomeRecommendationListData(
            val recommendationImageUrl: String,
            val recommendationTitle: String,
            val recommendationDiscountLabel: String,
            val recommendationSlashedPrice: String,
            val recommendationPrice: String,
            val recommendationApplink: String,
            val isCarousel: Boolean,
            val channel: DynamicHomeChannel.Channels,
            val grid: DynamicHomeChannel.Grid,
            val listener: HomeCategoryListener
    ): HomeRecommendationListCarousel

    data class HomeRecommendationListSeeMoreData(
            val channel: DynamicHomeChannel.Channels,
            val listener: HomeCategoryListener
    ): HomeRecommendationListCarousel

    class RecommendationListAdapter(private val recommendationList: List<HomeRecommendationListCarousel>): RecyclerView.Adapter<RecommendationListCarouselItem>() {
        companion object{
            private const val LAYOUT_TYPE_CAROUSEL = 87
            private const val LAYOUT_TYPE_NON_CAROUSEL = 90
            private const val LAYOUT_SEE_ALL_BUTTON = 91
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationListCarouselItem {
            val inflater = LayoutInflater.from(parent.context)
            return when (viewType) {
                LAYOUT_TYPE_CAROUSEL -> HomeRecommendationListViewHolder(inflater.inflate(R.layout.home_recommendation_list_card_carousel, parent, false))
                LAYOUT_SEE_ALL_BUTTON -> HomeRecommendationSeeMoreViewHolder(inflater.inflate(R.layout.home_banner_item_carousel_see_more, parent, false))
                else -> HomeRecommendationListViewHolder(inflater.inflate(R.layout.home_recommendation_list_card, parent, false))
            }
        }

        override fun getItemCount(): Int {
            return recommendationList.size
        }

        override fun getItemViewType(position: Int): Int {
            return if(recommendationList[position] is HomeRecommendationListSeeMoreData) LAYOUT_SEE_ALL_BUTTON
            else if (recommendationList[position] is HomeRecommendationListData && (recommendationList[position] as HomeRecommendationListData).isCarousel) LAYOUT_TYPE_CAROUSEL else LAYOUT_TYPE_NON_CAROUSEL
        }

        override fun onBindViewHolder(holder: RecommendationListCarouselItem, position: Int) {
            holder.bind(recommendationList[position])
        }
    }
}