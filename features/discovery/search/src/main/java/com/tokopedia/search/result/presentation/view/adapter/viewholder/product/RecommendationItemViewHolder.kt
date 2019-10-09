package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.annotation.LayoutRes
import android.support.annotation.Nullable
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
import com.tokopedia.search.result.presentation.model.BadgeItemViewModel
import com.tokopedia.search.result.presentation.model.LabelGroupViewModel
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
        val LAYOUT = R.layout.search_result_product_card_small_grid
    }

    private val context: Context = itemView.context

    private val productCardViewHintListener: ProductCardView? by lazy{
        itemView.findViewById<ProductCardView>(R.id.productCardView)
    }

    override fun bind(recommendationItemViewModel: RecommendationItemViewModel) {
        initProductCardContainer(recommendationItemViewModel)
        initProductImage(recommendationItemViewModel)
        initWishlistButton(recommendationItemViewModel)
        initPromoLabel(recommendationItemViewModel)
        initShopName(recommendationItemViewModel)
        initTitleTextView(recommendationItemViewModel)
        initSlashedPriceSection(recommendationItemViewModel)
        initPriceTextView(recommendationItemViewModel)
        initShopBadge(recommendationItemViewModel)
        initLocationTextView(recommendationItemViewModel)
        initCredibilitySection(recommendationItemViewModel)
        initOffersLabel(recommendationItemViewModel)
        initTopAdsIcon(recommendationItemViewModel)

        finishBindViewHolder()
    }

    private fun initProductCardContainer(recommendationItemViewModel: RecommendationItemViewModel) {

        productCardViewHintListener?.setOnClickListener {
            listener.onProductClick(recommendationItemViewModel.recommendationItem, adapterPosition.toString())
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
        productCardViewHintListener?.setButtonWishlistImage(recommendationItemViewModel.recommendationItem.isWishlist)
        productCardViewHintListener?.setButtonWishlistOnClickListener {
            listener.onWishlistClick(recommendationItemViewModel.recommendationItem)
        }
    }

    private fun initPromoLabel(recommendationItemViewModel: RecommendationItemViewModel) {
        val promoLabelViewModel : LabelGroupViewModel? = getFirstLabelGroupOfPosition(recommendationItemViewModel, LABEL_GROUP_POSITION_PROMO)

        if(promoLabelViewModel != null) {
            productCardViewHintListener?.setLabelPromoVisible(true)
            productCardViewHintListener?.setLabelPromoType(promoLabelViewModel.type)
            productCardViewHintListener?.setLabelPromoText(promoLabelViewModel.title)
        }
        else {
            productCardViewHintListener?.setLabelPromoVisible(false)
        }
    }

    @Nullable
    private fun getFirstLabelGroupOfPosition(recommendationItemViewModel: RecommendationItemViewModel, position: String): LabelGroupViewModel? {
        val labelGroupOfPosition = getLabelGroupOfPosition(recommendationItemViewModel, position)

        return if(labelGroupOfPosition != null && labelGroupOfPosition.isNotEmpty()) labelGroupOfPosition[0] else null
    }

    @Nullable
    private fun getLabelGroupOfPosition(recommendationItemViewModel: RecommendationItemViewModel, position: String): List<LabelGroupViewModel>? {
        return recommendationItemViewModel.labelGroupList.filter { labelGroup -> labelGroup.position == position }
    }

    private fun initShopName(recommendationItemViewModel: RecommendationItemViewModel) {
        val isShopNameShown = isShopNameShown(recommendationItemViewModel)
        productCardViewHintListener?.setShopNameVisible(isShopNameShown)

        if(isShopNameShown) {
            productCardViewHintListener?.setShopNameText(recommendationItemViewModel.shopName)
        }
    }

    private fun isShopNameShown(recommendationItemViewModel: RecommendationItemViewModel): Boolean {
        return recommendationItemViewModel.isShopOfficialStore
    }

    private fun initTitleTextView(recommendationItemViewModel: RecommendationItemViewModel) {
        productCardViewHintListener?.setProductNameVisible(true)
        productCardViewHintListener?.setProductNameText(recommendationItemViewModel.productName)
    }

    private fun initSlashedPriceSection(recommendationItemViewModel: RecommendationItemViewModel) {
        val isLabelDiscountVisible = isLabelDiscountVisible(recommendationItemViewModel)

        productCardViewHintListener?.setLabelDiscountVisible(isLabelDiscountVisible)
        productCardViewHintListener?.setSlashedPriceVisible(isLabelDiscountVisible)

        if (isLabelDiscountVisible) {
            productCardViewHintListener?.setLabelDiscountText(recommendationItemViewModel.discountPercentage)
            productCardViewHintListener?.setSlashedPriceText(recommendationItemViewModel.originalPrice)
        }
    }

    private fun isLabelDiscountVisible(recommendationItemViewModel: RecommendationItemViewModel): Boolean {
        return recommendationItemViewModel.discountPercentage > 0
    }

    private fun initPriceTextView(recommendationItemViewModel: RecommendationItemViewModel) {
        productCardViewHintListener?.setPriceVisible(true)
        productCardViewHintListener?.setPriceText(getPriceText(recommendationItemViewModel))
    }

    private fun getPriceText(recommendationItemViewModel: RecommendationItemViewModel) : String {
        return if(!TextUtils.isEmpty(recommendationItemViewModel.priceRange)) recommendationItemViewModel.priceRange
        else recommendationItemViewModel.price
    }

    private fun initShopBadge(recommendationItemViewModel: RecommendationItemViewModel) {
        val hasAnyBadgesShown = hasAnyBadgesShown(recommendationItemViewModel)
        productCardViewHintListener?.setShopBadgesVisible(hasAnyBadgesShown)

        if(hasAnyBadgesShown) {
            productCardViewHintListener?.removeAllShopBadges()
            loopBadgesListToLoadShopBadgeIcon(recommendationItemViewModel.badgesList)
        }
    }

    private fun hasAnyBadgesShown(recommendationItemViewModel: RecommendationItemViewModel): Boolean {
        return recommendationItemViewModel.badgesList.any { badge -> badge.isShown }
    }

    private fun loopBadgesListToLoadShopBadgeIcon(badgesList: List<BadgeItemViewModel>) {
        for (badgeItem in badgesList) {
            if (badgeItem.isShown) {
                loadShopBadgesIcon(badgeItem.imageUrl ?: "")
            }
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
        initCredibilityLabel(recommendationItemViewModel)
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

    private fun initCredibilityLabel(recommendationItemViewModel: RecommendationItemViewModel) {
        val isCredibilityLabelVisible = isCredibilityLabelVisible(recommendationItemViewModel)
        productCardViewHintListener?.setLabelCredibilityVisible(isCredibilityLabelVisible)

        if (isCredibilityLabelVisible) {
            val credibilityLabelViewModel : LabelGroupViewModel =
                    getFirstLabelGroupOfPosition(recommendationItemViewModel, LABEL_GROUP_POSITION_CREDIBILITY)!!

            productCardViewHintListener?.setLabelCredibilityType(credibilityLabelViewModel.type)
            productCardViewHintListener?.setLabelCredibilityText(credibilityLabelViewModel.title)
        }
    }

    private fun isCredibilityLabelVisible(recommendationItemViewModel: RecommendationItemViewModel): Boolean {
        return isRatingAndReviewNotVisible(recommendationItemViewModel)
                && isCredibilityLabelExists(recommendationItemViewModel)
    }

    private fun isRatingAndReviewNotVisible(recommendationItemViewModel: RecommendationItemViewModel): Boolean {
        return !isImageRatingVisible(recommendationItemViewModel) && !isReviewCountVisible(recommendationItemViewModel)
    }

    private fun isCredibilityLabelExists(recommendationItemViewModel: RecommendationItemViewModel): Boolean {
        return getFirstLabelGroupOfPosition(recommendationItemViewModel, LABEL_GROUP_POSITION_CREDIBILITY) != null
    }

    private fun initOffersLabel(recommendationItemViewModel: RecommendationItemViewModel) {
        val offersLabelViewModel = getFirstLabelGroupOfPosition(recommendationItemViewModel, LABEL_GROUP_POSITION_OFFERS)

        productCardViewHintListener?.setLabelOffersVisible(offersLabelViewModel != null)

        if(offersLabelViewModel != null) {
            setOffersLabelContent(offersLabelViewModel)
        }
    }

    private fun setOffersLabelContent(offersLabelViewModel: LabelGroupViewModel) {
        productCardViewHintListener?.setLabelOffersType(offersLabelViewModel.type)
        productCardViewHintListener?.setLabelOffersText(offersLabelViewModel.title)
    }

    private fun initTopAdsIcon(recommendationItemViewModel: RecommendationItemViewModel) {
        productCardViewHintListener?.setImageTopAdsVisible(recommendationItemViewModel.recommendationItem.isTopAds)
    }

    private fun finishBindViewHolder() {
        productCardViewHintListener?.realignLayout()
    }

}