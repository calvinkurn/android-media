package com.tokopedia.productcard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.productcard.utils.findViewByIdInViewStub
import com.tokopedia.unifycomponents.UnifyButton

class ProductCardListViewWithViewStub: BaseProductCardListView {
    override val buttonAddVariant: UnifyButton? by lazy {
        findViewByIdInViewStub(R.id.buttonAddVariantStub, R.id.buttonAddVariant)
    }

    override val buttonNotify: UnifyButton? by lazy {
        findViewByIdInViewStub(R.id.buttonNotifyStub, R.id.buttonNotify)
    }

    override val buttonAddToCartWishlist: UnifyButton? by lazy {
        findViewByIdInViewStub(R.id.buttonAddToCartWishlistStub, R.id.buttonAddToCartWishlist)
    }

    override val buttonSeeSimilarProductWishlist: UnifyButton? by lazy {
        findViewByIdInViewStub(R.id.buttonSeeSimilarProductWishlistStub, R.id.buttonSeeSimilarProductWishlist)
    }

    override val buttonAddToCart: UnifyButton? by lazy {
        findViewByIdInViewStub(R.id.buttonAddToCartStub, R.id.buttonAddToCart)
    }

    override val buttonDeleteProduct: UnifyButton? by lazy {
        findViewByIdInViewStub(R.id.buttonDeleteProductStub, R.id.buttonDeleteProduct)
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
        View.inflate(context, R.layout.product_card_list_with_viewstub_layout, this)
    }
}