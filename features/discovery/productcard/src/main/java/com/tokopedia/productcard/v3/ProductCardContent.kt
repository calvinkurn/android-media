package com.tokopedia.productcard.v3

import android.view.View
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.productcard.utils.init
import com.tokopedia.productcard.utils.loadIcon
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.productcard.v2.ProductCardModel
import kotlinx.android.synthetic.main.product_card_content_layout.view.*

internal fun View.renderProductCardContent(productCardModel: ProductCardModel) {
    textViewGimmick?.init(productCardModel.getTextGimmick())

    textViewProductName?.shouldShowWithAction(productCardModel.productName.isNotEmpty()) {
        it.text = productCardModel.productName
    }

    labelPrice?.init(productCardModel.getLabelPrice())

    labelDiscount?.shouldShowWithAction(productCardModel.discountPercentage.isNotEmpty()) {
        it.text = "${productCardModel.discountPercentage}%"
    }

    textViewSlashedPrice?.shouldShowWithAction(productCardModel.slashedPrice.isNotEmpty()) {
        it.text = productCardModel.slashedPrice
    }

    textViewPrice?.shouldShowWithAction(productCardModel.formattedPrice.isNotEmpty()) {
        it.text = productCardModel.formattedPrice
    }

    imageShopBadge?.shouldShowWithAction(productCardModel.shopBadgeList.getOrNull(0)?.imageUrl?.isNotEmpty() == true) {
        it.loadIcon(productCardModel.shopBadgeList.getOrNull(0)?.imageUrl ?: "")
    }

    textViewShopLocation?.shouldShowWithAction(productCardModel.shopLocation.isNotEmpty()) {
        it.text = productCardModel.shopLocation
    }

    imageRatingString?.showWithCondition(productCardModel.ratingString.isNotEmpty())
    textViewRatingString?.shouldShowWithAction(productCardModel.ratingString.isNotEmpty()) {
        it.text = productCardModel.ratingString
    }

    textViewReviewCount?.shouldShowWithAction(productCardModel.reviewCount > 0) {
        it.text = "(${productCardModel.reviewCount})"
    }

    imageFreeOngkirPromo?.shouldShowWithAction(productCardModel.freeOngkir.isActive) {
        it.loadIcon(productCardModel.freeOngkir.imageUrl)
    }
}