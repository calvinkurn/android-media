package com.tokopedia.productcard

import android.view.View
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.product_card_footer_layout.view.*

internal fun View.renderProductCardFooter(
    productCardModel: ProductCardModel,
    isProductCardList: Boolean,
) {
    buttonNotify?.showWithCondition(productCardModel.hasNotifyMeButton)

    if (isProductCardList) {
        val buttonSimilarProduct = findViewById<UnifyButton?>(R.id.buttonSeeSimilarProduct)
        buttonDeleteProduct?.showWithCondition(productCardModel.hasDeleteProductButton)
        buttonRemoveFromWishlist?.showWithCondition(productCardModel.hasRemoveFromWishlistButton)
        buttonSimilarProduct?.hide()
    } else {
        buttonDeleteProduct?.hide()
        buttonRemoveFromWishlist?.hide()
        renderSimilarProductButton(productCardModel)
    }
    renderWishlistComponents(productCardModel)
}

private fun View.renderSimilarProductButton(productCardModel: ProductCardModel) {
    val buttonSimilarProduct = findViewById<UnifyButton?>(R.id.buttonSeeSimilarProduct)
    buttonSimilarProduct?.showWithCondition(productCardModel.hasSimilarProductButton)
}

private fun View.renderWishlistComponents(productCardModel: ProductCardModel) {
    buttonThreeDotsWishlist?.showWithCondition(productCardModel.hasButtonThreeDotsWishlist)
    rlPrimaryButtonWishlist?.showWithCondition(productCardModel.willShowPrimaryButtonWishlist())
    buttonAddToCartWishlist?.showWithCondition(productCardModel.hasAddToCartWishlist)
    buttonSeeSimilarProductWishlist?.showWithCondition(productCardModel.hasSimilarProductWishlist)
}