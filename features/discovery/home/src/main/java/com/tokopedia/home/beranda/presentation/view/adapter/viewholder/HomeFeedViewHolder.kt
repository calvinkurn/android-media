package com.tokopedia.home.beranda.presentation.view.adapter.viewholder

import android.app.Activity
import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.gql.feed.Badge
import com.tokopedia.home.beranda.presentation.presenter.HomeFeedContract
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedViewModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.productcard.v2.ProductCardViewSmallGrid
import com.tokopedia.unifycomponents.Toaster

/**
 * Created by Lukas on 2019-07-15
 */

class HomeFeedViewHolder(itemView: View, private val homeFeedView: HomeFeedContract.View) : AbstractViewHolder<HomeFeedViewModel>(itemView) {

    companion object{
        @LayoutRes
        val LAYOUT = R.layout.home_feed_item
    }

    private val productCardView by lazy { itemView.findViewById<ProductCardViewSmallGrid>(R.id.productCardView) }

    override fun bind(element: HomeFeedViewModel) {
        setLayout(element)
    }

    private fun setLayout(element: HomeFeedViewModel){
        productCardView.run{
            removeAllShopBadges()
            setImageProductVisible(true)
            setProductNameVisible(true)
            setPriceVisible(true)
            setSlashedPriceVisible(element.discountPercentage > 0)
            setLabelDiscountVisible(element.discountPercentage > 0)
            setImageRatingVisible(element.rating > 0 && element.countReview > 0)
            setReviewCountVisible(element.rating > 0 && element.countReview > 0)
            setShopBadgesVisible(element.badges.isNotEmpty())
            setShopLocationVisible(true)
            setButtonWishlistVisible(true)
            setButtonWishlistImage(element.isWishList)
            setImageProductUrl(element.imageUrl)
            setProductNameText(element.productName)
            setPriceText(element.price)
            setImageTopAdsVisible(element.isTopAds)
            setSlashedPriceText(element.slashedPrice)
            setLabelDiscountText(element.discountPercentage)
            setRating(element.rating)
            setReviewCount(element.countReview)
            mapBadges(element.badges)
            setShopLocationText(element.location)
            setImageProductViewHintListener(element, object: ViewHintListener {
                override fun onViewHint() {
                    homeFeedView.onProductImpression(element, adapterPosition)
                }
            })
            realignLayout()
            setOnClickListener { homeFeedView.onProductClick(element, adapterPosition) }
            setButtonWishlistOnClickListener {
                homeFeedView.onWishlistClick(element, adapterPosition, !it.isActivated){ isSuccess, throwable ->
                    if(isSuccess){
                        it.isActivated = !it.isActivated
                        element.isWishList = it.isActivated
                        setButtonWishlistImage(it.isActivated)
                        if(it.isActivated){
                            showSuccessAddWishlist((context as Activity).findViewById(android.R.id.content), getString(R.string.msg_success_add_wishlist))
                        } else {
                            showSuccessRemoveWishlist((context as Activity).findViewById(android.R.id.content), getString(R.string.msg_success_remove_wishlist))
                        }
                    } else {
                        Toaster.showError(
                                this.rootView.findViewById(android.R.id.content),
                                ErrorHandler.getErrorMessage(it.context, throwable),
                                Snackbar.LENGTH_LONG)
                    }
                }
            }
        }
    }

    private fun mapBadges(badges: List<Badge>){
        for (badge in badges) {
            val view = LayoutInflater.from(productCardView.context).inflate(R.layout.home_layout_badge, null)
            ImageHandler.loadImageFitCenter(productCardView.context, view.findViewById(com.tokopedia.productcard.R.id.badge), badge.imageUrl)
            productCardView.addShopBadge(view)
        }
    }

    private fun showSuccessAddWishlist(view: View, message: String){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction(R.string.go_to_wishlist) { RouteManager.route(view.context, ApplinkConst.WISHLIST) }
                .show()
    }

    private fun showSuccessRemoveWishlist(view: View, message: String){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

}
