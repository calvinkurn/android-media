package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.graphics.Bitmap
import android.graphics.Paint
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
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.BadgeItemViewModel
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import kotlinx.android.synthetic.main.search_srp_item_grid_2019.view.*

class GridProductItem2019ViewHolder(
    itemView: View,
    protected val productListener: ProductListener
) : AbstractViewHolder<ProductItemViewModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_srp_item_grid_2019
    }

    protected val context = itemView.context!!

    override fun bind(productItem: ProductItemViewModel?) {
        if(productItem == null) return

        initProductCardContainer(productItem)
        initProductImage(productItem)
        initWishlistButtonContainer(productItem)
        initWishlistButton(productItem)
        initPromoLabel(productItem)
        initShopName(productItem)
        initShopImage(productItem)
        initTitleTextView(productItem)
        initSlashPrice(productItem)
        initPriceTextView(productItem)
        initShopBadge(productItem)
        initLocationTextView(productItem)
        initCredibilitySection(productItem)
        initOffersLabel(productItem)
        initTopAdsIcon(productItem)
    }

    private fun initProductCardContainer(productItem: ProductItemViewModel) {
        itemView.productCardContainer?.setOnLongClickListener {
            productListener.onLongClick(productItem, adapterPosition)
            true
        }

        itemView.productCardContainer?.setOnClickListener {
            productListener.onItemClicked(productItem, adapterPosition)
        }
    }

    private fun initProductImage(productItem: ProductItemViewModel) {
        itemView.productImage?.setViewHintListener(productItem) {
            productListener.onProductImpressed(productItem, adapterPosition)
        }

        if(itemView.productImage != null) {
            ImageHandler.loadImageThumbs(context, itemView.productImage, productItem.imageUrl)
        }
    }

    private fun initWishlistButtonContainer(productItem: ProductItemViewModel) {
        itemView.wishlistButtonContainer?.isEnabled = productItem.isWishlistButtonEnabled

        itemView.wishlistButtonContainer?.setOnClickListener {
            if(productItem.isWishlistButtonEnabled) {
                productListener.onWishlistButtonClicked(productItem)
            }
        }
    }

    private fun initWishlistButton(productItem: ProductItemViewModel) {
        if(productItem.isWishlisted) {
            itemView.wishlistButton?.setBackgroundResource(R.drawable.search_ic_wishlist_red)
        }
        else {
            itemView.wishlistButton?.setBackgroundResource(R.drawable.search_ic_wishlist)
        }
    }

    // TODO:: Dummy method, set Label from productItem instead
    private fun initPromoLabel(productItem: ProductItemViewModel) {
        itemView.promoLabel?.setLabelDesign(context.resources.getString(R.string.product_card_light_green))
        itemView.promoLabel?.visibility = View.VISIBLE
        itemView.promoLabel?.text = "Cashback 100%"
    }

    private fun initShopName(productItem: ProductItemViewModel) {
        if (isShopNameShown(productItem)) {
            itemView.shopNameTextView?.visibility = View.VISIBLE
            itemView.shopNameTextView?.text = if(!TextUtils.isEmpty(productItem.shopName)) productItem.shopName else "dummy name"
        }
        else {
            itemView.shopNameTextView?.visibility = View.GONE
        }
    }

    // TODO:: Dummy method, set Shop Name from productItem instead
    private fun isShopNameShown(productItem: ProductItemViewModel): Boolean {
        return adapterPosition % 2 == 0
    }

    private fun initShopImage(productItem: ProductItemViewModel) {
        if(isShopImageShown(productItem)) {
            itemView.shopImage?.visibility = View.VISIBLE

            if (itemView.shopImage != null) {
                ImageHandler.loadImageCircle2(context, itemView.shopImage, productItem.imageUrl700)
            }
        }
        else {
            itemView.shopImage?.visibility = View.GONE
        }
    }

    private fun isShopImageShown(productItem: ProductItemViewModel): Boolean {
        return adapterPosition % 2 == 0
    }

    private fun initTitleTextView(productItem: ProductItemViewModel) {
        setTitlePaddingTop(productItem)

        itemView.titleTextView?.text = MethodChecker.fromHtml(productItem.productName)
    }

    private fun setTitlePaddingTop(productItem: ProductItemViewModel) {
        var paddingTopPixel = 0

        if(!isShopNameShown(productItem)) {
            paddingTopPixel = convertDpToPixel(8)
        }

        itemView.titleTextView?.setPadding(
            itemView.titleTextView.paddingLeft,
            paddingTopPixel,
            itemView.titleTextView.paddingRight,
            itemView.titleTextView.paddingBottom
        )
    }

    private fun convertDpToPixel(dpValue: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    // TODO:: Dummy method, set Slash Price from productItem instead
    private fun initSlashPrice(productItem: ProductItemViewModel) {
        itemView.slashPriceContainer?.visibility = View.VISIBLE
        itemView.slashPriceLabel?.text = "20%"
        itemView.slashPriceLabel?.setLabelDesign(context.resources.getString(R.string.product_card_light_red))
        itemView.slashPriceTextView?.text = "Rp 10.000.000"
        itemView.slashPriceTextView?.paintFlags = itemView.slashPriceTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    private fun initPriceTextView(productItem: ProductItemViewModel) {
        itemView.priceTextView?.text = getPriceText(productItem)
    }

    private fun getPriceText(productItem: ProductItemViewModel) : CharSequence {
        return if(!TextUtils.isEmpty(productItem.priceRange)) productItem.priceRange
        else productItem.price
    }

    private fun initShopBadge(productItem: ProductItemViewModel) {
        if(itemView.shopBadgesContainer != null) {
            itemView.shopBadgesContainer.removeAllViews()
            loopBadgesListToLoadShopBadgeIcon(productItem.badgesList)
        }
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
            val view = LayoutInflater.from(context).inflate(R.layout.badge_layout, null)

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
            itemView.shopBadgesContainer?.addView(view)
        }
    }

    private fun loadShopBadgeFailed(view: View) {
        view.visibility = View.GONE
    }

    private fun initLocationTextView(productItem: ProductItemViewModel) {
        setLocationTextViewPadding(productItem)

        if(!TextUtils.isEmpty(productItem.shopCity)) {
            itemView.locationTextView?.text = MethodChecker.fromHtml(productItem.shopCity)
            itemView.locationTextView?.visibility = View.VISIBLE
        }
        else {
            itemView.locationTextView?.visibility = View.GONE
        }
    }

    private fun setLocationTextViewPadding(productItem: ProductItemViewModel) {
        if(itemView.locationTextView != null) {
            val paddingLeftDp = if (hasAnyBadgesShown(productItem)) 4 else 8

            itemView.locationTextView?.setPadding(
                paddingLeftDp,
                itemView.locationTextView.paddingTop,
                itemView.locationTextView.paddingRight,
                itemView.locationTextView.paddingBottom
            )
        }
    }

    private fun hasAnyBadgesShown(productItem: ProductItemViewModel): Boolean {
        return productItem.badgesList.any { badge -> badge.isShown }
    }

    private fun initCredibilitySection(productItem: ProductItemViewModel) {
        initRatingAndReview(productItem)
        initCredibilityLabel(productItem)
        initCredibilitySectionContainer(productItem)
    }

    private fun initRatingAndReview(productItem: ProductItemViewModel) {
        initRatingView(productItem)
        initReviewCount(productItem)
        initRatingAndReviewContainer(productItem)
    }

    private fun initRatingView(productItem: ProductItemViewModel) {
        if (isRatingViewVisible(productItem)) {
            itemView.ratingImage?.visibility = View.VISIBLE
            itemView.ratingImage?.setImageResource(getRatingDrawable(getStarCount(productItem)))
        } else {
            itemView.ratingImage?.visibility = View.GONE
        }
    }

    private fun isRatingViewVisible(productItem: ProductItemViewModel): Boolean {
        return productItem.rating != 0
    }

    private fun getRatingDrawable(param: Int): Int {
        return when (param) {
            0 -> R.drawable.search_ic_star_none
            1 -> R.drawable.search_ic_star_one
            2 -> R.drawable.search_ic_star_two
            3 -> R.drawable.search_ic_star_three
            4 -> R.drawable.search_ic_star_four
            5 -> R.drawable.search_ic_star_five
            else -> R.drawable.search_ic_star_none
        }
    }

    private fun getStarCount(productItem: ProductItemViewModel): Int {
        return if (productItem.isTopAds)
            Math.round(productItem.rating / 20f)
        else
            Math.round(productItem.rating.toFloat())
    }

    private fun initReviewCount(productItem: ProductItemViewModel) {
        if (isReviewCountVisible(productItem)) {
            setReviewCountPadding(productItem)

            itemView.reviewCountTextView?.visibility = View.VISIBLE
            itemView.reviewCountTextView?.text = getReviewCountFormattedAsText(productItem.countReview)
        } else {
            itemView.reviewCountTextView?.visibility = View.GONE
        }
    }

    private fun isReviewCountVisible(productItem: ProductItemViewModel): Boolean {
        return productItem.countReview != 0
    }

    private fun setReviewCountPadding(productItem: ProductItemViewModel) {
        if(itemView.reviewCountTextView != null) {
            val paddingLeftDp = if (isRatingViewVisible(productItem)) 4 else 0

            itemView.reviewCountTextView?.setPadding(
                paddingLeftDp,
                itemView.reviewCountTextView.paddingTop,
                itemView.reviewCountTextView.paddingRight,
                itemView.reviewCountTextView.paddingBottom
            )
        }
    }

    private fun getReviewCountFormattedAsText(countReview: Int): String {
        val reviewCountStringBuilder = StringBuilder()

        reviewCountStringBuilder.append("(")
        reviewCountStringBuilder.append(countReview)
        reviewCountStringBuilder.append(")")

        return reviewCountStringBuilder.toString()
    }

    private fun initRatingAndReviewContainer(productItem: ProductItemViewModel) {
        if(isRatingAndReviewContainerVisible(productItem)) {
            itemView.ratingReviewContainer?.visibility = View.VISIBLE
        }
        else {
            itemView.ratingReviewContainer?.visibility = View.GONE
        }
    }

    private fun isRatingAndReviewContainerVisible(productItem: ProductItemViewModel): Boolean {
        return isRatingViewVisible(productItem) || isReviewCountVisible(productItem)
    }

    // TODO:: Dummy method, set Label from productItem instead
    private fun initCredibilityLabel(productItem: ProductItemViewModel) {
        if (!isRatingAndReviewContainerVisible(productItem)) {
            if (isCredibilityLabelVisible(productItem)) {
                itemView.credibilityLabel?.setLabelDesign(context.resources.getString(R.string.product_card_light_blue))
                itemView.credibilityLabel?.visibility = View.VISIBLE
                itemView.credibilityLabel?.text = "Terbaroe"
            } else {
                itemView.credibilityLabel?.visibility = View.GONE
            }
        } else {
            itemView.credibilityLabel?.visibility = View.GONE
        }
    }

    // TODO:: Dummy method, set Label from productItem instead
    private fun isCredibilityLabelVisible(productItem: ProductItemViewModel): Boolean {
        return true
    }

    private fun initCredibilitySectionContainer(productItem: ProductItemViewModel) {
        if(isCredibilitySectionContainerVisible(productItem)) {
            itemView.credibilitySectionContainer?.visibility = View.VISIBLE
        }
        else {
            itemView.credibilitySectionContainer?.visibility = View.GONE
        }
    }

    private fun isCredibilitySectionContainerVisible(productItem: ProductItemViewModel): Boolean {
        return isRatingAndReviewContainerVisible(productItem) || isCredibilityLabelVisible(productItem)
    }

    // TODO:: Dummy method, set Label from productItem instead
    private fun initOffersLabel(productItem: ProductItemViewModel) {
        itemView.offersLabel?.setLabelDesign(context.resources.getString(R.string.product_card_dark_grey))
        itemView.offersLabel?.visibility = View.VISIBLE
        itemView.offersLabel?.text = "Tukar tambah"
    }

    private fun initTopAdsIcon(productItem: ProductItemViewModel) {
        if(productItem.isTopAds) {
            itemView.topAdsIconContainer?.visibility = View.VISIBLE
        }
        else {
            itemView.topAdsIconContainer?.visibility = View.GONE
        }
    }
}