package com.tokopedia.productcard.v2

import android.content.Context
import android.graphics.Paint
import android.support.annotation.DimenRes
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.design.image.SquareImageView
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.R
import com.tokopedia.productcard.utils.*
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

/**
 * This abstract class provides a basis for Custom View Product Card.
 * All the view components for Product Card are initialized and configured here.
 *
 * How to use:
 * 1. Choose one of ProductCardView subclasses and put it in the layout xml.
 * 2. Configure the components of the ProductCardView from the provided public methods,
 *    e.g. setImageProductUrl, setProductNameVisible, setProductNameText, etc.
 * 3. Call method realignLayout() after configuring the required ProductCardView components.
 */
abstract class ProductCardView: BaseCustomView {

    companion object {
        protected const val LIGHT_GREY = "lightGrey"
        protected const val LIGHT_BLUE = "lightBlue"
        protected const val LIGHT_GREEN = "lightGreen"
        protected const val LIGHT_RED = "lightRed"
        protected const val LIGHT_ORANGE = "lightOrange"
        protected const val DARK_GREY = "darkGrey"
        protected const val DARK_BLUE = "darkBlue"
        protected const val DARK_GREEN = "darkGreen"
        protected const val DARK_RED = "darkRed"
        protected const val DARK_ORANGE = "darkOrange"
    }

    /**
     * View components of a ProductCardView
     */
    protected var cardViewProductCard: CardView? = null
    protected var constraintLayoutProductCard: ConstraintLayout? = null
    protected var imageProduct: SquareImageView? = null
    protected var buttonWishlist: ImageView? = null
    protected var labelPromo: Label? = null
    protected var textViewShopName: Typography? = null
    protected var textViewProductName: Typography? = null
    protected var labelDiscount: Label? = null
    protected var textViewSlashedPrice: Typography? = null
    protected var textViewPrice: Typography? = null
    protected var linearLayoutShopBadges: LinearLayout? = null
    protected var textViewShopLocation: Typography? = null
    protected var linearLayoutImageRating: LinearLayout? = null
    protected var imageViewRating1: ImageView? = null
    protected var imageViewRating2: ImageView? = null
    protected var imageViewRating3: ImageView? = null
    protected var imageViewRating4: ImageView? = null
    protected var imageViewRating5: ImageView? = null
    protected var textViewReviewCount: Typography? = null
    protected var labelCredibility: Label? = null
    protected var labelOffers: Label? = null
    protected var imageTopAds: ImageView? = null

    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        init()
    }

    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        val inflatedView = View.inflate(context, getLayout(), this)

        findViews(inflatedView)

        postInit()
    }

    /**
     * Provides layout resource to be inflated in init() method.
     * Layouts could be different depending on its subclasses,
     * but all of them should contain all the view components required from a ProductCardView
     *
     */
    @LayoutRes
    protected abstract fun getLayout(): Int

    /**
     * Find view components from the getLayout() method.
     *
     * @param inflatedView View inflated from getLayout()
     */
    protected open fun findViews(inflatedView: View) {
        cardViewProductCard = inflatedView.findViewById(R.id.cardViewProductCard)
        constraintLayoutProductCard = inflatedView.findViewById(R.id.constraintLayoutProductCard)
        imageProduct = inflatedView.findViewById(R.id.imageProduct)
        buttonWishlist = inflatedView.findViewById(R.id.buttonWishlist)
        labelPromo = inflatedView.findViewById(R.id.labelPromo)
        textViewShopName = inflatedView.findViewById(R.id.textViewShopName)
        textViewProductName = inflatedView.findViewById(R.id.textViewProductName)
        labelDiscount = inflatedView.findViewById(R.id.labelDiscount)
        textViewSlashedPrice = inflatedView.findViewById(R.id.textViewSlashedPrice)
        textViewPrice = inflatedView.findViewById(R.id.textViewPrice)
        linearLayoutShopBadges = inflatedView.findViewById(R.id.linearLayoutShopBadges)
        textViewShopLocation = inflatedView.findViewById(R.id.textViewShopLocation)
        linearLayoutImageRating = inflatedView.findViewById(R.id.linearLayoutImageRating)
        imageViewRating1 = inflatedView.findViewById(R.id.imageViewRating1)
        imageViewRating2 = inflatedView.findViewById(R.id.imageViewRating2)
        imageViewRating3 = inflatedView.findViewById(R.id.imageViewRating3)
        imageViewRating4 = inflatedView.findViewById(R.id.imageViewRating4)
        imageViewRating5 = inflatedView.findViewById(R.id.imageViewRating5)
        textViewReviewCount = inflatedView.findViewById(R.id.textViewReviewCount)
        labelCredibility = inflatedView.findViewById(R.id.labelCredibility)
        labelOffers = inflatedView.findViewById(R.id.labelOffers)
        imageTopAds = inflatedView.findViewById(R.id.imageTopAds)
    }

    /**
     * "Force" configuration of some view components,
     * in case it cannot be configured from the layout xml.
     */
    protected open fun postInit() {
        textViewProductName?.setLineSpacing(0f, 1f)
    }

    /**
     * Realign the view components based on their visibility.
     * Make sure all the view components are configured before calling this method.
     */
    open fun realignLayout() {
        setProductNameMarginTop()
        setPriceMarginTop()
        setLocationMarginLeft()
        setReviewCountMarginLeft()
        setLabelOffersConstraint()
        setImageTopAdsConstraint()
    }

    protected open fun setProductNameMarginTop() {
        textViewProductName?.doIfVisible { textViewProductName ->
            val marginTopDp = getTitleMarginTop()
            setViewMargins(textViewProductName.id, ConstraintSet.TOP, marginTopDp)
        }
    }

    protected open fun getTitleMarginTop(): Int {
        return if (textViewShopName.isNotNullAndVisible) R.dimen.dp_2
        else R.dimen.dp_8
    }

    protected open fun setPriceMarginTop() {
        textViewPrice?.doIfVisible { textViewPrice ->
            val marginTopDp = getPriceMarginTop()
            setViewMargins(textViewPrice.id, ConstraintSet.TOP, marginTopDp)
        }
    }

    protected open fun getPriceMarginTop(): Int {
        return if (labelDiscount.isNotNullAndVisible) R.dimen.dp_2
        else R.dimen.dp_4
    }

    protected open fun setLocationMarginLeft() {
        textViewShopLocation?.doIfVisible { textViewShopLocation ->
            val marginStartDp = getLocationMarginLeft()
            setViewMargins(textViewShopLocation.id, ConstraintSet.START, marginStartDp)
        }
    }

    protected open fun getLocationMarginLeft(): Int {
        return if (linearLayoutShopBadges.isNotNullAndVisible) R.dimen.dp_4
        else R.dimen.dp_8
    }

    protected open fun setReviewCountMarginLeft() {
        textViewReviewCount?.doIfVisible { textViewReviewCount ->
            val marginStartDp = getReviewCountMarginLeft()
            setViewMargins(textViewReviewCount.id, ConstraintSet.START, marginStartDp)
        }
    }

    protected open fun getReviewCountMarginLeft(): Int {
        return if(linearLayoutImageRating.isNotNullAndVisible) R.dimen.dp_4
        else R.dimen.dp_8
    }

    protected open fun setLabelOffersConstraint() {
        labelOffers?.doIfVisible { labelOffers ->
            val labelOffersTopConstraintView = getLabelOffersTopConstraintView()

            labelOffersTopConstraintView?.let {
                setViewConstraint(
                        labelOffers.id, ConstraintSet.TOP, it.id, ConstraintSet.BOTTOM, R.dimen.dp_4
                )
            }
        }
    }

    protected open fun getLabelOffersTopConstraintView(): View? {
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

    protected open fun setImageTopAdsConstraint() {
        imageTopAds?.doIfVisible { imageTopAds ->
            textViewShopLocation?.doIfVisible { textViewShopLocation ->
                configureImageTopAdsConstraintBasedOnTextLocation(imageTopAds, textViewShopLocation)
            }
        }
    }

    protected open fun configureImageTopAdsConstraintBasedOnTextLocation(imageTopAds: View, textViewShopLocation: View) {
        if(isTextLocationIsAtBottomOfCard()) {
            setViewConstraint(imageTopAds.id, ConstraintSet.TOP, textViewShopLocation.id, ConstraintSet.TOP, R.dimen.dp_0)
            setViewConstraint(textViewShopLocation.id, ConstraintSet.END, imageTopAds.id, ConstraintSet.START, R.dimen.dp_4)
        }
        else {
            clearViewConstraint(imageTopAds.id, ConstraintSet.TOP)

            imageProduct?.doIfVisible { constraintLayoutProductCard ->
                setViewConstraint(textViewShopLocation.id, ConstraintSet.END, constraintLayoutProductCard.id, ConstraintSet.END, R.dimen.dp_8)
            }
        }
    }

    protected open fun isTextLocationIsAtBottomOfCard(): Boolean {
        return labelCredibility.isNullOrNotVisible
                && linearLayoutImageRating.isNullOrNotVisible
                && textViewReviewCount.isNullOrNotVisible
                && labelOffers.isNullOrNotVisible
    }

    open fun getCardViewRadius(): Float {
        return cardViewProductCard?.radius ?: 0f
    }

    open fun getCardViewMaxElevation(): Float {
        return cardViewProductCard?.maxCardElevation ?: 0f
    }

    open fun setImageProductVisible(isVisible: Boolean) {
        imageProduct?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    open fun setImageProductUrl(imageUrl: String) {
        imageProduct?.let {
            ImageHandler.loadImageThumbs(context, it, imageUrl)
        }
    }

    open fun setImageProductViewHintListener(holder: ImpressHolder, viewHintListener: ViewHintListener) {
        imageProduct?.addOnImpressionListener(holder, viewHintListener)
    }

    open fun setButtonWishlistVisible(isVisible: Boolean) {
        buttonWishlist?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    open fun setButtonWishlistImage(isWishlisted: Boolean) {
        if (isWishlisted) {
            buttonWishlist?.setImageResource(R.drawable.product_card_ic_wishlist_red)
        } else {
            buttonWishlist?.setImageResource(R.drawable.product_card_ic_wishlist)
        }
    }

    open fun setButtonWishlistOnClickListener(onClickListener: (view: View) -> Unit) {
        buttonWishlist?.setOnClickListener(onClickListener)
    }

    open fun setLabelPromoVisible(isVisible: Boolean) {
        labelPromo?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    open fun setLabelPromoText(promoLabelText: String) {
        labelPromo?.text = MethodChecker.fromHtml(promoLabelText)
    }

    open fun setLabelPromoType(promoLabelType: String) {
        labelPromo?.setLabelType(getLabelTypeFromString(promoLabelType))
    }

    protected open fun getLabelTypeFromString(labelType: String): Int {
        return when (labelType) {
            LIGHT_GREY -> Label.GENERAL_LIGHT_GREY
            LIGHT_BLUE -> Label.GENERAL_LIGHT_BLUE
            LIGHT_GREEN -> Label.GENERAL_LIGHT_GREEN
            LIGHT_RED -> Label.GENERAL_LIGHT_RED
            LIGHT_ORANGE -> Label.GENERAL_LIGHT_ORANGE
            DARK_GREY -> Label.GENERAL_DARK_GREY
            DARK_BLUE -> Label.GENERAL_DARK_BLUE
            DARK_GREEN -> Label.GENERAL_DARK_GREEN
            DARK_RED -> Label.GENERAL_DARK_RED
            DARK_ORANGE -> Label.GENERAL_DARK_ORANGE
            else -> Label.GENERAL_LIGHT_GREY
        }
    }

    open fun setShopNameVisible(isVisible: Boolean) {
        textViewShopName?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    open fun setShopNameText(shopName: String) {
        textViewShopName?.text = MethodChecker.fromHtml(shopName)
    }

    open fun setProductNameVisible(isVisible: Boolean) {
        textViewProductName?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    open fun setProductNameText(title: String) {
        textViewProductName?.text = MethodChecker.fromHtml(title)
    }

    open fun setLabelDiscountVisible(isVisible: Boolean) {
        labelDiscount?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    open fun setSlashedPriceInvisible(isInvisible: Boolean) {
        textViewSlashedPrice?.visibility = if (isInvisible) View.INVISIBLE else View.VISIBLE
    }

    open fun setLabelDiscountText(discount: Int) {
        val discountText = Integer.toString(discount) + "%"
        labelDiscount?.text = discountText
    }

    open fun setSlashedPriceVisible(isVisible: Boolean) {
        textViewSlashedPrice?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    open fun setSlashedPriceText(slashedPrice: String) {
        textViewSlashedPrice?.let {
            it.text = slashedPrice
            it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    open fun setPriceVisible(isVisible: Boolean) {
        textViewPrice?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    open fun setPriceText(price: String) {
        textViewPrice?.text = price
    }

    open fun setShopBadgesVisible(isVisible: Boolean) {
        linearLayoutShopBadges?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    open fun removeAllShopBadges() {
        linearLayoutShopBadges?.removeAllViews()
    }

    open fun addShopBadge(view: View) {
        linearLayoutShopBadges?.addView(view)
    }

    open fun setShopLocationVisible(isVisible: Boolean) {
        textViewShopLocation?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    open fun setShopLocationText(location: String) {
        textViewShopLocation?.text = MethodChecker.fromHtml(location)
    }

    open fun setImageRatingVisible(isVisible: Boolean) {
        linearLayoutImageRating?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    open fun setImageRatingInvisible(isInvisible: Boolean) {
        linearLayoutImageRating?.visibility = if (isInvisible) View.INVISIBLE else View.VISIBLE
    }

    open fun setRating(rating: Int) {
        imageViewRating1?.setImageResource(getRatingDrawable(rating >= 1))
        imageViewRating2?.setImageResource(getRatingDrawable(rating >= 2))
        imageViewRating3?.setImageResource(getRatingDrawable(rating >= 3))
        imageViewRating4?.setImageResource(getRatingDrawable(rating >= 4))
        imageViewRating5?.setImageResource(getRatingDrawable(rating >= 5))
    }

    @DrawableRes
    protected open fun getRatingDrawable(isActive: Boolean): Int {
        return if(isActive) R.drawable.product_card_ic_rating_active
        else R.drawable.product_card_ic_rating_default
    }

    open fun setReviewCountVisible(isVisible: Boolean) {
        textViewReviewCount?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    open fun setReviewCountInvisible(isInvisible: Boolean) {
        textViewReviewCount?.visibility = if (isInvisible) View.INVISIBLE else View.VISIBLE
    }

    open fun setLinesProductTitle(lines: Int){
        textViewProductName?.setLines(lines)
    }

    open fun setReviewCount(reviewCount: Int) {
        textViewReviewCount?.text = getReviewCountFormattedAsText(reviewCount)
    }

    open fun getReviewCountFormattedAsText(reviewCount: Int): String {
        return "($reviewCount)"
    }

    open fun setLabelCredibilityVisible(isVisible: Boolean) {
        labelCredibility?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    open fun setLabelCredibilityText(credibilityLabelText: String) {
        labelCredibility?.text = MethodChecker.fromHtml(credibilityLabelText)
    }

    open fun setLabelCredibilityType(credibilityLabelType: String) {
        labelCredibility?.setLabelType(getLabelTypeFromString(credibilityLabelType))
    }

    open fun setLabelOffersVisible(isVisible: Boolean) {
        labelOffers?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    open fun setLabelOffersText(offersLabelText: String) {
        labelOffers?.text = MethodChecker.fromHtml(offersLabelText)
    }

    open fun setLabelOffersType(offersLabelType: String) {
        labelOffers?.setLabelType(getLabelTypeFromString(offersLabelType))
    }

    open fun setImageTopAdsVisible(isVisible: Boolean) {
        imageTopAds?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    open fun setLabelDiscountInvisible(isInvisible: Boolean){
        labelDiscount?.visibility = if (isInvisible) View.INVISIBLE else View.VISIBLE
    }

    protected open fun setViewMargins(@IdRes viewId: Int, anchor: Int, marginDp: Int) {
        constraintLayoutProductCard.applyConstraintSet { constraintSet ->
            val marginPixel = getDimensionPixelSize(marginDp)
            constraintSet.setMargin(viewId, anchor, marginPixel)
        }
    }

    protected open fun setViewConstraint(
        @IdRes startLayoutId: Int,
        startSide: Int,
        @IdRes endLayoutId: Int,
        endSide: Int,
        @DimenRes marginDp: Int
    ) {
        constraintLayoutProductCard.applyConstraintSet { constraintSet ->
            val marginPixel = getDimensionPixelSize(marginDp)
            constraintSet.connect(startLayoutId, startSide, endLayoutId, endSide, marginPixel)
        }
    }

    protected open fun clearViewConstraint(@IdRes layoutId: Int, side: Int) {
        constraintLayoutProductCard.applyConstraintSet { constraintSet ->
            constraintSet.clear(layoutId, side)
        }
    }
}