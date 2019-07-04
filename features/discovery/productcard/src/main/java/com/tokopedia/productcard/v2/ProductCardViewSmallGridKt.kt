package com.tokopedia.productcard.v2

import android.content.Context
import android.support.constraint.ConstraintSet
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.productcard.R
import com.tokopedia.unifycomponents.Label

class ProductCardViewSmallGridKt: ProductCardViewKt {

    private var imageShop: ImageView? = null

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    override fun getLayout(): Int {
        return R.layout.product_card_layout_v2_small_grid
    }

    override fun findViews(inflatedView: View) {
        super.findViews(inflatedView)

        imageShop = inflatedView.findViewById(R.id.imageShop)
    }

    override fun realignLayout() {
        setTitleMarginTop()
        setPriceMarginTop()
        setLocationMarginLeft()
        setReviewCountMarginLeft()
        setLabelOffersConstraint()
    }

    private fun setTitleMarginTop() {
        textViewProductName?.let { textViewProductName ->
            if (isViewNotNullAndVisible(textViewProductName)) {

                val marginTopDp = textViewShopName?.let { textViewShopName ->
                    if(isViewNotNullAndVisible(textViewShopName)) R.dimen.dp_2 else R.dimen.dp_8
                } ?: R.dimen.dp_8

                setViewMarginsInConstraintLayoutProductCard(
                    textViewProductName.id,
                    ConstraintSet.TOP,
                    marginTopDp
                )
            }
        }
    }

    private fun setPriceMarginTop() {
        textViewPrice?.let { textViewPrice ->
            if (isViewNotNullAndVisible(textViewPrice)) {
                val marginTopDp = labelDiscount?.let { labelDiscount ->
                    if (isViewNotNullAndVisible(labelDiscount)) R.dimen.dp_2 else R.dimen.dp_4
                } ?: R.dimen.dp_4

                setViewMarginsInConstraintLayoutProductCard(
                    textViewPrice.id,
                    ConstraintSet.TOP,
                    marginTopDp
                )
            }
        }
    }

    private fun setLocationMarginLeft() {
        textViewShopLocation?.let { textViewShopLocation ->
            if (isViewNotNullAndVisible(textViewShopLocation)) {
                val marginStartDp = linearLayoutShopBadges?.let { linearLayoutShopBadges ->
                    if (isViewNotNullAndVisible(linearLayoutShopBadges)) R.dimen.dp_4 else R.dimen.dp_8
                } ?: R.dimen.dp_8

                setViewMarginsInConstraintLayoutProductCard(
                    textViewShopLocation.id,
                    ConstraintSet.START,
                    marginStartDp
                )
            }
        }
    }

    private fun setReviewCountMarginLeft() {
        textViewReviewCount?.let { textViewReviewCount ->
            if (isViewNotNullAndVisible(textViewReviewCount)) {
                val marginStartDp = imageRating?.let { imageRating ->
                    if (isViewNotNullAndVisible(imageRating)) R.dimen.dp_4 else R.dimen.dp_8
                } ?: R.dimen.dp_8

                setViewMarginsInConstraintLayoutProductCard(
                    textViewReviewCount.id,
                    ConstraintSet.START,
                    marginStartDp
                )
            }
        }
    }

    private fun setLabelOffersConstraint() {
        labelOffers?.let { labelOffers ->
            if (isViewNotNullAndVisible(labelOffers)) {
                setLabelOffersConstraintIfVisible(labelOffers)
            }
        }
    }

    private fun setLabelOffersConstraintIfVisible(labelOffers: Label) {
        val labelOffersTopConstraintView = when {
            isViewNotNullAndVisible(labelCredibility) -> {
                labelCredibility
            }
            isViewNotNullAndVisible(imageRating) -> {
                imageRating
            }
            isViewNotNullAndVisible(textViewReviewCount) -> {
                textViewReviewCount
            }
            isViewNotNullAndVisible(textViewShopLocation) -> {
                textViewShopLocation
            }
            else -> null
        }

        labelOffersTopConstraintView?.let {
            setViewConstraintInConstraintLayoutProductCard(
                labelOffers.id,
                ConstraintSet.TOP,
                it.id,
                ConstraintSet.BOTTOM,
                R.dimen.dp_8
            )
        }
    }

    fun setImageShopVisible(isVisible: Boolean) {
        imageShop?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setImageShopUrl(imageUrl: String) {
        imageShop?.let {
            ImageHandler.loadImageCircle2(context, it, imageUrl)
        }
    }
}