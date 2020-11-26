package com.tokopedia.home_recom.view.viewholder

import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.carouselproductcard.CarouselProductCardView
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.model.datamodel.RecommendationCarouselDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationCarouselItemDataModel
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter

/**
 * Created by lukas on 21/05/2019
 *
 * A class for holder view Recommendation Carousel
 */
class RecommendationCarouselViewHolder(val view: View) : AbstractViewHolder<RecommendationCarouselDataModel>(view) {

    private val title: TextView by lazy { view.findViewById<TextView>(R.id.title) }
    private val seeMore: TextView by lazy { view.findViewById<TextView>(R.id.see_more) }
    private val recyclerView: CarouselProductCardView by lazy { view.findViewById<CarouselProductCardView>(R.id.list) }
    private val list = mutableListOf<RecommendationCarouselItemDataModel>()

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
                        productRecommendation.listener.onProductClick(
                                productRecommendation.productItem,
                                productRecommendation.productItem.type,
                                productRecommendation.parentPosition,
                                carouselProductCardPosition)
                        if (productRecommendation.productItem.isTopAds) {
                            TopAdsUrlHitter(itemView.context).hitClickUrl(
                                    this.javaClass.simpleName,
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
                                    this.javaClass.simpleName,
                                    productRecommendation.productItem.trackerImageUrl,
                                    productRecommendation.productItem.productId.toString(),
                                    productRecommendation.productItem.name,
                                    productRecommendation.productItem.imageUrl
                            )
                        }
                        productRecommendation.listener.onProductImpression(productRecommendation.productItem)
                    }
                },
                productCardModelList = products.map {
                    ProductCardModel(
                            slashedPrice = it.productItem.slashedPrice,
                            productName = it.productItem.name,
                            formattedPrice = it.productItem.price,
                            productImageUrl = it.productItem.imageUrl,
                            isTopAds = it.productItem.isTopAds,
                            discountPercentage = it.productItem.discountPercentage.toString(),
                            reviewCount = it.productItem.countReview,
                            ratingCount = it.productItem.rating,
                            shopLocation = it.productItem.location,
                            isWishlistVisible = true,
                            isWishlisted = it.productItem.isWishlist,
                            shopBadgeList = it.productItem.badgesUrl.map {
                                ProductCardModel.ShopBadge(imageUrl = it
                                        ?: "")
                            },
                            freeOngkir = ProductCardModel.FreeOngkir(
                                    isActive = it.productItem.isFreeOngkirActive,
                                    imageUrl = it.productItem.freeOngkirImageUrl
                            ),
                            labelGroupList = it.productItem.labelGroupList.map { recommendationLabel ->
                                ProductCardModel.LabelGroup(
                                        title = recommendationLabel.title, position = recommendationLabel.position, type = recommendationLabel.type
                                )
                            }
                    )
                }

        )
    }
}