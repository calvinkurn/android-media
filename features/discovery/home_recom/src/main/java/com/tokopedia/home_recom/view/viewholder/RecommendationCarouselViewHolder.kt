package com.tokopedia.home_recom.view.viewholder

import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.carouselproductcard.CarouselProductCardView
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.model.datamodel.RecommendationCarouselDataModel
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter

/**
 * Created by lukas on 21/05/2019
 *
 * A class for holder view Recommendation Carousel
 */
class RecommendationCarouselViewHolder(val view: View, val listener: RecommendationListener) : AbstractViewHolder<RecommendationCarouselDataModel>(view) {

    private val title: TextView by lazy { view.findViewById<TextView>(R.id.title) }
    private val seeMore: TextView by lazy { view.findViewById<TextView>(R.id.see_more) }
    private val recyclerView: CarouselProductCardView by lazy { view.findViewById<CarouselProductCardView>(R.id.list) }

    companion object {
        private const val className = "com.tokopedia.home_recom.view.viewholder.RecommendationCarouselViewHolder"
    }

    override fun bind(element: RecommendationCarouselDataModel) {
        title.text = element.title
        seeMore.visibility = if(element.appLinkSeeMore.isEmpty()) View.GONE else View.VISIBLE
        seeMore.setOnClickListener {
            RouteManager.route(itemView.context, element.appLinkSeeMore)
        }
        setupRecyclerView(element)
    }

    private fun setupRecyclerView(dataModel: RecommendationCarouselDataModel){
        val products = dataModel.products
        recyclerView.bindCarouselProductCardViewGrid(
                carouselProductCardOnItemClickListener = object : CarouselProductCardListener.OnItemClickListener {
                    override fun onItemClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                        val productRecommendation = products.getOrNull(carouselProductCardPosition) ?: return
                        listener.onProductClick(
                                productRecommendation.productItem,
                                productRecommendation.productItem.type,
                                productRecommendation.parentPosition,
                                carouselProductCardPosition)
                        if (productRecommendation.productItem.isTopAds) {
                            TopAdsUrlHitter(itemView.context).hitClickUrl(
                                    className,
                                    productRecommendation.productItem.clickUrl,
                                    productRecommendation.productItem.productId.toString(),
                                    productRecommendation.productItem.name,
                                    productRecommendation.productItem.imageUrl
                            )
                        }
                    }
                },
                carouselProductCardOnItemImpressedListener = object : CarouselProductCardListener.OnItemImpressedListener {
                    override fun getImpressHolder(carouselProductCardPosition: Int): ImpressHolder? {
                        return products.getOrNull(carouselProductCardPosition)?.productItem
                    }

                    override fun onItemImpressed(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                        val productRecommendation = products.getOrNull(carouselProductCardPosition) ?: return
                        if(productRecommendation.productItem.isTopAds){
                            TopAdsUrlHitter(itemView.context).hitImpressionUrl(
                                    className,
                                    productRecommendation.productItem.trackerImageUrl,
                                    productRecommendation.productItem.productId.toString(),
                                    productRecommendation.productItem.name,
                                    productRecommendation.productItem.imageUrl
                            )
                        }
                        listener.onProductImpression(productRecommendation.productItem)
                    }
                },
                productCardModelList = products.map {
                    it.productItem.toProductCardModel()
                }

        )
    }
}