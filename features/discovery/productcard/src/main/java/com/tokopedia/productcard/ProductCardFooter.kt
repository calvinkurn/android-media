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
}

private fun View.renderSimilarProductButton(productCardModel: ProductCardModel) {
    val buttonSimilarProduct = findViewById<UnifyButton?>(R.id.buttonSeeSimilarProduct)
    buttonSimilarProduct?.showWithCondition(productCardModel.hasSimilarProductButton)
}
