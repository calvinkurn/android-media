package com.tokopedia.productcard.reimagine

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import androidx.constraintlayout.widget.ConstraintSet.BOTTOM
import androidx.constraintlayout.widget.ConstraintSet.END
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.view.setPadding
import com.tokopedia.productcard.R
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.iconunify.R as iconunifyR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

private const val ADD_TO_CART_PADDING_DP = 8
private const val ADD_TO_CART_TO_IMAGE_MARGIN_END = 6
private const val ADD_TO_CART_TO_IMAGE_MARGIN_BOTTOM = 6

internal fun AddToCartButton(constraintLayout: ConstraintLayout): UnifyButton {
    val addToCartButton = AddToCartButton(constraintLayout.context)

    constraintLayout.addView(addToCartButton)

    constraintLayout.applyConstraintSet {
        connect(R.id.productCardAddToCart, END, R.id.productCardImage, END)
        connect(R.id.productCardAddToCart, BOTTOM, R.id.productCardImage, BOTTOM)
    }

    return addToCartButton
}

private fun AddToCartButton(context: Context) = UnifyButton(context).apply {
    id = R.id.productCardAddToCart
    layoutParams = layoutParams()
    buttonSize = UnifyButton.Size.MICRO
    buttonVariant = UnifyButton.Variant.GHOST

    setDrawable(addToCartDrawable(context))

    setPadding(ADD_TO_CART_PADDING_DP.toPx())
}

private fun layoutParams() = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
    setMargins(
        0,
        0,
        ADD_TO_CART_TO_IMAGE_MARGIN_END.toPx(),
        ADD_TO_CART_TO_IMAGE_MARGIN_BOTTOM.toPx()
    )
}

private fun addToCartDrawable(context: Context): Drawable? =
    AppCompatResources.getDrawable(context, iconunifyR.drawable.iconunify_add)?.apply {
        mutate()
        colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
            ContextCompat.getColor(context, unifyprinciplesR.color.Unify_GN500),
            BlendModeCompat.SRC_ATOP
        )
    }
