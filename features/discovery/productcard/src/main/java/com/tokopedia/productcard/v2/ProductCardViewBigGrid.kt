package com.tokopedia.productcard.v2

import android.content.Context
import android.util.AttributeSet
import com.tokopedia.productcard.R

/**
 * ProductCardView with Big Grid layout.
 */
class ProductCardViewBigGrid: ProductCardView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    override fun getLayout(): Int {
        return R.layout.product_card_layout_v2_big_grid
    }
}