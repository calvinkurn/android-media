package com.tokopedia.play.extensions

import com.tokopedia.play.view.type.ProductButtonType


/**
 * @author by astidhiyaa on 26/10/22
 */

fun String.getCartAppLink(type: ProductButtonType, productId: String = "") : String =
    when (type) {
        ProductButtonType.ATC -> ""
        ProductButtonType.GCR -> ""
        ProductButtonType.OCC -> ""
        else -> ""
    }
