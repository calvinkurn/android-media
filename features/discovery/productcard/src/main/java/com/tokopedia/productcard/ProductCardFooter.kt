package com.tokopedia.productcard

import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.productcard.utils.findViewByIdInViewStub
import com.tokopedia.unifycomponents.UnifyButton

internal fun View.renderProductCardFooter(
    productCardModel: ProductCardModel,
    isProductCardList: Boolean,
) {
    val buttonRemoveFromWishlist = findViewById<FrameLayout?>(R.id.buttonRemoveFromWishlist)
    renderNotifyButton(productCardModel)

    if (isProductCardList) {
        renderDeleteProductButton(productCardModel)
        buttonRemoveFromWishlist?.showWithCondition(productCardModel.hasRemoveFromWishlistButton)
        val buttonSimilarProduct = findViewById<UnifyButton?>(R.id.buttonSeeSimilarProduct)
        buttonSimilarProduct?.hide()
    } else {
        val buttonDeleteProduct = findViewById<UnifyButton?>(R.id.buttonDeleteProduct)
        buttonDeleteProduct?.hide()
        buttonRemoveFromWishlist?.hide()
        renderSimilarProductButton(productCardModel)
    }
    renderWishlistComponents(productCardModel)
}

private fun View.renderNotifyButton(productCardModel: ProductCardModel) {
    val buttonNotify = findViewByIdInViewStub<UnifyButton?>(R.id.buttonNotifyStub, R.id.buttonNotify)
    buttonNotify?.showWithCondition(productCardModel.hasNotifyMeButton)
}

private fun View.renderDeleteProductButton(productCardModel: ProductCardModel) {
    val buttonDeleteProduct = findViewByIdInViewStub<UnifyButton?>(R.id.buttonDeleteProductStub, R.id.buttonDeleteProduct)
    buttonDeleteProduct?.showWithCondition(productCardModel.hasDeleteProductButton)
}

private fun View.renderSimilarProductButton(productCardModel: ProductCardModel) {
    val buttonSimilarProduct = findViewByIdInViewStub<UnifyButton?>(R.id.buttonSeeSimilarProductStub, R.id.buttonSeeSimilarProduct)
    buttonSimilarProduct?.showWithCondition(productCardModel.hasSimilarProductButton)
}

private fun View.renderWishlistComponents(productCardModel: ProductCardModel) {
    val buttonThreeDotsWishlist = findViewById<FrameLayout?>(R.id.buttonThreeDotsWishlist)
    val rlPrimaryButtonWishlist = findViewById<RelativeLayout?>(R.id.rlPrimaryButtonWishlist)
    buttonThreeDotsWishlist?.showWithCondition(productCardModel.hasButtonThreeDotsWishlist)
    rlPrimaryButtonWishlist?.showWithCondition(productCardModel.willShowPrimaryButtonWishlist())

    val buttonAddToCartWishlist = findViewByIdInViewStub<UnifyButton?>(R.id.buttonAddToCartWishlistStub, R.id.buttonAddToCartWishlist)
    buttonAddToCartWishlist?.showWithCondition(productCardModel.hasAddToCartWishlist)

    val buttonSeeSimilarProductWishlist = findViewByIdInViewStub<UnifyButton?>(R.id.buttonSeeSimilarProductWishlistStub, R.id.buttonSeeSimilarProductWishlist)
    buttonSeeSimilarProductWishlist?.showWithCondition(productCardModel.hasSimilarProductWishlist)
}