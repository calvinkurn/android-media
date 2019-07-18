package com.tokopedia.productcard.v2

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.constraint.ConstraintSet
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.productcard.R
import com.tokopedia.productcard.utils.doIfVisible
import com.tokopedia.productcard.utils.isNotNullAndVisible
import com.tokopedia.productcard.utils.isNullOrNotVisible
import com.tokopedia.unifycomponents.Label

/**
 * ProductCardView with Small Grid layout.
 */
class ProductCardViewSmallGrid: ProductCardView {

    private var imageShop: ImageView? = null

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
    }

    fun setImageShopVisible(isVisible: Boolean) {
        imageShop?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setImageShopUrl(imageUrl: String) {
        imageShop?.let { imageShop ->
            ImageHandler.loadImageCircle2(context, imageShop, imageUrl)
        }
    }
}