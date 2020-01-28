package com.tokopedia.home_wishlist.view.viewholder

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.carouselproductcard.CarouselProductCardView
import com.tokopedia.home_wishlist.util.GravitySnapHelper
import com.tokopedia.home_wishlist.R
import com.tokopedia.home_wishlist.analytics.WishlistTracking
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselDataModel
import com.tokopedia.home_wishlist.model.datamodel.RecommendationCarouselItemDataModel
import com.tokopedia.home_wishlist.view.custom.SpaceItemDecoration
import com.tokopedia.home_wishlist.view.listener.WishlistListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.smart_recycler_helper.SmartAbstractViewHolder
import com.tokopedia.smart_recycler_helper.SmartListener

class DynamicCarouselRecommendationViewHolder(val view: View) : SmartAbstractViewHolder<RecommendationCarouselDataModel>(view)  {
    private val title: TextView by lazy { view.findViewById<TextView>(R.id.title) }
    private val seeMore: TextView by lazy { view.findViewById<TextView>(R.id.see_more) }
    private val carouselProductCardView: CarouselProductCardView by lazy { view.findViewById<CarouselProductCardView>(R.id.list) }
    private val disabledView: View by lazy { view.findViewById<View>(R.id.disabled_view) }

    override fun bind(element: RecommendationCarouselDataModel, listener: SmartListener) {
        title.text = element.title
        seeMore.visibility = if(element.seeMoreAppLink.isEmpty()) View.GONE else View.VISIBLE
        seeMore.setOnClickListener{
            RouteManager.route(it.context, element.seeMoreAppLink)
        }
        carouselProductCardView.bindCarouselProductCardView(
                parentView = view,
                productCardModelList = convertIntoProductDataModel(element.list),
                carouselProductCardOnItemClickListener = object : CarouselProductCardListener.OnItemClickListener{
                    override fun onItemClick(productCardModel: ProductCardModel, childPosition: Int) {
                        (listener as WishlistListener).onProductClick(element.list[childPosition], adapterPosition, childPosition)
                    }
                },
                carouselProductCardOnWishlistItemClickListener = object : CarouselProductCardListener.OnWishlistItemClickListener{
                    override fun onWishlistItemClick(productCardModel: ProductCardModel, childPosition: Int) {
                        (listener as WishlistListener).onWishlistClick(adapterPosition, childPosition, productCardModel.isWishlisted)
                        WishlistTracking.clickWishlistIconRecommendation(
                                productId = element.list[childPosition].recommendationItem.productId.toString(),
                                isAdd = !element.list[childPosition].recommendationItem.isWishlist,
                                isTopAds = element.list[childPosition].recommendationItem.isTopAds,
                                recomTitle = element.title
                        )
                    }
                },
                carouselProductCardOnItemImpressedListener = object : CarouselProductCardListener.OnItemImpressedListener{
                    override fun getImpressHolder(adapterPosition: Int): ImpressHolder {
                        return element.list[adapterPosition].recommendationItem
                    }

                override fun onItemImpressed(productCardModel: ProductCardModel, adapterPosition: Int) {
                    (listener as WishlistListener).onProductImpression(element.list[adapterPosition], adapterPosition)
                }
            }
        )
        carouselProductCardView.setSnapHelper(GravitySnapHelper(Gravity.START))
    }

    override fun bind(element: RecommendationCarouselDataModel, listener: SmartListener, payloads: List<Any>) {
        if(payloads.isNotEmpty()){
            val bundle = payloads[0] as Bundle
            if(bundle.containsKey("isOnBulkRemoveProgress")){
                disabledView.visibility = if(bundle.getBoolean("isOnBulkRemoveProgress")) View.VISIBLE else View.GONE
            }

            if(bundle.containsKey("updateList")){
                val index = bundle.getInt("updateList")
                val isWishlist = bundle.getBoolean("updateIsWishlist")
                carouselProductCardView.updateWishlist(index, isWishlist)
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
                        ProductCardModel.ShopBadge(imageUrl = it?:"")
                    },
                    freeOngkir = ProductCardModel.FreeOngkir(
                            isActive = element.recommendationItem.isFreeOngkirActive,
                            imageUrl = element.recommendationItem.freeOngkirImageUrl
                    )
            )
        }
    }

    companion object{
        val LAYOUT = R.layout.layout_dynamic_recommendation_carousel
    }
}