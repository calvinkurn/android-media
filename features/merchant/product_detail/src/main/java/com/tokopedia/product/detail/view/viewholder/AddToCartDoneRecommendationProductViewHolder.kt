package com.tokopedia.product.detail.view.viewholder

import com.google.android.material.snackbar.Snackbar
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationProductDataModel
import com.tokopedia.productcard.v2.ProductCardView
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.unifycomponents.Toaster

class AddToCartDoneRecommendationProductViewHolder(
        itemView: View,
        val recommendationListener: RecommendationListener
) : AbstractViewHolder<AddToCartDoneRecommendationProductDataModel>(itemView) {

    companion object {
        val LAYOUT_RES = R.layout.item_product_recommendation_add_to_cart
    }

    private val productCardView: ProductCardView by lazy { itemView.findViewById<ProductCardView>(R.id.productCardView) }

    override fun bind(element: AddToCartDoneRecommendationProductDataModel) {
        productCardView.run {
            removeAllShopBadges()
            setImageProductVisible(true)
            setProductNameVisible(true)
            setPriceVisible(true)
            setSlashedPriceVisible(element.recommendationItem.discountPercentage > 0)
            setLabelDiscountVisible(element.recommendationItem.discountPercentage > 0)
            setImageRatingVisible(element.recommendationItem.rating > 0 && element.recommendationItem.countReview > 0)
            setReviewCountVisible(element.recommendationItem.rating > 0 && element.recommendationItem.countReview > 0)
            setShopLocationVisible(true)
            setButtonWishlistVisible(true)
            setButtonWishlistImage(element.recommendationItem.isWishlist)
            setImageProductUrl(element.recommendationItem.imageUrl)
            setProductNameText(element.recommendationItem.name)
            setPriceText(element.recommendationItem.price)
            setImageTopAdsVisible(element.recommendationItem.isTopAds)
            setSlashedPriceText(element.recommendationItem.slashedPrice)
            setLabelDiscountText(element.recommendationItem.discountPercentage)
            setRating(element.recommendationItem.rating)
            setReviewCount(element.recommendationItem.countReview)
            setShopLocationText(element.recommendationItem.location)
            setImageProductViewHintListener(element.recommendationItem, object : ViewHintListener {
                override fun onViewHint() {
                    recommendationListener.onProductImpression(element.recommendationItem)
                }
            })
            realignLayout()
            setOnClickListener {
                recommendationListener.onProductClick(
                        element.recommendationItem,
                        null,
                        element.parentAdapterPosition,
                        adapterPosition
                )
            }
            setButtonWishlistOnClickListener {
                recommendationListener.onWishlistClick(element.recommendationItem, !element.recommendationItem.isWishlist) { success, throwable ->
                    if (success) {
                        element.recommendationItem.isWishlist = !element.recommendationItem.isWishlist
                        setButtonWishlistImage(element.recommendationItem.isWishlist)
                        if (element.recommendationItem.isWishlist) {
                            showSuccessAddWishlist(
                                    itemView,
                                    getString(R.string.msg_success_add_wishlist)
                            )
                        } else {
                            showSuccessRemoveWishlist(
                                    itemView,
                                    getString(R.string.msg_success_remove_wishlist)
                            )
                        }
                    } else {
                        showError(rootView, throwable)
                    }
                }
            }
        }
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
        Toaster.showError(view, ErrorHandler.getErrorMessage(view.context, throwable), Snackbar.LENGTH_LONG)
    }

}