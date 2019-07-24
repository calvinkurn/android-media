package com.tokopedia.home.account.presentation.viewholder

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.home.account.R
import com.tokopedia.home.account.presentation.listener.AccountItemListener
import com.tokopedia.home.account.presentation.view.buyercardview.BuyerCard
import com.tokopedia.home.account.presentation.view.buyercardview.BuyerCardView
import com.tokopedia.home.account.presentation.viewmodel.BuyerCardViewModel
import com.tokopedia.home.account.presentation.viewmodel.RecommendationProductViewModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.v2.ProductCardView

/**
 * @author okasurya on 7/17/18.
 */
class RecommendationProductViewHolder(itemView: View, val accountItemListener: AccountItemListener) : AbstractViewHolder<RecommendationProductViewModel>(itemView) {
    private val productCardView: ProductCardView by lazy { itemView.findViewById<ProductCardView>(R.id.product_item) }

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
//            setButtonWishlistImage(element.product.isWishlist)
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
                    if (element.product.isTopAds) {
                        ImpresionTask().execute(element.product.trackerImageUrl)
                    }
                    accountItemListener.onProductRecommendationImpression(element.product)
                }
            })

            setOnClickListener {
                accountItemListener.onProductRecommendationClicked(element.productItem, element.productItem.type, element.parentPosition, adapterPosition)
                if (element.productItem.isTopAds) {
                    ImpresionTask().execute(element.productItem.clickUrl)
                }
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

    private fun mapBadges(badges: List<String?>){
        for (badge in badges) {
            val view = LayoutInflater.from(productCardView.context).inflate(com.tokopedia.productcard.R.layout.layout_badge, null)
            ImageHandler.loadImageFitCenter(productCardView.context, view.findViewById(com.tokopedia.productcard.R.id.badge), badge)
            productCardView.addShopBadge(view)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_recommendation
    }
}
