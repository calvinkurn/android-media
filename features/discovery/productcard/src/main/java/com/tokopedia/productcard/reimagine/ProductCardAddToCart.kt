package com.tokopedia.productcard.reimagine

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import androidx.constraintlayout.widget.ConstraintSet.BOTTOM
import androidx.constraintlayout.widget.ConstraintSet.END
import com.tokopedia.productcard.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.toPx

private const val MARGIN_END = 6
private const val MARGIN_BOTTOM = 6
private const val IMAGE_SIZE = 32

internal fun AddToCartButton(constraintLayout: ConstraintLayout): ImageUnify {
    val addToCartButton = AddToCartButton(constraintLayout.context)

    constraintLayout.addView(addToCartButton)

    constraintLayout.applyConstraintSet {
        connect(R.id.productCardAddToCart, END, R.id.productCardImage, END, MARGIN_END)
        connect(R.id.productCardAddToCart, BOTTOM, R.id.productCardImage, BOTTOM, MARGIN_BOTTOM)
    }

    return addToCartButton
}

private fun AddToCartButton(context: Context) = ImageUnify(context).apply {
    id = R.id.productCardAddToCart
    layoutParams = layoutParams()

    setImageDrawable(addToCartDrawable(context))
}

private fun layoutParams() = LayoutParams(IMAGE_SIZE.toPx(), IMAGE_SIZE.toPx())

private fun addToCartDrawable(context: Context): Drawable? =
    AppCompatResources.getDrawable(context, R.drawable.product_card_reimagine_add_to_cart)
