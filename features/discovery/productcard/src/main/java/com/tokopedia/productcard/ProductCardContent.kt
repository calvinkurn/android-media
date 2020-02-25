package com.tokopedia.productcard

import android.graphics.Paint
import android.view.View
import androidx.annotation.DrawableRes
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.productcard.utils.initLabelGroup
import com.tokopedia.productcard.utils.loadIcon
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.productcard.v2.ProductCardModel
import kotlinx.android.synthetic.main.product_card_content_layout.view.*

internal fun View.renderProductCardContent(productCardModel: ProductCardModel) {
    renderTextGimmick(productCardModel)
    renderTextProductName(productCardModel)
    renderDiscount(productCardModel)
    renderLabelPrice(productCardModel)
    renderTextPrice(productCardModel)
    renderShopBadge(productCardModel)
    renderTextShopLocation(productCardModel)
    renderRating(productCardModel)
    renderTextReview(productCardModel)
    renderTextCredibility(productCardModel)
    renderFreeOngkir(productCardModel)
    renderTextShipping(productCardModel)
}

private fun View.renderTextGimmick(productCardModel: ProductCardModel) {
    textViewGimmick?.initLabelGroup(productCardModel.getLabelGimmick())
}

private fun View.renderTextProductName(productCardModel: ProductCardModel) {
    textViewProductName?.shouldShowWithAction(productCardModel.productName.isNotEmpty()) {
        it.text = MethodChecker.fromHtml(productCardModel.productName)
    }
}

private fun View.renderDiscount(productCardModel: ProductCardModel) {
    labelDiscount?.shouldShowWithAction(productCardModel.discountPercentage.isNotEmpty()) {
        it.text = productCardModel.discountPercentage
    }

    textViewSlashedPrice?.shouldShowWithAction(productCardModel.slashedPrice.isNotEmpty()) {
        it.text = productCardModel.slashedPrice
        it.paintFlags = it.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }
}

private fun View.renderLabelPrice(productCardModel: ProductCardModel) {
    labelPrice?.initLabelGroup(productCardModel.getLabelPrice())
}

private fun View.renderTextPrice(productCardModel: ProductCardModel) {
    val priceToRender = productCardModel.getPriceToRender()

    textViewPrice?.shouldShowWithAction(priceToRender.isNotEmpty()) {
        it.text = priceToRender
    }
}

private fun ProductCardModel.getPriceToRender(): String {
    return if (priceRange.isNotEmpty()) priceRange else formattedPrice
}

private fun View.renderShopBadge(productCardModel: ProductCardModel) {
    val shopBadge = productCardModel.shopBadgeList.find { it.isShown && it.imageUrl.isNotEmpty() }
    imageShopBadge?.shouldShowWithAction(shopBadge != null) {
        it.loadIcon(shopBadge?.imageUrl ?: "")
    }
}

private fun View.renderTextShopLocation(productCardModel: ProductCardModel) {
    textViewShopLocation?.shouldShowWithAction(productCardModel.shopLocation.isNotEmpty()) {
        it.text = productCardModel.shopLocation
    }
}

private fun View.renderRating(productCardModel: ProductCardModel) {
    when {
        productCardModel.ratingString.isNotEmpty() -> renderRatingFloat(productCardModel)
        productCardModel.ratingCount > 0 -> renderRatingStars(productCardModel)
        else -> hideRating()
    }
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

private fun View.hideRating() {
    imageRatingString?.gone()
    textViewRatingString?.gone()
    linearLayoutImageRating?.gone()
}

private fun View.renderTextReview(productCardModel: ProductCardModel) {
    textViewReviewCount?.shouldShowWithAction(productCardModel.reviewCount > 0) {
        it.text = String.format(context.getString(R.string.product_card_review_count_format), productCardModel.reviewCount)
    }
}

private fun View.renderTextCredibility(productCardModel: ProductCardModel) {
    textViewIntegrity?.initLabelGroup(productCardModel.getLabelIntegrity())
}

private fun View.renderFreeOngkir(productCardModel: ProductCardModel) {
    imageFreeOngkirPromo?.shouldShowWithAction(productCardModel.freeOngkir.isActive) {
        it.loadIcon(productCardModel.freeOngkir.imageUrl)
    }
}

private fun View.renderTextShipping(productCardModel: ProductCardModel) {
    textViewShipping?.initLabelGroup(productCardModel.getLabelShipping())
}