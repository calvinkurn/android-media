package com.tokopedia.productcard.v2

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.productcard.R
import com.tokopedia.unifyprinciples.Typography

/**
 * ProductCardView with Big Grid layout.
 */
class ProductCardViewBigGrid: ProductCardView {

    private var textViewAddToCart: Typography? = null

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

    override fun findViews(inflatedView: View) {
        super.findViews(inflatedView)
        textViewAddToCart = inflatedView.findViewById(R.id.textViewAddToCart)
    }

    fun setAddToCartVisible(isVisible: Boolean) {
        textViewAddToCart?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setAddToCartOnClickListener(onClickListener: (view: View) -> Unit) {
        textViewAddToCart?.setOnClickListener(onClickListener)
    }
}