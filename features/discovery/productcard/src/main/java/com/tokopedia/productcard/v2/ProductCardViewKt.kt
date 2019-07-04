package com.tokopedia.productcard.v2

import android.content.Context
import android.graphics.Paint
import android.support.annotation.DimenRes
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.productcard.R
import com.tokopedia.topads.sdk.domain.model.ImpressHolder
import com.tokopedia.topads.sdk.view.ImpressedImageView
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

abstract class ProductCardViewKt: BaseCustomView {

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

    protected var constraintLayoutProductCard: ConstraintLayout? = null
    protected var imageProduct: ImpressedImageView? = null
    protected var buttonWishlist: ImageView? = null
    protected var labelPromo: Label? = null
    protected var textViewShopName: Typography? = null
    protected var textViewProductName: Typography? = null
    protected var labelDiscount: Label? = null
    protected var textViewSlashedPrice: Typography? = null
    protected var textViewPrice: Typography? = null
    protected var linearLayoutShopBadges: LinearLayout? = null
    protected var textViewShopLocation: Typography? = null
    protected var imageRating: ImageView? = null
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

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        val inflatedView = View.inflate(context, getLayout(), this)

        findViews(inflatedView)

        postInit()
    }

    @LayoutRes
    protected abstract fun getLayout(): Int

    protected open fun findViews(inflatedView: View) {
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
        imageRating = inflatedView.findViewById(R.id.imageRating)
        textViewReviewCount = inflatedView.findViewById(R.id.textViewReviewCount)
        labelCredibility = inflatedView.findViewById(R.id.labelCredibility)
        labelOffers = inflatedView.findViewById(R.id.labelOffers)
        imageTopAds = inflatedView.findViewById(R.id.imageTopAds)
    }

    protected open fun postInit() {
        textViewProductName?.setLineSpacing(0f, 1f)
    }

    abstract fun realignLayout()

    fun setImageProductVisible(isVisible: Boolean) {
        imageProduct?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setImageProductUrl(imageUrl: String) {
        imageProduct?.let {
            ImageHandler.loadImageThumbs(context, it, imageUrl)
        }
    }

    fun setImageProductViewHintListener(holder: ImpressHolder, listener: ImpressedImageView.ViewHintListener) {
        imageProduct?.setViewHintListener(holder, listener)
    }

    fun setButtonWishlistVisible(isVisible: Boolean) {
        buttonWishlist?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setButtonWishlistImage(isWishlisted: Boolean) {
        if (isWishlisted) {
            buttonWishlist?.setImageResource(R.drawable.product_card_ic_wishlist_red)
        } else {
            buttonWishlist?.setImageResource(R.drawable.product_card_ic_wishlist)
        }
    }

    fun setButtonWishlistOnClickListener(onClickListener: View.OnClickListener) {
        buttonWishlist?.setOnClickListener(onClickListener)
    }

    fun setLabelPromoVisible(isVisible: Boolean) {
        labelPromo?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setLabelPromoText(promoLabelText: String) {
        labelPromo?.text = MethodChecker.fromHtml(promoLabelText)
    }

    fun setLabelPromoType(promoLabelType: String) {
        labelPromo?.setLabelType(getLabelTypeFromString(promoLabelType))
    }

    protected fun getLabelTypeFromString(labelType: String): Int {
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

    fun setShopNameVisible(isVisible: Boolean) {
        textViewShopName?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setShopNameText(shopName: String) {
        textViewShopName?.text = MethodChecker.fromHtml(shopName)
    }

    fun setProductNameVisible(isVisible: Boolean) {
        textViewProductName?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setProductNameText(title: String) {
        textViewProductName?.text = MethodChecker.fromHtml(title)
    }

    fun setLabelDiscountVisible(isVisible: Boolean) {
        labelDiscount?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setLabelDiscountText(discount: Int) {
        val discountText = Integer.toString(discount) + "%"
        labelDiscount?.text = discountText
    }

    fun setSlashedPriceVisible(isVisible: Boolean) {
        textViewSlashedPrice?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setSlashedPriceText(slashedPrice: String) {
        textViewSlashedPrice?.let {
            it.text = slashedPrice
            it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    fun setPriceVisible(isVisible: Boolean) {
        textViewPrice?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setPriceText(price: String) {
        textViewPrice?.text = price
    }

    fun setShopBadgesVisible(isVisible: Boolean) {
        linearLayoutShopBadges?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun removeAllShopBadges() {
        linearLayoutShopBadges?.removeAllViews()
    }

    fun addShopBadge(view: View) {
        linearLayoutShopBadges?.addView(view)
    }

    fun setShopLocationVisible(isVisible: Boolean) {
        textViewShopLocation?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setShopLocationText(location: String) {
        textViewShopLocation?.text = MethodChecker.fromHtml(location)
    }

    fun setImageRatingVisible(isVisible: Boolean) {
        imageRating?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setRating(rating: Int) {
        imageRating?.setImageResource(getRatingDrawable(rating))
    }

    protected fun getRatingDrawable(param: Int): Int {
        return when (param) {
            0 -> R.drawable.ic_star_none
            1 -> R.drawable.ic_star_one
            2 -> R.drawable.ic_star_two
            3 -> R.drawable.ic_star_three
            4 -> R.drawable.ic_star_four
            5 -> R.drawable.ic_star_five
            else -> R.drawable.ic_star_none
        }
    }

    fun setReviewCountVisible(isVisible: Boolean) {
        textViewReviewCount?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setReviewCount(reviewCount: Int) {
        textViewReviewCount?.text = getReviewCountFormattedAsText(reviewCount)
    }

    fun getReviewCountFormattedAsText(reviewCount: Int): String {
        return "($reviewCount)"
    }

    fun setLabelCredibilityVisible(isVisible: Boolean) {
        labelCredibility?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setLabelCredibilityText(credibilityLabelText: String) {
        labelCredibility?.text = MethodChecker.fromHtml(credibilityLabelText)
    }

    fun setLabelCredibilityType(credibilityLabelType: String) {
        labelCredibility?.setLabelType(getLabelTypeFromString(credibilityLabelType))
    }

    fun setLabelOffersVisible(isVisible: Boolean) {
        labelOffers?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setLabelOffersText(offersLabelText: String) {
        labelOffers?.text = MethodChecker.fromHtml(offersLabelText)
    }

    fun setLabelOffersType(offersLabelType: String) {
        labelOffers?.setLabelType(getLabelTypeFromString(offersLabelType))
    }

    fun setImageTopAdsVisible(isVisible: Boolean) {
        imageTopAds?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    protected fun setViewMarginsInConstraintLayoutProductCard(
        @IdRes viewId: Int,
        anchor: Int,
        marginDp: Int
    ) {
        applyConstraintSetToConstraintLayoutProductCard { constraintSet ->
            val marginPixel = getDimensionPixelSize(marginDp)
            constraintSet.setMargin(viewId, anchor, marginPixel)
        }
    }

    private fun applyConstraintSetToConstraintLayoutProductCard(configureConstraintSet: (constraintSet: ConstraintSet) -> Unit) {
        constraintLayoutProductCard?.let {
            val constraintSet = ConstraintSet()

            constraintSet.clone(it)
            configureConstraintSet(constraintSet)
            constraintSet.applyTo(it)
        }
    }

    protected fun setViewConstraintInConstraintLayoutProductCard(
        @IdRes startLayoutId: Int,
        startSide: Int,
        @IdRes endLayoutId: Int,
        endSide: Int,
        @DimenRes marginDp: Int
    ) {
        applyConstraintSetToConstraintLayoutProductCard { constraintSet ->
            val marginPixel = getDimensionPixelSize(marginDp)
            constraintSet.connect(startLayoutId, startSide, endLayoutId, endSide, marginPixel)
        }
    }

    protected fun isViewNotNullAndVisible(view: View?): Boolean {
        return view != null && view.visibility == View.VISIBLE
    }

    protected fun getDimensionPixelSize(@DimenRes id: Int): Int {
        return context.resources.getDimensionPixelSize(id)
    }
}