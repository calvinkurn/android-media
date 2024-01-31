package com.tokopedia.productcard.reimagine

import android.view.LayoutInflater
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.BOTTOM
import androidx.constraintlayout.widget.ConstraintSet.END
import androidx.constraintlayout.widget.ConstraintSet.START
import androidx.constraintlayout.widget.ConstraintSet.TOP
import com.tokopedia.productcard.R
import com.tokopedia.productcard.utils.getPixel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.productcard.R as productcardR

internal data class AddToCartConstraints(
    @IdRes val start: Int,
    @IdRes val top: Int,
    @IdRes val end: Int,
    @IdRes val bottom: Int,
)

internal fun AddToCartButton(
    constraintLayout: ConstraintLayout,
    addToCartConstraints: AddToCartConstraints,
): UnifyButton? {
    val context = constraintLayout.context

    LayoutInflater
        .from(context)
        .inflate(R.layout.product_card_reimagine_add_to_cart_button, constraintLayout)

    constraintLayout.applyConstraintSet {
        val marginTop =
            context.getPixel(productcardR.dimen.product_card_reimagine_button_atc_margin_top)
        connect(R.id.productCardAddToCart, TOP, addToCartConstraints.top, BOTTOM, marginTop)
        connect(R.id.productCardAddToCart, BOTTOM, addToCartConstraints.bottom, BOTTOM)
        connect(R.id.productCardAddToCart, START, addToCartConstraints.start, START)
        connect(R.id.productCardAddToCart, END, addToCartConstraints.end, END)
    }

    return constraintLayout.findViewById(R.id.productCardAddToCart)
}
