package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

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
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.BadgeItemViewModel
import com.tokopedia.search.result.presentation.model.LabelGroupViewModel
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import kotlinx.android.synthetic.main.search_small_grid_product_card.view.*

private const val LABEL_GROUP_POSITION_PROMO = "promo"
private const val LABEL_GROUP_POSITION_CREDIBILITY = "credibility"
private const val LABEL_GROUP_POSITION_OFFERS = "offers"

open class SmallGridProductCardViewHolder(
    itemView: View,
    protected val productListener: ProductListener
) : AbstractViewHolder<ProductItemViewModel>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_small_grid_product_card
    }

    protected val context = itemView.context!!

    override fun bind(productItem: ProductItemViewModel?) {
        if (productItem == null) return

        initProductCardContainer(productItem)
        initProductImage(productItem)
        initWishlistButton(productItem)
        initPromoLabel(productItem)
        initShopName(productItem)
        initTitleTextView(productItem)
        initSlashedPriceSection(productItem)
        initPriceTextView(productItem)
        initShopBadge(productItem)
        initLocationTextView(productItem)
        initCredibilitySection(productItem)
        initOffersLabel(productItem)
        initTopAdsIcon(productItem)

        itemView.productCardView?.realignLayout()
    }

    private fun initProductCardContainer(productItem: ProductItemViewModel) {
        itemView.productCardView?.setOnLongClickListener {
            productListener.onLongClick(productItem, adapterPosition)
            true
        }

        itemView.productCardView?.setOnClickListener {
            productListener.onItemClicked(productItem, adapterPosition)
        }
    }

    private fun initProductImage(productItem: ProductItemViewModel) {
        itemView.productCardView?.setImageProductVisible(true)

        itemView.productCardView?.setImageProductUrl(productItem.imageUrl)

        itemView.productCardView?.setImageProductViewHintListener(productItem){
            productListener.onProductImpressed(productItem, adapterPosition)
        }
    }

    private fun initWishlistButton(productItem: ProductItemViewModel) {
        itemView.productCardView?.setButtonWishlistVisible(productItem.isWishlistButtonEnabled)
        itemView.productCardView?.setButtonWishlistImage(productItem.isWishlisted)
        itemView.productCardView?.setButtonWishlistOnClickListener {
            if (productItem.isWishlistButtonEnabled) {
                productListener.onWishlistButtonClicked(productItem)
            }
        }
    }

    private fun initPromoLabel(productItem: ProductItemViewModel) {
        val promoLabelViewModel : LabelGroupViewModel? = getFirstLabelGroupOfPosition(productItem, LABEL_GROUP_POSITION_PROMO)

        if(promoLabelViewModel != null) {
            itemView.productCardView?.setLabelPromoVisible(true)
            itemView.productCardView?.setLabelPromoType(promoLabelViewModel.type)
            itemView.productCardView?.setLabelPromoText(promoLabelViewModel.title)
        }
        else {
            itemView.productCardView?.setLabelPromoVisible(false)
        }
    }

    @Nullable
    private fun getFirstLabelGroupOfPosition(productItem: ProductItemViewModel, position: String): LabelGroupViewModel? {
        val labelGroupOfPosition = getLabelGroupOfPosition(productItem, position)

        return if(labelGroupOfPosition != null && labelGroupOfPosition.isNotEmpty()) labelGroupOfPosition[0] else null
    }

    @Nullable
    private fun getLabelGroupOfPosition(productItem: ProductItemViewModel, position: String): List<LabelGroupViewModel>? {
        return productItem.labelGroupList.filter { labelGroup -> labelGroup.position == position }
    }

    private fun initShopName(productItem: ProductItemViewModel) {
        val isShopNameShown = isShopNameShown(productItem)
        itemView.productCardView?.setShopNameVisible(isShopNameShown)

        if(isShopNameShown) {
            itemView.productCardView?.setShopNameText(productItem.shopName)
        }
    }

    private fun isShopNameShown(productItem: ProductItemViewModel): Boolean {
        return productItem.isShopOfficialStore
    }

    private fun initTitleTextView(productItem: ProductItemViewModel) {
        itemView.productCardView?.setProductNameVisible(true)
        itemView.productCardView?.setProductNameText(productItem.productName)
    }

    private fun initSlashedPriceSection(productItem: ProductItemViewModel) {
        val isLabelDiscountVisible = isLabelDiscountVisible(productItem)

        itemView.productCardView?.setLabelDiscountVisible(isLabelDiscountVisible)
        itemView.productCardView?.setSlashedPriceVisible(isLabelDiscountVisible)

        if (isLabelDiscountVisible) {
            itemView.productCardView?.setLabelDiscountText(productItem.discountPercentage)
            itemView.productCardView?.setSlashedPriceText(productItem.originalPrice)
        }
    }

    private fun isLabelDiscountVisible(productItem: ProductItemViewModel): Boolean {
        return productItem.discountPercentage > 0
    }

    private fun initPriceTextView(productItem: ProductItemViewModel) {
        itemView.productCardView?.setPriceVisible(true)
        itemView.productCardView?.setPriceText(getPriceText(productItem))
    }

    private fun getPriceText(productItem: ProductItemViewModel) : String {
        return if(!TextUtils.isEmpty(productItem.priceRange)) productItem.priceRange
        else productItem.price
    }

    private fun initShopBadge(productItem: ProductItemViewModel) {
        val hasAnyBadgesShown = hasAnyBadgesShown(productItem)
        itemView.productCardView?.setShopBadgesVisible(hasAnyBadgesShown)

        if(hasAnyBadgesShown) {
            itemView.productCardView?.removeAllShopBadges()
            loopBadgesListToLoadShopBadgeIcon(productItem.badgesList)
        }
    }

    private fun hasAnyBadgesShown(productItem: ProductItemViewModel): Boolean {
        return productItem.badgesList.any { badge -> badge.isShown }
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
            val view = LayoutInflater.from(context).inflate(R.layout.search_product_card_badge_layout, null)

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
            itemView.productCardView?.addShopBadge(view)
        }
    }

    private fun loadShopBadgeFailed(view: View) {
        view.visibility = View.GONE
    }

    private fun initLocationTextView(productItem: ProductItemViewModel) {
        val isShopLocationShown = isShopLocationShown(productItem)
        itemView.productCardView?.setShopLocationVisible(isShopLocationShown)

        if(isShopLocationShown) {
            itemView.productCardView?.setShopLocationText(productItem.shopCity)
        }
    }

    private fun isShopLocationShown(productItem: ProductItemViewModel) : Boolean {
        return !TextUtils.isEmpty(productItem.shopCity)
    }

    private fun initCredibilitySection(productItem: ProductItemViewModel) {
        initRatingAndReview(productItem)
        initCredibilityLabel(productItem)
    }

    private fun initRatingAndReview(productItem: ProductItemViewModel) {
        initRatingView(productItem)
        initReviewCount(productItem)
    }

    private fun initRatingView(productItem: ProductItemViewModel) {
        val isImageRatingVisible = isImageRatingVisible(productItem)

        itemView.productCardView?.setImageRatingVisible(isImageRatingVisible)

        if(isImageRatingVisible) {
            itemView.productCardView?.setRating(getStarCount(productItem))
        }
    }

    private fun isImageRatingVisible(productItem: ProductItemViewModel): Boolean {
        return productItem.rating != 0
    }

    private fun getStarCount(productItem: ProductItemViewModel): Int {
        return if (productItem.isTopAds)
            Math.round(productItem.rating / 20f)
        else
            Math.round(productItem.rating.toFloat())
    }

    private fun initReviewCount(productItem: ProductItemViewModel) {
        val isReviewCountVisible = isReviewCountVisible(productItem)
        itemView.productCardView?.setReviewCountVisible(isReviewCountVisible)

        if(isReviewCountVisible) {
            itemView.productCardView?.setReviewCount(productItem.countReview)
        }
    }

    private fun isReviewCountVisible(productItem: ProductItemViewModel): Boolean {
        return productItem.countReview != 0
    }

    private fun initCredibilityLabel(productItem: ProductItemViewModel) {
        val isCredibilityLabelVisible = isCredibilityLabelVisible(productItem)
        itemView.productCardView?.setLabelCredibilityVisible(isCredibilityLabelVisible)

        if (isCredibilityLabelVisible) {
            val credibilityLabelViewModel : LabelGroupViewModel =
                getFirstLabelGroupOfPosition(productItem, LABEL_GROUP_POSITION_CREDIBILITY)!!

            itemView.productCardView?.setLabelCredibilityType(credibilityLabelViewModel.type)
            itemView.productCardView?.setLabelCredibilityText(credibilityLabelViewModel.title)
        }
    }

    private fun isCredibilityLabelVisible(productItem: ProductItemViewModel): Boolean {
        return isRatingAndReviewNotVisible(productItem)
                && isCredibilityLabelExists(productItem)
    }

    private fun isRatingAndReviewNotVisible(productItem: ProductItemViewModel): Boolean {
        return !isImageRatingVisible(productItem) && !isReviewCountVisible(productItem)
    }

    private fun isCredibilityLabelExists(productItem: ProductItemViewModel): Boolean {
        return getFirstLabelGroupOfPosition(productItem, LABEL_GROUP_POSITION_CREDIBILITY) != null
    }

    private fun initOffersLabel(productItem: ProductItemViewModel) {
        val offersLabelViewModel = getFirstLabelGroupOfPosition(productItem, LABEL_GROUP_POSITION_OFFERS)

        itemView.productCardView?.setLabelOffersVisible(offersLabelViewModel != null)

        if(offersLabelViewModel != null) {
            setOffersLabelContent(offersLabelViewModel)
        }
    }

    private fun setOffersLabelContent(offersLabelViewModel: LabelGroupViewModel) {
        itemView.productCardView?.setLabelOffersType(offersLabelViewModel.type)
        itemView.productCardView?.setLabelOffersText(offersLabelViewModel.title)
    }

    private fun initTopAdsIcon(productItem: ProductItemViewModel) {
        itemView.productCardView?.setImageTopAdsVisible(productItem.isTopAds)
    }
}