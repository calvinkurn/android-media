package com.tokopedia.similarsearch.originalproduct

import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.similarsearch.R
import kotlinx.android.synthetic.main.similar_search_fragment_layout.view.*
import kotlinx.android.synthetic.main.similar_search_original_product_layout.view.*

internal class OriginalProductViewAnimator(
        private val fragmentView: View
) {
    companion object {
        private const val MAXIMUM_VERTICAL_SCROLL_DISTANCE = 250
        private const val IMAGE_PRODUCT_BASE_SIZE = 80f
        private const val IMAGE_PRODUCT_EXTRA_SIZE = 16f
        private const val VISIBILITY_THRESHOLD = 0.5f
    }

    private var containerTravelDistance = 0
    private var cardViewCollapsedHeight = 0
    private var constraintLayoutCollapsedHeight = 0
    private var textViewPriceMarginTop = 0
    private var textViewPriceTravelDistance = 0
    private var buttonBuyMarginTop = 0
    private var isAnimatorInitialized = false

    private var currentVerticalScrollOffset = 0
    private var currentVerticalScrollOffsetInPercent = 0f
    private var remainingVerticalScrollOffsetInPercent = 0f

    init {
        fragmentView.cardViewOriginalProductSimilarSearch?.post(this::initializeAfterOriginalProductViewReady)
    }

    private fun initializeAfterOriginalProductViewReady() {
        buttonBuyMarginTop = fragmentView.buttonBuy.getMarginTop()
        containerTravelDistance = getOriginalProductContainerTravelDistance()
        cardViewCollapsedHeight = getCardViewCollapsedHeight()
        constraintLayoutCollapsedHeight = getConstraintLayoutCollapsedHeight()
        textViewPriceMarginTop = fragmentView.textViewPrice.getMarginTop()
        textViewPriceTravelDistance = getTextViewPriceTravelDistance()

        isAnimatorInitialized = true
    }

    private fun View?.getMarginTop(): Int {
        if (this == null) return 0

        return if (this.layoutParams is ViewGroup.MarginLayoutParams)
            (this.layoutParams as ViewGroup.MarginLayoutParams).topMargin
        else 0
    }

    private fun getOriginalProductContainerTravelDistance() =
            (fragmentView.buttonBuy?.measuredHeight ?: 0) +
                buttonBuyMarginTop +
                (fragmentView.constraintLayoutOriginalProduct?.paddingBottom ?: 0)

    private fun getCardViewCollapsedHeight() =
            (fragmentView.cardViewOriginalProductSimilarSearch?.measuredHeight ?: 0) -
                    containerTravelDistance

    private fun getConstraintLayoutCollapsedHeight() =
            (fragmentView.constraintLayoutOriginalProduct?.measuredHeight ?: 0) -
                    containerTravelDistance

    private fun getTextViewPriceTravelDistance() =
            (fragmentView.textViewProductName?.measuredHeight ?: 0) -
                    (fragmentView.textViewProductNameCollapsed?.measuredHeight ?: 0)

    fun animateBasedOnScroll(verticalScrollDistance: Int) {
        if (!isAnimatorInitialized) return

        initializeResizingVariables(verticalScrollDistance)

        resizeContainer()

        transitioningChildViews()

        setButtonsClickableBasedOnVisibility()
    }

    private fun initializeResizingVariables(verticalScrollDistance: Int) {
        currentVerticalScrollOffset += verticalScrollDistance
        currentVerticalScrollOffsetInPercent = getCurrentVerticalScrollOffsetInPercent()
        remainingVerticalScrollOffsetInPercent = 1.0f - currentVerticalScrollOffsetInPercent
    }

    private fun getCurrentVerticalScrollOffsetInPercent() =
        (currentVerticalScrollOffset.toFloat() / MAXIMUM_VERTICAL_SCROLL_DISTANCE)
                .coerceAtMost(1.0f)

    private fun resizeContainer() {
        resizeCardView()
        resizeConstraintLayout()
    }

    private fun resizeCardView() {
        val cardViewHeight =
                cardViewCollapsedHeight + (remainingVerticalScrollOffsetInPercent * containerTravelDistance)

        fragmentView.cardViewOriginalProductSimilarSearch?.layoutParams?.height = cardViewHeight.toInt()
    }

    private fun resizeConstraintLayout() {
        val constraintLayoutHeight =
                constraintLayoutCollapsedHeight + (remainingVerticalScrollOffsetInPercent * containerTravelDistance)

        fragmentView.constraintLayoutOriginalProduct?.layoutParams?.height = constraintLayoutHeight.toInt()
    }

    private fun transitioningChildViews() {
        applyConstraintSet { constraintSet ->
            if (isFullyExpanded()) {
                connectButtonBuyTopToImageProductBottom(constraintSet)
                connectTextViewPriceTopToTextViewProductNameBottom(constraintSet)
            }
            else {
                connectButtonBuyBottomToContainerBottom(constraintSet)
                translateTextPricePosition(constraintSet)
            }

            resizeImageProduct(constraintSet)
            transitioningTextViewProductNameCollapsedState(constraintSet)
            translateButtonAddToCartPosition(constraintSet)
            transitioningButtonsVisibility(constraintSet)
        }
    }

    private fun isFullyExpanded(): Boolean {
        return currentVerticalScrollOffset == 0
    }

    private fun connectButtonBuyTopToImageProductBottom(constraintSet: ConstraintSet) {
        constraintSet.clear(R.id.buttonBuy, ConstraintSet.BOTTOM)
        constraintSet.connect(
                R.id.buttonBuy, ConstraintSet.TOP,
                R.id.imageProduct, ConstraintSet.BOTTOM,
                buttonBuyMarginTop
        )
    }

    private fun connectTextViewPriceTopToTextViewProductNameBottom(constraintSet: ConstraintSet) {
        constraintSet.connect(
                R.id.textViewPrice, ConstraintSet.TOP,
                R.id.textViewProductName, ConstraintSet.BOTTOM,
                textViewPriceMarginTop
        )
    }

    private fun connectButtonBuyBottomToContainerBottom(constraintSet: ConstraintSet) {
        constraintSet.clear(R.id.buttonBuy, ConstraintSet.TOP)
        constraintSet.connect(
                R.id.buttonBuy, ConstraintSet.BOTTOM,
                R.id.constraintLayoutOriginalProduct, ConstraintSet.BOTTOM
        )
    }

    private fun translateTextPricePosition(constraintSet: ConstraintSet) {
        val textViewPriceMarginTop = getTextViewPriceMarginTopCollapsedState()
        constraintSet.connect(
                R.id.textViewPrice, ConstraintSet.TOP,
                R.id.textViewProductNameCollapsed, ConstraintSet.BOTTOM,
                textViewPriceMarginTop.toInt()
        )
    }

    private fun getTextViewPriceMarginTopCollapsedState() =
        (remainingVerticalScrollOffsetInPercent * textViewPriceTravelDistance) + textViewPriceMarginTop

    private fun resizeImageProduct(constraintSet: ConstraintSet) {
        val imageProductCollapsedSize = getImageProductCollapsedSize()

        constraintSet.constrainWidth(R.id.imageProduct, imageProductCollapsedSize)
        constraintSet.constrainHeight(R.id.imageProduct, imageProductCollapsedSize)
    }

    private fun getImageProductCollapsedSize() =
            IMAGE_PRODUCT_BASE_SIZE.toPx() + getImageProductExtraSize()

    private fun getImageProductExtraSize() =
            (IMAGE_PRODUCT_EXTRA_SIZE * remainingVerticalScrollOffsetInPercent).toPx()

    private fun transitioningTextViewProductNameCollapsedState(constraintSet: ConstraintSet) {
        constraintSet.setAlpha(R.id.textViewProductName, remainingVerticalScrollOffsetInPercent)
        constraintSet.setAlpha(R.id.textViewProductNameCollapsed, currentVerticalScrollOffsetInPercent)
    }

    private fun translateButtonAddToCartPosition(constraintSet: ConstraintSet) {
        val buttonAddToCartMarginBottom = getButtonAddToCartMarginBottomCollapsedState()
        constraintSet.connect(
                R.id.buttonAddToCartCollapsed, ConstraintSet.BOTTOM,
                R.id.constraintLayoutOriginalProduct, ConstraintSet.BOTTOM,
                buttonAddToCartMarginBottom.toInt()
        )
    }

    private fun getButtonAddToCartMarginBottomCollapsedState() =
        remainingVerticalScrollOffsetInPercent * containerTravelDistance

    private fun transitioningButtonsVisibility(constraintSet: ConstraintSet) {
        constraintSet.setAlpha(R.id.buttonAddToCartCollapsed, currentVerticalScrollOffsetInPercent)
        constraintSet.setAlpha(R.id.buttonBuy, remainingVerticalScrollOffsetInPercent)
        constraintSet.setAlpha(R.id.buttonAddToCart, remainingVerticalScrollOffsetInPercent)
    }

    private fun setButtonsClickableBasedOnVisibility() {
        fragmentView.buttonBuy?.isClickable = remainingVerticalScrollOffsetInPercent > VISIBILITY_THRESHOLD
        fragmentView.buttonAddToCart?.isClickable = remainingVerticalScrollOffsetInPercent > VISIBILITY_THRESHOLD
        fragmentView.buttonAddToCartCollapsed?.isClickable = currentVerticalScrollOffsetInPercent > VISIBILITY_THRESHOLD
    }

    private fun applyConstraintSet(apply: (constraintSet: ConstraintSet) -> Unit) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(fragmentView.constraintLayoutOriginalProduct)
        apply(constraintSet)
        constraintSet.applyTo(fragmentView.constraintLayoutOriginalProduct)
    }

    private fun Float.toPx(): Int {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                this,
                fragmentView.resources.displayMetrics
        ).toInt()
    }
}