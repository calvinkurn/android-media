package com.tokopedia.similarsearch.originalproduct

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.similarsearch.R
import com.tokopedia.similarsearch.getsimilarproducts.model.Product
import kotlinx.android.synthetic.main.similar_search_original_product_layout.view.*

internal class OriginalProductView
    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null, @AttrRes defStyleAttr: Int = 0)
    : BaseCustomView(context, attributeSet, defStyleAttr) {

    private val inflatedView =
            LayoutInflater
                    .from(context)
                    .inflate(R.layout.similar_search_original_product_layout, this, true)
    private var originalProductCardViewAnimator: OriginalProductViewAnimator? = null
    private var originalProductViewListener: OriginalProductViewListener? = null

    fun bindOriginalProductView(similarSearchOriginalProduct: Product, originalProductViewListener: OriginalProductViewListener) {
        this.originalProductViewListener = originalProductViewListener

        initCardViewOriginalProduct()
        initImageProduct(similarSearchOriginalProduct)
        initButtonWishlist(similarSearchOriginalProduct)
        initProductName(similarSearchOriginalProduct)
        initProductPrice(similarSearchOriginalProduct)
        initShopLocation(similarSearchOriginalProduct)
        initRating(similarSearchOriginalProduct)
        initReview(similarSearchOriginalProduct)
        initOnButtonBuyClicked()
        initOnButtonAddToCartClicked()

        originalProductCardViewAnimator = OriginalProductViewAnimator(this)
    }

    private fun initCardViewOriginalProduct() {
        inflatedView.cardViewOriginalProductSimilarSearch?.setOnClickListener {
            originalProductViewListener?.onItemClicked()
        }
    }

    private fun initImageProduct(similarSearchOriginalProduct: Product) {
        ImageHandler.loadImageRounded2(context, inflatedView.imageProduct, similarSearchOriginalProduct.imageUrl, 6f.toPx())
    }

    private fun Float.toPx(): Float {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                this,
                resources.displayMetrics
        )
    }

    private fun initButtonWishlist(similarSearchOriginalProduct: Product) {
        updateWishlistStatus(similarSearchOriginalProduct.isWishlisted)

        inflatedView.buttonWishlist?.setOnClickListener {
            originalProductViewListener?.onButtonWishlistClicked()
        }
    }

    fun updateWishlistStatus(isWishlisted: Boolean) {
        if (isWishlisted) {
            inflatedView.buttonWishlist?.setImageResource(R.drawable.similar_search_ic_wishlist_active)
        }
        else {
            inflatedView.buttonWishlist?.setImageResource(R.drawable.similar_search_ic_wishlist_inactive)
        }
    }

    private fun initProductName(similarSearchOriginalProduct: Product) {
        inflatedView.textViewProductName?.text = similarSearchOriginalProduct.name
        inflatedView.textViewProductNameCollapsed?.text = similarSearchOriginalProduct.name
    }

    private fun initProductPrice(similarSearchOriginalProduct: Product) {
        inflatedView.textViewPrice?.text = similarSearchOriginalProduct.price
    }

    private fun initShopLocation(similarSearchOriginalProduct: Product) {
        inflatedView.textViewShopLocation?.text = similarSearchOriginalProduct.shop.location
    }

    private fun initRating(similarSearchOriginalProduct: Product) {
        if (similarSearchOriginalProduct.rating == 0) {
            hideRating()
        }
        else {
            showRating(similarSearchOriginalProduct.rating)
        }
    }

    private fun hideRating() {
        inflatedView.linearLayoutImageRating?.visibility = View.GONE
    }

    private fun showRating(rating: Int) {
        inflatedView.linearLayoutImageRating?.visibility = View.VISIBLE

        inflatedView.imageViewRating1?.setImageResource(getRatingDrawable(rating >= 1))
        inflatedView.imageViewRating2?.setImageResource(getRatingDrawable(rating >= 2))
        inflatedView.imageViewRating3?.setImageResource(getRatingDrawable(rating >= 3))
        inflatedView.imageViewRating4?.setImageResource(getRatingDrawable(rating >= 4))
        inflatedView.imageViewRating5?.setImageResource(getRatingDrawable(rating >= 5))
    }

    @DrawableRes
    private fun getRatingDrawable(isActive: Boolean): Int {
        return if(isActive) R.drawable.similar_search_ic_rating_active
        else R.drawable.similar_search_ic_rating_default
    }

    private fun initReview(similarSearchOriginalProduct: Product) {
        inflatedView.textViewReviewCount?.shouldShowWithAction(similarSearchOriginalProduct.countReview != 0) {
            inflatedView.textViewReviewCount?.text = context.getString(R.string.similar_search_original_product_review_count, similarSearchOriginalProduct.countReview.toString())
        }
    }

    private fun initOnButtonBuyClicked() {
        inflatedView.buttonBuy?.setOnClickListener {
            originalProductViewListener?.onButtonBuyClicked()
        }
    }

    private fun initOnButtonAddToCartClicked() {
        inflatedView.buttonAddToCart?.setOnClickListener {
            originalProductViewListener?.onButtonAddToCartClicked()
        }

        inflatedView.buttonAddToCartCollapsed?.setOnClickListener {
            originalProductViewListener?.onButtonAddToCartClicked()
        }
    }

    fun animateBasedOnScroll(dy: Int) {
        if (isVisible) {
            originalProductCardViewAnimator?.animateBasedOnScroll(dy)
        }
    }
}