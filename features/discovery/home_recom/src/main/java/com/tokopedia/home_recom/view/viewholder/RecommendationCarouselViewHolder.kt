package com.tokopedia.home_recom.view.viewholder

import android.app.Activity
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.carouselproductcard.CarouselProductCardListener
import com.tokopedia.carouselproductcard.CarouselProductCardView
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.model.datamodel.RecommendationCarouselItemDataModel
import com.tokopedia.home_recom.model.datamodel.RecommendationCarouselDataModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.unifycomponents.Toaster

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
    override fun bind(element: RecommendationCarouselDataModel) {
        title.text = element.title
        seeMore.setOnClickListener { RouteManager.route(itemView.context, element.appLinkSeeMore) }
        setupRecyclerView(element)
    }

    private fun setupRecyclerView(dataModel: RecommendationCarouselDataModel){
        val products = dataModel.products
        recyclerView.initCarouselProductCardView(
                isScrollable = true,
                carouselProductCardOnItemClickListener = object : CarouselProductCardListener.OnItemClickListener {
                    override fun onItemClick(productCardModel: ProductCardModel, adapterPosition: Int) {
                        val productRecommendation = products[adapterPosition]
                        productRecommendation.listener.onProductClick(
                                productRecommendation.productItem,
                                productRecommendation.productItem.type,
                                productRecommendation.parentPosition,
                                adapterPosition)
                        if (productRecommendation.productItem.isTopAds) {
                            ImpresionTask().execute(productRecommendation.productItem.clickUrl)
                        }
                    }
                },
                carouselProductCardOnItemImpressedListener = object : CarouselProductCardListener.OnItemImpressedListener {
                    override fun onItemImpressed(productCardModel: ProductCardModel, adapterPosition: Int) {
                        val productRecommendation = products[adapterPosition]
                        if(productRecommendation.productItem.isTopAds){
                            ImpresionTask().execute(productRecommendation.productItem.trackerImageUrl)
                        }
                        productRecommendation.listener.onProductImpression(productRecommendation.productItem)
                    }
                },
                carouselProductCardOnWishlistItemClickListener = object : CarouselProductCardListener.OnWishlistItemClickListener {
                    override fun onWishlistItemClick(productCardModel: ProductCardModel, adapterPosition: Int) {
                        val productRecommendation = products[adapterPosition]
                        productRecommendation.listener.onWishlistClick(productRecommendation.productItem,
                                !productRecommendation.productItem.isWishlist){ success, throwable ->
                            if(success){
                                productRecommendation.productItem.isWishlist = !productRecommendation.productItem.isWishlist
                                updateWishlist(adapterPosition, productRecommendation.productItem.isWishlist)
                                if(productRecommendation.productItem.isWishlist){
                                    showSuccessAddWishlist((view.context as Activity).findViewById(android.R.id.content), getString(R.string.msg_success_add_wishlist))
                                } else {
                                    showSuccessRemoveWishlist((view.context as Activity).findViewById(android.R.id.content), getString(R.string.msg_success_remove_wishlist))
                                }
                            }else {
                                showError(view.rootView, throwable)
                            }
                        }
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
                            isWishlistVisible = false,
                            isWishlisted = it.productItem.isWishlist,
                            shopBadgeList = it.productItem.badgesUrl.map {
                                ProductCardModel.ShopBadge(imageUrl = it?:"")
                            },
                            freeOngkir = ProductCardModel.FreeOngkir(
                                    isActive = it.productItem.isFreeOngkirActive,
                                    imageUrl = it.productItem.freeOngkirImageUrl
                            ),
                            labelPromo = ProductCardModel.Label(
                                    title = it.productItem.labelPromo.title,
                                    type = it.productItem.labelPromo.type
                            ),
                            labelCredibility = ProductCardModel.Label(
                                    title = it.productItem.labelCredibility.title,
                                    type = it.productItem.labelCredibility.type
                            ),
                            labelOffers = ProductCardModel.Label(
                                    title = it.productItem.labelOffers.title,
                                    type = it.productItem.labelOffers.type
                            )
                    )
                }

        )
    }

    fun updateWishlist(position: Int, isWishlist: Boolean){
        recyclerView.updateWishlist(position, isWishlist)
    }

    private fun showSuccessAddWishlist(view: View, message: String){
        Toaster.showNormalWithAction(view, message, Snackbar.LENGTH_LONG,
                view.context.getString(R.string.recom_go_to_wishlist), View.OnClickListener {
            RouteManager.route(view.context, ApplinkConst.WISHLIST)
        })
    }

    private fun showSuccessRemoveWishlist(view: View, message: String){
        Toaster.showNormal(view, message, Snackbar.LENGTH_LONG)
    }

    private fun showError(view: View, throwable: Throwable?){
        Toaster.showError(view,
                ErrorHandler.getErrorMessage(view.context, throwable), Snackbar.LENGTH_LONG)
    }

}