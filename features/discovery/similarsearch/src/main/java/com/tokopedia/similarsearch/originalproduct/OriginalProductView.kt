package com.tokopedia.similarsearch.originalproduct

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.similarsearch.R
import com.tokopedia.similarsearch.getsimilarproducts.model.Product
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import kotlin.LazyThreadSafetyMode.NONE

internal class OriginalProductView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0
) : BaseCustomView(context, attributeSet, defStyleAttr) {

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.similar_search_original_product_layout, this, true)
    }

    private var originalProductCardViewAnimator: OriginalProductViewAnimator? = null
    private var originalProductViewListener: OriginalProductViewListener? = null

    private val cardViewOriginalProductSimilarSearch: CardView? by lazy(NONE) {
        findViewById(R.id.cardViewOriginalProductSimilarSearch)
    }
    private val imageProduct: ImageUnify? by lazy(NONE) {
        findViewById(R.id.imageProduct)
    }
    private val buttonWishlist: ImageUnify? by lazy(NONE) {
        findViewById(R.id.buttonWishlist)
    }
    private val buttonBuy: UnifyButton? by lazy(NONE) {
        findViewById(R.id.buttonBuy)
    }
    private val buttonAddToCart: UnifyButton? by lazy(NONE) {
        findViewById(R.id.buttonAddToCart)
    }
    private val buttonAddToCartCollapsed: UnifyButton? by lazy(NONE) {
        findViewById(R.id.buttonAddToCartCollapsed)
    }
    private val textViewProductName: Typography? by lazy(NONE) {
        findViewById(R.id.textViewProductName)
    }
    private val textViewPrice: Typography? by lazy(NONE) {
        findViewById(R.id.textViewPrice)
    }
    private val textViewProductNameCollapsed: Typography? by lazy(NONE) {
        findViewById(R.id.textViewProductNameCollapsed)
    }
    private val textViewShopLocation: Typography? by lazy(NONE) {
        findViewById(R.id.textViewShopLocation)
    }
    private val textViewReviewCount: Typography? by lazy(NONE) {
        findViewById(R.id.textViewReviewCount)
    }
    private val linearLayoutImageRating: LinearLayout? by lazy(NONE) {
        findViewById(R.id.linearLayoutImageRating)
    }
    private val imageViewRating1: ImageUnify? by lazy(NONE) {
        findViewById(R.id.imageViewRating1)
    }
    private val imageViewRating2: ImageUnify? by lazy(NONE) {
        findViewById(R.id.imageViewRating2)
    }
    private val imageViewRating3: ImageUnify? by lazy(NONE) {
        findViewById(R.id.imageViewRating3)
    }
    private val imageViewRating4: ImageUnify? by lazy(NONE) {
        findViewById(R.id.imageViewRating4)
    }
    private val imageViewRating5: ImageUnify? by lazy(NONE) {
        findViewById(R.id.imageViewRating5)
    }

    fun bindOriginalProductView(
        similarSearchOriginalProduct: Product,
        originalProductViewListener: OriginalProductViewListener
    ) {
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
        cardViewOriginalProductSimilarSearch?.setOnClickListener {
            originalProductViewListener?.onItemClicked()
        }
    }

    private fun initImageProduct(similarSearchOriginalProduct: Product) {
        ImageHandler.loadImageRounded2(
            context,
            imageProduct,
            similarSearchOriginalProduct.imageUrl,
            6f.toPx()
        )
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

        buttonWishlist?.setOnClickListener {
            originalProductViewListener?.onButtonWishlistClicked()
        }
    }

    fun updateWishlistStatus(isWishlisted: Boolean) {
        if (isWishlisted) {
            buttonWishlist?.setImageResource(R.drawable.similar_search_ic_wishlist_active)
        } else {
            buttonWishlist?.setImageResource(R.drawable.similar_search_ic_wishlist_inactive)
        }
    }

    private fun initProductName(similarSearchOriginalProduct: Product) {
        textViewProductName?.text = similarSearchOriginalProduct.name
        textViewProductNameCollapsed?.text = similarSearchOriginalProduct.name
    }

    private fun initProductPrice(similarSearchOriginalProduct: Product) {
        textViewPrice?.text = similarSearchOriginalProduct.price
    }

    private fun initShopLocation(similarSearchOriginalProduct: Product) {
        textViewShopLocation?.text = similarSearchOriginalProduct.shop.location
    }

    private fun initRating(similarSearchOriginalProduct: Product) {
        if (similarSearchOriginalProduct.rating == 0) {
            hideRating()
        } else {
            showRating(similarSearchOriginalProduct.rating)
        }
    }

    private fun hideRating() {
        linearLayoutImageRating?.visibility = View.GONE
    }

    private fun showRating(rating: Int) {
        linearLayoutImageRating?.visibility = View.VISIBLE

        imageViewRating1?.setImageResource(getRatingDrawable(rating >= 1))
        imageViewRating2?.setImageResource(getRatingDrawable(rating >= 2))
        imageViewRating3?.setImageResource(getRatingDrawable(rating >= 3))
        imageViewRating4?.setImageResource(getRatingDrawable(rating >= 4))
        imageViewRating5?.setImageResource(getRatingDrawable(rating >= 5))
    }

    @DrawableRes
    private fun getRatingDrawable(isActive: Boolean): Int {
        return if (isActive) R.drawable.similar_search_ic_rating_active
        else R.drawable.similar_search_ic_rating_default
    }

    private fun initReview(similarSearchOriginalProduct: Product) {
        textViewReviewCount?.shouldShowWithAction(similarSearchOriginalProduct.countReview != 0) {
            textViewReviewCount?.text = context.getString(
                R.string.similar_search_original_product_review_count,
                similarSearchOriginalProduct.countReview.toString()
            )
        }
    }

    private fun initOnButtonBuyClicked() {
        buttonBuy?.setOnClickListener {
            originalProductViewListener?.onButtonBuyClicked()
        }
    }

    private fun initOnButtonAddToCartClicked() {
        buttonAddToCart?.setOnClickListener {
            originalProductViewListener?.onButtonAddToCartClicked()
        }

        buttonAddToCartCollapsed?.setOnClickListener {
            originalProductViewListener?.onButtonAddToCartClicked()
        }
    }

    fun animateBasedOnScroll(dy: Int) {
        if (isVisible) {
            originalProductCardViewAnimator?.animateBasedOnScroll(dy)
        }
    }
}
