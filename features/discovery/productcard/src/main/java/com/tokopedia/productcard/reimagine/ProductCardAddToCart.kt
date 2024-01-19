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
import com.tokopedia.productcard.R as productcardR

internal fun AddToCartButton(
    constraintLayout: ConstraintLayout,
    @IdRes constraintTopToBottomId: Int,
): UnifyButton {
    val context = constraintLayout.context
    val addToCartButton = AddToCartButton(context)

    constraintLayout.addView(addToCartButton)

    constraintLayout.applyConstraintSet {
        val marginTop =
            context.getPixel(productcardR.dimen.product_card_reimagine_button_atc_margin_top)

        connect(R.id.productCardAddToCart, START, PARENT_ID, START)
        connect(R.id.productCardAddToCart, TOP, constraintTopToBottomId, BOTTOM, marginTop)
        connect(R.id.productCardAddToCart, END, PARENT_ID, END)
        connect(R.id.productCardAddToCart, BOTTOM, PARENT_ID, BOTTOM)
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
    0, // Match constraint for constraint layout
    context.getPixel(R.dimen.product_card_reimagine_button_atc_height)
)
