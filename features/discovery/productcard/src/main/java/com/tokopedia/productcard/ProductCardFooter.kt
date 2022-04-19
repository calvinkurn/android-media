package com.tokopedia.productcard

import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.UnifyButton

internal fun View.renderProductCardFooter(
    productCardModel: ProductCardModel,
    isProductCardList: Boolean,
) {
    val buttonNotify = findViewById<UnifyButton?>(R.id.buttonNotify)
    val buttonDeleteProduct = findViewById<UnifyButton?>(R.id.buttonDeleteProduct)
    val buttonRemoveFromWishlist = findViewById<FrameLayout?>(R.id.buttonRemoveFromWishlist)
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
    val buttonThreeDotsWishlist = findViewById<FrameLayout?>(R.id.buttonThreeDotsWishlist)
    val rlPrimaryButtonWishlist = findViewById<RelativeLayout?>(R.id.rlPrimaryButtonWishlist)
    val buttonAddToCartWishlist = findViewById<UnifyButton?>(R.id.buttonAddToCartWishlist)
    val buttonSeeSimilarProductWishlist = findViewById<UnifyButton?>(R.id.buttonSeeSimilarProductWishlist)
    buttonThreeDotsWishlist?.showWithCondition(productCardModel.hasButtonThreeDotsWishlist)
    rlPrimaryButtonWishlist?.showWithCondition(productCardModel.willShowPrimaryButtonWishlist())
    buttonAddToCartWishlist?.showWithCondition(productCardModel.hasAddToCartWishlist)
    buttonSeeSimilarProductWishlist?.showWithCondition(productCardModel.hasSimilarProductWishlist)
}