package com.tokopedia.home_wishlist.view.viewholder

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.tokopedia.applink.RouteManager
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.carouselproductcard.CarouselProductCardView
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselDataModel
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselItemDataModel
import com.tokopedia.home_wishlist.view.listener.WishlistListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartExecutors
import com.tokopedia.smart_recycler_helper.SmartListener
import com.tokopedia.topads.sdk.utils.ImpresionTask


class RecommendationCarouselViewHolder(view: View, private val appExecutors: SmartExecutors) : SmartAbstractViewHolder<RecommendationCarouselDataModel>(view) {
    private val title: TextView by lazy { view.findViewById<TextView>(R.id.title) }
    private val seeMore: TextView by lazy { view.findViewById<TextView>(R.id.see_more) }
    private val recommendationCarouselProductCardView: CarouselProductCardView by lazy { view.findViewById<CarouselProductCardView>(R.id.recommendationCarouselProductCardView) }
    private val disabledView: View by lazy { view.findViewById<View>(R.id.disabled_view) }
    override fun bind(element: RecommendationCarouselDataModel, listener: SmartListener) {
        title.text = element.title
        disabledView.visibility = if(element.isOnBulkRemoveProgress) View.VISIBLE else View.GONE
        seeMore.visibility = if(element.seeMoreAppLink.isEmpty()) View.GONE else View.VISIBLE
        seeMore.setOnClickListener { RouteManager.route(itemView.context, element.seeMoreAppLink) }
        setupRecyclerView(element, listener)
    }

    private fun setupRecyclerView(dataModel: RecommendationCarouselDataModel, listener: SmartListener){
        recommendationCarouselProductCardView.bindCarouselProductCardViewGrid(
                productCardModelList = dataModel.list.map {
                    createProductCardModel(it)
                },
                carouselProductCardOnItemImpressedListener = object: CarouselProductCardListener.OnItemImpressedListener {
                    override fun onItemImpressed(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                        val element = dataModel.list.getOrNull(carouselProductCardPosition) ?: return

                        if(element.recommendationItem.isTopAds){
                            ImpresionTask(className).execute(element.recommendationItem.trackerImageUrl)
                        }

                        (listener as WishlistListener).onProductImpression(element, carouselProductCardPosition)
                    }

                    override fun getImpressHolder(carouselProductCardPosition: Int): ImpressHolder? {
                        return dataModel.list.getOrNull(carouselProductCardPosition)?.recommendationItem
                    }
                },
                carouselProductCardOnItemClickListener = object: CarouselProductCardListener.OnItemClickListener {
                    override fun onItemClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                        val element = dataModel.list.getOrNull(carouselProductCardPosition) ?: return

                        (listener as WishlistListener).onProductClick(element, element.parentPosition, carouselProductCardPosition)
                        if (element.recommendationItem.isTopAds) {
                            ImpresionTask(className).execute(element.recommendationItem.clickUrl)
                        }
                    }
                }
        )
    }

    private fun createProductCardModel(element: RecommendationCarouselItemDataModel): ProductCardModel {
        return ProductCardModel(
                slashedPrice = element.recommendationItem.slashedPrice,
                productName = element.recommendationItem.name,
                formattedPrice = element.recommendationItem.price,
                productImageUrl = element.recommendationItem.imageUrl,
                isTopAds = element.recommendationItem.isTopAds,
                discountPercentage = element.recommendationItem.discountPercentage,
                reviewCount = element.recommendationItem.countReview,
                ratingCount = element.recommendationItem.rating,
                shopLocation = element.recommendationItem.location,
                isWishlistVisible = true,
                isWishlisted = element.recommendationItem.isWishlist,
                shopBadgeList = element.recommendationItem.badgesUrl.map {
                    ProductCardModel.ShopBadge(imageUrl = it
                            ?: "")
                },
                freeOngkir = ProductCardModel.FreeOngkir(
                        isActive = element.recommendationItem.isFreeOngkirActive,
                        imageUrl = element.recommendationItem.freeOngkirImageUrl
                ),
                labelGroupList = element.recommendationItem.labelGroupList.map { recommendationLabel ->
                    ProductCardModel.LabelGroup(
                            position = recommendationLabel.position,
                            title = recommendationLabel.title,
                            type = recommendationLabel.type
                    )
                }
        )
    }

    override fun bind(element: RecommendationCarouselDataModel, listener: SmartListener, payloads: List<Any>) {
        if(payloads.isNotEmpty()){
            val bundle = payloads[0] as Bundle
            if(bundle.containsKey("isOnBulkRemoveProgress")){
                disabledView.visibility = if(bundle.getBoolean("isOnBulkRemoveProgress")) View.VISIBLE else View.GONE
            }
        }
    }

    companion object{
        val LAYOUT = R.layout.layout_recommendation_carousel
        private const val className: String = "com.tokopedia.home_wishlist.view.viewholder.RecommendationCarouselItemViewHolder"
    }
}