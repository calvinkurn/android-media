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
import com.tokopedia.productcard.utils.doIfVisible
import com.tokopedia.productcard.utils.isNotNullAndVisible
import com.tokopedia.productcard.utils.isNullOrNotVisible
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
        setImageTopAdsConstraint()
    }

    private fun setProductNameMarginTop() {
        textViewProductName?.doIfVisible { textViewProductName ->
            val marginTopDp = getTitleMarginTop()
            setViewMargins(textViewProductName.id, ConstraintSet.TOP, marginTopDp)
        }
    }

    private fun getTitleMarginTop(): Int {
        return if (textViewShopName.isNotNullAndVisible) R.dimen.dp_2
        else R.dimen.dp_8
    }

    private fun setPriceMarginTop() {
        textViewPrice?.doIfVisible { textViewPrice ->
            val marginTopDp = getPriceMarginTop()
            setViewMargins(textViewPrice.id, ConstraintSet.TOP, marginTopDp)
        }
    }

    private fun getPriceMarginTop(): Int {
        return if (labelDiscount.isNotNullAndVisible) R.dimen.dp_2
        else R.dimen.dp_4
    }

    private fun setLocationMarginLeft() {
        textViewShopLocation?.doIfVisible { textViewShopLocation ->
            val marginStartDp = getLocationMarginLeft()
            setViewMargins(textViewShopLocation.id, ConstraintSet.START, marginStartDp)
        }
    }

    private fun getLocationMarginLeft(): Int {
        return if (linearLayoutShopBadges.isNotNullAndVisible) R.dimen.dp_4
        else R.dimen.dp_8
    }

    private fun setReviewCountMarginLeft() {
        textViewReviewCount?.doIfVisible { textViewReviewCount ->
            val marginStartDp = getReviewCountMarginLeft()
            setViewMargins(textViewReviewCount.id, ConstraintSet.START, marginStartDp)
        }
    }

    private fun getReviewCountMarginLeft(): Int {
        return if(linearLayoutImageRating.isNotNullAndVisible) R.dimen.dp_4
        else R.dimen.dp_8
    }

    private fun setLabelOffersConstraint() {
        labelOffers?.doIfVisible { labelOffers ->
            val labelOffersTopConstraintView = getLabelOffersTopConstraintView()

            labelOffersTopConstraintView?.let {
                setViewConstraint(
                        labelOffers.id, ConstraintSet.TOP, it.id, ConstraintSet.BOTTOM, R.dimen.dp_4
                )
            }
        }
    }

    private fun getLabelOffersTopConstraintView(): View? {
        return when {
            labelCredibility.isNotNullAndVisible -> {
                labelCredibility
            }
            linearLayoutImageRating.isNotNullAndVisible -> {
                linearLayoutImageRating
            }
            textViewReviewCount.isNotNullAndVisible -> {
                textViewReviewCount
            }
            textViewShopLocation.isNotNullAndVisible -> {
                textViewShopLocation
            }
            else -> null
        }
    }

    private fun setImageTopAdsConstraint() {
        imageTopAds?.doIfVisible { imageTopAds ->
            textViewShopLocation?.doIfVisible { textViewShopLocation ->
                if(isTextLocationIsAtBottomOfCard()) {
                    setViewConstraint(imageTopAds.id, ConstraintSet.TOP, textViewShopLocation.id, ConstraintSet.TOP, R.dimen.dp_0)
                }
            }
        }
    }

    private fun isTextLocationIsAtBottomOfCard(): Boolean {
        return labelCredibility.isNullOrNotVisible
                && linearLayoutImageRating.isNullOrNotVisible
                && textViewReviewCount.isNullOrNotVisible
                && labelOffers.isNullOrNotVisible
    }

    fun setImageShopVisible(isVisible: Boolean) {
        imageShop?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setImageShopUrl(imageUrl: String) {
        imageShop?.let { imageShop ->
            ImageHandler.loadImageCircle2(context, imageShop, imageUrl)
        }
    }
}