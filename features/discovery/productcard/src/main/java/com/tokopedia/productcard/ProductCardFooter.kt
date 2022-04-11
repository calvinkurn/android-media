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
    val buttonNotifyStub = findViewById<ViewStub?>(R.id.buttonNotifyStub)
    if (productCardModel.hasNotifyMeButton) {
        buttonNotifyStub?.inflate()
    }
    val buttonNotify = findViewById<UnifyButton?>(R.id.buttonNotify)
    buttonNotify?.showWithCondition(productCardModel.hasNotifyMeButton)
}

private fun View.renderDeleteProductButton(productCardModel: ProductCardModel) {
    val buttonDeleteProductStub = findViewById<ViewStub?>(R.id.buttonDeleteProductStub)
    if (productCardModel.hasDeleteProductButton) {
        buttonDeleteProductStub?.inflate()
    }
    val buttonDeleteProduct = findViewById<UnifyButton?>(R.id.buttonDeleteProduct)
    buttonDeleteProduct?.showWithCondition(productCardModel.hasDeleteProductButton)
}

private fun View.renderSimilarProductButton(productCardModel: ProductCardModel) {
    val buttonSimilarProductStub = findViewById<ViewStub?>(R.id.buttonSeeSimilarProductStub)
    if (productCardModel.hasSimilarProductButton) {
        buttonSimilarProductStub?.inflate()
    }
    val buttonSimilarProduct = findViewById<UnifyButton?>(R.id.buttonSeeSimilarProduct)
    buttonSimilarProduct?.showWithCondition(productCardModel.hasSimilarProductButton)
}

private fun View.renderWishlistComponents(productCardModel: ProductCardModel) {
    val buttonAddToCartWishlistStub = findViewById<ViewStub?>(R.id.buttonAddToCartWishlistStub)
    val buttonThreeDotsWishlist = findViewById<FrameLayout?>(R.id.buttonThreeDotsWishlist)
    val rlPrimaryButtonWishlist = findViewById<RelativeLayout?>(R.id.rlPrimaryButtonWishlist)
    val buttonSeeSimilarProductWishlistStub = findViewById<ViewStub?>(R.id.buttonSeeSimilarProductWishlistStub)
    buttonThreeDotsWishlist?.showWithCondition(productCardModel.hasButtonThreeDotsWishlist)
    rlPrimaryButtonWishlist?.showWithCondition(productCardModel.willShowPrimaryButtonWishlist())

    if (productCardModel.hasAddToCartWishlist) {
        buttonAddToCartWishlistStub?.inflate()
    }
    val buttonAddToCartWishlist = findViewById<UnifyButton?>(R.id.buttonAddToCartWishlist)
    buttonAddToCartWishlist?.showWithCondition(productCardModel.hasAddToCartWishlist)

    if (productCardModel.hasSimilarProductWishlist) {
        buttonSeeSimilarProductWishlistStub?.inflate()
    }
    val buttonSeeSimilarProductWishlist = findViewById<UnifyButton?>(R.id.buttonSeeSimilarProductWishlist)
    buttonSeeSimilarProductWishlist?.showWithCondition(productCardModel.hasSimilarProductWishlist)
}