package com.tokopedia.productcard.reimagine

import android.content.Context
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import androidx.constraintlayout.widget.ConstraintSet.BOTTOM
import androidx.constraintlayout.widget.ConstraintSet.END
import androidx.constraintlayout.widget.ConstraintSet.PARENT_ID
import androidx.constraintlayout.widget.ConstraintSet.START
import androidx.constraintlayout.widget.ConstraintSet.TOP
import com.tokopedia.productcard.R
import com.tokopedia.productcard.utils.getPixel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toPx
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
): UnifyButton {
    val context = constraintLayout.context
    val addToCartButton = AddToCartButton(context)

    constraintLayout.addView(addToCartButton)

    constraintLayout.applyConstraintSet {
        val marginTop =
            context.getPixel(productcardR.dimen.product_card_reimagine_button_atc_margin_top)
        connect(R.id.productCardAddToCart, TOP, addToCartConstraints.top, BOTTOM, marginTop)
        connect(
            R.id.productCardAddToCart,
            BOTTOM,
            addToCartConstraints.bottom,
            BOTTOM,
        )
        connect(R.id.productCardAddToCart, START, addToCartConstraints.start, END)
        connect(R.id.productCardAddToCart, END, addToCartConstraints.end, START)
    }

    return addToCartButton
}

private fun AddToCartButton(context: Context) = UnifyButton(context).apply {
    id = R.id.productCardAddToCart
    layoutParams = layoutParams(context)
    buttonSize = UnifyButton.Size.MICRO
    buttonVariant = UnifyButton.Variant.GHOST
    text = context.resources.getString(productcardR.string.product_card_text_add_to_cart_grid)
}

private fun layoutParams(context: Context) = LayoutParams(
    LayoutParams.MATCH_CONSTRAINT,
    context.getPixel(R.dimen.product_card_reimagine_button_atc_height)
).apply {
    verticalBias = 1f
}
