package com.tokopedia.home_recom.view.viewholder

import android.app.Activity
import android.graphics.Color
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.model.datamodel.RecommendationItemDataModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.productcard.v2.ProductCardView
import com.tokopedia.topads.sdk.utils.ImpresionTask

class RecommendationItemViewHolder(
       private val view: View
) : AbstractViewHolder<RecommendationItemDataModel>(view){

    private val productCardView: ProductCardView by lazy { view.findViewById<ProductCardView>(R.id.product_item) }

    override fun bind(element: RecommendationItemDataModel) {
        productCardView.run {
            removeAllShopBadges()
            setProductNameVisible(true)
            setPriceVisible(true)
            setImageProductVisible(true)
            setButtonWishlistVisible(true)
            setSlashedPriceVisible(element.productItem.slashedPriceInt > 0 && element.productItem.discountPercentage > 0)
            setLabelDiscountVisible(element.productItem.slashedPriceInt > 0 && element.productItem.discountPercentage > 0)
            setImageRatingVisible(element.productItem.rating > 0 && element.productItem.countReview > 0)
            setReviewCountVisible(element.productItem.rating > 0 && element.productItem.countReview > 0)
            setShopLocationVisible(element.productItem.badgesUrl.isNotEmpty())
            setButtonWishlistVisible(true)
            setShopBadgesVisible(true)
            setButtonWishlistImage(element.productItem.isWishlist)
            setProductNameText(element.productItem.name)
            setPriceText(element.productItem.price)
            setImageProductUrl(element.productItem.imageUrl)
            setImageTopAdsVisible(element.productItem.isTopAds)
            setSlashedPriceText(element.productItem.slashedPrice)
            setLabelDiscountText(element.productItem.discountPercentage)
            setReviewCount(element.productItem.countReview)
            setRating(element.productItem.rating)
            mapBadges(element.productItem.badgesUrl)
            setShopLocationText(element.productItem.location)
            realignLayout()
            setImageProductViewHintListener(element.productItem, object: ViewHintListener {
                override fun onViewHint() {
                    if(element.productItem.isTopAds){
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
                element.listener.onWishlistClick(element.productItem, !element.productItem.isWishlist){ success, throwable ->
                    if(success){
                        element.productItem.isWishlist = !element.productItem.isWishlist
                        setButtonWishlistImage(element.productItem.isWishlist)
                        if(element.productItem.isWishlist){
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

    private fun mapBadges(badges: List<String?>){
        for (badge in badges) {
            val view = LayoutInflater.from(productCardView.context).inflate(com.tokopedia.productcard.R.layout.layout_badge, null)
            ImageHandler.loadImageFitCenter(productCardView.context, view.findViewById(com.tokopedia.productcard.R.id.badge), badge)
            productCardView.addShopBadge(view)
        }
    }

    private fun showSuccessAddWishlist(view: View, message: String){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction(R.string.recom_go_to_wishlist) { RouteManager.route(view.context, ApplinkConst.WISHLIST) }
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

}