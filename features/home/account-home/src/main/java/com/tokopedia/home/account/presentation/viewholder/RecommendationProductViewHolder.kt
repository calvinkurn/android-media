package com.tokopedia.home.account.presentation.viewholder

import android.app.Activity
import android.graphics.Color
import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.account.R
import com.tokopedia.home.account.presentation.listener.AccountItemListener
import com.tokopedia.home.account.presentation.viewmodel.RecommendationProductViewModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.productcard.v2.ProductCardView
import com.tokopedia.topads.sdk.utils.ImpresionTask

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
            removeAllShopBadges()
            setProductNameVisible(true)
            setPriceVisible(true)
            setImageProductVisible(true)
            setButtonWishlistVisible(true)
            setSlashedPriceVisible(element.product.slashedPriceInt > 0 && element.product.discountPercentage > 0)
            setLabelDiscountVisible(element.product.slashedPriceInt > 0 && element.product.discountPercentage > 0)
            setImageRatingVisible(element.product.rating > 0 && element.product.countReview > 0)
            setReviewCountVisible(element.product.rating > 0 && element.product.countReview > 0)
            setShopLocationVisible(true)
            setButtonWishlistVisible(element.product.badgesUrl.isNotEmpty())
            setShopBadgesVisible(true)
            setButtonWishlistImage(element.product.isWishlist)
            setProductNameText(element.product.name)
            setPriceText(element.product.price)
            setImageProductUrl(element.product.imageUrl)
            setImageTopAdsVisible(element.product.isTopAds)
            setSlashedPriceText(element.product.slashedPrice)
            setLabelDiscountText(element.product.discountPercentage)
            setReviewCount(element.product.countReview)
            setRating(element.product.rating)
            mapBadges(element.product.badgesUrl)
            setShopLocationText(element.product.location)
            realignLayout()
            setImageProductViewHintListener(element.product, object : ViewHintListener {
                override fun onViewHint() {
                    accountItemListener.onProductRecommendationImpression(element.product, adapterPosition)
                    if (element.product.isTopAds) {
                        ImpresionTask().execute(element.product.trackerImageUrl)
                    }
                }
            })

            setOnClickListener {
                accountItemListener.onProductRecommendationClicked(element.product, adapterPosition, element.widgetTitle)
                if (element.product.isTopAds) {
                    ImpresionTask().execute(element.product.clickUrl)
                }
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
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction(R.string.account_go_to_wishlist) { RouteManager.route(view.context, ApplinkConst.WISHLIST) }
                .show()
    }

    private fun showSuccessRemoveWishlist(view: View, message: String){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    private fun showError(view: View, throwable: Throwable?){
        val snackbar = Snackbar.make(
                view,
                ErrorHandler.getErrorMessage(view.context, throwable),
                Snackbar.LENGTH_LONG)
        val snackbarView = snackbar.view
        val padding = view.resources.getDimensionPixelSize(R.dimen.dp_16)
        snackbarView.setPadding(padding, 0, padding, 0)
        snackbarView.setBackgroundColor(Color.TRANSPARENT)
        val rootSnackBarView = snackbarView as FrameLayout
        rootSnackBarView.getChildAt(0).setBackgroundResource(R.drawable.bg_toaster_error)
        snackbar.show()

    }

    private fun mapBadges(badges: List<String?>){
        for (badge in badges) {
            val view = LayoutInflater.from(productCardView.context).inflate(com.tokopedia.productcard.R.layout.layout_badge, null)
            ImageHandler.loadImageFitCenter(productCardView.context, view.findViewById(com.tokopedia.productcard.R.id.badge), badge)
            productCardView.addShopBadge(view)
        }
    }
}
