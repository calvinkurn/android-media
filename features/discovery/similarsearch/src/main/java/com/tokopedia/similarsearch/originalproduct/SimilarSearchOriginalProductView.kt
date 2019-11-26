package com.tokopedia.similarsearch.originalproduct

import android.view.View
import androidx.annotation.DrawableRes
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.similarsearch.R
import com.tokopedia.similarsearch.getsimilarproducts.model.Product
import kotlinx.android.synthetic.main.similar_search_fragment_layout.view.*
import kotlinx.android.synthetic.main.similar_search_original_product_layout.view.*

internal class SimilarSearchOriginalProductView(
        private val similarSearchOriginalProductViewListener: SimilarSearchOriginalProductViewListener
) {

    private val fragmentView = similarSearchOriginalProductViewListener.getFragmentView()
    private val context = fragmentView.context

    fun bindOriginalProductView(similarSearchOriginalProduct: Product) {
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
    }

    private fun initCardViewOriginalProduct() {
        fragmentView.cardViewOriginalProductSimilarSearch.visible()

        fragmentView.cardViewOriginalProductSimilarSearch?.setOnClickListener {
            similarSearchOriginalProductViewListener.onItemClicked()
        }
    }

    private fun initImageProduct(similarSearchOriginalProduct: Product) {
        ImageHandler.loadImageRounded2(context, fragmentView.imageProduct, similarSearchOriginalProduct.imageUrl, 8f)
    }

    private fun initButtonWishlist(similarSearchOriginalProduct: Product) {
        updateWishlistStatus(similarSearchOriginalProduct.isWishlisted)

        fragmentView.buttonWishlist?.setOnClickListener {
            similarSearchOriginalProductViewListener.onButtonWishlistClicked()
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

    private fun initProductName(similarSearchOriginalProduct: Product) {
        fragmentView.textViewProductName?.text = similarSearchOriginalProduct.name
    }

    private fun initProductPrice(similarSearchOriginalProduct: Product) {
        fragmentView.textViewPrice?.text = similarSearchOriginalProduct.price
    }

    private fun initShopLocation(similarSearchOriginalProduct: Product) {
        fragmentView.textViewShopLocation?.text = similarSearchOriginalProduct.shop.location
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

    private fun initReview(similarSearchOriginalProduct: Product) {
        fragmentView.textViewReviewCount?.shouldShowWithAction(similarSearchOriginalProduct.countReview != 0) {
            fragmentView.textViewReviewCount?.text = context.getString(R.string.similar_search_original_product_review_count, similarSearchOriginalProduct.countReview.toString())
        }
    }

    private fun initOnButtonBuyClicked() {
        fragmentView.buttonBuy?.setOnClickListener {
            similarSearchOriginalProductViewListener.onButtonBuyClicked()
        }
    }

    private fun initOnButtonAddToCartClicked() {
        fragmentView.buttonAddToCart?.setOnClickListener {
            similarSearchOriginalProductViewListener.onButtonAddToCartClicked()
        }
    }
}