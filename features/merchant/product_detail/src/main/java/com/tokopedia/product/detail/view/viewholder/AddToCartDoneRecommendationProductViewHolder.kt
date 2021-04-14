package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationProductDataModel
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.productcard.v2.ProductCardView
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
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
            setProductModel(
                    element.recommendationItem.toProductCardModel(),
                    BlankSpaceConfig(
                            ratingCount = true,
                            discountPercentage = true,
                            twoLinesProductName = true
                    )
            )
            setImageProductViewHintListener(element.recommendationItem, object : ViewHintListener {
                override fun onViewHint() {
                    recommendationListener.onProductImpression(element.recommendationItem)
                }
            })
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
                                    getString(com.tokopedia.topads.sdk.R.string.msg_success_add_wishlist)
                            )
                        } else {
                            showSuccessRemoveWishlist(
                                    itemView,
                                    getString(com.tokopedia.topads.sdk.R.string.msg_success_remove_wishlist)
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
        Toaster.build(view, message, Snackbar.LENGTH_LONG,
                Toaster.TYPE_NORMAL, view.context.getString(R.string.recom_go_to_wishlist), View.OnClickListener {
            RouteManager.route(view.context, ApplinkConst.WISHLIST)
        }).show()
    }

    private fun showSuccessRemoveWishlist(view: View, message: String){
        Toaster.build(view, message, Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL).show()
    }

    private fun showError(view: View, throwable: Throwable?){
        Toaster.build(view, ErrorHandler.getErrorMessage(view.context, throwable), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).show()
    }

}