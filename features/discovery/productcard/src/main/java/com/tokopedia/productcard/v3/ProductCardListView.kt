package com.tokopedia.productcard.v3

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.productcard.R
import com.tokopedia.productcard.utils.loadImageRounded
import com.tokopedia.productcard.v2.ProductCardModel
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.product_card_grid_layout.view.*

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
        imageProduct.loadImageRounded(productCardModel.productImageUrl)

        renderProductCardContent(productCardModel)

        imageThreeDots.showWithCondition(productCardModel.hasOptions)
    }
}