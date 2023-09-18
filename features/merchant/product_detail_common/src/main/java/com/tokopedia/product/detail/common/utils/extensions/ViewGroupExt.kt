package com.tokopedia.product.detail.common.utils.extensions

import android.view.View
import android.view.ViewGroup

/**
 * Created by yovi.putra on 02/08/22"
 * Project name: android-tokopedia-core
 **/

/**
 * Executes [block] with a typed version of the View's layoutParams and reassigns the
 * layoutParams with the updated version.
 *
 * @see View.getLayoutParams
 * @see View.setLayoutParams
 **/
@JvmName("updateLayoutParamsTyped")
inline fun <reified T : ViewGroup.LayoutParams> View.updateLayoutParams(
    block: T?.() -> Unit
) {
    val params = layoutParams as? T
    block(params)
    layoutParams = params
}

fun String.validDimensionRatio() = matches(Regex("[0-9]+:[0-9]+"))
