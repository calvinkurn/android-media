package com.tokopedia.productcard.v2

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.constraint.ConstraintSet
import android.util.AttributeSet
import android.view.View
import com.tokopedia.productcard.R
import com.tokopedia.unifycomponents.Label

/**
 * ProductCardView with Big Grid layout.
 */
class ProductCardViewBigGrid: ProductCardView {

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs)

    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int
    ): super(context, attrs, defStyleAttr)

    override fun getLayout(): Int {
        return R.layout.product_card_layout_v2_big_grid
    }

    override fun realignLayout() {
        setProductNameMarginTop()
        setPriceMarginTop()
        setLocationMarginLeft()
        setReviewCountMarginLeft()
        setLabelOffersConstraint()
    }

    private fun setProductNameMarginTop() {
        textViewProductName?.let { textViewProductName ->
            if (isViewNotNullAndVisible(textViewProductName)) {
                val marginTopDp = getTitleMarginTop()
                setViewMargins(textViewProductName.id, ConstraintSet.TOP, marginTopDp)
            }
        }
    }

    private fun getTitleMarginTop(): Int {
        return if (isViewNotNullAndVisible(textViewShopName)) R.dimen.dp_2
        else R.dimen.dp_8
    }

    private fun setPriceMarginTop() {
        textViewPrice?.let { textViewPrice ->
            if (isViewNotNullAndVisible(textViewPrice)) {
                val marginTopDp = getPriceMarginTop()
                setViewMargins(textViewPrice.id, ConstraintSet.TOP, marginTopDp)
            }
        }
    }

    private fun getPriceMarginTop(): Int {
        return if (isViewNotNullAndVisible(labelDiscount)) R.dimen.dp_2
        else R.dimen.dp_4
    }

    private fun setLocationMarginLeft() {
        textViewShopLocation?.let { textViewShopLocation ->
            if (isViewNotNullAndVisible(textViewShopLocation)) {
                val marginStartDp = getLocationMarginLeft()
                setViewMargins(textViewShopLocation.id, ConstraintSet.START, marginStartDp)
            }
        }
    }

    private fun getLocationMarginLeft(): Int {
        return if (isViewNotNullAndVisible(linearLayoutShopBadges)) R.dimen.dp_4
        else R.dimen.dp_8
    }

    private fun setReviewCountMarginLeft() {
        textViewReviewCount?.let { textViewReviewCount ->
            if (isViewNotNullAndVisible(textViewReviewCount)) {
                val marginStartDp = getReviewCountMarginLeft()
                setViewMargins(textViewReviewCount.id, ConstraintSet.START, marginStartDp)
            }
        }
    }

    private fun getReviewCountMarginLeft(): Int {
        return if(isViewNotNullAndVisible(linearLayoutImageRating)) R.dimen.dp_4
        else R.dimen.dp_8
    }

    private fun setLabelOffersConstraint() {
        labelOffers?.let { labelOffers ->
            if (isViewNotNullAndVisible(labelOffers)) {
                setLabelOffersConstraintIfVisible(labelOffers)
            }
        }
    }

    private fun setLabelOffersConstraintIfVisible(labelOffers: Label) {
        val labelOffersTopConstraintView = getLabelOffersTopConstraintView()

        labelOffersTopConstraintView?.let {
            setViewConstraint(
                    labelOffers.id, ConstraintSet.TOP, it.id, ConstraintSet.BOTTOM, R.dimen.dp_8
            )
        }
    }

    private fun getLabelOffersTopConstraintView(): View? {
        return when {
            isViewNotNullAndVisible(labelCredibility) -> {
                labelCredibility
            }
            isViewNotNullAndVisible(linearLayoutImageRating) -> {
                linearLayoutImageRating
            }
            isViewNotNullAndVisible(textViewReviewCount) -> {
                textViewReviewCount
            }
            isViewNotNullAndVisible(textViewShopLocation) -> {
                textViewShopLocation
            }
            else -> null
        }
    }
}