package com.tokopedia.productcard

import android.view.View
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.product_card_add_to_cart_wishlist_button.view.*
import kotlinx.android.synthetic.main.product_card_delete_product_button.view.*
import kotlinx.android.synthetic.main.product_card_footer_layout.view.*
import kotlinx.android.synthetic.main.product_card_notify_button.view.*
import kotlinx.android.synthetic.main.product_card_see_similar_product_wishlist_button.view.*

internal fun View.renderProductCardFooter(
    productCardModel: ProductCardModel,
    isProductCardList: Boolean,
) {
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
    if (productCardModel.hasNotifyMeButton) {
        buttonNotifyStub?.inflate()
    }
    buttonNotify?.showWithCondition(productCardModel.hasNotifyMeButton)
}

private fun View.renderDeleteProductButton(productCardModel: ProductCardModel) {
    if (productCardModel.hasDeleteProductButton) {
        buttonDeleteProductStub?.inflate()
    }
    buttonDeleteProduct?.showWithCondition(productCardModel.hasDeleteProductButton)
}

private fun View.renderSimilarProductButton(productCardModel: ProductCardModel) {
    val buttonSimilarProduct = findViewById<UnifyButton?>(R.id.buttonSeeSimilarProduct)
    if (productCardModel.hasSimilarProductButton) {
        buttonSeeSimilarProductStub?.inflate()
    }
    buttonSimilarProduct?.showWithCondition(productCardModel.hasSimilarProductButton)
}

private fun View.renderWishlistComponents(productCardModel: ProductCardModel) {
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