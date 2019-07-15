package com.tokopedia.home.beranda.presentation.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.gql.feed.Badge
import com.tokopedia.home.beranda.presentation.presenter.HomeFeedContract
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeFeedViewModel
import com.tokopedia.productcard.v2.ProductCardViewSmallGrid

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
        productCardView.setImageProductUrl(element.imageUrl)
        productCardView.setProductNameText(element.productName)
        productCardView.setPriceText(element.price)
        productCardView.setImageTopAdsVisible(element.isTopAds)
        productCardView.setButtonWishlistVisible(true)
        productCardView.setSlashedPriceText(element.slashedPrice)
        productCardView.setLabelDiscountText(element.discountPercentage)
        productCardView.setRating(element.rating)
        productCardView.setReviewCount(element.countReview)
        mapBadges(element.badges)
        productCardView.setShopLocationText(element.location)
        productCardView.setImageProductViewHintListener(element){
            homeFeedView.onProductImpression(element, adapterPosition)
        }
    }

    private fun mapBadges(badges: List<Badge>){
        for (badge in badges) {
            val view = LayoutInflater.from(productCardView.context).inflate(com.tokopedia.productcard.R.layout.layout_badge, productCardView)
            ImageHandler.loadImageFitCenter(productCardView.context, view.findViewById(com.tokopedia.productcard.R.id.badge), badge.imageUrl)
            productCardView.addShopBadge(view)
        }
    }
}
