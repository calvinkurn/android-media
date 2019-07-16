package com.tokopedia.home.beranda.presentation.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.gql.feed.Badge
import com.tokopedia.home.beranda.presentation.presenter.HomeFeedContract
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedViewModel
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
            if(element.discountPercentage > 0){
                setSlashedPriceVisible(true)
                setLabelDiscountVisible(true)
            }
            setImageRatingVisible(true)
            setReviewCountVisible(true)
            setShopBadgesVisible(true)
            setShopLocationVisible(true)
            setButtonWishlistVisible(true)
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
            setImageProductViewHintListener(element){
                homeFeedView.onProductImpression(element, adapterPosition)
            }
            realignLayout()
            setButtonWishlistOnClickListener {
                homeFeedView.onWishlistClick(element, adapterPosition, !it.isActivated){ isSuccess, throwable ->
                    if(isSuccess){
                        it.isActivated = !it.isActivated
                        setButtonWishlistImage(it.isActivated)
                    } else {
                        Toaster.showError(
                                findViewById(android.R.id.content),
                                ErrorHandler.getErrorMessage(it.context, throwable),
                                Snackbar.LENGTH_LONG)
                    }
                }
            }
        }
    }

    private fun mapBadges(badges: List<Badge>){
        for (badge in badges) {
            val view = LayoutInflater.from(productCardView.context).inflate(com.tokopedia.productcard.R.layout.layout_badge, null)
            ImageHandler.loadImageFitCenter(productCardView.context, view.findViewById(com.tokopedia.productcard.R.id.badge), badge.imageUrl)
            productCardView.addShopBadge(view)
        }
    }
}
