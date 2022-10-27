package com.tokopedia.play.extensions

import com.tokopedia.play.view.type.ProductAction
import com.tokopedia.play.view.type.ProductButtonColor
import com.tokopedia.play.view.type.ProductButtonType
import com.tokopedia.unifycomponents.UnifyButton


/**
 * @author by astidhiyaa on 26/10/22
 */

fun UnifyButton.generateButton(color: ProductButtonColor){
    when (color) {
        ProductButtonColor.PRIMARY_BUTTON -> {
            buttonVariant = UnifyButton.Variant.FILLED
            buttonType = UnifyButton.Type.MAIN
            isEnabled = true
        }
        ProductButtonColor.SECONDARY_BUTTON -> {
            buttonVariant = UnifyButton.Variant.GHOST
            buttonType = UnifyButton.Type.MAIN
            isEnabled = true
        }
        ProductButtonColor.PRIMARY_DISABLED_BUTTON -> {
            buttonVariant = UnifyButton.Variant.FILLED
            buttonType = UnifyButton.Type.MAIN
            isEnabled = false
        }
        ProductButtonColor.SECONDARY_DISABLED_BUTTON -> {
            buttonVariant = UnifyButton.Variant.GHOST
            buttonType = UnifyButton.Type.MAIN
            isEnabled = false
        }
    }
}

fun String.getCartAppLink(type: ProductButtonType, productId: String = "") : String =
    when (type) {
        ProductButtonType.ATC -> ""
        ProductButtonType.GCR -> ""
        ProductButtonType.OCC -> ""
        else -> ""
    }

val ProductButtonType.toAction : ProductAction
    get() = when (this) {
        ProductButtonType.ATC -> ProductAction.AddToCart
        ProductButtonType.GCR -> ProductAction.Buy
        ProductButtonType.OCC -> ProductAction.OCC
        else -> ProductAction.AddToCart
    }
