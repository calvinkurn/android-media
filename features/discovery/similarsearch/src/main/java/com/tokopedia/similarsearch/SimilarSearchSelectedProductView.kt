package com.tokopedia.similarsearch

import android.view.View
import androidx.annotation.DrawableRes
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.discovery.common.model.SimilarSearchSelectedProduct
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import kotlinx.android.synthetic.main.similar_search_selected_product_layout.view.*

internal class SimilarSearchSelectedProductView(
        private val similarSearchSelectedProductViewListener: SimilarSearchSelectedProductViewListener
) {

    private val fragmentView = similarSearchSelectedProductViewListener.getFragmentView()
    private val context = fragmentView.context

    fun bindSelectedProductView() {
        val similarSearchSelectedProduct = similarSearchSelectedProductViewListener.getSelectedProduct() ?: return

        initImageProduct(similarSearchSelectedProduct)
        initButtonWishlist(similarSearchSelectedProduct)
        initProductName(similarSearchSelectedProduct)
        initProductPrice(similarSearchSelectedProduct)
        initShopLocation(similarSearchSelectedProduct)
        initRating(similarSearchSelectedProduct)
        initReview(similarSearchSelectedProduct)
    }

    private fun initImageProduct(similarSearchSelectedProduct: SimilarSearchSelectedProduct) {
        ImageHandler.loadImageRounded2(context, fragmentView.imageProduct, similarSearchSelectedProduct.imageUrl, 8f)
    }

    private fun initButtonWishlist(similarSearchSelectedProduct: SimilarSearchSelectedProduct) {
        updateWishlistStatus(similarSearchSelectedProduct.isWishlisted)

        fragmentView.buttonWishlist?.setOnClickListener {
            similarSearchSelectedProductViewListener.onButtonWishlistClicked()
        }
    }

    private fun initProductName(similarSearchSelectedProduct: SimilarSearchSelectedProduct) {
        fragmentView.textViewProductName?.text = similarSearchSelectedProduct.name
    }

    private fun initProductPrice(similarSearchSelectedProduct: SimilarSearchSelectedProduct) {
        fragmentView.textViewPrice?.text = similarSearchSelectedProduct.price
    }

    private fun initShopLocation(similarSearchSelectedProduct: SimilarSearchSelectedProduct) {
        fragmentView.textViewShopLocation?.text = similarSearchSelectedProduct.location
    }

    private fun initRating(similarSearchSelectedProduct: SimilarSearchSelectedProduct) {
        if (similarSearchSelectedProduct.ratingCount == 0) {
            hideRating()
        }
        else {
            showRating(similarSearchSelectedProduct.ratingCount)
        }
    }

    private fun hideRating() {
        fragmentView.linearLayoutImageRating?.visibility = View.GONE
    }

    private fun showRating(rating: Int) {
        fragmentView.linearLayoutImageRating?.visibility = View.VISIBLE

        fragmentView.imageViewRating1?.setImageResource(getRatingDrawable(rating >= 1))
        fragmentView.imageViewRating2?.setImageResource(getRatingDrawable(rating >= 2))
        fragmentView.imageViewRating3?.setImageResource(getRatingDrawable(rating >= 3))
        fragmentView.imageViewRating4?.setImageResource(getRatingDrawable(rating >= 4))
        fragmentView.imageViewRating5?.setImageResource(getRatingDrawable(rating >= 5))
    }

    @DrawableRes
    private fun getRatingDrawable(isActive: Boolean): Int {
        return if(isActive) R.drawable.similar_search_ic_rating_active
        else R.drawable.similar_search_ic_rating_default
    }

    private fun initReview(similarSearchSelectedProduct: SimilarSearchSelectedProduct) {
        fragmentView.textViewReviewCount?.shouldShowWithAction(similarSearchSelectedProduct.reviewCount != 0) {
            fragmentView.textViewReviewCount?.text = context.getString(R.string.similar_search_selected_product_review_count, similarSearchSelectedProduct.reviewCount.toString())
        }
    }

    fun updateWishlistStatus(isWishlisted: Boolean) {
        if (isWishlisted) {
            fragmentView.buttonWishlist?.setImageResource(R.drawable.similar_search_ic_wishlist_active)
        }
        else {
            fragmentView.buttonWishlist?.setImageResource(R.drawable.similar_search_ic_wishlist_inactive)
        }
    }
}