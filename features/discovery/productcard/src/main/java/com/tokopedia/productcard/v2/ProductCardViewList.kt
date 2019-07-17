package com.tokopedia.productcard.v2

import android.content.Context
import android.support.constraint.ConstraintSet
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.tokopedia.productcard.R
import com.tokopedia.unifycomponents.Label

/**
 * ProductCardView with List layout.
 */
class ProductCardViewList: ProductCardView {

    private var cardViewImageProduct: CardView? = null

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs)

    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int
    ): super(context, attrs, defStyleAttr)

    override fun getLayout(): Int {
        return R.layout.product_card_layout_v2_list
    }

    override fun findViews(inflatedView: View) {
        super.findViews(inflatedView)

        cardViewImageProduct = inflatedView.findViewById(R.id.cardViewImageProduct)
    }

    override fun realignLayout() {
        setProductNameMarginTop()
        setPriceMarginTop()
        setLocationMarginLeft()
        setReviewCountMarginLeft()
        setLabelOffersConstraint()
        setImageTopAdsConstraint()
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
                    labelOffers.id, ConstraintSet.TOP, it.id, ConstraintSet.BOTTOM, R.dimen.dp_4
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

    private fun setImageTopAdsConstraint() {
        imageTopAds?.let { imageTopAds ->
            if (isViewNotNullAndVisible(imageTopAds)) {
                setImageTopAdsConstraintIfVisible(imageTopAds)
            }
        }
    }

    private fun setImageTopAdsConstraintIfVisible(imageTopAds: ImageView) {
        textViewShopLocation?.let { textViewShopLocation ->
            if(isViewNotNullAndVisible(textViewShopLocation) && isTextLocationIsAtBottomOfCard()) {
                setViewConstraint(imageTopAds.id, ConstraintSet.TOP, textViewShopLocation.id, ConstraintSet.TOP, R.dimen.dp_0)
            }
        }
    }

    private fun isTextLocationIsAtBottomOfCard(): Boolean {
        return !isViewNotNullAndVisible(labelCredibility)
                && !isViewNotNullAndVisible(linearLayoutImageRating)
                && !isViewNotNullAndVisible(textViewReviewCount)
                && !isViewNotNullAndVisible(labelOffers)
    }
}