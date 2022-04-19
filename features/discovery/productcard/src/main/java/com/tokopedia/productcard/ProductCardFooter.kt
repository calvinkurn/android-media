package com.tokopedia.productcard

import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.productcard.utils.showViewInViewStubWithConditional
import com.tokopedia.unifycomponents.UnifyButton

internal fun View.renderProductCardFooter(
    productCardModel: ProductCardModel,
    isProductCardList: Boolean,
) {
    val buttonRemoveFromWishlist = findViewById<FrameLayout?>(R.id.buttonRemoveFromWishlist)
    showViewInViewStubWithConditional<UnifyButton?>(R.id.buttonNotifyStub, R.id.buttonNotify, productCardModel.hasNotifyMeButton)

    if (isProductCardList) {
        val buttonSimilarProduct = findViewById<UnifyButton?>(R.id.buttonSeeSimilarProduct)
        showViewInViewStubWithConditional<UnifyButton?>(R.id.buttonDeleteProductStub, R.id.buttonDeleteProduct, productCardModel.hasDeleteProductButton)
        buttonRemoveFromWishlist?.showWithCondition(productCardModel.hasRemoveFromWishlistButton)

        buttonSimilarProduct?.hide()
    } else {
        val buttonDeleteProduct = findViewById<UnifyButton?>(R.id.buttonDeleteProduct)
        buttonDeleteProduct?.hide()
        buttonRemoveFromWishlist?.hide()
        renderSimilarProductButton(productCardModel)
    }
    renderWishlistComponents(productCardModel)
}

private fun View.renderSimilarProductButton(productCardModel: ProductCardModel) {
    showViewInViewStubWithConditional<UnifyButton?>(R.id.buttonSeeSimilarProductStub, R.id.buttonSeeSimilarProduct, productCardModel.hasSimilarProductButton)
}

private fun View.renderWishlistComponents(productCardModel: ProductCardModel) {
    val buttonThreeDotsWishlist = findViewById<FrameLayout?>(R.id.buttonThreeDotsWishlist)
    val rlPrimaryButtonWishlist = findViewById<RelativeLayout?>(R.id.rlPrimaryButtonWishlist)
    buttonThreeDotsWishlist?.showWithCondition(productCardModel.hasButtonThreeDotsWishlist)
    rlPrimaryButtonWishlist?.showWithCondition(productCardModel.willShowPrimaryButtonWishlist())
    showViewInViewStubWithConditional<UnifyButton?>(R.id.buttonAddToCartWishlistStub, R.id.buttonAddToCartWishlist, productCardModel.hasAddToCartWishlist)
    showViewInViewStubWithConditional<UnifyButton?>(R.id.buttonSeeSimilarProductWishlistStub, R.id.buttonSeeSimilarProductWishlist, productCardModel.hasSimilarProductWishlist)

}