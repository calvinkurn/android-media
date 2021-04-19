package com.tokopedia.productcard.v2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.media.loader.loadImageWithTarget
import com.tokopedia.media.loader.utils.MediaTarget
import com.tokopedia.productcard.ProductCardModel
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
@Deprecated("Please use ProductCardGridView or ProductCardListView")
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
    protected var imageProduct: ImageView? = null
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
    protected var imageFreeOngkirPromo: ImageView? = null
    protected var labelOffers: Label? = null
    protected var imageTopAds: ImageView? = null
    protected var blankSpaceConfig = BlankSpaceConfig()

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
        imageFreeOngkirPromo = inflatedView.findViewById(R.id.imageFreeOngkirPromo)
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
        setLocationConstraintEnd()
        setReviewCountMarginLeft()
        setImageFreeOngkirPromoConstraint()
        setLabelOffersConstraint()
        setTopAdsTopConstraint()
    }

    protected open fun setProductNameMarginTop() {
        textViewProductName?.doIfVisible { textViewProductName ->
            val marginTopDp = getTitleMarginTop()
            setViewMargins(textViewProductName.id, ConstraintSet.TOP, marginTopDp)
        }
    }

    protected open fun getTitleMarginTop(): Int {
        return if (textViewShopName.isNotNullAndVisible) com.tokopedia.design.R.dimen.dp_2
        else com.tokopedia.design.R.dimen.dp_8
    }

    protected open fun setPriceMarginTop() {
        textViewPrice?.doIfVisible { textViewPrice ->
            val marginTopDp = getPriceMarginTop()
            setViewMargins(textViewPrice.id, ConstraintSet.TOP, marginTopDp)
        }
    }

    protected open fun getPriceMarginTop(): Int {
        return if (labelDiscount.isNotNullAndVisible) com.tokopedia.design.R.dimen.dp_2
        else com.tokopedia.design.R.dimen.dp_4
    }

    protected open fun setLocationMarginLeft() {
        textViewShopLocation?.doIfVisible { textViewShopLocation ->
            val marginStartDp = getLocationMarginLeft()
            setViewMargins(textViewShopLocation.id, ConstraintSet.START, marginStartDp)
        }
    }

    protected open fun getLocationMarginLeft(): Int {
        return if (linearLayoutShopBadges.isNotNullAndVisible) com.tokopedia.design.R.dimen.dp_4
        else com.tokopedia.design.R.dimen.dp_8
    }

    protected open fun setLocationConstraintEnd() {
        textViewShopLocation?.doIfVisible { textViewShopLocation ->
            imageTopAds?.doIfVisible { imageTopAds ->
                configureTextViewLocationConstraintBasedOnPosition(imageTopAds, textViewShopLocation)
            }
        }
    }

    protected open fun configureTextViewLocationConstraintBasedOnPosition(imageTopAds: View, textViewShopLocation: View) {
        if(isTextLocationIsAtBottomOfCard()) {
            setViewConstraint(textViewShopLocation.id, ConstraintSet.END, imageTopAds.id, ConstraintSet.START, com.tokopedia.design.R.dimen.dp_4)
        }
        else {
            imageProduct?.doIfVisible { imageProduct ->
                setViewConstraint(textViewShopLocation.id, ConstraintSet.END, imageProduct.id, ConstraintSet.END, com.tokopedia.design.R.dimen.dp_8)
            }
        }
    }

    protected open fun isTextLocationIsAtBottomOfCard(): Boolean {
        return labelCredibility.isNullOrNotVisible
                && linearLayoutImageRating.isNullOrNotVisible
                && textViewReviewCount.isNullOrNotVisible
                && labelOffers.isNullOrNotVisible
    }

    protected open fun setReviewCountMarginLeft() {
        textViewReviewCount?.doIfVisible { textViewReviewCount ->
            val marginStartDp = getReviewCountMarginLeft()
            setViewMargins(textViewReviewCount.id, ConstraintSet.START, marginStartDp)
        }
    }

    protected open fun getReviewCountMarginLeft(): Int {
        return if(linearLayoutImageRating.isNotNullAndVisible) com.tokopedia.design.R.dimen.dp_4
        else com.tokopedia.design.R.dimen.dp_8
    }

    protected open fun setImageFreeOngkirPromoConstraint() {
        imageFreeOngkirPromo?.doIfVisible { imageFreeOngkirPromo ->
            val imageFreeOngkirPromoTopConstraintView = getImageFreeOngkirTopConstraintView()

            imageFreeOngkirPromoTopConstraintView?.let {
                setViewConstraint(
                        imageFreeOngkirPromo.id, ConstraintSet.TOP, it.id, ConstraintSet.BOTTOM, com.tokopedia.design.R.dimen.dp_8
                )
            }
        }
    }

    protected open fun getImageFreeOngkirTopConstraintView(): View? {
        return when {
            labelCredibility.isNotNullAndVisible -> labelCredibility
            linearLayoutImageRating.isNotNullAndVisible -> linearLayoutImageRating
            textViewReviewCount.isNotNullAndVisible -> textViewReviewCount
            textViewShopLocation.isNotNullAndVisible -> textViewShopLocation
            else -> null
        }
    }

    protected open fun setLabelOffersConstraint() {
        labelOffers?.doIfVisible { labelOffers ->
            val labelOffersTopConstraintView = getLabelOffersTopConstraintView()

            labelOffersTopConstraintView?.let {
                setViewConstraint(
                        labelOffers.id, ConstraintSet.TOP, it.id, ConstraintSet.BOTTOM, com.tokopedia.design.R.dimen.dp_8
                )
            }
        }
    }

    protected open fun getLabelOffersTopConstraintView(): View? {
        return when {
            imageFreeOngkirPromo.isNotNullAndVisible -> imageFreeOngkirPromo
            else -> getImageFreeOngkirTopConstraintView()
        }
    }

    protected open fun setTopAdsTopConstraint() {
        imageTopAds?.doIfVisible { imageTopAds ->
            val imageTopAdsTopConstraintView = getImageTopAdsTopConstraintView()

            imageTopAdsTopConstraintView?.let {
                setViewConstraint(
                        imageTopAds.id, ConstraintSet.TOP, it.id, ConstraintSet.TOP, com.tokopedia.design.R.dimen.dp_0
                )
            }
        }
    }

    private fun getImageTopAdsTopConstraintView(): View? {
        return when {
            labelOffers.isNotNullAndVisible -> labelOffers
            else -> getLabelOffersTopConstraintView()
        }
    }

    open fun getCardViewRadius(): Float {
        return cardViewProductCard?.radius ?: 0f
    }

    open fun getCardViewMaxElevation(): Float {
        return cardViewProductCard?.maxCardElevation ?: 0f
    }

    open fun setProductModel(productCardModel: ProductCardModel, blankSpaceConfig: BlankSpaceConfig = this.blankSpaceConfig) {
        this.blankSpaceConfig = blankSpaceConfig

        initProductImage(productCardModel.productImageUrl)
        initWishlist(productCardModel.isWishlistVisible, productCardModel.isWishlisted)
        initShopName(productCardModel.shopName)
        initLabelPromo(productCardModel.labelPromo)
        initProductName(productCardModel.productName)
        initLabelDiscount(productCardModel.discountPercentage)
        initSlashedPrice(productCardModel.slashedPrice)
        initProductPrice(productCardModel.formattedPrice)
        initShopBadgeList(productCardModel.shopBadgeList)
        initShopLocation(productCardModel.shopLocation)
        initRating(productCardModel.ratingCount)
        initReview(productCardModel.reviewCount)
        initLabelCredibility(productCardModel.ratingCount, productCardModel.reviewCount, productCardModel.labelCredibility)
        initLabelOffers(productCardModel.labelOffers)
        initFreeOngkir(productCardModel.freeOngkir)
        initTopAdsIcon(productCardModel.isTopAds)

        realignLayout()
    }

    open protected fun initProductImage(productImageUrl: String) {
        imageProduct?.shouldShowWithAction(productImageUrl.isNotEmpty()) {
            it.loadImage(productImageUrl)
        }
    }

    internal open fun initWishlist(isWishlistVisible: Boolean, isWishlisted: Boolean) {
        buttonWishlist.shouldShowWithAction(isWishlistVisible) {
            if (isWishlisted) {
                it.setImageResource(R.drawable.product_card_ic_wishlist_red)
            } else {
                it.setImageResource(R.drawable.product_card_ic_wishlist)
            }
        }
    }

    internal open fun initShopName(shopName: String) {
        textViewShopName.setTextWithBlankSpaceConfig(shopName, blankSpaceConfig.shopName)
    }

    internal open fun initLabelPromo(labelPromoModel: ProductCardModel.Label) {
        val isLabelPromoVisible = labelPromoModel.title.isNotEmpty()

        labelPromo.configureVisibilityWithBlankSpaceConfig(isLabelPromoVisible, blankSpaceConfig.labelPromo) {
            it.text = labelPromoModel.title
            it.setLabelType(getLabelTypeFromString(labelPromoModel.type))
        }
    }

    internal open fun initProductName(productName: String) {
        if (blankSpaceConfig.twoLinesProductName) textViewProductName?.setLines(2)
        textViewProductName.setTextWithBlankSpaceConfig(productName, blankSpaceConfig.productName)
    }

    internal open fun initLabelDiscount(discountPercentage: String) {
        val isLabelDiscountVisible = getIsLabelDiscountVisible(discountPercentage)

        labelDiscount.configureVisibilityWithBlankSpaceConfig(
                isLabelDiscountVisible, blankSpaceConfig.discountPercentage) {
            it.text = MethodChecker.fromHtml(discountPercentage)
        }
    }

    internal fun getIsLabelDiscountVisible(discountPercentage: String): Boolean {
        return discountPercentage.isNotEmpty()
                && discountPercentage.trim() != "0"
    }

    internal open fun initSlashedPrice(slashedPrice: String) {
        textViewSlashedPrice.configureVisibilityWithBlankSpaceConfig(
                slashedPrice.isNotEmpty(), blankSpaceConfig.slashedPrice) {
            it.text = slashedPrice
            it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    internal open fun initProductPrice(formattedPrice: String) {
        textViewPrice.setTextWithBlankSpaceConfig(formattedPrice, blankSpaceConfig.price)
    }

    internal open fun initShopBadgeList(shopBadgeList: List<ProductCardModel.ShopBadge>) {
        removeAllShopBadges()

        linearLayoutShopBadges.configureVisibilityWithBlankSpaceConfig(hasAnyBadgesShown(shopBadgeList), blankSpaceConfig.shopBadge) {
            loopBadgesListToLoadShopBadgeIcon(shopBadgeList)
        }
    }

    internal fun hasAnyBadgesShown(shopBadgeList: List<ProductCardModel.ShopBadge>): Boolean {
        return shopBadgeList.any { badge -> badge.isShown }
    }

    internal fun loopBadgesListToLoadShopBadgeIcon(shopBadgeList: List<ProductCardModel.ShopBadge>) {
        for (badgeItem in shopBadgeList) {
            if (badgeItem.isShown) {
                loadShopBadgesIcon(badgeItem.imageUrl)
            }
        }
    }

    private fun loadShopBadgesIcon(url: String) {
        if(!TextUtils.isEmpty(url)) {
            val view = LayoutInflater.from(context).inflate(R.layout.product_card_badge_layout, null)

            loadImageWithTarget(context, url, {
                centerCrop()
            }, MediaTarget(
                    view.findViewById<ImageView>(R.id.badge),
                    onReady = { badgeView, resource ->
                        loadShopBadgeSuccess(view, badgeView, resource)
                    },
                    onFailed = { _, _ ->
                        loadShopBadgeFailed(view)
                    }
            ))
        }
    }

    private fun loadShopBadgeSuccess(layout: View, badgeView: ImageView, bitmap: Bitmap) {
        if (bitmap.height <= 1 && bitmap.width <= 1) {
            layout.visibility = View.GONE
        } else {
            badgeView.setImageBitmap(bitmap)
            layout.visibility = View.VISIBLE
            addShopBadge(layout)
        }
    }

    private fun loadShopBadgeFailed(view: View) {
        view.visibility = View.GONE
    }

    internal open fun initShopLocation(shopLocation: String) {
        textViewShopLocation.setTextWithBlankSpaceConfig(shopLocation, blankSpaceConfig.shopLocation)
    }

    internal open fun initRating(ratingCount: Int) {
        linearLayoutImageRating.configureVisibilityWithBlankSpaceConfig(
                ratingCount > 0, blankSpaceConfig.ratingCount) {
            setRating(ratingCount)
        }
    }

    internal open fun initReview(reviewCount: Int) {
        textViewReviewCount.configureVisibilityWithBlankSpaceConfig(
                reviewCount > 0, blankSpaceConfig.reviewCount) {
            it.text = getReviewCountFormattedAsText(reviewCount)
        }
    }

    internal open fun initLabelCredibility(ratingCount: Int, reviewCount: Int, labelCredibilityModel: ProductCardModel.Label) {
        val isLabelCredibilityVisible = isLabelCredibilityVisible(ratingCount, reviewCount, labelCredibilityModel)

        labelCredibility.configureVisibilityWithBlankSpaceConfig(isLabelCredibilityVisible, blankSpaceConfig.labelCredibility) {
            it.text = labelCredibilityModel.title
            it.setLabelType(getLabelTypeFromString(labelCredibilityModel.type))
        }
    }

    internal fun isLabelCredibilityVisible(ratingCount: Int, reviewCount: Int, labelCredibilityModel: ProductCardModel.Label): Boolean {
        return labelCredibilityModel.title.isNotEmpty() && ratingCount == 0 && reviewCount == 0
    }

    internal open fun initLabelOffers(labelOffersModel: ProductCardModel.Label) {
        labelOffers.configureVisibilityWithBlankSpaceConfig(
                labelOffersModel.title.isNotEmpty(), blankSpaceConfig.labelOffers) {
            it.text = labelOffersModel.title
            it.setLabelType(getLabelTypeFromString(labelOffersModel.type))
        }
    }

    internal open fun initFreeOngkir(freeOngkir: ProductCardModel.FreeOngkir) {
        val shouldShowFreeOngkirImage = freeOngkir.isActive && freeOngkir.imageUrl.isNotEmpty()

        imageFreeOngkirPromo.configureVisibilityWithBlankSpaceConfig(
                shouldShowFreeOngkirImage, blankSpaceConfig.freeOngkir) {
            it.loadIcon(freeOngkir.imageUrl)
        }
    }

    internal open fun initTopAdsIcon(isVisible: Boolean) {
        imageTopAds?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    open fun setImageProductVisible(isVisible: Boolean) {
        imageProduct?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    open fun setImageProductUrl(imageUrl: String) {
        imageProduct?.let {
            it.loadImage(imageUrl)
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
        val discountText = "$discount%"
        labelDiscount?.text = discountText
    }

    open fun setLabelDiscountText(discount: String) {
        labelDiscount?.text = discount
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

    open fun setCardHeight(height: Int) {
        val layoutParams = cardViewProductCard?.layoutParams
        layoutParams?.height = height
        cardViewProductCard?.layoutParams = layoutParams
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
}
