package com.tokopedia.gamification.pdp.presentation.viewHolders

import android.app.Activity
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.gamification.R
import com.tokopedia.gamification.pdp.data.Recommendation
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardViewSmallGrid
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.unifycomponents.Toaster

class RecommendationVH(itemView: View, val recommendationListener: RecommendationListener) : AbstractViewHolder<Recommendation>(itemView) {
    private val productCardView = itemView.findViewById<ProductCardViewSmallGrid>(R.id.productCardView)

    override fun bind(element: Recommendation?) {
        if (element != null) {
            productCardView.run {
                setProductModel(getProductModel(element))
                        setImageProductViewHintListener(element.recommendationItem, object: ViewHintListener {
                    override fun onViewHint() {
                        recommendationListener.onProductImpression(element.recommendationItem)
                    }
                })

                setOnClickListener {
                    recommendationListener.onProductClick(element.recommendationItem, null, adapterPosition)
                }

                setButtonWishlistOnClickListener {
                    //todo Rahul check this thing
                    val rootView = (context as Activity).window.decorView
                    recommendationListener.onWishlistClick(element.recommendationItem, !element.recommendationItem.isWishlist){ success, throwable ->
                        if(success){
                            element.recommendationItem.isWishlist = !element.recommendationItem.isWishlist
                            setButtonWishlistImage(element.recommendationItem.isWishlist)
                            if(element.recommendationItem.isWishlist){
                                showSuccessAddWishlist(rootView, getString(R.string.msg_success_add_wishlist))
                            } else {
                                showSuccessRemoveWishlist(rootView, getString(R.string.msg_success_remove_wishlist))
                            }
                        }else {
                            showError(rootView, throwable)
                        }
                    }
                }
            }
        }
    }

    private fun getProductModel(element: Recommendation): ProductCardModel {
        return ProductCardModel(
                slashedPrice = element.recommendationItem.slashedPrice,
                productName = element.recommendationItem.name,
                formattedPrice = element.recommendationItem.price,
                productImageUrl = element.recommendationItem.imageUrl,
                isTopAds = element.recommendationItem.isTopAds,
                discountPercentage = element.recommendationItem.discountPercentage.toString(),
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
                )
        )
    }

    companion object {
        val LAYOUT = R.layout.item_recommendation_pdp_gami
    }

    private fun showSuccessAddWishlist(view: View, message: String){
        Toaster.make(view,
                message,
                Snackbar.LENGTH_LONG,
                actionText = view.context.getString(R.string.gami_recom_go_to_wishlist),
                clickListener = View.OnClickListener {
                    RouteManager.route(view.context, ApplinkConst.WISHLIST)
                },
                type = Toaster.TYPE_NORMAL)
    }

    private fun showSuccessRemoveWishlist(view: View, message: String){
        Toaster.make(view, message, Snackbar.LENGTH_LONG)
    }

    private fun showError(view: View, throwable: Throwable?){
        val message = ErrorHandler.getErrorMessage(view.context, throwable)
        Toaster.make(view,
                message,
                Snackbar.LENGTH_LONG,
                type = Toaster.TYPE_NORMAL)
    }
}