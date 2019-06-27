package com.tokopedia.productcard

import android.content.Context
import android.support.annotation.LayoutRes
import android.util.AttributeSet

class ProductCardViewSearchSmallGrid : ProductCardViewSearch {

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    @LayoutRes
    override fun getLayout(): Int {
        return R.layout.product_card_layout_search_small_grid
    }

    override fun realignLayout() {
        setTitleMarginTop()
        setPriceMarginTop()
        setLocationMarginLeft()
        setReviewCountMarginLeft()
        setOffersLabelConstraint()
    }

    private fun setTitleMarginTop() {
        val marginTopPixel =
            if (isViewVisible(textShopName))
                getDimensionPixelSize(R.dimen.dp_2)
            else
                getDimensionPixelSize(R.dimen.dp_8)

        setMarginsToView(textName, -1, marginTopPixel, -1, -1)
    }

    private fun setPriceMarginTop() {
        val marginTopPixel =
            if (isViewVisible(textShopName))
                getDimensionPixelSize(R.dimen.dp_2)
            else
                getDimensionPixelSize(R.dimen.dp_4)

        setMarginsToView(textPrice, -1, marginTopPixel, -1, -1)
    }

    private fun setLocationMarginLeft() {
        val marginLeftPixel =
            if (isViewVisible(shopBadgesContainer))
                getDimensionPixelSize(R.dimen.dp_4)
            else
                getDimensionPixelSize(R.dimen.dp_8)

        setMarginsToView(textLocation, marginLeftPixel, -1, -1, -1)
    }

    private fun setReviewCountMarginLeft() {
        val marginLeftPixel =
            if (isViewVisible(reviewCountView))
                getDimensionPixelSize(R.dimen.dp_4)
            else
                getDimensionPixelSize(R.dimen.dp_8)

        setMarginsToView(reviewCountView, marginLeftPixel, -1, -1, -1)
    }

    private fun setOffersLabelConstraint() {
        when {
            isViewVisible(credibilityLabel) -> {
                setViewConstraintTopToBottomOf(offersLabel.id, credibilityLabel.id, R.dimen.dp_4)
            }
            isViewVisible(ratingView) -> {
                setViewConstraintTopToBottomOf(offersLabel.id, ratingView.id, R.dimen.dp_4)
            }
            isViewVisible(reviewCountView) -> {
                setViewConstraintTopToBottomOf(offersLabel.id, reviewCountView.id, R.dimen.dp_4)
            }
            else -> {
                setViewConstraintTopToBottomOf(offersLabel.id, textLocation.id, R.dimen.dp_4)
            }
        }
    }
}