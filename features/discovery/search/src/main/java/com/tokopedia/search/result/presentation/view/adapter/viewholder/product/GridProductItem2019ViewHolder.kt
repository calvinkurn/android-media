package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.graphics.Paint
import android.support.annotation.LayoutRes
import android.text.TextUtils
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import kotlinx.android.synthetic.main.search_srp_item_grid_2019.view.*
import java.lang.StringBuilder

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
        initTitleTextView(productItem)
        initSlashPrice(productItem)
        initPriceTextView(productItem)
        initLocationTextView(productItem)
        initRatingAndReview(productItem)
        initCredibilityLabel(productItem)
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

    private fun initTitleTextView(productItem: ProductItemViewModel) {
        itemView.titleTextView?.text = MethodChecker.fromHtml(productItem.productName)
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

    private fun initLocationTextView(productItem: ProductItemViewModel) {
        if(!TextUtils.isEmpty(productItem.shopCity)) {
            itemView.locationTextView?.text = MethodChecker.fromHtml(productItem.shopCity)
            itemView.locationTextView?.visibility = View.VISIBLE
        }
        else {
            itemView.locationTextView?.visibility = View.GONE
        }
    }

    private fun initRatingAndReview(productItem: ProductItemViewModel) {
        initRatingView(productItem)
        initReviewCount(productItem)
        initRatingAndReviewContainer(productItem)
    }

    private fun initRatingView(productItem: ProductItemViewModel) {
        if (productItem.rating != 0) {
            itemView.ratingImage?.visibility = View.VISIBLE
            itemView.ratingImage?.setImageResource(getRatingDrawable(getStarCount(productItem)))
        } else {
            itemView.ratingImage?.visibility = View.GONE
        }
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
        if (productItem.countReview != 0) {
            itemView.reviewCountTextView?.visibility = View.VISIBLE
            itemView.reviewCountTextView?.text = getReviewCountFormattedAsText(productItem.countReview)
        } else {
            itemView.reviewCountTextView?.visibility = View.GONE
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
        return productItem.rating != 0 || productItem.countReview != 0
    }

    // TODO:: Dummy method, set Label from productItem instead
    private fun initCredibilityLabel(productItem: ProductItemViewModel) {
        if(!isRatingAndReviewContainerVisible(productItem)) {
            if(true) { // Check product has credibility label
                itemView.credibilityLabel?.setLabelDesign(context.resources.getString(R.string.product_card_light_blue))
                itemView.credibilityLabel?.visibility = View.VISIBLE
                itemView.credibilityLabel?.text = "Terbaroe"
                return
            }
        }

        itemView.credibilityLabel?.visibility = View.GONE
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