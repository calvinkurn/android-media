package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.showToasterError
import com.tokopedia.product.detail.data.model.addtocartrecommendation.AddToCartDoneRecommendationProductDataModel
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.productcard.v2.ProductCardView
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.TYPE_ERROR
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import com.tokopedia.wishlistcommon.util.WishlistV2CommonConsts.TOASTER_RED
import com.tokopedia.wishlistcommon.util.WishlistV2RemoteConfigRollenceUtil

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
                var isUsingV2 = false
                context?.let {
                    if (WishlistV2RemoteConfigRollenceUtil.isUsingAddRemoveWishlistV2(it)) isUsingV2 = true
                }
                if (isUsingV2) {
                    recommendationListener.onWishlistV2Click(element.recommendationItem, !element.recommendationItem.isWishlist)
                } else {
                    recommendationListener.onWishlistClick(element.recommendationItem, !element.recommendationItem.isWishlist) { success, throwable ->
                        if (success) {
                            element.recommendationItem.isWishlist = !element.recommendationItem.isWishlist
                            setButtonWishlistImage(element.recommendationItem.isWishlist)
                            if (element.recommendationItem.isWishlist) {
                                showSuccessAddWishlist(
                                    itemView,
                                    getString(com.tokopedia.wishlist_common.R.string.on_success_add_to_wishlist_msg)
                                )
                            } else {
                                showSuccessRemoveWishlist(
                                    itemView,
                                    getString(com.tokopedia.wishlist_common.R.string.on_success_remove_from_wishlist_msg)
                                )
                            }
                        } else {
                            showError(rootView, throwable)
                        }
                    }
                }
            }
        }
    }

    private fun showSuccessAddWishlist(view: View, message: String){
        val msg = getString(com.tokopedia.wishlist_common.R.string.on_success_add_to_wishlist_msg)
        val ctaText = getString(com.tokopedia.wishlist_common.R.string.cta_success_add_to_wishlist)
        Toaster.build(view, msg, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL, ctaText) { RouteManager.route(view.context, ApplinkConst.WISHLIST) }.show()
    }

    private fun showSuccessRemoveWishlist(view: View, message: String){
        Toaster.build(view, message, Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL).show()
    }

    private fun showError(view: View, throwable: Throwable?){
        Toaster.build(view, ErrorHandler.getErrorMessage(view.context, throwable), Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).show()
    }

    private fun showToasterWishlistV2(view: View, message: String, typeToaster: Int, ctaText: String) {
        if (ctaText.isEmpty()) {
            Toaster.build(view, message, Toaster.LENGTH_SHORT, typeToaster).show()
        } else {
            Toaster.build(view, message, Toaster.LENGTH_SHORT, typeToaster, ctaText) {
                RouteManager.route(view.context, ApplinkConst.WISHLIST)
            }.show()
        }
    }

}