package com.tokopedia.home.beranda.presentation.view.customview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.design.image.SquareImageView
import com.tokopedia.home.R
import com.tokopedia.home.beranda.helper.HomeImageHandler
import com.tokopedia.home.beranda.helper.glide.loadImage
import com.tokopedia.home.beranda.helper.glide.loadImageFitCenter
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Lukas on 01/10/19
 */
class ThematicCardView : BaseCustomView {
    companion object {
        const val LIGHT_GREY = "lightGrey"
        const val LIGHT_BLUE = "lightBlue"
        const val LIGHT_GREEN = "lightGreen"
        const val LIGHT_RED = "lightRed"
        const val LIGHT_ORANGE = "lightOrange"
        const val DARK_GREY = "darkGrey"
        const val DARK_BLUE = "darkBlue"
        const val DARK_GREEN = "darkGreen"
        const val DARK_RED = "darkRed"
        const val DARK_ORANGE = "darkOrange"
    }
    /**
     * View components of a ProductCardView
     */
    private var cardViewProductCard: CardView? = null
    private var constraintLayoutProductCard: ConstraintLayout? = null
    private var imageProduct: SquareImageView? = null
    private var labelPromo: Label? = null
    private var textViewShopName: Typography? = null
    private var textViewProductName: Typography? = null
    private var labelDiscount: Label? = null
    private var textViewSlashedPrice: Typography? = null
    private var textViewPrice: Typography? = null
    private var linearLayoutShopBadges: LinearLayout? = null
    private var textViewShopLocation: Typography? = null
    private var linearLayoutImageRating: LinearLayout? = null
    private var imageViewRating1: ImageView? = null
    private var imageViewRating2: ImageView? = null
    private var imageViewRating3: ImageView? = null
    private var imageViewRating4: ImageView? = null
    private var imageViewRating5: ImageView? = null
    private var textViewReviewCount: Typography? = null
    private var imageFreeOngkirPromo: ImageView? = null
    private var labelCredibility: Label? = null
    private var blankSpaceConfig = BlankSpaceConfig()

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

    private fun init(){
        findViews(View.inflate(context, R.layout.thematic_card_view, this))
    }


    /**
     * Find view components from the getLayout() method.
     *
     * @param inflatedView View inflated from getLayout()
     */
    private fun findViews(inflatedView: View) {
        cardViewProductCard = inflatedView.findViewById(R.id.cardViewProductCard)
        constraintLayoutProductCard = inflatedView.findViewById(R.id.constraintLayoutProductCard)
        imageProduct = inflatedView.findViewById(R.id.imageProduct)
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
    }

    fun setBlankSpaceConfig(blankSpaceConfig: BlankSpaceConfig){
        this.blankSpaceConfig = blankSpaceConfig
    }

    fun initProductImage(productImageUrl: String) {
        imageProduct?.shouldShowWithAction(productImageUrl.isNotEmpty()) {
            it.loadImage(productImageUrl)
        }
    }

    fun initShopName(shopName: String) {
        textViewShopName.setTextWithBlankSpaceConfig(shopName, blankSpaceConfig.shopName)
    }

    fun initLabelPromo(title: String, labelType: String) {
        labelPromo.shouldShowWithAction(title.isNotEmpty()) {
            it.text = title
            it.setLabelType(getLabelTypeFromString(labelType))
        }
    }

    fun initProductName(productName: String) {
        if (blankSpaceConfig.twoLinesProductName) textViewProductName?.setLines(2)
        textViewProductName.setTextWithBlankSpaceConfig(productName, blankSpaceConfig.productName)
    }

    fun initLabelDiscount(discountPercentage: String) {
        val isLabelDiscountVisible = getIsLabelDiscountVisible(discountPercentage)

        labelDiscount.configureVisibilityWithBlankSpaceConfig(
                isLabelDiscountVisible, blankSpaceConfig.discountPercentage) {
            it.text = MethodChecker.fromHtml(discountPercentage)
        }
    }

    private fun getIsLabelDiscountVisible(discountPercentage: String): Boolean {
        return discountPercentage.isNotEmpty()
                && discountPercentage.trim() != "0"
    }

    fun initSlashedPrice(slashedPrice: String) {
        textViewSlashedPrice.configureVisibilityWithBlankSpaceConfig(
                slashedPrice.isNotEmpty(), blankSpaceConfig.slashedPrice) {
            it.text = slashedPrice
            it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    fun initProductPrice(formattedPrice: String) {
        textViewPrice.setTextWithBlankSpaceConfig(formattedPrice, blankSpaceConfig.price)
    }

    private fun hasAnyBadgesShown(shopBadgeList: List<ProductCardModel.ShopBadge>): Boolean {
        return shopBadgeList.any { badge -> badge.isShown }
    }

    private fun loopBadgesListToLoadShopBadgeIcon(shopBadgeList: List<ProductCardModel.ShopBadge>) {
        for (badgeItem in shopBadgeList) {
            if (badgeItem.isShown) {
                loadShopBadgesIcon(badgeItem.imageUrl)
            }
        }
    }

    private fun loadShopBadgesIcon(url: String) {
        if(!TextUtils.isEmpty(url)) {
            val view = LayoutInflater.from(context).inflate(com.tokopedia.productcard.R.layout.product_card_badge_layout, null)
            ImageHandler.loadImageBitmap2(context, url, object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    loadShopBadgeSuccess(view, resource)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    loadShopBadgeFailed(view)
                }
            })
        }
    }

    private fun loadShopBadgeSuccess(view: View, bitmap: Bitmap) {
        val image = view.findViewById<ImageView>(com.tokopedia.productcard.R.id.badge)

        if (bitmap.height <= 1 && bitmap.width <= 1) {
            view.visibility = View.GONE
        } else {
            image.setImageBitmap(bitmap)
            view.visibility = View.VISIBLE
            addShopBadge(view)
        }
    }

    private fun loadShopBadgeFailed(view: View) {
        view.visibility = View.GONE
    }

    private fun initShopLocation(shopLocation: String) {
        textViewShopLocation.setTextWithBlankSpaceConfig(shopLocation, blankSpaceConfig.shopLocation)
    }

    fun initRating(ratingCount: Int) {
        linearLayoutImageRating.configureVisibilityWithBlankSpaceConfig(
                ratingCount > 0, blankSpaceConfig.ratingCount) {
            setRating(ratingCount)
        }
    }

    fun initFreeOngkir(isActive: Boolean = false, imageUrl: String = "") {
        val shouldShowFreeOngkirImage = isActive && imageUrl.isNotEmpty()

        imageFreeOngkirPromo.configureVisibilityWithBlankSpaceConfig(
                shouldShowFreeOngkirImage, blankSpaceConfig.freeOngkir) {
            it.loadImageFitCenter(imageUrl)
        }
    }

    private fun initReview(reviewCount: Int) {
        textViewReviewCount.configureVisibilityWithBlankSpaceConfig(
                reviewCount > 0, blankSpaceConfig.reviewCount) {
            it.text = getReviewCountFormattedAsText(reviewCount)
        }
    }

    private fun initLabelCredibility(labelCredibilityModel: ProductCardModel.Label) {
        labelCredibility.configureVisibilityWithBlankSpaceConfig(
                labelCredibilityModel.title.isNotEmpty(), blankSpaceConfig.labelCredibility) {
            it.text = labelCredibilityModel.title
            it.setLabelType(getLabelTypeFromString(labelCredibilityModel.type))
        }
    }

    fun setImageProductVisible(isVisible: Boolean) {
        imageProduct?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setImageProductUrl(imageUrl: String) {
        imageProduct?.let {
            ImageHandler.loadImageThumbs(context, it, imageUrl)
        }
    }

    fun setImageProductViewHintListener(holder: ImpressHolder, viewHintListener: ViewHintListener) {
        imageProduct?.addOnImpressionListener(holder, viewHintListener)
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

    private fun getLabelTypeFromString(labelType: String): Int {
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

    fun setSlashedPriceInvisible(isInvisible: Boolean) {
        textViewSlashedPrice?.visibility = if (isInvisible) View.INVISIBLE else View.VISIBLE
    }

    fun setLabelDiscountText(discount: Int) {
        val discountText = "$discount%"
        labelDiscount?.text = discountText
    }

    fun setLabelDiscountText(discount: String) {
        labelDiscount?.text = discount
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
        linearLayoutImageRating?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setImageRatingInvisible(isInvisible: Boolean) {
        linearLayoutImageRating?.visibility = if (isInvisible) View.INVISIBLE else View.VISIBLE
    }

    fun setRating(rating: Int) {
        imageViewRating1?.setImageResource(getRatingDrawable(rating >= 1))
        imageViewRating2?.setImageResource(getRatingDrawable(rating >= 2))
        imageViewRating3?.setImageResource(getRatingDrawable(rating >= 3))
        imageViewRating4?.setImageResource(getRatingDrawable(rating >= 4))
        imageViewRating5?.setImageResource(getRatingDrawable(rating >= 5))
    }

    override fun setOnClickListener(clickListener: OnClickListener?) {
        cardViewProductCard?.setOnClickListener(clickListener)
    }

    @DrawableRes
    fun getRatingDrawable(isActive: Boolean): Int {
        return if(isActive) com.tokopedia.productcard.R.drawable.product_card_ic_rating_active
        else com.tokopedia.productcard.R.drawable.product_card_ic_rating_default
    }

    fun setReviewCountVisible(isVisible: Boolean) {
        textViewReviewCount?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setReviewCountInvisible(isInvisible: Boolean) {
        textViewReviewCount?.visibility = if (isInvisible) View.INVISIBLE else View.VISIBLE
    }

    fun setLinesProductTitle(lines: Int){
        textViewProductName?.setLines(lines)
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
    
    fun setLabelDiscountInvisible(isInvisible: Boolean){
        labelDiscount?.visibility = if (isInvisible) View.INVISIBLE else View.VISIBLE
    }

    fun setViewMargins(@IdRes viewId: Int, anchor: Int, marginDp: Int) {
        constraintLayoutProductCard.applyConstraintSet { constraintSet ->
            val marginPixel = getDimensionPixelSize(marginDp)
            constraintSet.setMargin(viewId, anchor, marginPixel)
        }
    }

    fun setViewConstraint(
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

    private fun <T: View> T?.shouldShowWithAction(shouldShow: Boolean, action: (T) -> Unit) {
        if (this == null) return

        if (shouldShow) {
            this.visibility = View.VISIBLE
            action(this)
        } else {
            this.visibility = View.GONE
        }
    }

    private fun ConstraintLayout?.applyConstraintSet(configureConstraintSet: (ConstraintSet) -> Unit) {
        this?.let {
            val constraintSet = ConstraintSet()

            constraintSet.clone(it)
            configureConstraintSet(constraintSet)
            constraintSet.applyTo(it)
        }
    }

    private fun View.getDimensionPixelSize(@DimenRes id: Int): Int {
        return this.context.resources.getDimensionPixelSize(id)
    }

    private fun <T: View> T?.configureVisibilityWithBlankSpaceConfig(isVisible: Boolean, blankSpaceConfigValue: Boolean, action: (T) -> Unit) {
        if (this == null) return

        visibility = if (isVisible) {
            action(this)
            View.VISIBLE
        } else {
            getViewNotVisibleWithBlankSpaceConfig(blankSpaceConfigValue)
        }
    }

    private fun getViewNotVisibleWithBlankSpaceConfig(blankSpaceConfigValue: Boolean): Int {
        return if (blankSpaceConfigValue) {
            View.INVISIBLE
        }
        else {
            View.GONE
        }
    }

    private fun TextView?.setTextWithBlankSpaceConfig(textValue: String, blankSpaceConfigValue: Boolean) {
        this?.configureVisibilityWithBlankSpaceConfig(textValue.isNotEmpty(), blankSpaceConfigValue) {
            it.text = MethodChecker.fromHtml(textValue)
        }
    }

}