package com.tokopedia.productcard.v2

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.constraint.ConstraintSet
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.productcard.R
import com.tokopedia.unifycomponents.Label

/**
 * ProductCardView with Small Grid layout.
 */
class ProductCardViewSmallGrid: ProductCardView {

    private var imageShop: ImageView? = null

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs)

    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int
    ): super(context, attrs, defStyleAttr)

    override fun getLayout(): Int {
        return R.layout.product_card_layout_v2_small_grid
    }

    override fun findViews(inflatedView: View) {
        super.findViews(inflatedView)

        imageShop = inflatedView.findViewById(R.id.imageShop)
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
        return if(isViewNotNullAndVisible(imageRating)) R.dimen.dp_4
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
    }

    fun setImageShopVisible(isVisible: Boolean) {
        imageShop?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setImageShopUrl(imageUrl: String) {
        imageShop?.let { imageShop ->
            ImageHandler.loadImageCircle2(context, imageShop, imageUrl)
        }
    }

    override fun setLabelPromoType(promoLabelType: String) {
        super.setLabelPromoType(promoLabelType)
        setLabelPromoCornerRadius()
    }

    private fun setLabelPromoCornerRadius() {
        val labelPromoBackground = labelPromo?.background?.mutate()

        if (labelPromoBackground != null
                && labelPromoBackground is GradientDrawable) {
            setLabelPromoBackgroundCornerRadii(labelPromoBackground)
        }
    }

    private fun setLabelPromoBackgroundCornerRadii(labelPromoBackground: GradientDrawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val cornerRadii =
                    labelPromoBackground.cornerRadii ?: floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)

            labelPromoBackground.cornerRadii =
                    floatArrayOf(0f, 0f, cornerRadii[2], cornerRadii[3], 0f, 0f, 0f, 0f)
        }
    }
}