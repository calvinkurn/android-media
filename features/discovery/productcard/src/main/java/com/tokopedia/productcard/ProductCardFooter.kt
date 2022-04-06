package com.tokopedia.productcard

import android.view.View
import android.view.ViewStub
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.UnifyButton

internal fun View.renderProductCardFooter(
    productCardModel: ProductCardModel,
    isProductCardList: Boolean,
) {
    val buttonDeleteProduct = findViewById<UnifyButton?>(R.id.buttonDeleteProduct)
    val buttonRemoveFromWishlist = findViewById<FrameLayout?>(R.id.buttonRemoveFromWishlist)
    renderNotifyButton(productCardModel)

    if (isProductCardList) {
        val buttonSimilarProduct = findViewById<UnifyButton?>(R.id.buttonSeeSimilarProduct)
        renderDeleteProductButton(productCardModel)
        buttonRemoveFromWishlist?.showWithCondition(productCardModel.hasRemoveFromWishlistButton)
        buttonSimilarProduct?.hide()
    } else {
        buttonDeleteProduct?.hide()
        buttonRemoveFromWishlist?.hide()
        renderSimilarProductButton(productCardModel)
    }
    renderWishlistComponents(productCardModel)
}

private fun View.renderNotifyButton(productCardModel: ProductCardModel) {
    val buttonNotifyStub = findViewById<ViewStub?>(R.id.buttonNotifyStub)
    val buttonNotify = findViewById<UnifyButton?>(R.id.buttonNotify)
    if (productCardModel.hasNotifyMeButton) {
        buttonNotifyStub?.inflate()
    }
    buttonNotify?.showWithCondition(productCardModel.hasNotifyMeButton)
}

private fun View.renderDeleteProductButton(productCardModel: ProductCardModel) {
    val buttonDeleteProductStub = findViewById<ViewStub?>(R.id.buttonDeleteProductStub)
    val buttonDeleteProduct = findViewById<UnifyButton?>(R.id.buttonDeleteProduct)
    if (productCardModel.hasDeleteProductButton) {
        buttonDeleteProductStub?.inflate()
    }
    buttonDeleteProduct?.showWithCondition(productCardModel.hasDeleteProductButton)
}

private fun View.renderSimilarProductButton(productCardModel: ProductCardModel) {
    val buttonSimilarProductStub = findViewById<ViewStub?>(R.id.buttonSeeSimilarProductStub)
    val buttonSimilarProduct = findViewById<UnifyButton?>(R.id.buttonSeeSimilarProduct)
    if (productCardModel.hasSimilarProductButton) {
        buttonSimilarProductStub?.inflate()
    }
    buttonSimilarProduct?.showWithCondition(productCardModel.hasSimilarProductButton)
}

private fun View.renderWishlistComponents(productCardModel: ProductCardModel) {
    val buttonAddToCartWishlistStub = findViewById<ViewStub?>(R.id.buttonAddToCartWishlistStub)
    val buttonThreeDotsWishlist = findViewById<FrameLayout?>(R.id.buttonThreeDotsWishlist)
    val rlPrimaryButtonWishlist = findViewById<RelativeLayout?>(R.id.rlPrimaryButtonWishlist)
    val buttonAddToCartWishlist = findViewById<UnifyButton?>(R.id.buttonAddToCartWishlist)
    val buttonSeeSimilarProductWishlistStub = findViewById<ViewStub?>(R.id.buttonSeeSimilarProductWishlistStub)
    val buttonSeeSimilarProductWishlist = findViewById<UnifyButton?>(R.id.buttonSeeSimilarProductWishlist)
    buttonThreeDotsWishlist?.showWithCondition(productCardModel.hasButtonThreeDotsWishlist)
    rlPrimaryButtonWishlist?.showWithCondition(productCardModel.willShowPrimaryButtonWishlist())
    if (productCardModel.hasAddToCartWishlist) {
        buttonAddToCartWishlistStub?.inflate()
    }
    buttonAddToCartWishlist?.showWithCondition(productCardModel.hasAddToCartWishlist)
    if (productCardModel.hasSimilarProductWishlist) {
        buttonSeeSimilarProductWishlistStub?.inflate()
    }
    buttonSeeSimilarProductWishlist?.showWithCondition(productCardModel.hasSimilarProductWishlist)
}