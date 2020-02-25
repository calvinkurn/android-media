package com.tokopedia.productcard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.bumptech.glide.Glide
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.utils.initLabelGroup
import com.tokopedia.productcard.utils.loadImageRounded
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.product_card_content_layout.view.*
import kotlinx.android.synthetic.main.product_card_list_layout.view.*

class ProductCardListView: BaseCustomView {

    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.product_card_list_layout, this)
    }

    fun setProductModel(productCardModel: ProductCardModel) {
        imageProduct?.loadImageRounded(productCardModel.productImageUrl)

        labelProductStatus?.initLabelGroup(productCardModel.getLabelProductStatus())

        textTopAds?.showWithCondition(productCardModel.isTopAds)

        renderProductCardContent(productCardModel)

        imageThreeDots?.showWithCondition(productCardModel.hasOptions)

        buttonRemoveFromWishlist?.showWithCondition(productCardModel.hasRemoveFromWishlistButton)

        buttonAddToCart?.showWithCondition(productCardModel.hasAddToCartButton)
    }

    fun setImageProductViewHintListener(impressHolder: ImpressHolder, viewHintListener: ViewHintListener) {
        imageProduct?.addOnImpressionListener(impressHolder, viewHintListener)
    }

    fun setThreeDotsOnClickListener(threeDotsClickListener: (View) -> Unit) {
        imageThreeDots?.setOnClickListener(threeDotsClickListener)
    }

    fun setRemoveWishlistOnClickListener(removeWishlistClickListener: (View) -> Unit) {
        buttonRemoveFromWishlist?.setOnClickListener(removeWishlistClickListener)
    }

    fun setAddToCartOnClickListener(addToCartClickListener: (View) -> Unit) {
        buttonAddToCart?.setOnClickListener(addToCartClickListener)
    }

    fun setCardHeight(height: Int) {
        val layoutParams = cardViewProductCard?.layoutParams
        layoutParams?.height = height
        cardViewProductCard?.layoutParams = layoutParams
    }

    fun recycle() {
        Glide.with(context).clear(imageProduct)
        Glide.with(context).clear(imageFreeOngkirPromo)
    }




    /**
     * Special cases for specific pages
     * */
    fun wishlistPage_hideCTAButton(isVisible: Boolean) {
        buttonAddToCart?.showWithCondition(isVisible)
        buttonRemoveFromWishlist?.showWithCondition(isVisible)
    }

    fun wishlistPage_enableButtonAddToCart(){
        buttonAddToCart?.isEnabled = true
        buttonAddToCart?.buttonVariant = UnifyButton.Variant.GHOST
        buttonAddToCart?.text = context.getString(R.string.product_card_text_add_to_cart_list)
    }

    fun wishlistPage_disableButtonAddToCart(){
        buttonAddToCart?.isEnabled = false
        buttonAddToCart?.text = context.getString(R.string.product_card_text_add_to_cart_list)
    }

    fun wishlistPage_setOutOfStock(){
        buttonAddToCart?.isEnabled = false
        buttonAddToCart?.buttonVariant = UnifyButton.Variant.FILLED
        buttonAddToCart?.text = context.getString(R.string.product_card_out_of_stock)
    }
}