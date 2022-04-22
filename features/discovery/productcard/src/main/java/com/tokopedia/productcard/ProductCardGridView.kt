package com.tokopedia.productcard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.unifycomponents.UnifyButton

class ProductCardGridView: BaseProductCardGridView {
    override val buttonSimilarProduct: UnifyButton? by lazy {
        findViewById(R.id.buttonSeeSimilarProduct)
    }

    override val buttonAddVariant: UnifyButton? by lazy {
        findViewById( R.id.buttonAddVariant)
    }

    override val buttonNotify: UnifyButton? by lazy {
        findViewById(R.id.buttonNotify)
    }

    override val buttonAddToCartWishlist: UnifyButton? by lazy {
        findViewById(R.id.buttonAddToCartWishlist)
    }

    override val buttonSeeSimilarProductWishlist: UnifyButton? by lazy {
        findViewById(R.id.buttonSeeSimilarProductWishlist)
    }

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
        View.inflate(context, R.layout.product_card_grid_layout, this)
    }
}