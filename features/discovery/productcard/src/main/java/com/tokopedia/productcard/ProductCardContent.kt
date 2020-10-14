package com.tokopedia.productcard

import android.graphics.Paint
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.productcard.utils.*
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.getTypeface
import kotlinx.android.synthetic.main.product_card_content_layout.view.*


internal fun View.renderProductCardContent(productCardModel: ProductCardModel) {
    renderTextGimmick(productCardModel)
    renderPdpCountView(productCardModel)
    renderTextProductName(productCardModel)
    renderDiscount(productCardModel)
    renderLabelPrice(productCardModel)
    renderTextPrice(productCardModel)
    renderShopBadge(productCardModel)
    renderTextShopLocation(productCardModel)
    renderRating(productCardModel)
    renderTextReview(productCardModel)
    renderTextCredibility(productCardModel)
    renderShopRating(productCardModel)
    renderFreeOngkir(productCardModel)
    renderTextShipping(productCardModel)
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
        it.contentDescription = context.getString(R.string.content_desc_textViewProductName, productNameFromHtml)
        it.text = productNameFromHtml
    }
}

private fun View.renderDiscount(productCardModel: ProductCardModel) {
    labelDiscount?.shouldShowWithAction(productCardModel.discountPercentage.isNotEmpty()) {
        it.text = productCardModel.discountPercentage
    }

    textViewSlashedPrice?.shouldShowWithAction(productCardModel.slashedPrice.isNotEmpty()) {
        it.contentDescription = context.getString(R.string.content_desc_textViewSlashedPrice, productCardModel.slashedPrice)
        it.text = productCardModel.slashedPrice
        it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }
}

private fun View.renderLabelPrice(productCardModel: ProductCardModel) {
    if (productCardModel.isShowDiscountOrSlashPrice())
        labelPrice?.initLabelGroup(null)
    else
        labelPrice?.initLabelGroup(productCardModel.getLabelPrice())
}

private fun View.renderTextPrice(productCardModel: ProductCardModel) {
    val priceToRender = productCardModel.getPriceToRender()

    textViewPrice?.shouldShowWithAction(priceToRender.isNotEmpty()) {
        it.contentDescription  = context.getString(R.string.content_desc_textViewPrice, priceToRender)
        it.text = priceToRender
    }
}

private fun ProductCardModel.getPriceToRender(): String {
    return if (priceRange.isNotEmpty()) priceRange else formattedPrice
}

private fun View.renderShopBadge(productCardModel: ProductCardModel) {
    val shopBadge = productCardModel.shopBadgeList.find { it.isShown && it.imageUrl.isNotEmpty() }
    imageShopBadge?.shouldShowWithAction(productCardModel.isShowShopBadge()) {
        it.loadIcon(shopBadge?.imageUrl ?: "")
    }
}

private fun View.renderTextShopLocation(productCardModel: ProductCardModel) {
    textViewShopLocation?.shouldShowWithAction(productCardModel.shopLocation.isNotEmpty()) {
        it.contentDescription = context.getString(R.string.content_desc_textViewShopLocation, productCardModel.shopLocation)
        it.text = productCardModel.shopLocation
    }
}

private fun View.renderRating(productCardModel: ProductCardModel) {
    when {
        !productCardModel.willShowRatingAndReviewCount() -> hideRating()
        productCardModel.ratingString.isNotEmpty() -> renderRatingFloat(productCardModel)
        productCardModel.ratingCount > 0 -> renderRatingStars(productCardModel)
    }
}

private fun View.hideRating() {
    imageRatingString?.gone()
    textViewRatingString?.gone()
    linearLayoutImageRating?.gone()
}

private fun View.renderRatingFloat(productCardModel: ProductCardModel) {
    imageRatingString?.visible()
    textViewRatingString?.visible()
    textViewRatingString?.text = productCardModel.ratingString

    linearLayoutImageRating?.gone()
}

private fun View.renderRatingStars(productCardModel: ProductCardModel) {
    imageRatingString?.gone()
    textViewRatingString?.gone()

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
    if (productCardModel.willShowRatingAndReviewCount())
        textViewIntegrity?.initLabelGroup(null)
    else
        textViewIntegrity?.initLabelGroup(productCardModel.getLabelIntegrity())
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

        val charcoalGrey44 = ContextCompat.getColor(this.context, R.color.charcoal_grey_44)
        val charcoalGrey68 = ContextCompat.getColor(this.context, R.color.charcoal_grey_68)

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


