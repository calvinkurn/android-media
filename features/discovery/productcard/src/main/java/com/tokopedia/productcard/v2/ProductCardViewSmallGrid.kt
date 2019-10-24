package com.tokopedia.productcard.v2

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.productcard.R
import com.tokopedia.unifyprinciples.Typography

/**
 * ProductCardView with Small Grid layout.
 */
class ProductCardViewSmallGrid: ProductCardView {

    private var imageShop: ImageView? = null
    private var textViewAddToCart: Typography? = null

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs)

    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int
    ): super(context, attrs, defStyleAttr)

    override fun getLayout(): Int {
        return R.layout.product_card_layout_v2_small_grid
    }

    override fun findViews(inflatedView: View) {
        super.findViews(inflatedView)

        imageShop = inflatedView.findViewById(R.id.imageShop)
        textViewAddToCart = inflatedView.findViewById(R.id.textViewAddToCart)
    }

    fun setImageShopVisible(isVisible: Boolean) {
        imageShop?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setImageShopUrl(imageUrl: String) {
        imageShop?.let { imageShop ->
            ImageHandler.loadImageCircle2(context, imageShop, imageUrl)
        }
    }

    fun setAddToCartVisible(isVisible: Boolean) {
        textViewAddToCart?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setAddToCartOnClickListener(onClickListener: (view: View) -> Unit) {
        textViewAddToCart?.setOnClickListener(onClickListener)
    }

    fun setCardHeight(height: Int) {
        val layoutParams = cardViewProductCard?.layoutParams
        layoutParams?.height = height
        cardViewProductCard?.layoutParams = layoutParams
    }
}