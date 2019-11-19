package com.tokopedia.home.account.presentation.viewholder

import android.app.Activity
import androidx.annotation.LayoutRes
import com.google.android.material.snackbar.Snackbar
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.account.R
import com.tokopedia.home.account.presentation.listener.AccountItemListener
import com.tokopedia.home.account.presentation.viewmodel.RecommendationProductViewModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.productcard.v2.ProductCardView
import com.tokopedia.unifycomponents.Toaster

/**
 * @author devarafikry on 24/07/19.
 */
class RecommendationProductViewHolder(itemView: View, val accountItemListener: AccountItemListener) : AbstractViewHolder<RecommendationProductViewModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_account_product_recommendation
    }
    private val productCardView: ProductCardView by lazy { itemView.findViewById<ProductCardView>(R.id.account_product_recommendation) }

    override fun bind(element: RecommendationProductViewModel) {
        productCardView.run {
            setProductModel(
                    ProductCardModel(
                            slashedPrice = element.product.slashedPrice,
                            productName = element.product.name,
                            formattedPrice = element.product.price,
                            productImageUrl = element.product.imageUrl,
                            isTopAds = element.product.isTopAds,
                            discountPercentage = element.product.discountPercentage.toString(),
                            reviewCount = element.product.countReview,
                            ratingCount = element.product.rating,
                            shopLocation = element.product.location,
                            isWishlistVisible = true,
                            isWishlisted = element.product.isWishlist,
                            shopBadgeList = element.product.badgesUrl.map {
                                ProductCardModel.ShopBadge(imageUrl = it?:"")
                            },
                            freeOngkir = ProductCardModel.FreeOngkir(
                                    isActive = element.product.isFreeOngkirActive,
                                    imageUrl = element.product.freeOngkirImageUrl
                            )
                    )
            )
            setImageProductViewHintListener(element.product, object : ViewHintListener {
                override fun onViewHint() {
                    accountItemListener.onProductRecommendationImpression(element.product, adapterPosition)
                }
            })

            setOnClickListener {
                accountItemListener.onProductRecommendationClicked(element.product, adapterPosition, element.widgetTitle)
            }


            setButtonWishlistOnClickListener {
                accountItemListener.onProductRecommendationWishlistClicked(element.product,
                        !element.product.isWishlist){ success, throwable ->
                    if(success){
                        element.product.isWishlist = !element.product.isWishlist
                        setButtonWishlistImage(element.product.isWishlist)
                        if(element.product.isWishlist){
                            showSuccessAddWishlist((context as Activity).findViewById(android.R.id.content), getString(R.string.msg_success_add_wishlist))
                        } else {
                            showSuccessRemoveWishlist((context as Activity).findViewById(android.R.id.content), getString(R.string.msg_success_remove_wishlist))
                        }
                    }else {
                        showError(rootView, throwable)
                    }
                }
            }

        }
    }

    private fun showSuccessAddWishlist(view: View, message: String){
        Toaster.showNormalWithAction(
                view,
                message,
                Snackbar.LENGTH_LONG,
                view.context.getString(R.string.account_go_to_wishlist),
                View.OnClickListener {
                    RouteManager.route(view.context, ApplinkConst.WISHLIST)
                }
                )
    }

    private fun showSuccessRemoveWishlist(view: View, message: String){
        Toaster.showNormal(view, message, Snackbar.LENGTH_LONG)
    }

    private fun showError(view: View, throwable: Throwable?){
        Toaster.showError(view,
            ErrorHandler.getErrorMessage(view.context, throwable),
            Snackbar.LENGTH_LONG)
    }

    private fun mapBadges(badges: List<String?>){
        for (badge in badges) {
            val view = LayoutInflater.from(productCardView.context).inflate(R.layout.layout_badge, null)
            ImageHandler.loadImageFitCenter(productCardView.context, view.findViewById(R.id.badge), badge)
            productCardView.addShopBadge(view)
        }
    }
}
