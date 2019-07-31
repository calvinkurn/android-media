package com.tokopedia.navigation.presentation.adapter.viewholder

import android.app.Activity
import android.content.Context
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.navigation.R
import com.tokopedia.navigation.analytics.InboxGtmTracker
import com.tokopedia.navigation.domain.model.Recommendation
import com.tokopedia.navigation.presentation.view.InboxAdapterListener
import com.tokopedia.navigation.presentation.view.listener.RecommendationListener
import com.tokopedia.navigation.util.RecomSnackBar
import com.tokopedia.productcard.v2.ProductCardView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker
import com.tokopedia.topads.sdk.domain.model.Category
import com.tokopedia.topads.sdk.domain.model.Product
import com.tokopedia.topads.sdk.utils.ImpresionTask

/**
 * Author errysuprayogi on 13,March,2019
 * Modify Lukas on July 31, 2019
 */
class RecommendationViewHolder(itemView: View, private val listener: InboxAdapterListener, private val recommendationListener: RecommendationListener) : AbstractViewHolder<Recommendation>(itemView){
    private val productCardView by lazy { itemView.findViewById<ProductCardView>(R.id.productCardView) }
    private val context: Context? = itemView.context

    private fun onImpressionTopAds(item: RecommendationItem) {
        val product = Product()
        val pos = (adapterPosition - listener.getStartProductPosition())
        product.id = item.productId.toString()
        product.name = item.name
        product.priceFormat = item.price
        product.category = Category(item.departmentId)
        TopAdsGtmTracker.getInstance().addInboxProductViewImpressions(product, pos, item.recommendationType)
    }

    private fun onImpressionOrganic(item: RecommendationItem) {
        val pos = (adapterPosition - listener.getStartProductPosition())
        InboxGtmTracker.getInstance().addInboxProductViewImpressions(item, pos)
    }

    private fun onClickTopAds(item: RecommendationItem) {
        val product = Product()
        val pos = (adapterPosition - listener.getStartProductPosition())
        product.id = item.productId.toString()
        product.name = item.name
        product.priceFormat = item.price
        product.category = Category(item.departmentId)
        context?.run {
            TopAdsGtmTracker.getInstance().eventInboxProductClick(context, product, pos, item.recommendationType)
        }
    }

    private fun onClickOrganic(item: RecommendationItem) {
        val pos = (adapterPosition - listener.getStartProductPosition())
        context?.run {
            InboxGtmTracker.getInstance().eventInboxProductClick(context, item, pos)
        }
    }

    override fun bind(element: Recommendation) {
        productCardView.run {
            removeAllShopBadges()
            setProductNameVisible(true)
            setPriceVisible(true)
            setImageProductVisible(true)
            setButtonWishlistVisible(true)
            setSlashedPriceVisible(element.recommendationItem.slashedPriceInt > 0 && element.recommendationItem.discountPercentage > 0)
            setLabelDiscountVisible(element.recommendationItem.slashedPriceInt > 0 && element.recommendationItem.discountPercentage > 0)
            setImageRatingVisible(element.recommendationItem.rating > 0 && element.recommendationItem.countReview > 0)
            setReviewCountVisible(element.recommendationItem.rating > 0 && element.recommendationItem.countReview > 0)
            setShopLocationVisible(element.recommendationItem.badgesUrl.isNotEmpty())
            setShopBadgesVisible(true)
            setButtonWishlistImage(element.recommendationItem.isWishlist)
            setProductNameText(element.recommendationItem.name)
            setPriceText(element.recommendationItem.price)
            setImageProductUrl(element.recommendationItem.imageUrl)
            setImageTopAdsVisible(element.recommendationItem.isTopAds)
            setSlashedPriceText(element.recommendationItem.slashedPrice)
            setLabelDiscountText(element.recommendationItem.discountPercentage)
            setReviewCount(element.recommendationItem.countReview)
            setRating(element.recommendationItem.rating)
            mapBadges(element.recommendationItem.badgesUrl)
            setShopLocationText(element.recommendationItem.location)
            realignLayout()
            setImageProductViewHintListener(element.recommendationItem, object: ViewHintListener {
                override fun onViewHint() {
                    if(element.recommendationItem.isTopAds){
                        ImpresionTask().execute(element.recommendationItem.trackerImageUrl)
                        onImpressionTopAds(element.recommendationItem)
                    }else {
                        onImpressionOrganic(element.recommendationItem)
                    }
                }
            })

            setOnClickListener {
                listener.onItemClickListener(element, adapterPosition)
                if (element.recommendationItem.isTopAds) {
                    ImpresionTask().execute(element.recommendationItem.clickUrl)
                    onClickTopAds(element.recommendationItem)
                }else {
                    onClickOrganic(element.recommendationItem)
                }
            }

            setButtonWishlistOnClickListener {
                recommendationListener.onWishlistClick(element.recommendationItem, !element.recommendationItem.isWishlist){ success, throwable ->
                    if(success){
                        element.recommendationItem.isWishlist = !element.recommendationItem.isWishlist
                        setButtonWishlistImage(element.recommendationItem.isWishlist)
                        InboxGtmTracker.getInstance().eventClickRecommendationWishlist(context, element.recommendationItem, element.recommendationItem.isWishlist)
                        if(element.recommendationItem.isWishlist){
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
        RecomSnackBar.showSuccessWithAction(view, message, view.context.getString(R.string.recom_go_to_wishlist)){
            RouteManager.route(view.context, ApplinkConst.WISHLIST)
        }
    }

    private fun showSuccessRemoveWishlist(view: View, message: String){
        RecomSnackBar.showSuccess(view, message)
    }

    private fun showError(view: View, throwable: Throwable?){
        RecomSnackBar.showError(view, throwable)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_recomendation
    }
}
