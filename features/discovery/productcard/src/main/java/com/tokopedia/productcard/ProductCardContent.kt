package com.tokopedia.productcard

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.productcard.utils.*
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.getTypeface
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil
import kotlinx.android.synthetic.main.product_card_content_layout.view.*

internal fun View.renderProductCardContent(productCardModel: ProductCardModel) {
    renderTextGimmick(productCardModel)
    renderPdpCountView(productCardModel)
    renderTextProductName(productCardModel)
    renderLabelGroupVariant(productCardModel)
    renderDiscount(productCardModel)
    renderLabelPrice(productCardModel)
    renderTextPrice(productCardModel)
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
}


private fun View.renderTextGimmick(productCardModel: ProductCardModel) {
    if (productCardModel.isShowLabelGimmick())
        textViewGimmick?.initLabelGroup(productCardModel.getLabelGimmick())
    else
        textViewGimmick?.initLabelGroup(null)
}

private fun View.renderPdpCountView(productCardModel: ProductCardModel) {
    imageViewPdpView?.hide()
    textViewPdpView?.shouldShowWithAction(productCardModel.pdpViewCount.isNotEmpty()) {
        it.text = MethodChecker.fromHtml(productCardModel.pdpViewCount)
        imageViewPdpView.show()
    }
}

private fun View.renderTextProductName(productCardModel: ProductCardModel) {
    textViewProductName?.shouldShowWithAction(productCardModel.productName.isNotEmpty()) {
        val productNameFromHtml = MethodChecker.fromHtml(productCardModel.productName)
        TextAndContentDescriptionUtil.setTextAndContentDescription(it, productNameFromHtml.toString(), context.getString(R.string.content_desc_textViewProductName))
    }
}

private fun View.renderLabelGroupVariant(productCardModel: ProductCardModel) {
    val willShowVariant = productCardModel.willShowVariant()

    if (willShowVariant) {
        textViewProductName?.isSingleLine = true
    }
    else {
        textViewProductName?.isSingleLine = false
        textViewProductName?.maxLines = 2
        textViewProductName?.ellipsize = TextUtils.TruncateAt.END
    }

    labelVariantContainer?.shouldShowWithAction(willShowVariant) { labelVariantContainer ->
        labelVariantContainer.removeAllViews()

        val marginStart = 4.toPx()
        val colorSampleSize = 14.toPx()

        productCardModel.getRenderedLabelGroupVariantList().forEachIndexed { index, labelVariant ->
            val hasMarginStart = index > 0

            when {
                labelVariant.isColor() -> {
                    labelVariantContainer.addLabelVariantColor(labelVariant, hasMarginStart, colorSampleSize, marginStart)
                }
                labelVariant.isSize() -> {
                    labelVariantContainer.addLabelVariantSize(labelVariant, hasMarginStart, marginStart)
                }
                labelVariant.isCustom() -> {
                    labelVariantContainer.addLabelVariantCustom(labelVariant, marginStart)
                }
            }
        }
    }
}

private fun LinearLayout.addLabelVariantColor(
        labelVariant: ProductCardModel.LabelGroupVariant,
        hasMarginStart: Boolean,
        colorSampleSize: Int,
        marginStart: Int
) {
    val gradientDrawable = createColorSampleDrawable(context, labelVariant.hexColor)

    val layoutParams = LinearLayout.LayoutParams(colorSampleSize, colorSampleSize)
    layoutParams.marginStart = if (hasMarginStart) marginStart else 0

    val colorSampleImageView = ImageView(context)
    colorSampleImageView.setImageDrawable(gradientDrawable)
    colorSampleImageView.layoutParams = layoutParams
    colorSampleImageView.tag = LABEL_VARIANT_TAG

    addView(colorSampleImageView)
}

internal fun createColorSampleDrawable(context: Context, colorString: String): GradientDrawable {
    val gradientDrawable = GradientDrawable()

    gradientDrawable.shape = GradientDrawable.OVAL
    gradientDrawable.cornerRadii = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
    gradientDrawable.setStroke(2, ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N100))
    gradientDrawable.setColor(safeParseColor(colorString))

    return gradientDrawable
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

private fun LinearLayout.addLabelVariantSize(
        labelVariant: ProductCardModel.LabelGroupVariant,
        hasMarginStart: Boolean,
        marginStart: Int
) {
    val layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    layoutParams.marginStart = if (hasMarginStart) marginStart else 0

    val unifyLabel = Label(context)
    unifyLabel.setLabelType(labelVariant.type.toUnifyLabelType())
    unifyLabel.text = labelVariant.title
    unifyLabel.layoutParams = layoutParams
    unifyLabel.tag = LABEL_VARIANT_TAG

    addView(unifyLabel)
}

private fun LinearLayout.addLabelVariantCustom(labelVariant: ProductCardModel.LabelGroupVariant, marginStart: Int) {
    val layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    layoutParams.topMargin = 1.toPx() // Small hack to make custom label center
    layoutParams.marginStart = marginStart

    val typography = Typography(context)
    typography.weightType = Typography.BOLD
    typography.setType(Typography.SMALL)
    typography.text = "+${labelVariant.title}"
    typography.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
    typography.layoutParams = layoutParams
    typography.tag = LABEL_VARIANT_TAG

    addView(typography)
}

private fun View.renderDiscount(productCardModel: ProductCardModel) {
    labelDiscount?.shouldShowWithAction(productCardModel.discountPercentage.isNotEmpty()) {
        TextAndContentDescriptionUtil.setTextAndContentDescription(it, productCardModel.discountPercentage, context.getString(R.string.content_desc_labelDiscount))
    }

    textViewSlashedPrice?.shouldShowWithAction(productCardModel.slashedPrice.isNotEmpty()) {
        TextAndContentDescriptionUtil.setTextAndContentDescription(it, productCardModel.slashedPrice, context.getString(R.string.content_desc_textViewSlashedPrice))
        it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }
}

private fun View.renderLabelPrice(productCardModel: ProductCardModel) {
    moveLabelPriceConstraint(productCardModel)

    if (productCardModel.isShowDiscountOrSlashPrice())
        labelPrice?.initLabelGroup(null)
    else
        labelPrice?.initLabelGroup(productCardModel.getLabelPrice())
}

private fun View.moveLabelPriceConstraint(productCardModel: ProductCardModel) {
    val targetConstraint = if (productCardModel.discountPercentage.isNotEmpty()) R.id.labelDiscount else R.id.textViewSlashedPrice
    val view = findViewById<ConstraintLayout?>(R.id.productCardContentLayout)

    view?.let {
        val constraintSet = ConstraintSet().apply { clone(it) }

        constraintSet.clear(R.id.labelPrice, ConstraintSet.TOP)
        constraintSet.connect(R.id.labelPrice, ConstraintSet.TOP, targetConstraint, ConstraintSet.BOTTOM, 4.toPx())

        constraintSet.applyTo(it)
    }
}

private fun View.renderTextPrice(productCardModel: ProductCardModel) {
    val priceToRender = productCardModel.getPriceToRender()

    textViewPrice?.shouldShowWithAction(priceToRender.isNotEmpty()) {
        TextAndContentDescriptionUtil.setTextAndContentDescription(it, priceToRender, context.getString(R.string.content_desc_textViewPrice))
    }
}

private fun ProductCardModel.getPriceToRender(): String {
    return if (priceRange.isNotEmpty()) priceRange else formattedPrice
}

private fun View.renderTextFulfillment(productCardModel: ProductCardModel) {
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
    val shopBadge = productCardModel.shopBadgeList.find { it.isShown && it.imageUrl.isNotEmpty() }
    imageShopBadge?.shouldShowWithAction(productCardModel.isShowShopBadge()) {
        it.loadIcon(shopBadge?.imageUrl ?: "")
    }
}

private fun View.renderTextShopLocation(productCardModel: ProductCardModel) {
    textViewShopLocation?.shouldShowWithAction(productCardModel.shopLocation.isNotEmpty() && !productCardModel.willShowFulfillment()) {
        TextAndContentDescriptionUtil.setTextAndContentDescription(it, productCardModel.shopLocation, context.getString(R.string.content_desc_textViewShopLocation))
    }
}

private fun View.renderRating(productCardModel: ProductCardModel) {
    when {
        !productCardModel.willShowRatingAndReviewCount() -> hideRating()
        productCardModel.ratingCount > 0 -> renderRatingStars(productCardModel)
    }
}

private fun View.hideRating() {
    linearLayoutImageRating?.gone()
}

private fun View.renderRatingStars(productCardModel: ProductCardModel) {
    linearLayoutImageRating?.visible()
    setImageRating(productCardModel.ratingCount)
}

private fun View.setImageRating(rating: Int) {
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
    textViewReviewCount?.shouldShowWithAction(productCardModel.willShowRatingAndReviewCount()) {
        it.text = String.format(context.getString(R.string.product_card_review_count_format), productCardModel.reviewCount)
    }
}

private fun View.renderTextCredibility(productCardModel: ProductCardModel) {
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
    val willShowSalesRatingFloat = productCardModel.willShowRating()

    imageSalesRatingFloat?.showWithCondition(willShowSalesRatingFloat)

    salesRatingFloat?.shouldShowWithAction(willShowSalesRatingFloat) {
        it.text = productCardModel.countSoldRating
    }
}

private fun View.renderTextIntegrityWithSalesRatingFloat(productCardModel: ProductCardModel) {
    val willShowSalesAndRating = productCardModel.willShowSalesAndRating()

    salesRatingFloatLine?.showWithCondition(willShowSalesAndRating)

    textViewSales?.shouldShowWithAction(willShowSalesAndRating) {
        it.initLabelGroup(productCardModel.getLabelIntegrity())
    }
}

private fun View.renderShopRating(productCardModel: ProductCardModel) {
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

        val charcoalGrey44 = ContextCompat.getColor(this.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44)
        val charcoalGrey68 = ContextCompat.getColor(this.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68)

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
    imageFreeOngkirPromo?.shouldShowWithAction(productCardModel.isShowFreeOngkirBadge()) {
        it.loadIcon(productCardModel.freeOngkir.imageUrl)
    }
}

private fun View.renderTextShipping(productCardModel: ProductCardModel) {
    if (productCardModel.isShowFreeOngkirBadge())
        textViewShipping?.initLabelGroup(null)
    else
        textViewShipping?.initLabelGroup(productCardModel.getLabelShipping())
}

private fun View.renderTextETA(productCardModel: ProductCardModel) {
    textViewETA?.initLabelGroup(productCardModel.getLabelETA())
}

