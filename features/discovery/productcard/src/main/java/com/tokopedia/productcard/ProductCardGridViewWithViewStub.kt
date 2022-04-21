package com.tokopedia.productcard

import android.content.Context
import android.util.AttributeSet
import android.view.View

class ProductCardGridViewWithViewStub: BaseProductCardGridView {
    override val view: View
        get() = this

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
        View.inflate(context, R.layout.product_card_grid_with_viewstub_layout, this)
    }
}