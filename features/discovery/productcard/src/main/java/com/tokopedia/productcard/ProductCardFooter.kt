package com.tokopedia.productcard

import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.productcard.utils.findViewById
import com.tokopedia.productcard.utils.ViewId
import com.tokopedia.productcard.utils.ViewStubId
import com.tokopedia.productcard.utils.showWithCondition
import com.tokopedia.unifycomponents.UnifyButton

internal fun View.renderProductCardFooter(
    productCardModel: ProductCardModel,
    isProductCardList: Boolean,
) {
    val buttonRemoveFromWishlist = findViewById<FrameLayout?>(R.id.buttonRemoveFromWishlist)
    showWithCondition<UnifyButton?>(
        ViewStubId(R.id.buttonNotifyStub),
        ViewId(R.id.buttonNotify),
        productCardModel.hasNotifyMeButton
    )

    if (isProductCardList) {
        val buttonSimilarProduct = findViewById<UnifyButton?>(R.id.buttonSeeSimilarProduct)
        showWithCondition<UnifyButton?>(
            ViewStubId(R.id.buttonDeleteProductStub),
            ViewId(R.id.buttonDeleteProduct),
            productCardModel.hasDeleteProductButton
        )
        buttonRemoveFromWishlist?.showWithCondition(productCardModel.hasRemoveFromWishlistButton)
        buttonSimilarProduct?.hide()
    } else {
        val buttonDeleteProduct = findViewById<UnifyButton?>(R.id.buttonDeleteProduct)
        buttonDeleteProduct?.hide()
        buttonRemoveFromWishlist?.hide()
        renderSimilarProductButton(productCardModel)
    }

    renderWishlistComponents(productCardModel)

    val buttonSeeOtherProduct = findViewById<UnifyButton?>(
        ViewStubId(R.id.buttonSeeOtherProductStub),
        ViewId(R.id.buttonSeeOtherProduct),
    )
    buttonSeeOtherProduct?.shouldShowWithAction(productCardModel.willShowButtonSeeOtherProduct()) {
        buttonSeeOtherProduct.text = productCardModel.seeOtherProductText
    }
}

private fun View.renderSimilarProductButton(productCardModel: ProductCardModel) {
    showWithCondition<UnifyButton?>(
        ViewStubId(R.id.buttonSeeSimilarProductStub),
        ViewId(R.id.buttonSeeSimilarProduct),
        productCardModel.hasSimilarProductButton)
}

private fun View.renderWishlistComponents(productCardModel: ProductCardModel) {
    val buttonThreeDotsWishlist = findViewById<FrameLayout?>(R.id.buttonThreeDotsWishlist)
    val rlPrimaryButtonWishlist = findViewById<RelativeLayout?>(R.id.rlPrimaryButtonWishlist)
    buttonThreeDotsWishlist?.showWithCondition(productCardModel.hasButtonThreeDotsWishlist)
    rlPrimaryButtonWishlist?.showWithCondition(productCardModel.willShowPrimaryButtonWishlist())
    showWithCondition<UnifyButton?>(
        ViewStubId(R.id.buttonAddToCartWishlistStub),
        ViewId(R.id.buttonAddToCartWishlist),
        productCardModel.hasAddToCartWishlist)
    showWithCondition<UnifyButton?>(
        ViewStubId(R.id.buttonSeeSimilarProductWishlistStub),
        ViewId(R.id.buttonSeeSimilarProductWishlist),
        productCardModel.hasSimilarProductWishlist)

}
