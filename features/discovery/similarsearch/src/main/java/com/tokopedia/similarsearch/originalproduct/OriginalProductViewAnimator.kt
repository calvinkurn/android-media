package com.tokopedia.similarsearch.originalproduct

import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.similarsearch.R
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

internal class OriginalProductViewAnimator(
        private val originalProductView: View
) {
    companion object {
        private const val MAXIMUM_VERTICAL_SCROLL_DISTANCE = 250
        private const val IMAGE_PRODUCT_BASE_SIZE = 80f
        private const val IMAGE_PRODUCT_EXTRA_SIZE = 16f
        private const val VISIBILITY_THRESHOLD = 0.5f
        private const val ATC_BUTTONS_VERTICAL_SCROLL_DISTANCE = 75
        private const val ATC_BUTTONS_COLLAPSED_VERTICAL_SCROLL_START_VISIBLE = 70
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

    private val cardViewOriginalProductSimilarSearch: CardView? by lazy(LazyThreadSafetyMode.NONE) {
       originalProductView.findViewById(R.id.cardViewOriginalProductSimilarSearch)
    }
    private val buttonBuy: UnifyButton? by lazy(LazyThreadSafetyMode.NONE) {
        originalProductView.findViewById(R.id.buttonBuy)
    }
    private val buttonAddToCart: UnifyButton? by lazy(LazyThreadSafetyMode.NONE) {
        originalProductView.findViewById(R.id.buttonAddToCart)
    }
    private val buttonAddToCartCollapsed: UnifyButton? by lazy(LazyThreadSafetyMode.NONE) {
        originalProductView.findViewById(R.id.buttonAddToCartCollapsed)
    }
    private val textViewProductName: Typography? by lazy(LazyThreadSafetyMode.NONE) {
        originalProductView.findViewById(R.id.textViewProductName)
    }
    private val textViewPrice: Typography? by lazy(LazyThreadSafetyMode.NONE) {
        originalProductView.findViewById(R.id.textViewPrice)
    }
    private val textViewProductNameCollapsed: Typography? by lazy(LazyThreadSafetyMode.NONE) {
        originalProductView.findViewById(R.id.textViewProductNameCollapsed)
    }
    private val constraintLayoutOriginalProduct: ConstraintLayout? by lazy(LazyThreadSafetyMode.NONE) {
        originalProductView.findViewById(R.id.constraintLayoutOriginalProduct)
    }

    init {
        cardViewOriginalProductSimilarSearch?.post(this::initializeAfterOriginalProductViewReady)
    }

    private fun initializeAfterOriginalProductViewReady() {
        buttonBuyMarginTop = buttonBuy.getMarginTop()
        containerTravelDistance = getOriginalProductContainerTravelDistance()
        cardViewCollapsedHeight = getCardViewCollapsedHeight()
        constraintLayoutCollapsedHeight = getConstraintLayoutCollapsedHeight()
        textViewPriceMarginTop = textViewPrice.getMarginTop()
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
            (buttonBuy?.measuredHeight ?: 0) +
                    buttonBuyMarginTop +
                    ((constraintLayoutOriginalProduct?.paddingBottom ?: 0) * 2)

    private fun getCardViewCollapsedHeight() =
            (cardViewOriginalProductSimilarSearch?.measuredHeight ?: 0) -
                    containerTravelDistance

    private fun getConstraintLayoutCollapsedHeight() =
            (constraintLayoutOriginalProduct?.measuredHeight ?: 0) -
                    containerTravelDistance

    private fun getTextViewPriceTravelDistance() =
            (textViewProductName?.measuredHeight ?: 0) -
                    (textViewProductNameCollapsed?.measuredHeight ?: 0)

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

        cardViewOriginalProductSimilarSearch?.layoutParams?.height = cardViewHeight.toInt()
    }

    private fun resizeConstraintLayout() {
        val constraintLayoutHeight =
                constraintLayoutCollapsedHeight + (remainingVerticalScrollOffsetInPercent * containerTravelDistance)

        constraintLayoutOriginalProduct?.layoutParams?.height = constraintLayoutHeight.toInt()
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
            translateButtonAddToCartCollapsedPosition(constraintSet)
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

    private fun translateButtonAddToCartCollapsedPosition(constraintSet: ConstraintSet) {
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
        val addToCartCollapsedAlpha = getAddToCartCollapsedAlpha()
        constraintSet.setAlpha(R.id.buttonAddToCartCollapsed, addToCartCollapsedAlpha)

        val addToCartButtonsAlpha = getAddToCartButtonsAlpha()
        constraintSet.setAlpha(R.id.buttonBuy, addToCartButtonsAlpha)
        constraintSet.setAlpha(R.id.buttonAddToCart, addToCartButtonsAlpha)
    }

    private fun getAddToCartCollapsedAlpha() =
            (getCurrentVerticalOffsetATCCollapsed() / getMaximumVerticalScrollATCCollapsed())
                    .coerceAtMost(1f)

    private fun getCurrentVerticalOffsetATCCollapsed() =
            (currentVerticalScrollOffset - ATC_BUTTONS_COLLAPSED_VERTICAL_SCROLL_START_VISIBLE)
                    .toFloat()
                    .coerceAtLeast(0f)

    private fun getMaximumVerticalScrollATCCollapsed() =
            MAXIMUM_VERTICAL_SCROLL_DISTANCE - ATC_BUTTONS_COLLAPSED_VERTICAL_SCROLL_START_VISIBLE

    private fun getAddToCartButtonsAlpha() =
            1.0f - getCurrentVerticalScrollOffsetATCButtonsInPercent()

    private fun getCurrentVerticalScrollOffsetATCButtonsInPercent() =
            (currentVerticalScrollOffset.toFloat() / ATC_BUTTONS_VERTICAL_SCROLL_DISTANCE)
                    .coerceAtMost(1.0f)

    private fun setButtonsClickableBasedOnVisibility() {
        buttonBuy?.isClickable = buttonBuy.getAlphaOrZero() > VISIBILITY_THRESHOLD
        buttonAddToCart?.isClickable = buttonAddToCart.getAlphaOrZero() > VISIBILITY_THRESHOLD
        buttonAddToCartCollapsed?.isClickable = buttonAddToCartCollapsed.getAlphaOrZero() > VISIBILITY_THRESHOLD
    }

    private fun View?.getAlphaOrZero(): Float {
        return this?.alpha ?: 0f
    }

    private fun applyConstraintSet(apply: (constraintSet: ConstraintSet) -> Unit) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayoutOriginalProduct)
        apply(constraintSet)
        constraintSet.applyTo(constraintLayoutOriginalProduct)
    }

    private fun Float.toPx(): Int {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                this,
                originalProductView.resources.displayMetrics
        ).toInt()
    }
}