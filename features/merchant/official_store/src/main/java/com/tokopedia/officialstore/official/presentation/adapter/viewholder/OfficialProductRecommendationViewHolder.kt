package com.tokopedia.officialstore.official.presentation.adapter.viewholder

import android.app.Activity
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.ProductRecommendationViewModel
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardView
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.topads.sdk.utils.ImpresionTask
import com.tokopedia.unifycomponents.Toaster


class OfficialProductRecommendationViewHolder(
        view: View,
        val recommendationListener: RecommendationListener
): AbstractViewHolder<ProductRecommendationViewModel>(view) {

    private val productCardView: ProductCardView by lazy { view.findViewById<ProductCardView>(R.id.product_item) }

    override fun bind(element: ProductRecommendationViewModel) {
        productCardView.run {
            setProductModel(
                    ProductCardModel(
                            slashedPrice = element.productItem.slashedPrice,
                            productName = element.productItem.name,
                            formattedPrice = element.productItem.price,
                            productImageUrl = element.productItem.imageUrl,
                            isTopAds = element.productItem.isTopAds,
                            discountPercentage = element.productItem.discountPercentage.toString(),
                            reviewCount = element.productItem.countReview,
                            ratingCount = element.productItem.rating,
                            shopLocation = element.productItem.location,
                            isWishlistVisible = true,
                            isWishlisted = element.productItem.isWishlist,
                            shopBadgeList = element.productItem.badgesUrl.map {
                                ProductCardModel.ShopBadge(imageUrl = it?: "")
                            },
                            freeOngkir = ProductCardModel.FreeOngkir(
                                    isActive = element.productItem.isFreeOngkirActive,
                                    imageUrl = element.productItem.freeOngkirImageUrl
                            )
                    )
            )

            setImageProductViewHintListener(element.productItem, object: ViewHintListener {
                override fun onViewHint() {
                    if (element.productItem.isTopAds) {
                        ImpresionTask().execute(element.productItem.trackerImageUrl)
                    }
                    element.listener.onProductImpression(element.productItem)
                }
            })

            setOnClickListener {
                element.listener.onProductClick(element.productItem, element.productItem.type, adapterPosition)
                if (element.productItem.isTopAds) ImpresionTask().execute(element.productItem.clickUrl)
            }

            setButtonWishlistOnClickListener {
                element.listener.onWishlistClick(element.productItem, !element.productItem.isWishlist) { success, throwable ->
                    if (success) {
                        element.productItem.isWishlist = !element.productItem.isWishlist
                        setButtonWishlistImage(element.productItem.isWishlist)
                        if (element.productItem.isWishlist) {
                            showSuccessAddWishlist((context as Activity).findViewById(android.R.id.content), getString(R.string.msg_success_add_wishlist))
                        } else {
                            showSuccessRemoveWishlist((context as Activity).findViewById(android.R.id.content), getString(R.string.msg_success_remove_wishlist))
                        }
                    } else {
                        showError(rootView, throwable)
                    }
                }
            }
        }
    }

    private fun showSuccessAddWishlist(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Lihat Wishlist") { RouteManager.route(view.context, ApplinkConst.WISHLIST) }
                .show()
    }

    private fun showSuccessRemoveWishlist(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showError(view: View, throwable: Throwable?) {
        Toaster.showError(view, ErrorHandler.getErrorMessage(view.context, throwable), Snackbar.LENGTH_LONG)
    }

    companion object {
        val LAYOUT = R.layout.viewmodel_product_recommendation_item
    }

}