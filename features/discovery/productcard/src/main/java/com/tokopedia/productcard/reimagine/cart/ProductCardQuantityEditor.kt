package com.tokopedia.productcard.reimagine.cart

import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.productcard.R
import com.tokopedia.productcard.reimagine.ProductCardModel
import com.tokopedia.productcard.reimagine.ProductCardType
import com.tokopedia.productcard.reimagine.applyConstraintSet
import com.tokopedia.productcard.utils.getPixel
import com.tokopedia.unifycomponents.QuantityEditorUnify

internal fun renderQuantityEditor(
    constraintLayout: ConstraintLayout?,
    type: ProductCardType,
    productCardModel: ProductCardModel,
) {
    constraintLayout ?: return

    val quantityEditor = constraintLayout.findViewById<QuantityEditorUnify?>(R.id.productCardQuantityEditor)
    val deleteCartButton = constraintLayout.findViewById<IconUnify?>(R.id.productCardButtonDeleteCart)
    val quantityEditorComponents = QuantityEditorComponents(quantityEditor, deleteCartButton)

    val inflatedQuantityEditorComponents =
        if (quantityEditorComponents.isNull()) inflateComponents(constraintLayout, type)
        else quantityEditorComponents

    if (productCardModel.showQuantityEditor()) inflatedQuantityEditorComponents.show()
    else inflatedQuantityEditorComponents.hide()
}

private fun inflateComponents(
    constraintLayout: ConstraintLayout,
    type: ProductCardType,
): QuantityEditorComponents {
    val context = constraintLayout.context

    LayoutInflater
        .from(context)
        .inflate(R.layout.product_card_reimagine_quantity_editor, constraintLayout)

    constraintLayout.applyConstraintSet {
        val marginTop =
            context.getPixel(R.dimen.product_card_reimagine_button_atc_margin_top)

        val addToCartConstraints = type.addToCartConstraints()

        connect(
            R.id.productCardQuantityEditor,
            ConstraintSet.TOP,
            addToCartConstraints.top,
            ConstraintSet.BOTTOM,
            marginTop
        )
    }

    return QuantityEditorComponents(
        constraintLayout.findViewById(R.id.productCardQuantityEditor),
        constraintLayout.findViewById(R.id.productCardButtonDeleteCart),
    )
}

private data class QuantityEditorComponents(
    val quantityEditor: QuantityEditorUnify?,
    val deleteCartButton: IconUnify?,
) {

    fun show() {
        quantityEditor?.show()
        deleteCartButton?.show()
    }

    fun hide() {
        quantityEditor?.hide()
        deleteCartButton?.hide()
    }

    fun isNull() = quantityEditor == null || deleteCartButton == null
}
