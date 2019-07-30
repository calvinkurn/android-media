package com.tokopedia.productcard.v2

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import com.tokopedia.productcard.R

/**
 * ProductCardView with List layout.
 */
class ProductCardViewList: ProductCardView {

    private var cardViewImageProduct: CardView? = null

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs)

    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int
    ): super(context, attrs, defStyleAttr)

    override fun getLayout(): Int {
        return R.layout.product_card_layout_v2_list
    }

    override fun findViews(inflatedView: View) {
        super.findViews(inflatedView)

        cardViewImageProduct = inflatedView.findViewById(R.id.cardViewImageProduct)
    }

    override fun setImageTopAdsConstraint() {
        // Do nothing
        // Top Ads icon constraints do not need to be realigned
    }
}