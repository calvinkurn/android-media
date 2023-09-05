package com.tokopedia.productcard

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.productcard.utils.CLOSE_BOLD_TAG
import com.tokopedia.productcard.utils.CustomTypefaceSpan
import com.tokopedia.productcard.utils.OPEN_BOLD_TAG
import com.tokopedia.productcard.utils.ROBOTO_BOLD
import com.tokopedia.productcard.utils.ROBOTO_REGULAR
import com.tokopedia.productcard.utils.applyConstraintSet
import com.tokopedia.productcard.utils.initLabelGroup
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.getTypeface
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil

internal fun View.renderProductCardContent(
    productCardModel: ProductCardModel,
    isMergePriceSection: Boolean = false,
    isMergeShippingSection: Boolean = false,
) {
    renderTextGimmick(productCardModel)
    renderPdpCountView(productCardModel)
    renderTextProductName(productCardModel)
    renderLabelGroupVariant(productCardModel)
    renderTextCategoryAndCostPerUnit(productCardModel)
    renderTextPrice(productCardModel)
    renderDiscount(productCardModel)
    renderLabelPrice(productCardModel)
    renderShopBadge(productCardModel)
    renderTextFulfillment(productCardModel)
    renderTextShopLocation(productCardModel)
    renderRating(productCardModel)
    renderTextReview(productCardModel)
    renderTextCredibility(productCardModel)
    renderSalesAndRating(productCardModel)
    renderShopRating(productCardModel)
    renderFreeOngkir(productCardModel)
    renderTextShipping(productCardModel)
    renderTextETA(productCardModel)
    productCardModel.layoutStrategy.configContentPosition(this)

    configureConstraint(productCardModel, isMergePriceSection, isMergeShippingSection)
}

private fun View.renderTextGimmick(productCardModel: ProductCardModel) {
    productCardModel.layoutStrategy.renderTextGimmick(this, productCardModel)
}

private fun View.renderPdpCountView(productCardModel: ProductCardModel) {
    val imageViewPdpView = findViewById<ImageView?>(R.id.imageViewPdpView)
    val textViewPdpView = findViewById<Typography?>(R.id.textViewPdpView)
    imageViewPdpView?.hide()
    textViewPdpView?.shouldShowWithAction(productCardModel.pdpViewCount.isNotEmpty()) {
        it.text = MethodChecker.fromHtml(productCardModel.pdpViewCount)
        imageViewPdpView.show()
    }
}

private fun View.renderTextProductName(productCardModel: ProductCardModel) {
    val textViewProductName = findViewById<Typography?>(R.id.textViewProductName)
    textViewProductName?.shouldShowWithAction(productCardModel.productName.isNotEmpty()) {
        val productNameFromHtml = MethodChecker.fromHtml(productCardModel.productName)
        TextAndContentDescriptionUtil.setTextAndContentDescription(it, productNameFromHtml.toString(), context.getString(R.string.content_desc_textViewProductName))

        val willShowVariant = productCardModel.willShowVariant()

        if (productCardModel.layoutStrategy.isSingleLine(willShowVariant)) {
            it.isSingleLine = true
        } else {
            it.isSingleLine = false
            it.maxLines = 2
            it.ellipsize = TextUtils.TruncateAt.END
        }
    }
}

private fun View.renderLabelGroupVariant(productCardModel: ProductCardModel) {
    val willShowVariant = productCardModel.willShowVariant()

    productCardModel.layoutStrategy.renderVariant(
        willShowVariant,
        this,
        productCardModel,
    )
}

internal fun safeParseColor(color: String): Int {
    return try {
        Color.parseColor(color)
    }
    catch (throwable: Throwable) {
        throwable.printStackTrace()
        0
    }
}

fun View.renderTextCategoryAndCostPerUnit(productCardModel: ProductCardModel) {
    val textViewCategory = findViewById<Typography?>(R.id.textViewCategory)
    val dividerCategory = findViewById<View?>(R.id.dividerCategory)
    val textViewCostPerUnit = findViewById<Typography?>(R.id.textViewCostPerUnit)
    textViewCategory?.shouldShowWithAction(productCardModel.isShowLabelCategory()) {
        it.initLabelGroup(productCardModel.getLabelCategory())
    }

    dividerCategory?.showWithCondition(productCardModel.isShowCategoryAndCostPerUnit())

    textViewCostPerUnit?.shouldShowWithAction(productCardModel.isShowLabelCostPerUnit()) {
        it.initLabelGroup(productCardModel.getLabelCostPerUnit())
    }
}

private fun View.renderTextPrice(productCardModel: ProductCardModel) {
    val textViewPrice = findViewById<Typography?>(R.id.textViewPrice)
    moveTextPriceConstraint(productCardModel)

    val priceToRender = productCardModel.getPriceToRender()

    textViewPrice?.shouldShowWithAction(priceToRender.isNotEmpty()) {
        TextAndContentDescriptionUtil.setTextAndContentDescription(it, priceToRender, context.getString(R.string.content_desc_textViewPrice))
    }
}

private fun View.moveTextPriceConstraint(productCardModel: ProductCardModel) {
    val hasLabelCostPerUnit = productCardModel.getLabelCostPerUnit()?.title?.isNotEmpty() == true
    val targetConstraint = if (hasLabelCostPerUnit) R.id.textViewCostPerUnit else R.id.textViewCategory
    val view = findViewById<ConstraintLayout?>(R.id.productCardContentLayout)

    view?.applyConstraintSet {
        it.connect(R.id.textViewPrice, ConstraintSet.TOP, targetConstraint, ConstraintSet.BOTTOM, 4.toPx())
    }
}

private fun View.renderDiscount(productCardModel: ProductCardModel) {
    val labelDiscount = findViewById<Label?>(R.id.labelDiscount)
    val textViewSlashedPrice = findViewById<Typography?>(R.id.textViewSlashedPrice)

    productCardModel.layoutStrategy.moveDiscountConstraint(this, productCardModel)
    productCardModel.layoutStrategy.setDiscountMargin(labelDiscount)

    labelDiscount?.shouldShowWithAction(productCardModel.discountPercentage.isNotEmpty()) {
        TextAndContentDescriptionUtil.setTextAndContentDescription(it, productCardModel.discountPercentage, context.getString(R.string.content_desc_labelDiscount))
    }

    textViewSlashedPrice?.shouldShowWithAction(productCardModel.slashedPrice.isNotEmpty()) {
        TextAndContentDescriptionUtil.setTextAndContentDescription(it, productCardModel.slashedPrice, context.getString(R.string.content_desc_textViewSlashedPrice))
        it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }
}

private fun View.renderLabelPrice(productCardModel: ProductCardModel) {
    productCardModel.layoutStrategy.renderLabelPrice(this, productCardModel)
}

private fun ProductCardModel.getPriceToRender(): String {
    return if (priceRange.isNotEmpty()) priceRange else formattedPrice
}

private fun View.renderTextFulfillment(productCardModel: ProductCardModel) {
    val imageFulfillment = findViewById<AppCompatImageView?>(R.id.imageFulfillment)
    val dividerFulfillment = findViewById<View?>(R.id.dividerFulfillment)
    val textViewFulfillment = findViewById<Typography?>(R.id.textViewFulfillment)
    if (productCardModel.willShowFulfillment()) {
        val labelGroup = productCardModel.getLabelFulfillment() ?: return

        imageFulfillment?.show()
        imageFulfillment?.loadIcon(labelGroup.imageUrl)

        dividerFulfillment?.showWithCondition(productCardModel.isShowShopBadge())

        textViewFulfillment?.initLabelGroup(productCardModel.getLabelFulfillment())
    }
    else {
        imageFulfillment?.hide()
        dividerFulfillment?.hide()
        textViewFulfillment?.initLabelGroup(null)
    }
}

private fun View.renderShopBadge(productCardModel: ProductCardModel) {
    val imageShopBadge = findViewById<ImageView?>(R.id.imageShopBadge)
    val shopBadge = productCardModel.shopBadgeList.find { it.isShown && it.imageUrl.isNotEmpty() }
    imageShopBadge?.shouldShowWithAction(productCardModel.isShowShopBadge()) {
        it.loadIcon(shopBadge?.imageUrl ?: "")
    }
}

private fun View.renderTextShopLocation(productCardModel: ProductCardModel) {
    val textViewShopLocation = findViewById<Typography?>(R.id.textViewShopLocation)
    textViewShopLocation?.shouldShowWithAction(
        productCardModel.isShowShopLocation()
    ) {
        TextAndContentDescriptionUtil.setTextAndContentDescription(
            it,
            productCardModel.shopLocation,
            context.getString(R.string.content_desc_textViewShopLocation),
        )
    }
}

private fun View.renderRating(productCardModel: ProductCardModel) {
    when {
        !productCardModel.willShowRatingAndReviewCount() -> hideRating()
        productCardModel.ratingCount > 0 -> renderRatingStars(productCardModel)
    }
}

private fun View.hideRating() {
    val linearLayoutImageRating = findViewById<LinearLayout?>(R.id.linearLayoutImageRating)
    linearLayoutImageRating?.gone()
}

private fun View.renderRatingStars(productCardModel: ProductCardModel) {
    val linearLayoutImageRating = findViewById<LinearLayout?>(R.id.linearLayoutImageRating)
    linearLayoutImageRating?.visible()
    setImageRating(productCardModel.ratingCount)
}

@Suppress("MagicNumber")
private fun View.setImageRating(rating: Int) {
    val imageViewRating1 = findViewById<ImageView?>(R.id.imageViewRating1)
    val imageViewRating2 = findViewById<ImageView?>(R.id.imageViewRating2)
    val imageViewRating3 = findViewById<ImageView?>(R.id.imageViewRating3)
    val imageViewRating4 = findViewById<ImageView?>(R.id.imageViewRating4)
    val imageViewRating5 = findViewById<ImageView?>(R.id.imageViewRating5)
    imageViewRating1?.setImageResource(getRatingDrawable(rating >= 1))
    imageViewRating2?.setImageResource(getRatingDrawable(rating >= 2))
    imageViewRating3?.setImageResource(getRatingDrawable(rating >= 3))
    imageViewRating4?.setImageResource(getRatingDrawable(rating >= 4))
    imageViewRating5?.setImageResource(getRatingDrawable(rating >= 5))
}

@DrawableRes
private fun getRatingDrawable(isActive: Boolean): Int {
    return if(isActive) R.drawable.product_card_ic_rating_active
    else R.drawable.product_card_ic_rating_default
}

private fun View.renderTextReview(productCardModel: ProductCardModel) {
    val textViewReviewCount = findViewById<Typography?>(R.id.textViewReviewCount)
    textViewReviewCount?.shouldShowWithAction(productCardModel.willShowRatingAndReviewCount()) {
        it.text = String.format(context.getString(R.string.product_card_review_count_format), productCardModel.reviewCount)
    }
}

private fun View.renderTextCredibility(productCardModel: ProductCardModel) {
    val textViewIntegrity = findViewById<Typography?>(R.id.textViewIntegrity)
    if (productCardModel.willShowRatingAndReviewCount() || productCardModel.willShowSalesAndRating())
        textViewIntegrity?.initLabelGroup(null)
    else
        textViewIntegrity?.initLabelGroup(productCardModel.getLabelIntegrity())
}

private fun View.renderSalesAndRating(productCardModel: ProductCardModel) {
    renderSalesRatingFloat(productCardModel)
    renderTextIntegrityWithSalesRatingFloat(productCardModel)
}

private fun View.renderSalesRatingFloat(productCardModel: ProductCardModel) {
    val imageSalesRatingFloat = findViewById<IconUnify?>(R.id.productCardImageSalesRatingFloat)
    val salesRatingFloat = findViewById<Typography?>(R.id.salesRatingFloat)
    val willShowSalesRatingFloat = productCardModel.willShowRating()

    imageSalesRatingFloat?.showWithCondition(willShowSalesRatingFloat)

    salesRatingFloat?.shouldShowWithAction(willShowSalesRatingFloat) {
        it.text = productCardModel.countSoldRating
    }
}

private fun View.renderTextIntegrityWithSalesRatingFloat(productCardModel: ProductCardModel) {
    val salesRatingFloatLine = findViewById<View?>(R.id.salesRatingFloatLine)
    val textViewSales = findViewById<Typography?>(R.id.textViewSales)
    val willShowSalesAndRating = productCardModel.willShowSalesAndRating()

    salesRatingFloatLine?.showWithCondition(willShowSalesAndRating)

    textViewSales?.shouldShowWithAction(willShowSalesAndRating) {
        it.initLabelGroup(productCardModel.getLabelIntegrity())
    }
}

private fun View.renderShopRating(productCardModel: ProductCardModel) {
    val imageShopRating = findViewById<ImageView?>(R.id.imageShopRating)
    val textViewShopRating = findViewById<Typography?>(R.id.textViewShopRating)
    if (productCardModel.isShowShopRating()) {
        imageShopRating?.visible()
        imageShopRating.setImageResource(getShopRatingDrawable(productCardModel))
        textViewShopRating?.shouldShowWithAction(productCardModel.isShowShopRating()) {
            it.setShopRatingText(productCardModel.shopRating)
        }
    }
    else {
        imageShopRating?.gone()
        textViewShopRating?.gone()
    }
}

private fun Typography.setShopRatingText(shopRating: String) {
    val boldTypeface = getTypeface(this.context, ROBOTO_BOLD)
    val regularTypeface = getTypeface(this.context, ROBOTO_REGULAR)

    if (boldTypeface != null && regularTypeface != null) {
        setShopRatingTextWithMultipleTypeface(shopRating, regularTypeface, boldTypeface)
    }
    else {
        text = MethodChecker.fromHtml(shopRating)
    }
}

private fun Typography.setShopRatingTextWithMultipleTypeface(shopRating: String, regularTypeface: Typeface, boldTypeface: Typeface) {
    val startBold = shopRating.indexOf(OPEN_BOLD_TAG)
    val endBold = shopRating.indexOf(CLOSE_BOLD_TAG)

    if (startBold in 0 until endBold) {
        changeFontInsideBoldTag(shopRating, startBold, endBold, regularTypeface, boldTypeface)
    }
    else {
        text = MethodChecker.fromHtml(shopRating)
    }
}

private fun Typography.changeFontInsideBoldTag(shopRating: String, startBold: Int, endBold: Int, regularTypeface: Typeface, boldTypeface: Typeface) {
    try {
        val beforeBoldTag = MethodChecker.fromHtml(shopRating.substring(0, startBold)).toString()
        val inBoldTag = shopRating.substring(startBold + OPEN_BOLD_TAG.length, endBold)
        val afterBoldTag = " " + MethodChecker.fromHtml(shopRating.substring(endBold + CLOSE_BOLD_TAG.length, shopRating.length)).toString()

        val spannableShopRating = SpannableString(beforeBoldTag + inBoldTag + afterBoldTag)

        val beforeBoldTagStart = 0
        val beforeBoldTagEnd = beforeBoldTag.length
        val inBoldTagEnd = beforeBoldTagEnd + inBoldTag.length
        val afterBoldTagEnd = inBoldTagEnd + afterBoldTag.length

        val charcoalGrey44 = ContextCompat.getColor(this.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_44)
        val charcoalGrey68 = ContextCompat.getColor(this.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_68)

        spannableShopRating.setSpan(CustomTypefaceSpan("", regularTypeface, charcoalGrey44), beforeBoldTagStart, beforeBoldTagEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableShopRating.setSpan(CustomTypefaceSpan("", boldTypeface, charcoalGrey68), beforeBoldTagEnd, inBoldTagEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableShopRating.setSpan(CustomTypefaceSpan("", regularTypeface, charcoalGrey44), inBoldTagEnd, afterBoldTagEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        text = spannableShopRating
    }
    catch (e: Exception) {
        text = MethodChecker.fromHtml(shopRating)
    }
}

private fun getShopRatingDrawable(productCardModel: ProductCardModel) =
        if (productCardModel.isShopRatingYellow) R.drawable.product_card_ic_rating_active
        else R.drawable.product_card_ic_shop_rating

private fun View.renderFreeOngkir(productCardModel: ProductCardModel) {
    val imageFreeOngkirPromo = findViewById<ImageView?>(R.id.imageFreeOngkirPromo)
    imageFreeOngkirPromo?.shouldShowWithAction(productCardModel.isShowFreeOngkirBadge()) {
        it.loadIcon(productCardModel.freeOngkir.imageUrl)
    }
}

private fun View.renderTextShipping(productCardModel: ProductCardModel) {
    val textViewShipping = findViewById<Typography?>(R.id.textViewShipping)
    if (productCardModel.isShowFreeOngkirBadge())
        textViewShipping?.initLabelGroup(null)
    else
        textViewShipping?.initLabelGroup(productCardModel.getLabelShipping())
}

private fun View.renderTextETA(productCardModel: ProductCardModel) {
    productCardModel.layoutStrategy.renderTextEta(this, productCardModel)
}

private fun View.configureConstraint(
    productCardModel: ProductCardModel,
    isMergePriceSection: Boolean,
    isMergeShippingSection: Boolean,
) {
    val view = findViewById<ConstraintLayout?>(R.id.productCardContentLayout)

    view?.applyConstraintSet {
        if (isMergePriceSection) mergePriceSection(it)
        else splitPriceSection(it)

        if (isMergeShippingSection) mergeShippingSection(it, productCardModel)
        else splitShippingSection(it)
    }
}

private fun mergePriceSection(constraintSet: ConstraintSet) {
    constraintSet.connect(R.id.labelPrice, ConstraintSet.TOP, R.id.textViewPrice, ConstraintSet.TOP, 0.toPx())
    constraintSet.connect(R.id.labelPrice, ConstraintSet.START, R.id.textViewPrice, ConstraintSet.END, 4.toPx())

    constraintSet.connect(R.id.labelDiscount, ConstraintSet.TOP, R.id.textViewPrice, ConstraintSet.TOP, 0.toPx())
    constraintSet.connect(R.id.labelDiscount, ConstraintSet.START, R.id.labelPrice, ConstraintSet.END, 4.toPx())

    constraintSet.connect(R.id.textViewSlashedPrice, ConstraintSet.TOP, R.id.textViewPrice, ConstraintSet.TOP, 0.toPx())
    constraintSet.setGoneMargin(R.id.textViewSlashedPrice, ConstraintSet.START, 4.toPx())
}

private fun mergeShippingSection(it: ConstraintSet, productCardModel: ProductCardModel) {
    it.connect(R.id.textViewShipping, ConstraintSet.TOP, R.id.imageShopRating, ConstraintSet.BOTTOM, 5.toPx())
    it.connect(R.id.textViewShipping, ConstraintSet.START, R.id.imageFreeOngkirPromo, ConstraintSet.END, 0.toPx())

    val isShowFreeOngkirBadge = productCardModel.isShowFreeOngkirBadge()
    val labelETAMarginStart = if (isShowFreeOngkirBadge) 4.toPx() else 0.toPx()
    it.connect(R.id.textViewETA, ConstraintSet.TOP, R.id.imageShopRating, ConstraintSet.BOTTOM, 7.toPx())
    it.connect(R.id.textViewETA, ConstraintSet.START, R.id.textViewShipping, ConstraintSet.END, labelETAMarginStart)
}

private fun splitPriceSection(constraintSet: ConstraintSet) {
    constraintSet.connect(
        R.id.labelPrice,
        ConstraintSet.TOP,
        R.id.textViewPrice,
        ConstraintSet.BOTTOM,
        2.toPx()
    )
    constraintSet.connect(
        R.id.labelPrice,
        ConstraintSet.START,
        ConstraintSet.PARENT_ID,
        ConstraintSet.START,
        0.toPx()
    )

    constraintSet.connect(
        R.id.labelDiscount,
        ConstraintSet.TOP,
        R.id.textViewPrice,
        ConstraintSet.BOTTOM,
        2.toPx()
    )
    constraintSet.connect(
        R.id.labelDiscount,
        ConstraintSet.START,
        ConstraintSet.PARENT_ID,
        ConstraintSet.START,
        0.toPx()
    )

    constraintSet.connect(
        R.id.textViewSlashedPrice,
        ConstraintSet.TOP,
        R.id.labelDiscount,
        ConstraintSet.TOP,
        0.toPx()
    )
    constraintSet.connect(
        R.id.textViewSlashedPrice,
        ConstraintSet.START,
        R.id.labelDiscount,
        ConstraintSet.END,
        4.toPx()
    )
    constraintSet.setGoneMargin(R.id.textViewSlashedPrice, ConstraintSet.START, 0)
}

private fun splitShippingSection(constraintSet: ConstraintSet) {
    constraintSet.connect(
        R.id.textViewShipping,
        ConstraintSet.TOP,
        R.id.imageFreeOngkirPromo,
        ConstraintSet.BOTTOM,
        4.toPx()
    )
    constraintSet.connect(
        R.id.textViewShipping,
        ConstraintSet.START,
        ConstraintSet.PARENT_ID,
        ConstraintSet.START,
        0
    )

    constraintSet.connect(
        R.id.textViewETA,
        ConstraintSet.TOP,
        R.id.textViewShipping,
        ConstraintSet.BOTTOM,
        4.toPx()
    )
    constraintSet.connect(
        R.id.textViewETA,
        ConstraintSet.START,
        ConstraintSet.PARENT_ID,
        ConstraintSet.START,
        0
    )
}
