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
import com.tokopedia.smart_recycler_helper.SmartListener
import kotlinx.android.synthetic.main.layout_dynamic_recommendation_carousel.view.*

class DynamicCarouselRecommendationViewHolder(val view: View) : SmartAbstractViewHolder<RecommendationCarouselDataModel>(view)  {
    private val title: TextView by lazy { view.findViewById<TextView>(R.id.title) }
    private val seeMore: TextView by lazy { view.findViewById<TextView>(R.id.see_more) }
    private val carouselProductCardView: CarouselProductCardView by lazy { view.findViewById<CarouselProductCardView>(R.id.list) }
    private val disabledView: View by lazy { view.findViewById<View>(R.id.disabled_view) }

    override fun bind(element: RecommendationCarouselDataModel, listener: SmartListener) {
        itemView.dynamic_recommendation_container?.visibility = if(element.list.isEmpty()) View.GONE else View.VISIBLE
        title.text = element.title
        seeMore.visibility = if(element.seeMoreAppLink.isEmpty()) View.GONE else View.VISIBLE
        seeMore.setOnClickListener{
            RouteManager.route(it.context, element.seeMoreAppLink)
        }
        carouselProductCardView.bindCarouselProductCardViewGrid(
                productCardModelList = convertIntoProductDataModel(element.list),
                carouselProductCardOnItemClickListener = object : CarouselProductCardListener.OnItemClickListener{
                    override fun onItemClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                        val wishlistDataModel = element.list.getOrNull(carouselProductCardPosition) ?: return

                        (listener as WishlistListener).onProductClick(wishlistDataModel, adapterPosition, carouselProductCardPosition)
                    }
                },
                carouselProductCardOnItemImpressedListener = object : CarouselProductCardListener.OnItemImpressedListener{
                    override fun getImpressHolder(carouselProductCardPosition: Int): ImpressHolder? {
                        return element.list.getOrNull(carouselProductCardPosition)?.recommendationItem
                    }

                    override fun onItemImpressed(productCardModel: ProductCardModel, carouselProductCardPosition: Int) {
                        val wishlistDataModel = element.list.getOrNull(carouselProductCardPosition) ?: return
                        (listener as WishlistListener).onProductImpression(wishlistDataModel, carouselProductCardPosition)
                    }
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

    private fun convertIntoProductDataModel(recommendationItems: List<RecommendationCarouselItemDataModel>): List<ProductCardModel>{
        return recommendationItems.map { element ->
            ProductCardModel(
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
                        ProductCardModel.ShopBadge(imageUrl = it ?: "")
                    },
                    freeOngkir = ProductCardModel.FreeOngkir(
                            isActive = element.recommendationItem.isFreeOngkirActive,
                            imageUrl = element.recommendationItem.freeOngkirImageUrl
                    ),
                    labelGroupList = element.recommendationItem.labelGroupList.map { recommendationLabel ->
                        ProductCardModel.LabelGroup(
                                position = recommendationLabel.position,
                                title = recommendationLabel.title,
                                type = recommendationLabel.type,
                                imageUrl = recommendationLabel.imageUrl
                        )
                    }
            )
        }
    }

    companion object{
        val LAYOUT = R.layout.layout_dynamic_recommendation_carousel
    }
}