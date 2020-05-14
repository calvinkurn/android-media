package com.tokopedia.productcard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.productcard.utils.glideClear
import com.tokopedia.productcard.utils.loadImage
import com.tokopedia.productcard.utils.loadImageWithOutPlaceholder
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.product_card_flashsale_layout.view.*

/**
 * @author by yoasfs on 2020-03-07
 */

class ProductCardFlashSaleView: BaseCustomView, IProductCardFlashSaleView {

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
        View.inflate(context, R.layout.product_card_flashsale_layout, this)
    }

    override fun setProductModel(productCardModel: ProductCardFlashSaleModel) {
        imageLoadingProduct?.show()
        imageProduct?.loadImageWithOutPlaceholder(productCardModel.productImageUrl){
            imageLoadingProduct.hide()
        }
        renderProductCardFlashSaleContent(productCardModel)
    }

    override fun getCardMaxElevation() = cardViewProductCard?.maxCardElevation ?: 0f

    override fun getCardRadius() = cardViewProductCard?.radius ?: 0f

    fun applyCarousel() {
        setCardHeightMatchParent()
    }

    private fun setCardHeightMatchParent() {
        val layoutParams = cardViewProductCard?.layoutParams
        layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
        cardViewProductCard?.layoutParams = layoutParams
    }

    override fun recycle() {
        imageProduct?.glideClear(context)
    }

}