package com.tokopedia.home_component.viewholders

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
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.decoration.SimpleHorizontalLinearLayoutDecoration
import com.tokopedia.home_component.listener.RecommendationListCarouselListener
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.util.setGradientBackground
import com.tokopedia.home_component.visitable.RecommendationListCarouselDataModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForListView
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class RecommendationListCarouselViewHolder(itemView: View,
                                           private val listCarouselListener: RecommendationListCarouselListener?,
                                           private val parentRecycledViewPool: RecyclerView.RecycledViewPool?): AbstractViewHolder<RecommendationListCarouselDataModel>(itemView), CoroutineScope {

    private val masterJob = SupervisorJob()

    override val coroutineContext = masterJob + Dispatchers.Main

    override fun bind(element: RecommendationListCarouselDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    override fun bind(element: RecommendationListCarouselDataModel) {
        val listCarouselTitle = itemView.findViewById<Typography>(R.id.list_carousel_title)
        val listCarouselDescription = itemView.findViewById<Typography>(R.id.list_carousel_description)
        val listCarouselView = itemView.findViewById<View>(R.id.list_carousel_view)
        val listCarouselRecyclerView = itemView.findViewById<RecyclerView>(R.id.recycleList)
        val listCarouselBannerHeader = itemView.findViewById<View>(R.id.list_carousel_banner_header)
        val listCarouselCloseButton = itemView.findViewById<AppCompatImageView>(R.id.buy_again_close_image_view)

        val channel = element.channelModel
        val banner = channel.channelBanner
        val channelConfig = channel.channelConfig

        setViewportImpression(element)

        banner.let {
            val textColor = if (banner.textColor.isEmpty())
                ContextCompat.getColor(itemView.context, R.color.Neutral_N50) else Color.parseColor(banner.textColor)
            if(channelConfig.hasCloseButton){
                listCarouselCloseButton.show()
                listCarouselCloseButton.setOnClickListener {
                    listCarouselListener?.onBuyAgainCloseChannelClick(channel, adapterPosition)
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
                layoutManager = if (channel.channelGrids.size > 1) {
                    LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                } else {
                    GridLayoutManager(itemView.context, 1)
                }
                val tempDataList: MutableList<ProductCardModel> = mutableListOf()
                val newList : MutableList<HomeRecommendationListCarousel> = channel.channelGrids.map {
                    val productData = mapGridToProductData(it)
                    tempDataList.add(productData)
                    HomeRecommendationListData(
                            it.imageUrl,
                            it.name,
                            it.discount,
                            it.slashedPrice,
                            it.price,
                            it.applink,
                            it.isTopads,
                            channel.channelGrids.size > 1,
                            channel,
                            it,
                            adapterPosition,
                            listCarouselListener,
                            productData
                    )
                }.toMutableList()
                if(channel.channelGrids.size > 1 && channel.channelHeader.applink.isNotEmpty()) newList.add(HomeRecommendationListSeeMoreData(channel, listCarouselListener, adapterPosition))
                launch {
                    try {
                        setHeightBasedOnProductCardMaxHeight(tempDataList)
                    }
                    catch (throwable: Throwable) {
                        throwable.printStackTrace()
                    }
                }
                adapter = RecommendationListAdapter(newList, listCarouselListener)
                setRecycledViewPool(parentRecycledViewPool)
                clearItemRecyclerViewDecoration(this)
                if (channel.channelGrids.size > 1) {
                    addItemDecoration(SimpleHorizontalLinearLayoutDecoration())
                }
            }
        }
    }

    private suspend fun RecyclerView.setHeightBasedOnProductCardMaxHeight(
            productCardModelList: List<ProductCardModel>) {
        val productCardHeight = getProductCardMaxHeight(productCardModelList)

        val carouselLayoutParams = this.layoutParams
        carouselLayoutParams?.height = productCardHeight
        this.layoutParams = carouselLayoutParams
    }

    suspend fun getProductCardMaxHeight(productCardModelList: List<ProductCardModel>): Int {
        return productCardModelList.getMaxHeightForListView(itemView.context, Dispatchers.Default)
    }

    fun mapGridToProductData(grid: ChannelGrid) :ProductCardModel{
        return ProductCardModel(
                productImageUrl = grid.imageUrl,
                productName = grid.name,
                discountPercentage = grid.discount,
                slashedPrice = grid.slashedPrice,
                formattedPrice = grid.price,
                hasAddToCartButton = grid.hasBuyButton,
                isTopAds = grid.isTopads,
                addToCardText = itemView.context.getString(R.string.home_global_component_buy_again)
        )
    }


    private fun setViewportImpression(element: RecommendationListCarouselDataModel) {
        itemView.addOnImpressionListener(element.channelModel) {
            listCarouselListener?.onRecommendationCarouselChannelImpression(element.channelModel, adapterPosition)
        }
    }

    private fun clearItemRecyclerViewDecoration(itemRecyclerView: RecyclerView) {
        while (itemRecyclerView.itemDecorationCount > 0) {
            itemRecyclerView.removeItemDecorationAt(0)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_component_recommendation_list_carousel
    }

    abstract class RecommendationListCarouselItem(itemView: View): RecyclerView.ViewHolder(itemView){
        abstract fun bind(homeRecommendationListData: HomeRecommendationListCarousel)
    }

    class HomeRecommendationListViewHolder(
            itemView: View,
            val listCarouselListener: RecommendationListCarouselListener?
    ): RecommendationListCarouselItem(itemView) {
        private val recommendationCard = itemView.findViewById<ProductCardListView>(R.id.productCardView)

        override fun bind(recommendation: HomeRecommendationListCarousel) {
            recommendationCard.applyCarousel()
            if(recommendation is HomeRecommendationListData) {
                itemView.addOnImpressionListener(recommendation) {
                    listCarouselListener?.onRecommendationCarouselGridImpression(
                            recommendation.channelModel,
                            recommendation.grid,
                            adapterPosition,
                            recommendation.parentPosition,
                            false
                    )
                }
                recommendationCard.setProductModel(
                        ProductCardModel(
                                productImageUrl = recommendation.recommendationImageUrl,
                                productName = recommendation.recommendationTitle,
                                discountPercentage = recommendation.recommendationDiscountLabel,
                                slashedPrice = recommendation.recommendationSlashedPrice,
                                formattedPrice = recommendation.recommendationPrice,
                                hasAddToCartButton = recommendation.grid.hasBuyButton,
                                isTopAds = recommendation.isTopAds
                        )
                )
                val addToCartButton = recommendationCard.findViewById<UnifyButton>(R.id.buttonAddToCart)
                addToCartButton.text = itemView.context.getString(R.string.home_global_component_buy_again)
                recommendationCard.setAddToCartOnClickListener {
                    recommendation.listener?.onBuyAgainOneClickCheckOutClick(recommendation.grid, recommendation.channelModel, adapterPosition)
                }
                itemView.setOnClickListener {
                    listCarouselListener?.onRecommendationProductClick(
                            recommendation.channelModel,
                            recommendation.grid,
                            adapterPosition,
                            recommendation.recommendationApplink
                    )
                }
            } else if(recommendation is HomeRecommendationListSeeMoreData) {
                itemView.addOnImpressionListener(recommendation) {
                    listCarouselListener?.onRecommendationCarouselGridImpression(
                            recommendation.channel,
                            null,
                            adapterPosition,
                            recommendation.parentPosition,
                            true
                    )
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
                    homeRecommendationListData.listener?.onRecommendationSeeMoreClick(
                            applink = homeRecommendationListData.channel.channelHeader.applink,
                            channelModel = homeRecommendationListData.channel
                    )
                }
            }
        }

    }

    open class HomeRecommendationListCarousel: ImpressHolder()

    data class HomeRecommendationListData(
            val recommendationImageUrl: String,
            val recommendationTitle: String,
            val recommendationDiscountLabel: String,
            val recommendationSlashedPrice: String,
            val recommendationPrice: String,
            val recommendationApplink: String,
            val isTopAds: Boolean,
            val isCarousel: Boolean,
            val channelModel: ChannelModel,
            val grid: ChannelGrid,
            val parentPosition: Int,
            val listener: RecommendationListCarouselListener?,
            val productData: ProductCardModel
    ): HomeRecommendationListCarousel()

    data class HomeRecommendationListSeeMoreData(
            val channel: ChannelModel,
            val listener: RecommendationListCarouselListener?,
            val parentPosition: Int
    ): HomeRecommendationListCarousel()

    class RecommendationListAdapter(private val recommendationList: List<HomeRecommendationListCarousel>, private val listener: RecommendationListCarouselListener?): RecyclerView.Adapter<RecommendationListCarouselItem>() {
        companion object{
            private const val LAYOUT_TYPE_CAROUSEL = 87
            private const val LAYOUT_TYPE_NON_CAROUSEL = 90
            private const val LAYOUT_SEE_ALL_BUTTON = 91
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationListCarouselItem {
            val inflater = LayoutInflater.from(parent.context)
            return when (viewType) {
                LAYOUT_TYPE_CAROUSEL -> HomeRecommendationListViewHolder(inflater.inflate(R.layout.layout_dynamic_recommendation_list_card_carousel, parent, false), listener)
                LAYOUT_SEE_ALL_BUTTON -> HomeRecommendationSeeMoreViewHolder(inflater.inflate(R.layout.layout_dynamic_recommendation_list_see_more, parent, false))
                else -> HomeRecommendationListViewHolder(inflater.inflate(R.layout.layout_dynamic_recommendation_list_card, parent, false), listener)
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