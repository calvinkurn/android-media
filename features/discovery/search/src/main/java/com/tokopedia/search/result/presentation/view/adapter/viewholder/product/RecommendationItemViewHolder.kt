package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.annotation.LayoutRes
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.v2.ProductCardView
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.RecommendationItemViewModel

/**
 * Created by Lukas on 09/10/19
 */
class RecommendationItemViewHolder (
        itemView: View,
        val listener: RecommendationListener
) : AbstractViewHolder<RecommendationItemViewModel>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_recommendation_card_small_grid
    }

    private val context: Context = itemView.context

    private val productCardViewHintListener: ProductCardView? by lazy{
        itemView.findViewById<ProductCardView>(R.id.productCardView)
    }

    override fun bind(recommendationItemViewModel: RecommendationItemViewModel) {
        initProductCardContainer(recommendationItemViewModel)
        initProductImage(recommendationItemViewModel)
        initWishlistButton(recommendationItemViewModel)
        initShopName(recommendationItemViewModel)
        initTitleTextView(recommendationItemViewModel)
        initSlashedPriceSection(recommendationItemViewModel)
        initPriceTextView(recommendationItemViewModel)
        initShopBadge(recommendationItemViewModel)
        initLocationTextView(recommendationItemViewModel)
        initCredibilitySection(recommendationItemViewModel)
        initTopAdsIcon(recommendationItemViewModel)
        finishBindViewHolder()
    }

    private fun initProductCardContainer(recommendationItemViewModel: RecommendationItemViewModel) {

        productCardViewHintListener?.setOnClickListener {
            listener.onProductClick(recommendationItemViewModel.recommendationItem, "", adapterPosition)
        }
    }

    private fun initProductImage(recommendationItemViewModel: RecommendationItemViewModel) {
        productCardViewHintListener?.setImageProductVisible(true)

        setImageProductUrl(recommendationItemViewModel)

        productCardViewHintListener?.setImageProductViewHintListener(recommendationItemViewModel, createImageProductViewHintListener(recommendationItemViewModel))
    }

    private fun createImageProductViewHintListener(recommendationItemViewModel: RecommendationItemViewModel): ViewHintListener {
        return object: ViewHintListener {
            override fun onViewHint() {
                listener.onProductImpression(recommendationItemViewModel.recommendationItem)
            }
        }
    }

    private fun setImageProductUrl(recommendationItemViewModel: RecommendationItemViewModel) {
        val imageUrl = recommendationItemViewModel.recommendationItem.imageUrl

        productCardViewHintListener?.setImageProductUrl(imageUrl)
    }

    private fun initWishlistButton(recommendationItemViewModel: RecommendationItemViewModel) {
        productCardViewHintListener?.setButtonWishlistVisible(true)
        productCardViewHintListener?.setButtonWishlistImage(recommendationItemViewModel.recommendationItem.isWishlist)
        productCardViewHintListener?.setButtonWishlistOnClickListener {
            listener.onWishlistClick(recommendationItemViewModel.recommendationItem, recommendationItemViewModel.recommendationItem.isWishlist){ success, throwable ->
                /** do nothing **/
            }
        }
    }

    private fun initShopName(recommendationItemViewModel: RecommendationItemViewModel) {
        val isShopNameShown = isShopNameShown(recommendationItemViewModel)
        productCardViewHintListener?.setShopNameVisible(isShopNameShown)

        if(isShopNameShown) {
            productCardViewHintListener?.setShopNameText(recommendationItemViewModel.recommendationItem.shopName)
        }
    }

    private fun isShopNameShown(recommendationItemViewModel: RecommendationItemViewModel): Boolean {
        return recommendationItemViewModel.recommendationItem.badgesUrl.contains("official_store")
    }

    private fun initTitleTextView(recommendationItemViewModel: RecommendationItemViewModel) {
        productCardViewHintListener?.setProductNameVisible(true)
        productCardViewHintListener?.setProductNameText(recommendationItemViewModel.recommendationItem.name)
    }

    private fun initSlashedPriceSection(recommendationItemViewModel: RecommendationItemViewModel) {
        val isLabelDiscountVisible = isLabelDiscountVisible(recommendationItemViewModel)

        productCardViewHintListener?.setLabelDiscountVisible(isLabelDiscountVisible)
        productCardViewHintListener?.setSlashedPriceVisible(isLabelDiscountVisible)

        if (isLabelDiscountVisible) {
            productCardViewHintListener?.setLabelDiscountText(recommendationItemViewModel.recommendationItem.discountPercentage)
            productCardViewHintListener?.setSlashedPriceText(recommendationItemViewModel.recommendationItem.slashedPrice)
        }
    }

    private fun isLabelDiscountVisible(recommendationItemViewModel: RecommendationItemViewModel): Boolean {
        return recommendationItemViewModel.recommendationItem.discountPercentage > 0
    }

    private fun initPriceTextView(recommendationItemViewModel: RecommendationItemViewModel) {
        productCardViewHintListener?.setPriceVisible(true)
        productCardViewHintListener?.setPriceText(getPriceText(recommendationItemViewModel))
    }

    private fun getPriceText(recommendationItemViewModel: RecommendationItemViewModel) : String {
        return recommendationItemViewModel.recommendationItem.price
    }

    private fun initShopBadge(recommendationItemViewModel: RecommendationItemViewModel) {
        val hasAnyBadgesShown = hasAnyBadgesShown(recommendationItemViewModel)
        productCardViewHintListener?.setShopBadgesVisible(hasAnyBadgesShown)

        if(hasAnyBadgesShown) {
            productCardViewHintListener?.removeAllShopBadges()
            loopBadgesListToLoadShopBadgeIcon(recommendationItemViewModel.recommendationItem.badgesUrl)
        }
    }

    private fun hasAnyBadgesShown(recommendationItemViewModel: RecommendationItemViewModel): Boolean {
        return recommendationItemViewModel.recommendationItem.badgesUrl.isNotEmpty()
    }

    private fun loopBadgesListToLoadShopBadgeIcon(badgesList: List<String?>) {
        for (badgeItem in badgesList) {
            loadShopBadgesIcon(badgeItem ?: "")
        }
    }

    private fun loadShopBadgesIcon(url: String) {
        if(!TextUtils.isEmpty(url)) {
            val view = LayoutInflater.from(context).inflate(R.layout.search_result_product_card_badge_layout, null)

            ImageHandler.loadImageBitmap2(context, url, object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(bitmap: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                    loadShopBadgeSuccess(view, bitmap)
                }

                override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                    super.onLoadFailed(e, errorDrawable)
                    loadShopBadgeFailed(view)
                }
            })
        }
    }

    private fun loadShopBadgeSuccess(view: View, bitmap: Bitmap) {
        val image = view.findViewById<ImageView>(R.id.badge)

        if (bitmap.height <= 1 && bitmap.width <= 1) {
            view.visibility = View.GONE
        } else {
            image.setImageBitmap(bitmap)
            view.visibility = View.VISIBLE
            productCardViewHintListener?.addShopBadge(view)
        }
    }

    private fun loadShopBadgeFailed(view: View) {
        view.visibility = View.GONE
    }

    private fun initLocationTextView(recommendationItemViewModel: RecommendationItemViewModel) {
        val isShopLocationShown = isShopLocationShown(recommendationItemViewModel)
        productCardViewHintListener?.setShopLocationVisible(isShopLocationShown)

        if(isShopLocationShown) {
            productCardViewHintListener?.setShopLocationText(recommendationItemViewModel.recommendationItem.location)
        }
    }

    private fun isShopLocationShown(recommendationItemViewModel: RecommendationItemViewModel) : Boolean {
        return !TextUtils.isEmpty(recommendationItemViewModel.recommendationItem.location)
    }

    private fun initCredibilitySection(recommendationItemViewModel: RecommendationItemViewModel) {
        initRatingAndReview(recommendationItemViewModel)
    }

    private fun initRatingAndReview(recommendationItemViewModel: RecommendationItemViewModel) {
        initRatingView(recommendationItemViewModel)
        initReviewCount(recommendationItemViewModel)
    }

    private fun initRatingView(recommendationItemViewModel: RecommendationItemViewModel) {
        val isImageRatingVisible = isImageRatingVisible(recommendationItemViewModel)

        productCardViewHintListener?.setImageRatingVisible(isImageRatingVisible)

        if(isImageRatingVisible) {
            productCardViewHintListener?.setRating(getStarCount(recommendationItemViewModel))
        }
    }

    private fun isImageRatingVisible(recommendationItemViewModel: RecommendationItemViewModel): Boolean {
        return recommendationItemViewModel.recommendationItem.rating != 0
    }

    private fun getStarCount(recommendationItemViewModel: RecommendationItemViewModel): Int {
        return if (recommendationItemViewModel.recommendationItem.isTopAds)
            Math.round(recommendationItemViewModel.recommendationItem.rating / 20f)
        else
            Math.round(recommendationItemViewModel.recommendationItem.rating.toFloat())
    }

    private fun initReviewCount(recommendationItemViewModel: RecommendationItemViewModel) {
        val isReviewCountVisible = isReviewCountVisible(recommendationItemViewModel)
        productCardViewHintListener?.setReviewCountVisible(isReviewCountVisible)

        if(isReviewCountVisible) {
            productCardViewHintListener?.setReviewCount(recommendationItemViewModel.recommendationItem.countReview)
        }
    }

    private fun isReviewCountVisible(recommendationItemViewModel: RecommendationItemViewModel): Boolean {
        return recommendationItemViewModel.recommendationItem.countReview != 0
    }

    private fun initTopAdsIcon(recommendationItemViewModel: RecommendationItemViewModel) {
        productCardViewHintListener?.setImageTopAdsVisible(recommendationItemViewModel.recommendationItem.isTopAds)
    }

    private fun finishBindViewHolder() {
        productCardViewHintListener?.realignLayout()
    }

}